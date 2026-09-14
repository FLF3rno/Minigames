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
	private static final Identifier BEAM = Identifier.fromNamespaceAndPath("minigames", "textures/entities/beam_renderer/omega_laser_beam.png");

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		if (entity.tickCount < 35) {
			double _ty = y + 0.12;
			entity.teleportTo(x, _ty, z);
			if (entity instanceof ServerPlayer _serverPlayer)
				_serverPlayer.connection.teleport(x, _ty, z, entity.getYRot(), entity.getXRot());
			return;
		}
		if (entity.tickCount == 36) {
			entity.setNoGravity(false);
		}
	}

	public static void fireLaser(Level level, FlavioOmegaLaserEntity laser) {
		if (laser == null || level == null) return;

		double x = laser.getX();
		double y = laser.getY();
		double z = laser.getZ();

		// Trigger fire animation
		laser.getEntityData().set(FlavioOmegaLaserEntity.ANIM, 1000);
		laser.getEntityData().set(FlavioOmegaLaserEntity.ANIM, 0);

		// Calculate beam direction based on cannon body rotation + head yaw & pitch
		float totalYaw = laser.getYRot() + laser.getHeadYaw();
		float totalPitch = laser.getHeadPitch();

		Vec3 start = new Vec3(x, y + 5.4, z);
		Vec3 direction = Vec3.directionFromRotation(totalPitch, totalYaw);

		double maxDistance = 128.0;
		Vec3 end = start.add(direction.scale(maxDistance));
		BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, laser));
		if (blockHit.getType() == HitResult.Type.BLOCK) {
			end = blockHit.getLocation();
		}

		AABB box = new AABB(start, end).inflate(1.5);
		List<Entity> candidates = level.getEntities(
				(Entity) null,
				box,
				e -> e instanceof LivingEntity && e.isAlive() && e != laser
		);
		List<Entity> hits = new ArrayList<>();

		for (Entity e : candidates) {
			AABB bb = e.getBoundingBox().inflate(0.5);
			Optional<Vec3> intersection = bb.clip(start, end);
			if (intersection.isPresent()) {
				hits.add(e);
			}
		}

		for (Entity affectedEntity : hits) {
			if (affectedEntity instanceof Player _player) {
				if (!_player.hasEffect(MinigamesModMobEffects.BLESSED)) {
					if (level instanceof ServerLevel serverLevel) {
						_player.hurtServer(serverLevel, serverLevel.damageSources().generic(), 7.0F);
					}
				}
			} else {
				if (level instanceof ServerLevel serverLevel && affectedEntity instanceof LivingEntity livingEntity) {
					if (!livingEntity.hasEffect(MinigamesModMobEffects.BLESSED)) {
						affectedEntity.hurtServer(serverLevel, serverLevel.damageSources().generic(), 100000.0F);
					}
				}
			}
		}

		if (laser instanceof LivingEntity livingEntity1) {
			livingEntity1.removeEffect(MinigamesModMobEffects.BLESSED);
		}

		// Screenshake
		for (Player p : level.players()) {
			ApplyScreenshakeProcedure.execute(1, 30);
		}

		// Light and chunks
		AffectLightingMin(level, -8);
		AffectLightingMax(level, -8);
		if (level.isClientSide()) {
			UpdateChunkProcedure.execute(x, z);
		}

		// Render beam
		RenderBeamXYZProcedure.execute(laser, true, start.x, start.y, start.z, 2, 30, end.x, end.y, end.z, "beam", BEAM);

		// Sound
		if (!level.isClientSide()) {
			level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:laser_cannon_impact")), SoundSource.HOSTILE, 4, 1);
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

	private static Entity findFurthestEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range, java.util.function.Predicate<Entity> predicate) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), predicate).stream()
				.filter(e -> e.isAlive() && (!(e instanceof Player p) || !p.isSpectator()))
				.max(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z)))
				.orElse(null);
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range, java.util.function.Predicate<Entity> predicate) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), predicate).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return findEntityInWorldRange(world, clazz, x, y, z, range, e -> true);
	}
}