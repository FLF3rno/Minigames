package net.mcreator.minigames.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.OmegaLaserTickProcedure;
import net.mcreator.minigames.client.model.animations.flavio_omega_laserAnimation;

public class FlavioOmegaLaserEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(FlavioOmegaLaserEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(FlavioOmegaLaserEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> HEAD_YAW = SynchedEntityData.defineId(FlavioOmegaLaserEntity.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> HEAD_PITCH = SynchedEntityData.defineId(FlavioOmegaLaserEntity.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Integer> RELOAD_PROGRESS = SynchedEntityData.defineId(FlavioOmegaLaserEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> IS_LOCKED = SynchedEntityData.defineId(FlavioOmegaLaserEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> MANNED_COUNT = SynchedEntityData.defineId(FlavioOmegaLaserEntity.class, EntityDataSerializers.INT);
	public static final int MAX_RELOAD = 100;

	public final AnimationState animationState0 = new AnimationState();

	private final java.util.List<Integer> spawnedScreenIds = new java.util.ArrayList<>();
	private final java.util.Set<java.util.UUID> mannedPlayerUuids = new java.util.HashSet<>();
	private boolean screensSpawned = false;

	public FlavioOmegaLaserEntity(EntityType<FlavioOmegaLaserEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(true);
		setPersistenceRequired();
		this.moveControl = new FlyingMoveControl(this, 10, true);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		if (ANIM.equals(data)) {
			switch (this.entityData.get(ANIM)) {
				case -1 :
					this.animationState0.stop();
					break;
				case 0 :
					this.animationState0.start(this.tickCount);
					break;
			}
		}
		super.onSyncedDataUpdated(data);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "omega_laser");
		builder.define(ANIM, 0);
		builder.define(HEAD_YAW, 0.0f);
		builder.define(HEAD_PITCH, 0.0f);
		builder.define(RELOAD_PROGRESS, 100);
		builder.define(IS_LOCKED, true);
		builder.define(MANNED_COUNT, 0);
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	public float getHeadYaw() {
		return this.entityData.get(HEAD_YAW);
	}

	public void setHeadYaw(float yaw) {
		this.entityData.set(HEAD_YAW, yaw);
	}

	public float getHeadPitch() {
		return this.entityData.get(HEAD_PITCH);
	}

	public void setHeadPitch(float pitch) {
		this.entityData.set(HEAD_PITCH, pitch);
	}

	public int getReloadProgress() {
		return this.entityData.get(RELOAD_PROGRESS);
	}

	public void setReloadProgress(int progress) {
		this.entityData.set(RELOAD_PROGRESS, progress);
	}

	public boolean isControlsLocked() {
		return this.entityData.get(IS_LOCKED);
	}

	public void setControlsLocked(boolean locked) {
		this.entityData.set(IS_LOCKED, locked);
	}

	public void registerPlayerManned(Player player) {
		if (player != null && !player.level().isClientSide()) {
			mannedPlayerUuids.add(player.getUUID());
			this.entityData.set(MANNED_COUNT, mannedPlayerUuids.size());
		}
	}

	public void unregisterPlayerManned(Player player) {
		if (player != null && !player.level().isClientSide()) {
			mannedPlayerUuids.remove(player.getUUID());
			this.entityData.set(MANNED_COUNT, mannedPlayerUuids.size());
		}
	}

	public int getMannedPlayerCount() {
		return this.entityData.get(MANNED_COUNT);
	}

	public int getRequiredPlayerCount() {
		return (int) this.level().players().stream()
				.filter(p -> p.isAlive() && !p.isSpectator())
				.count();
	}

	public void handleSteerInput(Player player, int screenIndex, float deltaPitch, float deltaYaw) {
		if (isControlsLocked()) return;
		int total = Math.max(1, Math.min(4, getRequiredPlayerCount()));
		// Check role permissions based on total players and screenIndex
		boolean canPitch = false;
		boolean canYaw = false;

		if (total == 1 && screenIndex == 0) {
			canPitch = true;
			canYaw = true;
		} else if (total == 2) {
			if (screenIndex == 1) canPitch = true;
		} else if (total == 3) {
			if (screenIndex == 1) canPitch = true;
			if (screenIndex == 2) canYaw = true;
		} else if (total >= 4) {
			if (screenIndex == 1) canPitch = true;
			if (screenIndex == 2) canYaw = true;
		}

		if (canPitch && deltaPitch != 0) {
			float newPitch = net.minecraft.util.Mth.clamp(getHeadPitch() + deltaPitch, -75.0f, 75.0f);
			setHeadPitch(newPitch);
		}
		if (canYaw && deltaYaw != 0) {
			float newYaw = net.minecraft.util.Mth.clamp(getHeadYaw() + deltaYaw, -175.0f, 175.0f);
			setHeadYaw(newYaw);
		}
	}

	public void handleFireInput(Player player, int screenIndex) {
		if (isControlsLocked()) return;
		int total = Math.max(1, Math.min(4, getRequiredPlayerCount()));
		boolean canShoot = (screenIndex == 0); // Player 1 always shoots in all setups
		if (canShoot && getReloadProgress() >= MAX_RELOAD) {
			setReloadProgress(0);
			OmegaLaserTickProcedure.fireLaser(this.level(), this);
		}
	}

	public void handleScreenExit(Player player, int screenIndex) {
		unregisterPlayerManned(player);
	}

	private void checkAndSpawnScreens() {
		if (this.level().isClientSide() || screensSpawned || this.tickCount < 36) return;
		int count = Math.max(1, Math.min(4, getRequiredPlayerCount()));

		// Spawn count screens around or in front/behind of the laser cannon
		// Cannon faces entity.getYRot(). Let's place screens behind the cannon at -3.5 blocks distance
		double baseRad = Math.toRadians(this.getYRot());
		double backX = -Math.sin(baseRad);
		double backZ = Math.cos(baseRad);
		double rightX = Math.cos(baseRad);
		double rightZ = Math.sin(baseRad);

		double originX = this.getX() - backX * 3.5;
		double originZ = this.getZ() - backZ * 3.5;
		double originY = this.getY();

		float spacing = 2.2f;
		float startOffset = -(count - 1) * spacing / 2.0f;

		for (int i = 0; i < count; i++) {
			FlavioOmegaLaserScreenEntity screen = net.mcreator.minigames.init.MinigamesModEntities.FLAVIO_OMEGA_LASER_SCREEN.get().create(this.level(), net.minecraft.world.entity.EntitySpawnReason.MOB_SUMMONED);
			if (screen != null) {
				double offset = startOffset + i * spacing;
				double sx = originX + rightX * offset;
				double sz = originZ + rightZ * offset;
				screen.setPos(sx, originY, sz);
				screen.setYRot(this.getYRot());
				screen.setLaserId(this.getId());
				screen.setScreenIndex(i);
				this.level().addFreshEntity(screen);
				spawnedScreenIds.add(screen.getId());
			}
		}
		screensSpawned = true;
	}

	public void despawnScreens() {
		if (this.level().isClientSide()) return;
		for (int id : spawnedScreenIds) {
			Entity e = this.level().getEntity(id);
			if (e != null && e.isAlive()) {
				e.discard();
			}
		}
		spawnedScreenIds.clear();
	}

	@Override
	public void remove(RemovalReason reason) {
		despawnScreens();
		super.remove(reason);
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new FlyingPathNavigation(this, world);
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.iron.hit"));
	}

	@Override
	public boolean causeFallDamage(double l, float d, DamageSource source) {
		return false;
	}

	@Override
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "omega_laser"));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			// Align camera entity view angles with cannon body + head aim
			this.setYRot(this.getYRot());
			this.setYHeadRot(this.getYRot() + this.getHeadYaw());
			this.setXRot(this.getHeadPitch());

			if (this.animationState0.isStarted()) {
				float elapsedSeconds = this.animationState0.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= flavio_omega_laserAnimation.fire.lengthInSeconds()) {
					if (!flavio_omega_laserAnimation.fire.looping())
						this.animationState0.stop();
					else
						this.animationState0.start(this.tickCount);
				}
			}
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.level().isClientSide()) {
			checkAndSpawnScreens();

			// Clean up manned players that are no longer valid (e.g. disconnected or dead)
			mannedPlayerUuids.removeIf(uuid -> {
				Player p = this.level().getPlayerByUUID(uuid);
				return p == null || !p.isAlive() || p.isSpectator();
			});
			this.entityData.set(MANNED_COUNT, mannedPlayerUuids.size());

			int required = getRequiredPlayerCount();
			int manned = mannedPlayerUuids.size();

			// Must have at least 1 player in the game and all required non-spectators manned
			if (required > 0 && manned >= required) {
				setControlsLocked(false);
			} else {
				setControlsLocked(true);
				// Ease position back to default (0, 0)
				float curYaw = getHeadYaw();
				float curPitch = getHeadPitch();
				if (Math.abs(curYaw) > 0.5f) {
					setHeadYaw(curYaw * 0.85f);
				} else {
					setHeadYaw(0.0f);
				}
				if (Math.abs(curPitch) > 0.5f) {
					setHeadPitch(curPitch * 0.85f);
				} else {
					setHeadPitch(0.0f);
				}
			}

			// Auto-reload progress increment
			int currentReload = getReloadProgress();
			if (currentReload < MAX_RELOAD) {
				setReloadProgress(Math.min(MAX_RELOAD, currentReload + 1));
			}
		}
		OmegaLaserTickProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return true;
	}

	@Override
	public boolean canBeCollidedWith(Entity sourceentity) {
		return true;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
	}

	@Override
	protected float getFlyingSpeed() {
		return (float) this.getAttributeValue(Attributes.FLYING_SPEED);
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 100);
		builder = builder.add(Attributes.FLYING_SPEED, 0.3);
		return builder;
	}
}