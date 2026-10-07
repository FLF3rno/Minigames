package net.mcreator.minigames.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.BlockTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.ApplyEffectProcedure;
import net.mcreator.minigames.init.MinigamesModSounds;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.client.model.animations.church_sharpenerAnimation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.Set;
import java.util.List;
import java.util.Collections;

public class ChurchSharpenerEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(ChurchSharpenerEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(ChurchSharpenerEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_ID = SynchedEntityData.defineId(ChurchSharpenerEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> PINNED_TARGET_ID = SynchedEntityData.defineId(ChurchSharpenerEntity.class, EntityDataSerializers.INT);

	public static final int STATE_SHARPENING = 0;
	public static final int STATE_CHARGING = 1;
	public static final int STATE_ATTACKING = 2;
	public static final int STATE_STUNNED = 3;

	private static final Set<UUID> PINNED_VICTIMS = Collections.newSetFromMap(new ConcurrentHashMap<>());

	public final AnimationState animationState0 = new AnimationState();
	public final AnimationState animationState1 = new AnimationState();
	public final AnimationState animationState2 = new AnimationState();

	private int state = STATE_SHARPENING;
	private int stateTicks = 50;
	private int soundTicks = 0;
	private LivingEntity pinnedTarget = null;

	public ChurchSharpenerEntity(EntityType<ChurchSharpenerEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
		setPersistenceRequired();
	}

	public int getState() {
		return this.state;
	}

	public static boolean isVictimPinned(Entity entity) {
		if (entity == null) {
			return false;
		}
		if (!entity.level().isClientSide()) {
			return PINNED_VICTIMS.contains(entity.getUUID());
		}
		for (ChurchSharpenerEntity sharpener : entity.level().getEntitiesOfClass(ChurchSharpenerEntity.class, entity.getBoundingBox().inflate(4.0))) {
			if (sharpener.entityData.get(PINNED_TARGET_ID) == entity.getId()) {
				return true;
			}
		}
		return false;
	}

	private void unpinVictim() {
		if (this.pinnedTarget != null) {
			PINNED_VICTIMS.remove(this.pinnedTarget.getUUID());
			if (this.pinnedTarget instanceof LivingEntity living) {
				living.removeEffect(MinigamesModMobEffects.SLOWDOWN);
			}
			this.pinnedTarget = null;
		}
		this.entityData.set(PINNED_TARGET_ID, -1);
	}

	@Override
	public void push(double x, double y, double z) {
		if (this.state == STATE_CHARGING) {
			return;
		}
		super.push(x, y, z);
	}

	@Override
	public void push(Entity entity) {
		if (this.state == STATE_CHARGING) {
			return;
		}
		super.push(entity);
	}

	@Override
	public void knockback(double strength, double x, double z) {
		if (this.state == STATE_CHARGING) {
			return;
		}
		super.knockback(strength, x, z);
	}

	@Override
	public boolean isPushable() {
		if (this.state == STATE_CHARGING) {
			return false;
		}
		return super.isPushable();
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
				case -3 :
					this.animationState2.stop();
					break;
				case 0 :
					this.animationState1.stop();
					this.animationState2.stop();
					this.animationState0.start(this.tickCount);
					break;
				case 1 :
					this.animationState0.stop();
					this.animationState2.stop();
					this.animationState1.start(this.tickCount);
					break;
				case 2 :
					this.animationState0.stop();
					this.animationState1.stop();
					this.animationState2.start(this.tickCount);
					break;
			}
		}
		super.onSyncedDataUpdated(data);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "church_sharpener");
		builder.define(ANIM, 0);
		builder.define(DATA_ID, 0);
		builder.define(PINNED_TARGET_ID, -1);
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
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
		var mapVars = net.mcreator.minigames.network.MinigamesModVariables.MapVariables.get(this.level());
		if (roomID > 0) {
			return mapVars.currentRoomID == roomID;
		}
		if (!mapVars.playingDungeons) {
			return true;
		}
		Player player = this.level().getNearestPlayer(this, 64.0);
		if (player == null) return false;
		int playerGridX = (int) Math.floor(player.getX() / 29.0);
		int playerGridZ = (int) Math.floor(player.getZ() / 29.0);
		int entityGridX = (int) Math.floor(this.getX() / 29.0);
		int entityGridZ = (int) Math.floor(this.getZ() / 29.0);
		return playerGridX == entityGridX && playerGridZ == entityGridZ;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true) {
			@Override
			public boolean canUse() {
				return ChurchSharpenerEntity.this.isCurrentRoomActive() && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
				return ChurchSharpenerEntity.this.isCurrentRoomActive() && super.canContinueToUse();
			}
		});
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
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
		valueOutput.putInt("DataID", this.entityData.get(DATA_ID));
		valueOutput.putInt("SharpenerState", this.state);
		valueOutput.putInt("StateTicks", this.stateTicks);
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "church_sharpener"));
		this.entityData.set(DATA_ID, valueInput.getIntOr("DataID", 0));
		this.state = valueInput.getIntOr("SharpenerState", STATE_SHARPENING);
		this.stateTicks = valueInput.getIntOr("StateTicks", 50);
	}

	private boolean isDirectWallHit() {
		double rad = Math.toRadians(this.getYRot());
		double fX = -Math.sin(rad);
		double fZ = Math.cos(rad);
		double checkX = this.getX() + fX * 0.65;
		double checkZ = this.getZ() + fZ * 0.65;
		BlockPos midPos = BlockPos.containing(checkX, this.getY() + 0.8, checkZ);
		BlockState midState = this.level().getBlockState(midPos);
		if (midState.getBlock() instanceof StairBlock || midState.is(BlockTags.STAIRS)) {
			return false;
		}
		if (midState.getBlock() instanceof SlabBlock || midState.is(BlockTags.SLABS)) {
			return false;
		}
		if (midState.getCollisionShape(this.level(), midPos).isEmpty()) {
			return false;
		}
		BlockPos footPos = BlockPos.containing(checkX, this.getY() + 0.2, checkZ);
		BlockState footState = this.level().getBlockState(footPos);
		if (footState.getBlock() instanceof StairBlock || footState.is(BlockTags.STAIRS)) {
			return false;
		}
		return true;
	}

	@Override
	public void travel(Vec3 travelVector) {
		if (this.isAlive()) {
			if (this.state == STATE_SHARPENING || this.state == STATE_ATTACKING || this.state == STATE_STUNNED) {
				super.travel(Vec3.ZERO);
				return;
			}
			if (this.state == STATE_CHARGING) {
				Vec3 vel = this.getDeltaMovement();
				if (!this.isNoGravity()) {
					vel = vel.add(0, -0.08, 0);
				}
				this.move(MoverType.SELF, vel);
				if (!this.level().isClientSide() && this.horizontalCollision && (Math.abs(vel.x) > 0.05 || Math.abs(vel.z) > 0.05) && this.isDirectWallHit()) {
					this.state = STATE_STUNNED;
					this.stateTicks = 50;
					this.entityData.set(ANIM, 2);
					this.setDeltaMovement(0, 0, 0);
					super.hurtServer((ServerLevel) this.level(), this.damageSources().generic(), 5.0F);
					return;
				}
				Vec3 afterMove = this.getDeltaMovement();
				double drag = this.onGround() ? 0.95 : 0.98;
				this.setDeltaMovement(afterMove.x * drag, afterMove.y * 0.98, afterMove.z * drag);
				return;
			}
		}
		super.travel(travelVector);
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damagesource, float amount) {
		if (this.state == STATE_ATTACKING) {
			this.unpinVictim();
			this.state = STATE_STUNNED;
			this.stateTicks = 50;
			this.entityData.set(ANIM, 2);
			this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
			this.level().playSound(null, this.getX(), this.getY(), this.getZ(), MinigamesModSounds.PVP_SWORDSHIT.get(), SoundSource.HOSTILE, 1.0F, 2.0F);
			Entity attacker = damagesource.getEntity() != null ? damagesource.getEntity() : damagesource.getDirectEntity();
			if (attacker != null) {
				double dx = attacker.getX() - this.getX();
				double dz = attacker.getZ() - this.getZ();
				float hitYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
				this.setYRot(hitYaw);
				this.yRotO = hitYaw;
				this.setYHeadRot(hitYaw);
				this.yBodyRot = hitYaw;
			}
			return false;
		}
		return super.hurtServer(level, damagesource, amount);
	}

	@Override
	public void die(DamageSource damageSource) {
		this.unpinVictim();
		super.die(damageSource);
	}

	@Override
	public void remove(Entity.RemovalReason reason) {
		this.unpinVictim();
		super.remove(reason);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			int currentAnim = this.entityData.get(ANIM);
			if (currentAnim == 0 && !this.animationState0.isStarted()) {
				this.animationState1.stop();
				this.animationState2.stop();
				this.animationState0.start(this.tickCount);
			} else if (currentAnim == 1 && !this.animationState1.isStarted()) {
				this.animationState0.stop();
				this.animationState2.stop();
				this.animationState1.start(this.tickCount);
			} else if (currentAnim == 2 && !this.animationState2.isStarted()) {
				this.animationState0.stop();
				this.animationState1.stop();
				this.animationState2.start(this.tickCount);
			}

			if (this.animationState0.isStarted()) {
				float elapsedSeconds = this.animationState0.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= church_sharpenerAnimation.sharpening.lengthInSeconds()) {
					if (!church_sharpenerAnimation.sharpening.looping())
						this.animationState0.stop();
					else
						this.animationState0.start(this.tickCount);
				}
			}
			if (this.animationState1.isStarted()) {
				float elapsedSeconds = this.animationState1.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= church_sharpenerAnimation.chase.lengthInSeconds()) {
					this.animationState1.start(this.tickCount);
				}
			}
			if (this.animationState2.isStarted()) {
				float elapsedSeconds = this.animationState2.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= church_sharpenerAnimation.stun.lengthInSeconds()) {
					if (!church_sharpenerAnimation.stun.looping())
						this.animationState2.stop();
					else
						this.animationState2.start(this.tickCount);
				}
			}
		} else {
			if (!this.isAlive())
				return;

			if (this.state == STATE_SHARPENING) {
				this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
				if (this.entityData.get(ANIM) != 0) {
					this.entityData.set(ANIM, 0);
				}
				if (!this.isCurrentRoomActive()) {
					this.stateTicks = 20;
					return;
				}
				this.stateTicks--;
				if (this.stateTicks == 42 || this.stateTicks == 19) {
					this.level().playSound(null, this.getX(), this.getY(), this.getZ(), MinigamesModSounds.PVP_SWORDSHIT.get(), SoundSource.HOSTILE, 1.0F, 0.6F);
				}
				if (this.stateTicks <= 0) {
					this.state = STATE_CHARGING;
					this.entityData.set(ANIM, 1);
					this.soundTicks = 0;
					LivingEntity target = this.getTarget();
					if (target == null || !target.isAlive() || (target instanceof Player player && (player.isCreative() || player.isSpectator()))) {
						Player nearestPlayer = this.level().getNearestPlayer(this, 32.0);
						if (nearestPlayer != null && (nearestPlayer.isCreative() || nearestPlayer.isSpectator() || !nearestPlayer.isAlive())) {
							nearestPlayer = null;
						}
						target = nearestPlayer;
						if (target != null) {
							this.setTarget(target);
						}
					}
					if (target != null) {
						double dx = target.getX() - this.getX();
						double dz = target.getZ() - this.getZ();
						float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
						this.setYRot(targetYaw);
						this.yRotO = targetYaw;
						this.setYHeadRot(targetYaw);
						this.yBodyRot = targetYaw;
					}
				}
			} else if (this.state == STATE_CHARGING) {
				if (!this.isCurrentRoomActive()) {
					this.state = STATE_SHARPENING;
					this.stateTicks = 50;
					this.entityData.set(ANIM, 0);
					this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
					this.setTarget(null);
					return;
				}
				this.soundTicks++;
				if (this.soundTicks % 2 == 0) {
					this.level().playSound(null, this.getX(), this.getY(), this.getZ(), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.witch.throw")), SoundSource.HOSTILE, 0.7F, 2.0F);
				}

				AABB hitBox = this.getBoundingBox().inflate(0.35);
				List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, hitBox, p -> p.isAlive() && !p.isCreative() && !p.isSpectator());
				if (!nearbyPlayers.isEmpty()) {
					Player hitPlayer = nearbyPlayers.get(0);
					this.pinnedTarget = hitPlayer;
					this.state = STATE_ATTACKING;
					this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
					PINNED_VICTIMS.add(hitPlayer.getUUID());
					this.entityData.set(PINNED_TARGET_ID, hitPlayer.getId());
					ApplyEffectProcedure.execute(this.level(), this.pinnedTarget, false, 50, 6000, "minigames:slowdown");
					return;
				}

				LivingEntity target = this.getTarget();
				if (target == null || !target.isAlive() || (target instanceof Player player && (player.isCreative() || player.isSpectator()))) {
					Player nearestPlayer = this.level().getNearestPlayer(this, 32.0);
					if (nearestPlayer != null && (nearestPlayer.isCreative() || nearestPlayer.isSpectator() || !nearestPlayer.isAlive())) {
						nearestPlayer = null;
					}
					target = nearestPlayer;
					if (target != null) {
						this.setTarget(target);
					}
				}

				if (target != null) {
					double dx = target.getX() - this.getX();
					double dz = target.getZ() - this.getZ();
					float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
					float currentYaw = this.getYRot();
					float yawDiff = Mth.wrapDegrees(targetYaw - currentYaw);
					float turnRate = 4.0F;
					float newYaw = currentYaw + Mth.clamp(yawDiff, -turnRate, turnRate);
					this.setYRot(newYaw);
					this.yRotO = newYaw;
					this.setYHeadRot(newYaw);
					this.yBodyRot = newYaw;
				}

				float rad = (float) Math.toRadians(this.getYRot());
				double forwardX = -Math.sin(rad);
				double forwardZ = Math.cos(rad);
				double targetSpeed = 0.45;
				Vec3 vel = this.getDeltaMovement();
				double blend = 0.12;
				double newVx = vel.x * (1.0 - blend) + (forwardX * targetSpeed) * blend;
				double newVz = vel.z * (1.0 - blend) + (forwardZ * targetSpeed) * blend;
				this.setDeltaMovement(newVx, vel.y, newVz);
			} else if (this.state == STATE_ATTACKING) {
				this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
				if (this.pinnedTarget == null || !this.pinnedTarget.isAlive() || this.distanceToSqr(this.pinnedTarget) > 6.25 || (this.pinnedTarget instanceof Player p && (p.isCreative() || p.isSpectator()))) {
					this.unpinVictim();
					this.state = STATE_CHARGING;
					this.soundTicks = 0;
					return;
				}
				double dx = this.pinnedTarget.getX() - this.getX();
				double dz = this.pinnedTarget.getZ() - this.getZ();
				float faceYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
				this.setYRot(faceYaw);
				this.yRotO = faceYaw;
				this.setYHeadRot(faceYaw);
				this.yBodyRot = faceYaw;
				PINNED_VICTIMS.add(this.pinnedTarget.getUUID());
				this.entityData.set(PINNED_TARGET_ID, this.pinnedTarget.getId());
				Vec3 targetVel = this.pinnedTarget.getDeltaMovement();
				this.pinnedTarget.hurtServer((ServerLevel) this.level(), this.damageSources().mobAttack(this), 1.0F);
				this.pinnedTarget.setDeltaMovement(targetVel);
			} else if (this.state == STATE_STUNNED) {
				this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
				if (this.entityData.get(ANIM) != 2) {
					this.entityData.set(ANIM, 2);
				}
				this.stateTicks--;
				if (this.stateTicks <= 0) {
					this.state = STATE_SHARPENING;
					this.stateTicks = 50;
					this.entityData.set(ANIM, 0);
				}
			}
		}
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 40);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 64);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(MinigamesModAttributes.DROPPED_COINS, 10);
		return builder;
	}
}