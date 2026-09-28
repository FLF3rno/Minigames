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

		if (entity instanceof LivingEntity _living && _living.hasEffect(MinigamesModMobEffects.STUNNED)) {
			return;
		}

		int cycleTick = entity.tickCount % 90;
		if (!(entity.getEntityData().get(LaserStatueEntity.DATA_active))) { cycleTick = 0;}

		int roomID = 0;
		if (entity instanceof LaserStatueEntity laserStatue) {
			roomID = laserStatue.getRoomID();
		}
		if (roomID == 0) {
			roomID = (int) entity.getPersistentData().getDoubleOr("DataID", 0);
			if (roomID == 0) {
				roomID = entity.getPersistentData().getIntOr("DataID", 0);
			}
		}

		boolean inCombat = MinigamesModVariables.MapVariables.get(world).inCombat;
		int currentRoomID = (int) MinigamesModVariables.MapVariables.get(world).currentRoomID;

		boolean inCurrentRoom = inCombat && roomID > 0 && currentRoomID == roomID;

		Vec3 doorOffset = MinigamesModVariables.MapVariables.get(world).DoorOffset;
		if (inCurrentRoom && doorOffset != null && (doorOffset.x() != 0 || doorOffset.z() != 0)) {
			int combatGridX = (int) Math.floor(doorOffset.x() / 29.0);
			int combatGridZ = (int) Math.floor(doorOffset.z() / 29.0);
			int statueGridX = (int) Math.floor(entity.getX() / 29.0);
			int statueGridZ = (int) Math.floor(entity.getZ() / 29.0);
			if (combatGridX != statueGridX || combatGridZ != statueGridZ) {
				inCurrentRoom = false;
			}
		}

		if (entity.getEntityData().get(LaserStatueEntity.DATA_active) && inCurrentRoom) {
			if (cycleTick == 50) {
				telegraph = true;
			}

			if (cycleTick == 89) {
				attack = true;
			}

			Vec3 start = new Vec3(entity.getX(), entity.getY() + 1.5, entity.getZ());
			boolean hasTarget = false;
			int statueGridX = (int) Math.floor(entity.getX() / 29.0);
			int statueGridZ = (int) Math.floor(entity.getZ() / 29.0);

			if (attack || telegraph) {
				for (Player targetPlayer : world.getEntitiesOfClass(Player.class, entity.getBoundingBox().inflate(32.0D))) {
					if (targetPlayer.isCreative() || targetPlayer.isSpectator() || !targetPlayer.isAlive() || targetPlayer.hasEffect(MinigamesModMobEffects.BLESSED)) {
						continue;
					}
					int playerGridX = (int) Math.floor(targetPlayer.getX() / 29.0);
					int playerGridZ = (int) Math.floor(targetPlayer.getZ() / 29.0);
					if (playerGridX != statueGridX || playerGridZ != statueGridZ) {
						continue;
					}
					hasTarget = true;

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
			}

			if (attack && hasTarget && world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:holy_laser")), SoundSource.HOSTILE, 4, 1);
				}
			}
		}
	}
}