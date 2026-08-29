package net.mcreator.minigames.procedures;

import net.mcreator.minigames.entity.FlavioSweeperEntity;
import net.mcreator.minigames.entity.FlavioTrapdoor2Entity;
import net.mcreator.minigames.entity.FlavioTrapdoor3Entity;
import net.mcreator.minigames.entity.FlavioTrapdoorEntity;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OmegaLaserTickProcedure {
	public static boolean tracking;
	private static float lockedYaw;
	private static float bodyYaw;
	private static float lockedPitch;
	private static Identifier beam = Identifier.fromNamespaceAndPath("minigames", "textures/entities/beam_renderer/omega_laser_beam.png");
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean attack = false;
		boolean telegraph = false;

		if (entity.tickCount < 35) {
				double _ty = y + 0.12;
				entity.teleportTo(x, _ty, z);
				if (entity instanceof ServerPlayer _serverPlayer)
					_serverPlayer.connection.teleport(x, _ty, z, entity.getYRot(), entity.getXRot());

		} else if (entity.tickCount > 65) {
			if (entity.tickCount % 120 == 0) {
				entity.getEntityData().set(FlavioSweeperEntity.ANIM, 1000);
				entity.getEntityData().set(FlavioSweeperEntity.ANIM, 0);

				tracking = true;

				bodyYaw = entity.getYRot();
			}
			if (entity.tickCount % 120 == 60) {
				tracking = false;

				lockedYaw = entity.getYRot();
				lockedPitch = entity.getXRot();
				telegraph = true;
			}
		}
		Player player = (Player)findEntityInWorldRange(world, Player.class, x, y, z, 60);
		Vec3 start = new Vec3(entity.getX(), entity.getY() + 5, entity.getZ());
		Vec3 target = player.getEyePosition();

		Vec3 direction = target.subtract(start).normalize();
		double maxDistance = 128.0;

		Vec3 end = start.add(direction.scale(maxDistance));
		BlockHitResult blockHit = world.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
		AABB box = new AABB(start, end).inflate(1.0);
		if (blockHit.getType() == HitResult.Type.BLOCK) {
			end = blockHit.getLocation();
		}
		List<Entity> candidates = world.getEntities(
                (Entity) null,
				box,
				e -> e instanceof LivingEntity && e.isAlive()
		);
		List<Entity> hits = new ArrayList<>();

		for (Entity e : candidates) {
			AABB bb = e.getBoundingBox().inflate(0.3);

			Optional<Vec3> intersection = bb.clip(start, end);

			if (intersection.isPresent()) {
				hits.add(e);
			}
		}
		if (attack) {
			for (Entity affectedEntity : hits) {

				if (affectedEntity instanceof Player _player) {
					if (world instanceof ServerLevel serverLevel) {
						_player.hurtServer(serverLevel, serverLevel.damageSources().generic(), 7.0F);
					}
				} else {
					if (world instanceof ServerLevel serverLevel) {
                    	affectedEntity.hurtServer(serverLevel, serverLevel.damageSources().generic(), 100000.0F);
					}
                }
			}

			RenderBeamXYZProcedure.execute(entity, true, start.x, start.y, start.z, 2, 30, end.x, end.y, end.z, "beam", beam);
		}
		if (telegraph) {
			ParticleFlowHelperProcedure.execute(world, 100, 1, "linear", "minecraft:portal", end, start);
		}
	}
	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}
