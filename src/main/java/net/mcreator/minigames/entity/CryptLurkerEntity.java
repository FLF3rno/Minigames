package net.mcreator.minigames.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.procedures.AnimationWalkingProcedure;
import net.mcreator.minigames.procedures.ApplyTelegraphProcedure;
import net.mcreator.minigames.procedures.ExplodeProcedure;
import net.mcreator.minigames.client.model.animations.crypt_lurkerAnimation;

import java.util.EnumSet;

public class CryptLurkerEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(CryptLurkerEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(CryptLurkerEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_ID = SynchedEntityData.defineId(CryptLurkerEntity.class, EntityDataSerializers.INT);
	public final AnimationState animationState0 = new AnimationState();
	public final AnimationState animationState1 = new AnimationState();

	private int slamTicks = 0;
	private int slamCooldown = 0;

	public CryptLurkerEntity(EntityType<CryptLurkerEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
		setPersistenceRequired();
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

	public boolean isCurrentRoomActive() {
		int roomID = getRoomID();
		return roomID <= 0 || net.mcreator.minigames.network.MinigamesModVariables.MapVariables.get(this.level()).currentRoomID == roomID;
	}

	public boolean isSlamming() {
		return this.slamTicks > 0;
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
					break;
				case 1 :
					this.animationState1.start(this.tickCount);
					applySlamTelegraph();
					break;
			}
		}
		super.onSyncedDataUpdated(data);
	}

	private void applySlamTelegraph() {
		if (this.level().isClientSide()) {
			ClientTelegraphHelper.apply(this);
		}
	}

	private static class ClientTelegraphHelper {
		private static void apply(CryptLurkerEntity entity) {
			double cx = entity.getX();
			double cy = entity.getY();
			double cz = entity.getZ();
			int r = 4;
			for (int dx = -r; dx <= r; dx++) {
				for (int dz = -r; dz <= r; dz++) {
					if (dx * dx + dz * dz <= 17.5) {
						ApplyTelegraphProcedure.execute(entity.level(), cx + dx, cy, cz + dz, 25, 0);
					}
				}
			}
		}
	}

	public void performSlamExplosion() {
		if (this.level() instanceof ServerLevel serverLevel) {
			double ex = this.getX();
			double ey = this.getY();
			double ez = this.getZ();
			serverLevel.sendParticles(ParticleTypes.EXPLOSION, ex, ey + 0.5, ez, 2, 0.5, 0.2, 0.5, 0.0);
			ExplodeProcedure.execute(serverLevel, ex, ey, ez, this, true, true, 7.0, 1.0, 4.0, "groundbreaking");
			AABB hitArea = this.getBoundingBox().inflate(4.0);
			Vec3 center = this.position();
			for (LivingEntity victim : serverLevel.getEntitiesOfClass(LivingEntity.class, hitArea)) {
				if (victim != this && victim.position().distanceTo(center) <= 4.5) {
					if (victim.invulnerableTime <= 10) {
						victim.hurtServer(serverLevel, new DamageSource(serverLevel.holderOrThrow(DamageTypes.EXPLOSION), this), 7.0F);
					}
				}
			}
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "crypt_lurker");
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
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new CryptLurkerSlamGoal(this));
		this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8) {
			@Override
			public boolean canUse() {
				return CryptLurkerEntity.this.isCurrentRoomActive() && !CryptLurkerEntity.this.isSlamming() && super.canUse();
			}
		});
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this) {
			@Override
			public boolean canUse() {
				return !CryptLurkerEntity.this.isSlamming() && super.canUse();
			}
		});

		this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
			@Override
			public boolean canUse() {
				return CryptLurkerEntity.this.isCurrentRoomActive() && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
				return CryptLurkerEntity.this.isCurrentRoomActive() && super.canContinueToUse();
			}
		});
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false) {
			@Override
			public boolean canUse() {
				return CryptLurkerEntity.this.isCurrentRoomActive() && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
				return CryptLurkerEntity.this.isCurrentRoomActive() && super.canContinueToUse();
			}
		});
	}

	public static class CryptLurkerSlamGoal extends Goal {
		private final CryptLurkerEntity mob;

		public CryptLurkerSlamGoal(CryptLurkerEntity mob) {
			this.mob = mob;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public boolean canUse() {
			if (!this.mob.isCurrentRoomActive()) {
				return false;
			}
			LivingEntity target = this.mob.getTarget();
			return target != null && target.isAlive();
		}

		@Override
		public boolean canContinueToUse() {
			if (!this.mob.isCurrentRoomActive()) {
				return false;
			}
			LivingEntity target = this.mob.getTarget();
			return target != null && target.isAlive();
		}

		@Override
		public void tick() {
			LivingEntity target = this.mob.getTarget();
			if (target == null || !target.isAlive()) {
				return;
			}

			this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

			if (this.mob.isSlamming()) {
				return;
			}

			// Follow player
			this.mob.getNavigation().moveTo(target, 1.2);
		}
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
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
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
		valueOutput.putInt("DataID", this.entityData.get(DATA_ID));
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "crypt_lurker"));
		this.entityData.set(DATA_ID, valueInput.getIntOr("DataID", 0));
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			if (!this.isCurrentRoomActive()) {
				if (this.getTarget() != null) {
					this.setTarget(null);
				}
				if (this.getNavigation().isInProgress()) {
					this.getNavigation().stop();
				}
			}

			if (this.slamTicks > 0) {
				this.getNavigation().stop();
				Vec3 vel = this.getDeltaMovement();
				this.setDeltaMovement(0, vel.y, 0);

				LivingEntity target = this.getTarget();
				if (target != null && target.isAlive()) {
					this.getLookControl().setLookAt(target, 30.0F, 30.0F);
				}

				this.slamTicks++;

				// At 1.25s (25 ticks), detonate the 7 dmg groundbreaking explosion
				if (this.slamTicks == 25) {
					this.performSlamExplosion();
				}

				// At 2.0s (40 ticks), slam ends
				if (this.slamTicks >= 40) {
					this.slamTicks = 0;
					this.slamCooldown = 20;
					this.entityData.set(ANIM, 0);
				}
			} else {
				if (this.slamCooldown > 0) {
					this.slamCooldown--;
				} else if (this.isCurrentRoomActive()) {
					LivingEntity target = this.getTarget();
					if (target != null && target.isAlive()) {
						double distSq = this.distanceToSqr(target);
						if (distSq <= 20.25) { // Within 4.5 blocks
							this.slamTicks = 1;
							this.getNavigation().stop();
							Vec3 vel = this.getDeltaMovement();
							this.setDeltaMovement(0, vel.y, 0);
							this.entityData.set(ANIM, 1);
						}
					}
				}
			}
		} else {
			this.animationState0.animateWhen(AnimationWalkingProcedure.execute(this), this.tickCount);
			if (this.animationState1.isStarted()) {
				float elapsedSeconds = this.animationState1.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= crypt_lurkerAnimation.slam.lengthInSeconds()) {
					if (!crypt_lurkerAnimation.slam.looping())
						this.animationState1.stop();
					else
						this.animationState1.start(this.tickCount);
				}
			}
		}
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.22);
		builder = builder.add(Attributes.MAX_HEALTH, 80);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 7);
		builder = builder.add(Attributes.FOLLOW_RANGE, 64);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 100);

		return builder;
	}
}