package net.mcreator.minigames.procedures;

import net.mcreator.minigames.entity.FlavioOmegaLaserEntity;
import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OmegaLaserTickProcedure {
	public static boolean tracking;
	private static float lockedYaw;
	private static float bodyYaw;
	private static float lockedPitch;
	private static Vec3 lockedTarget = null;
	private static final Identifier BEAM = Identifier.fromNamespaceAndPath("minigames", "textures/entities/beam_renderer/omega_laser_beam.png");

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
			return;
		}

			int cycleTick = entity.tickCount % 120;

			if (cycleTick == 0) {
				if (entity instanceof FlavioOmegaLaserEntity laser) {
					laser.getEntityData().set(FlavioOmegaLaserEntity.ANIM, 1000);
					laser.getEntityData().set(FlavioOmegaLaserEntity.ANIM, 0);
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:laser_cannon_windup")), SoundSource.HOSTILE, 4, 0.9f);
						}
					}
				}
				tracking = true;
				bodyYaw = entity.getYRot();
			} else if (cycleTick == 60) {
				tracking = false;
				lockedYaw = entity.getYRot();
				lockedPitch = entity.getXRot();
			}

			if (cycleTick >= 60 && cycleTick < 90) {
				telegraph = true;
			}

			if (cycleTick == 90) {
				attack = true;
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					ApplyScreenshakeProcedure.execute(1, 30);
				}

			}
			if (cycleTick == 90) {
				AffectLightingMin(world, -8);
				AffectLightingMax(world, -8);
				if (world.isClientSide()) {
                    UpdateChunkProcedure.execute(x, z);
                }
			}
			if (cycleTick >= 111 && cycleTick <= 118) {
				AffectLightingMin(world, 1);
				AffectLightingMax(world, 1);
				if (world.isClientSide()) {
					UpdateChunkProcedure.execute(x, z);
				}
			}

		Player player = (Player) findEntityInWorldRange(world, Player.class, x, y, z, 60);
		Vec3 start = new Vec3(entity.getX(), entity.getY() + 5.4, entity.getZ());

		if (tracking && player != null) {
			lockedTarget = player.getEyePosition();
		}

		if (lockedTarget == null) {
			if (player != null) {
				lockedTarget = player.getEyePosition();
			} else {
				lockedTarget = start.add(entity.getLookAngle().scale(30.0));
			}
		}

		Vec3 direction = lockedTarget.subtract(start);
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

		AABB box = new AABB(start, end).inflate(1.0);
		List<Entity> candidates = world.getEntities(
				(Entity) null,
				box,
				e -> e instanceof LivingEntity && e.isAlive() && e != entity
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
					if (world instanceof ServerLevel serverLevel && affectedEntity instanceof LivingEntity livingEntity) {
						if (!livingEntity.hasEffect(MinigamesModMobEffects.BLESSED)) {
							affectedEntity.hurtServer(serverLevel, serverLevel.damageSources().generic(), 100000.0F);
						}
					}
				}
			}
			if (entity instanceof LivingEntity livingEntity1) {
				livingEntity1.removeEffect(MinigamesModMobEffects.BLESSED);
			}
			RenderBeamXYZProcedure.execute(entity, true, start.x, start.y, start.z, 2, 30, end.x, end.y, end.z, "beam", BEAM);
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:laser_cannon_impact")), SoundSource.HOSTILE, 4, 1);
				}
			}
		}

		if (telegraph) {
			Vec3 end2 = new Vec3(end.x, end.y - 0.3f, end.z);
			ParticleFlowHelperProcedure.execute(world, 100, 1, "linear", "minecraft:portal", end2, start);
		}
	}

	private static void AffectLightingMin(LevelAccessor world, int change) {
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.minimumLightLevel += change;
				_vars.markSyncDirty();
			}
		}
	}
	private static void AffectLightingMax(LevelAccessor world, int change) {
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.maximumLightLevel += change;
				_vars.markSyncDirty();
			}
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}
