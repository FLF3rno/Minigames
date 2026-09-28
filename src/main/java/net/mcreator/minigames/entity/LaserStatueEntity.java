package net.mcreator.minigames.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.mcreator.minigames.init.MinigamesModAttributes;

import net.mcreator.minigames.procedures.LaserStatueTickProcedure;
import net.mcreator.minigames.procedures.LaserStatueHurtProcedure;
import net.mcreator.minigames.client.model.animations.laser_statueAnimation;
import net.mcreator.minigames.init.MinigamesModMobEffects;

public class LaserStatueEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(LaserStatueEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(LaserStatueEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> DATA_active = SynchedEntityData.defineId(LaserStatueEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> DATA_ID = SynchedEntityData.defineId(LaserStatueEntity.class, EntityDataSerializers.INT);
	public final AnimationState animationState0 = new AnimationState();
	public final AnimationState animationState1 = new AnimationState();

	public LaserStatueEntity(EntityType<LaserStatueEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(true);
		setPersistenceRequired();
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		if (ANIM.equals(data)) {
			switch (this.entityData.get(ANIM)) {
				case -1 :
					this.animationState0.stop();
					break;
				case -2 :
					this.animationState1.stop();
					break;
				case 0 :
					this.animationState1.stop();
					this.animationState0.start(this.tickCount);
					break;
				case 1 :
					this.animationState0.stop();
					this.animationState1.start(this.tickCount);
					break;
			}
		}
		super.onSyncedDataUpdated(data);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "laser_statue");
		builder.define(ANIM, 0);
		builder.define(DATA_active, true);
		builder.define(DATA_ID, 0);
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.polished_tuff.hit"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.respawn_anchor.deplete"));
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damagesource, float amount) {
		boolean retval = super.hurtServer(level, damagesource, amount);
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		Level world = this.level();
		Entity entity = this;
		Entity sourceentity = damagesource.getEntity();
		Entity immediatesourceentity = damagesource.getDirectEntity();

		LaserStatueHurtProcedure.execute(world, x, y, z, entity, amount);
		return retval;
	}

	@Override
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
		valueOutput.putBoolean("Dataactive", this.entityData.get(DATA_active));
		valueOutput.putInt("Anim", this.entityData.get(ANIM));
		valueOutput.putInt("DataID", this.entityData.get(DATA_ID));
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "laser_statue"));
		this.entityData.set(DATA_active, valueInput.getBooleanOr("Dataactive", true));
		this.entityData.set(ANIM, valueInput.getIntOr("Anim", 0));
		this.entityData.set(DATA_ID, valueInput.getIntOr("DataID", 0));
	}

	public int getRoomID() {
		int id = this.entityData.get(DATA_ID);
		if (id == 0) {
			id = this.getPersistentData().getIntOr("DataID", (int) this.getPersistentData().getDoubleOr("DataID", 0));
			if (id != 0) {
				this.entityData.set(DATA_ID, id);
			}
		}
		return id;
	}

	private boolean killingPartner = false;

	public LaserStatueEntity findPartner() {
		int myId = this.getRoomID();
		int myGridX = (int) Math.floor(this.getX() / 29.0);
		int myGridZ = (int) Math.floor(this.getZ() / 29.0);
		for (LaserStatueEntity other : this.level().getEntitiesOfClass(LaserStatueEntity.class, this.getBoundingBox().inflate(32.0D))) {
			if (other != this && other.isAlive()) {
				int otherGridX = (int) Math.floor(other.getX() / 29.0);
				int otherGridZ = (int) Math.floor(other.getZ() / 29.0);
				if (myGridX == otherGridX && myGridZ == otherGridZ) {
					if (myId <= 0 || other.getRoomID() <= 0 || other.getRoomID() == myId) {
						return other;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void die(DamageSource damageSource) {
		super.die(damageSource);
		if (killingPartner) return;
		killingPartner = true;
		try {
			LaserStatueEntity partner = findPartner();
			if (partner != null && partner.isAlive()) {
				partner.removeEffect(MinigamesModMobEffects.BLESSED);
				if (this.level() instanceof ServerLevel serverLevel) {
					partner.hurtServer(serverLevel, damageSource, Float.MAX_VALUE);
				}
				if (partner.isAlive()) {
					partner.die(damageSource);
				}
			}
		} finally {
			killingPartner = false;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			int currentAnim = this.entityData.get(ANIM);
			if (currentAnim == 0 && !this.animationState0.isStarted()) {
				this.animationState1.stop();
				this.animationState0.start(this.tickCount);
			} else if (currentAnim == 1 && !this.animationState1.isStarted()) {
				this.animationState0.stop();
				this.animationState1.start(this.tickCount);
			}
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		getRoomID();
		LaserStatueTickProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entityIn) {
	}

	@Override
	protected void pushEntities() {
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0);
		builder = builder.add(Attributes.MAX_HEALTH, 55);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
		builder = builder.add(Attributes.FOLLOW_RANGE, 64);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 100);
		builder = builder.add(MinigamesModAttributes.DROPPED_COINS, 10);
		return builder;
	}
}