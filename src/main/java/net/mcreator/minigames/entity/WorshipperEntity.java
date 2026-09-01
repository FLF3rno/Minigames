package net.mcreator.minigames.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.HealthMultiplierProcedure;
import net.mcreator.minigames.client.model.animations.worshipperAnimation;

import javax.annotation.Nullable;

import net.mcreator.minigames.init.MinigamesModAttributes;

public class WorshipperEntity extends Monster {
	private static final String LOCAL_ATTACK_COOLDOWN_KEY = "local_attack_cooldown";
	private static final String ATTACK_ANIM_TICKS_KEY = "attack_anim_ticks";
	private static final int LOCAL_ATTACK_COOLDOWN_TICKS = 18;
	private static final int ATTACK_ANIM_DURATION_TICKS = 18;
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(WorshipperEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(WorshipperEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_ID = SynchedEntityData.defineId(WorshipperEntity.class, EntityDataSerializers.INT);
	public final AnimationState animationState0 = new AnimationState();
	public final AnimationState animationState2 = new AnimationState();

	public WorshipperEntity(EntityType<WorshipperEntity> type, Level world) {
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
				case -3 :
					this.animationState2.stop();
					break;
				case 0 :
					this.animationState0.start(this.tickCount);
					break;
				case 2 :
					this.animationState2.start(this.tickCount);
					break;
			}
		}
		super.onSyncedDataUpdated(data);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "worshipper");
		builder.define(ANIM, 0);
		builder.define(DATA_ID, 0);
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
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true) {
			@Override
			protected boolean canPerformAttack(LivingEntity entity) {
				return this.isTimeToAttack() && this.mob.distanceToSqr(entity) < (this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth()) && this.mob.getSensing().hasLineOfSight(entity);
			}
		});
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(4, new FloatGoal(this));
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.skeleton.ambient"));
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.skeleton.step")), 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.skeleton.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.skeleton.death"));
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData livingdata) {
		return super.finalizeSpawn(world, difficulty, reason, livingdata);
	}

	@Override
	public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
		valueOutput.putInt("DataID", this.entityData.get(DATA_ID));
	}

	@Override
	public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "worshipper"));
		this.entityData.set(DATA_ID, valueInput.getIntOr("DataID", 0));
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			int localAttackCooldown = this.getPersistentData().getIntOr(LOCAL_ATTACK_COOLDOWN_KEY, 0);
			if (localAttackCooldown > 0) {
				this.getPersistentData().putInt(LOCAL_ATTACK_COOLDOWN_KEY, localAttackCooldown - 1);
			}
			int attackAnimTicks = this.getPersistentData().getIntOr(ATTACK_ANIM_TICKS_KEY, 0);
			if (attackAnimTicks > 0) {
				attackAnimTicks--;
				this.getPersistentData().putInt(ATTACK_ANIM_TICKS_KEY, attackAnimTicks);
				if (attackAnimTicks == 0) {
					this.entityData.set(ANIM, -3);
				}
			}
		}
		if (this.level().isClientSide()) {
			if (this.animationState0.isStarted()) {
				float elapsedSeconds = this.animationState0.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= worshipperAnimation.idle.lengthInSeconds()) {
					if (!worshipperAnimation.idle.looping())
						this.animationState0.stop();
					else
						this.animationState0.start(this.tickCount);
				}
			}
		}
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity target) {
		int localAttackCooldown = this.getPersistentData().getIntOr(LOCAL_ATTACK_COOLDOWN_KEY, 0);
		if (localAttackCooldown > 0) {
			return false;
		}
		boolean attacked = super.doHurtTarget(level, target);
		if (attacked) {
			this.getPersistentData().putInt(LOCAL_ATTACK_COOLDOWN_KEY, LOCAL_ATTACK_COOLDOWN_TICKS);
			this.getPersistentData().putInt(ATTACK_ANIM_TICKS_KEY, ATTACK_ANIM_DURATION_TICKS);
			this.entityData.set(ANIM, 2);
		}
		return attacked;
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.2);
		builder = builder.add(Attributes.MAX_HEALTH, 15);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 32);
		builder = builder.add(Attributes.STEP_HEIGHT, 2);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.15);
		builder = builder.add(MinigamesModAttributes.DROPPED_COINS, 1);
		return builder;
	}
}
