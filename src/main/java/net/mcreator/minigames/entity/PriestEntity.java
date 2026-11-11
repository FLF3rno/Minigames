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
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.procedures.PriestOnEntityTickUpdateProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.client.model.animations.priestAnimation;

public class PriestEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(PriestEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(PriestEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_ID = SynchedEntityData.defineId(PriestEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_activeTick = SynchedEntityData.defineId(PriestEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<String> DATA_target = SynchedEntityData.defineId(PriestEntity.class, EntityDataSerializers.STRING);
	public final AnimationState animationState0 = new AnimationState();
	public final AnimationState animationState1 = new AnimationState();

	public PriestEntity(EntityType<PriestEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
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
					this.animationState0.start(this.tickCount);
					break;
				case 1 :
					this.animationState1.start(this.tickCount);
					break;
			}
		}
		super.onSyncedDataUpdated(data);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "priest");
		builder.define(ANIM, 0);
		builder.define(DATA_ID, 0);
		builder.define(DATA_activeTick, 0);
		builder.define(DATA_target, "");
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.sculk_vein.hit"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.sculk.break"));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
		valueOutput.putInt("DataID", this.entityData.get(DATA_ID));
		valueOutput.putInt("DataactiveTick", this.entityData.get(DATA_activeTick));
		valueOutput.putString("Datatarget", this.entityData.get(DATA_target));
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "priest"));
		this.entityData.set(DATA_ID, valueInput.getIntOr("DataID", 0));
		this.entityData.set(DATA_activeTick, valueInput.getIntOr("DataactiveTick", 0));
		this.entityData.set(DATA_target, valueInput.getStringOr("Datatarget", ""));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			if (this.animationState0.isStarted()) {
				float elapsedSeconds = this.animationState0.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= priestAnimation.spawn.lengthInSeconds()) {
					if (!priestAnimation.spawn.looping())
						this.animationState0.stop();
					else
						this.animationState0.start(this.tickCount);
				}
			}
			if (this.animationState1.isStarted()) {
				float elapsedSeconds = this.animationState1.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= priestAnimation.idle.lengthInSeconds()) {
					if (!priestAnimation.idle.looping())
						this.animationState1.stop();
					else
						this.animationState1.start(this.tickCount);
				}
			}
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		PriestOnEntityTickUpdateProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
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
		builder = builder.add(Attributes.MAX_HEALTH, 15);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 100);
		builder = builder.add(MinigamesModAttributes.DROPPED_COINS, 4);
		return builder;
	}
}