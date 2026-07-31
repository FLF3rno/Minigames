package net.mcreator.minigames.entity;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import net.mcreator.minigames.DigGraveGoal;
import net.mcreator.minigames.ChargeAttackGoal;
import net.mcreator.minigames.MoveToCoarseDirtGoal;
import net.mcreator.minigames.client.model.animations.gravediggerAnimation;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.init.MinigamesModBlocks;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class GravediggerEntity extends Monster {
	private static final String DIG_STATE_KEY = "dig_state";
	private static final String DIG_TICKS_REMAINING_KEY = "dig_ticks_remaining";
	private static final String DIG_TARGET_X_KEY = "dig_target_x";
	private static final String DIG_TARGET_Y_KEY = "dig_target_y";
	private static final String DIG_TARGET_Z_KEY = "dig_target_z";
	private static final int DIG_DURATION_TICKS = 30;
	private static final int DIG_FIRST_SOUND_TICK = 12;
	private static final int DIG_SECOND_SOUND_TICK = 24;
	private static final Identifier DIG_KB_MODIFIER_ID = Identifier.parse("minigames:gravedigger_dig_kb");

	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(GravediggerEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(GravediggerEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_ID = SynchedEntityData.defineId(GravediggerEntity.class, EntityDataSerializers.INT);
	public final AnimationState animationState1 = new AnimationState();
	public final AnimationState animationState2 = new AnimationState();
	public final AnimationState animationState3 = new AnimationState();

	public GravediggerEntity(EntityType<GravediggerEntity> type, Level world) {
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
					this.animationState1.stop();
					break;
				case -2 :
					this.animationState2.stop();
					break;
				case -3 :
					this.animationState3.stop();
					break;
				case 0 :
					this.animationState1.start(this.tickCount);
					break;
				case 1 :
					this.animationState3.start(this.tickCount);
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
		builder.define(TEXTURE, "gravedigger");
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
		this.goalSelector.addGoal(1, new DigGraveGoal(this, 20));
		this.goalSelector.addGoal(2, new MoveToCoarseDirtGoal(this, 1.3, 20));
		this.goalSelector.addGoal(3, new ChargeAttackGoal(this, 1.2, 20, 13));
		this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public @Nullable SoundEvent getHurtSound(net.minecraft.world.damagesource.@NonNull DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.pillager.hurt"));
	}

	@Override
	public @Nullable SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.pillager.death"));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
		valueOutput.putInt("DataID", this.entityData.get(DATA_ID));
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "gravedigger"));
		this.entityData.set(DATA_ID, valueInput.getIntOr("DataID", 0));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			if (!this.isDigging() && !this.animationState3.isStarted() && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4D) {
				if (!this.animationState1.isStarted()) {
					this.entityData.set(ANIM, 0);
				}
			} else if (this.animationState1.isStarted() && !this.isDigging() && !this.animationState3.isStarted()) {
				this.entityData.set(ANIM, -1);
			}
			if (this.animationState1.isStarted()) {
				float elapsedSeconds = this.animationState1.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= gravediggerAnimation.walk.lengthInSeconds() && !gravediggerAnimation.walk.looping()) {
					this.entityData.set(ANIM, -1);
				}
			}
			if (this.animationState3.isStarted()) {
				float elapsedSeconds = this.animationState3.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= gravediggerAnimation.attack.lengthInSeconds() && !gravediggerAnimation.attack.looping()) {
					this.entityData.set(ANIM, -4);
				}
			}
			if (this.animationState2.isStarted()) {
				float elapsedSeconds = this.animationState2.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= gravediggerAnimation.dig.lengthInSeconds() && !gravediggerAnimation.dig.looping()) {
					this.entityData.set(ANIM, -2);
				}
			}
			return;
		}

		int digState = this.getPersistentData().getIntOr(DIG_STATE_KEY, 0);
		if (digState <= 0) {
			return;
		}

		this.getNavigation().stop();
		this.setDeltaMovement(Vec3.ZERO);
		this.setYRot(this.yRotO);
		this.setXRot(this.xRotO);
		this.yBodyRot = this.yBodyRotO;
		this.yHeadRot = this.yBodyRotO;

		int remaining = this.getPersistentData().getIntOr(DIG_TICKS_REMAINING_KEY, DIG_DURATION_TICKS);
		int elapsed = DIG_DURATION_TICKS - remaining;
		BlockPos targetPos = BlockPos.containing(
				this.getPersistentData().getDoubleOr(DIG_TARGET_X_KEY, this.getX()),
				this.getPersistentData().getDoubleOr(DIG_TARGET_Y_KEY, this.getY()),
				this.getPersistentData().getDoubleOr(DIG_TARGET_Z_KEY, this.getZ())
		);

		if (elapsed == DIG_FIRST_SOUND_TICK) {
			this.level().playSound(null, targetPos, SoundEvents.ROOTED_DIRT_HIT, SoundSource.BLOCKS, 2.0F, 0.6F);
		}
		if (elapsed == DIG_SECOND_SOUND_TICK) {
			this.level().playSound(null, targetPos, SoundEvents.ROOTED_DIRT_HIT, SoundSource.BLOCKS, 2.0F, 1.0F);
			if (this.level() instanceof ServerLevel serverLevel) {
				this.spawnDirtBurst(serverLevel, targetPos);
				this.spawnBuriedEntity(serverLevel, targetPos);
			}
			this.endDigging();
			return;
		}

		this.getPersistentData().putInt(DIG_TICKS_REMAINING_KEY, remaining - 1);
	}

	public void startAttackAnimation() {
		this.entityData.set(ANIM, -3);
		this.entityData.set(ANIM, 1);
	}

	public void stopAttackAnimation() {
		this.entityData.set(ANIM, -3);
	}

	public void doChargedAttack(Player targetPlayer) {
		if (this.level() instanceof ServerLevel serverLevel) {
			super.doHurtTarget(serverLevel, targetPlayer);
		}
	}

	public boolean isDigging() {
		return this.getPersistentData().getIntOr(DIG_STATE_KEY, 0) > 0;
	}

	public void beginDigging(BlockPos targetPos) {
		if (this.level().isClientSide() || this.isDigging()) {
			return;
		}
		this.getNavigation().stop();
		this.setDeltaMovement(Vec3.ZERO);
		this.getPersistentData().putInt(DIG_STATE_KEY, 1);
		this.getPersistentData().putInt(DIG_TICKS_REMAINING_KEY, DIG_DURATION_TICKS);
		this.getPersistentData().putDouble(DIG_TARGET_X_KEY, targetPos.getX() + 0.5D);
		this.getPersistentData().putDouble(DIG_TARGET_Y_KEY, targetPos.getY());
		this.getPersistentData().putDouble(DIG_TARGET_Z_KEY, targetPos.getZ() + 0.5D);
		this.setDigKnockbackImmune(true);
		this.entityData.set(ANIM, 2);
		this.level().setBlockAndUpdate(targetPos, MinigamesModBlocks.EMPTY_COARSE_DIRT.get().defaultBlockState());
	}

	private void endDigging() {
		this.getPersistentData().remove(DIG_STATE_KEY);
		this.getPersistentData().remove(DIG_TICKS_REMAINING_KEY);
		this.getPersistentData().remove(DIG_TARGET_X_KEY);
		this.getPersistentData().remove(DIG_TARGET_Y_KEY);
		this.getPersistentData().remove(DIG_TARGET_Z_KEY);
		this.setDigKnockbackImmune(false);
		this.entityData.set(ANIM, -2);
	}

	private void setDigKnockbackImmune(boolean enabled) {
		if (this.getAttribute(Attributes.KNOCKBACK_RESISTANCE) == null) {
			return;
		}
		if (enabled) {
			if (this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getModifier(DIG_KB_MODIFIER_ID) == null) {
				this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addTransientModifier(new AttributeModifier(DIG_KB_MODIFIER_ID, 1.0D, AttributeModifier.Operation.ADD_VALUE));
			}
		} else {
			this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(DIG_KB_MODIFIER_ID);
		}
	}

	private void spawnDirtBurst(ServerLevel serverLevel, BlockPos targetPos) {
		Vec3 look = this.getLookAngle();
		if (look.lengthSqr() < 1.0E-4D) {
			look = Vec3.directionFromRotation(this.getXRot(), this.getYRot());
		}
		look = look.normalize();
		Vec3 origin = Vec3.atCenterOf(targetPos).add(look.scale(0.7D)).add(0.0D, 1.0D, 0.0D);
		BlockParticleOption dirtParticle = new BlockParticleOption(ParticleTypes.BLOCK, net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState());
		RandomSource random = this.random;
		for (int i = 0; i < 36; i++) {
			double spreadX = (random.nextDouble() - 0.5D) * 0.35D;
			double spreadY = random.nextDouble() * 0.12D + 0.05D;
			double spreadZ = (random.nextDouble() - 0.5D) * 0.35D;
			serverLevel.sendParticles(dirtParticle, origin.x, origin.y, origin.z, 1, look.x * 0.18D + spreadX, spreadY, look.z * 0.18D + spreadZ, 0.0D);
		}
	}

	private void spawnBuriedEntity(ServerLevel serverLevel, BlockPos targetPos) {
		TagKey<EntityType<?>> buriedTag = TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:buried"));
		var types = BuiltInRegistries.ENTITY_TYPE.stream().filter(entityType -> entityType.builtInRegistryHolder().is(buriedTag)).toList();
		if (types.isEmpty()) {
			return;
		}
		EntityType<?> entityType = types.get(this.random.nextInt(types.size()));
		BlockPos spawnPos = targetPos.above();
		Entity spawned = entityType.spawn(serverLevel, spawnPos, EntitySpawnReason.MOB_SUMMONED);
		if (spawned != null) {
			spawned.setPos(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D);
		}
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.15);
		builder = builder.add(Attributes.MAX_HEALTH, 50);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 7);
		builder = builder.add(Attributes.FOLLOW_RANGE, 24);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.15);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 3);
		builder = builder.add(MinigamesModAttributes.DROPPED_COINS, 10);
		return builder;
	}
}