package net.mcreator.minigames.entity;

import net.mcreator.minigames.FlavioFightManager;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.procedures.FlavioTickProcedure;
import net.mcreator.minigames.client.model.animations.flavioAnimation;

public class FlavioEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(FlavioEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(FlavioEntity.class, EntityDataSerializers.INT);
	public final AnimationState animationState0 = new AnimationState();
	public final AnimationState animationState1 = new AnimationState();
	public final AnimationState animationState3 = new AnimationState();
	public final AnimationState animationState4 = new AnimationState();

	public FlavioEntity(EntityType<FlavioEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
		setPersistenceRequired();
		refreshDimensions();
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
				case -4 :
					this.animationState3.stop();
					break;
				case -5 :
					this.animationState4.stop();
					break;
				case 0 :
					this.animationState0.start(this.tickCount);
					break;
				case 1 :
					this.animationState1.start(this.tickCount);
					break;
				case 3 :
					this.animationState3.start(this.tickCount);
					break;
				case 4 :
					this.animationState4.start(this.tickCount);
					break;
			}
		}
		super.onSyncedDataUpdated(data);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "flavio");
		builder.define(ANIM, 0);
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
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.8, false) {
			@Override
			public boolean canUse() {
				return FlavioFightManager.phase >= 5 && super.canUse();
			}
			@Override
			public boolean canContinueToUse() {
				return FlavioFightManager.phase >= 5 && super.canContinueToUse();
			}
			@Override
			protected boolean canPerformAttack(LivingEntity entity) {
				boolean canAttack = this.isTimeToAttack() && FlavioFightManager.phase >= 5
						&& this.mob.distanceToSqr(entity)
						< (this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth())
						&& this.mob.getSensing().hasLineOfSight(entity);

				if (canAttack) {
					FlavioEntity.this.entityData.set(ANIM, 1000);
					FlavioEntity.this.entityData.set(ANIM, 3);
				}

				return canAttack;
			}
		});
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true)
		{
			@Override
			public boolean canUse() {
				return FlavioFightManager.phase >= 5 && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
				return FlavioFightManager.phase >= 5 && super.canContinueToUse();
			}
		});
		this.goalSelector.addGoal(3, new FloatGoal(this));
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.death"));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "flavio"));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			if (this.animationState0.isStarted()) {
				float elapsedSeconds = this.animationState0.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= flavioAnimation.idle.lengthInSeconds()) {
					if (!flavioAnimation.idle.looping())
						this.animationState0.stop();
					else
						this.animationState0.start(this.tickCount);
				}
			}
			if (this.animationState1.isStarted()) {
				float elapsedSeconds = this.animationState1.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= flavioAnimation.press_button.lengthInSeconds()) {
					if (!flavioAnimation.press_button.looping())
						this.animationState1.stop();
					else
						this.animationState1.start(this.tickCount);
				}
			}
			if (this.animationState3.isStarted()) {
				float elapsedSeconds = this.animationState3.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= flavioAnimation.punch.lengthInSeconds()) {
					if (!flavioAnimation.punch.looping())
						this.animationState3.stop();
					else
						this.animationState3.start(this.tickCount);
				}
			}
			if (this.animationState4.isStarted()) {
				float elapsedSeconds = this.animationState4.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= flavioAnimation.death.lengthInSeconds()) {
					if (!flavioAnimation.death.looping())
						this.animationState4.stop();
					else
						this.animationState4.start(this.tickCount);
				}
			}
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		FlavioTickProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
		if (FlavioFightManager.flavio != this) {
            FlavioFightManager.flavio = this;
        }
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

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		return super.getDefaultDimensions(pose).scale(0.85f);
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.24);
		builder = builder.add(Attributes.MAX_HEALTH, 1000);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 60);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 100);
		return builder;
	}
}