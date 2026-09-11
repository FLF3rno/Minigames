package net.mcreator.minigames.procedures;

import net.mcreator.minigames.entity.LaserStatueEntity;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LaserStatueTickProcedure {
	private static final Identifier BEAM = Identifier.fromNamespaceAndPath("minigames", "textures/entities/beam_renderer/holy_laser.png");

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {

		boolean attack = false;
		boolean telegraph = false;

		int cycleTick = entity.tickCount % 90;
		if (!(entity.getEntityData().get(LaserStatueEntity.DATA_active))) { cycleTick = 0;}
		if (entity.getEntityData().get(LaserStatueEntity.DATA_active) && MinigamesModVariables.MapVariables.get(world).currentRoomID == entity.getPersistentData().getDoubleOr("DataID", 0)) {
		if (cycleTick == 50) {
			telegraph = true;
		}

		if (cycleTick == 89) {
			attack = true;
		}

		Vec3 start = new Vec3(entity.getX(), entity.getY() + 1.5, entity.getZ());

		if (attack || telegraph) {

			for (Entity targetPlayer : new ArrayList<>(world.players())) {
				if (targetPlayer instanceof LivingEntity _livingTarget && _livingTarget.hasEffect(MinigamesModMobEffects.BLESSED)) {
					continue;
				}

				Vec3 direction = targetPlayer.position().subtract(start);
				if (direction.lengthSqr() < 1.0E-6D) {
					direction = new Vec3(0, 0, 1);
				} else {
					direction = direction.normalize();
				}

				double maxDistance = 128.0;
				Vec3 end = start.add(direction.scale(maxDistance));
				BlockHitResult blockHit = world.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));

				if (blockHit.getType() == HitResult.Type.BLOCK) {
					end = blockHit.getLocation();
				}

				if (telegraph) {
					ParticleFlowHelperProcedure.execute(world, 50, 1, "linear", "minigames:blessed_particle", end, start);
				}

				if (attack) {
					AABB box = new AABB(start, end).inflate(1.0);
					List<Entity> candidates = world.getEntities(
							entity,
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

					for (Entity affectedEntity : hits) {
						if (affectedEntity instanceof Player _player) {
							if (!_player.hasEffect(MinigamesModMobEffects.BLESSED)) {
								if (world instanceof ServerLevel serverLevel) {
									_player.hurtServer(serverLevel, serverLevel.damageSources().generic(), 3.0F);
								}
							}
						}
					}

					RenderBeamXYZProcedure.execute(entity, true, start.x, start.y, start.z, 2, 15, end.x, end.y, end.z, "beam", BEAM);
				}
			}

			if (attack && world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:holy_laser")), SoundSource.HOSTILE, 4, 1);
				}
			}
		}
		}
	}
}