package net.mcreator.minigames.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.entity.projectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;

import java.util.UUID;

import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.entity.GrappleEntity;

public class GrapplingHitboxEntity extends Monster {
	private static final double LEAD_BACK_OFFSET = 1.0;
	private static final double LEAD_VERTICAL_OFFSET = -0.1;
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<String> DATA_owner = SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<String> DATA_target = SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.STRING);
	// Required by GrappleEntity and GrapplingHookRightclickedProcedure.
	public static final EntityDataAccessor<Integer> DATA_pullTicks = SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.INT);

	public GrapplingHitboxEntity(EntityType<GrapplingHitboxEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(true);
		setPersistenceRequired();
		this.moveControl = new FlyingMoveControl(this, 10, true);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "empty");
		builder.define(ANIM, 0);
		builder.define(DATA_owner, "");
		builder.define(DATA_target, "");
		builder.define(DATA_pullTicks, 0);
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
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
	public Vec3 getPassengerRidingPosition(Entity entity) {
		return super.getPassengerRidingPosition(entity).add(0, -0.35F, 0);
	}

	@Override
	public Vec3 getLeashOffset(float partialTick) {
		String ownerId = this.entityData.get(DATA_owner);
		if (!ownerId.isEmpty() && this.level() instanceof ServerLevel serverLevel) {
			try {
				Entity owner = serverLevel.getEntity(UUID.fromString(ownerId));
				if (owner != null) {
					GrappleEntity grapple = serverLevel.getEntitiesOfClass(GrappleEntity.class, this.getBoundingBox().inflate(64)).stream()
							.filter(g -> g.getOwner() != null && g.getOwner().getStringUUID().equals(owner.getStringUUID())).findFirst().orElse(null);
					if (grapple != null && this.entityData.get(DATA_target).isEmpty()) {
						Vec3 backwards = grapple.getLookAngle().scale(-LEAD_BACK_OFFSET);
						return new Vec3(backwards.x, LEAD_VERTICAL_OFFSET, backwards.z);
					}
				}
			} catch (IllegalArgumentException ignored) {
			}
		}
		return super.getLeashOffset(partialTick);
	}

	@Override
	public boolean causeFallDamage(double l, float d, DamageSource source) {
		return false;
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damagesource, float amount) {
		if (damagesource.is(DamageTypes.IN_FIRE))
			return false;
		if (damagesource.getDirectEntity() instanceof AbstractArrow)
			return false;
		if (damagesource.getDirectEntity() instanceof Player)
			return false;
		if (damagesource.getDirectEntity() instanceof AbstractThrownPotion || damagesource.getDirectEntity() instanceof AreaEffectCloud || damagesource.typeHolder().is(NeoForgeMod.POISON_DAMAGE))
			return false;
		if (damagesource.is(DamageTypes.FALL))
			return false;
		if (damagesource.is(DamageTypes.CACTUS))
			return false;
		if (damagesource.is(DamageTypes.DROWN))
			return false;
		if (damagesource.is(DamageTypes.LIGHTNING_BOLT))
			return false;
		if (damagesource.is(DamageTypes.EXPLOSION) || damagesource.is(DamageTypes.PLAYER_EXPLOSION))
			return false;
		if (damagesource.is(DamageTypes.TRIDENT))
			return false;
		if (damagesource.is(DamageTypes.FALLING_ANVIL))
			return false;
		if (damagesource.is(DamageTypes.DRAGON_BREATH))
			return false;
		if (damagesource.is(DamageTypes.WITHER) || damagesource.is(DamageTypes.WITHER_SKULL))
			return false;
		return super.hurtServer(level, damagesource, amount);
	}

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}

	@Override
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
		valueOutput.putString("Dataowner", this.entityData.get(DATA_owner));
		valueOutput.putString("Datatarget", this.entityData.get(DATA_target));
		valueOutput.putInt("PullTicks", this.entityData.get(DATA_pullTicks));
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "empty"));
		this.entityData.set(DATA_owner, valueInput.getStringOr("Dataowner", ""));
		this.entityData.set(DATA_target, valueInput.getStringOr("Datatarget", ""));
		this.entityData.set(DATA_pullTicks, valueInput.getIntOr("PullTicks", 0));
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
	public void travel(Vec3 dir) {
		this.travelFlying(dir, (float) this.getAttributeValue(Attributes.FLYING_SPEED));
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
		if (!this.level().isClientSide()) {
			String ownerId = this.entityData.get(DATA_owner);
			if (!ownerId.isEmpty()) {
				try {
					Entity owner = ((ServerLevel) this.level()).getEntity(UUID.fromString(ownerId));
					if (owner != null) {
						this.setLeashedTo(owner, true);
					}
				} catch (IllegalArgumentException ignored) {
				}
			}
			String targetId = this.entityData.get(DATA_target);
			if (!targetId.isEmpty()) {
				try {
					Entity target = ((ServerLevel) this.level()).getEntity(UUID.fromString(targetId));
					Entity owner = ((ServerLevel) this.level()).getEntity(UUID.fromString(ownerId));
					if (owner != null && target instanceof LivingEntity livingTarget) {
						this.setPos(livingTarget.getX(), livingTarget.getY(), livingTarget.getZ());
						this.setDeltaMovement(0, 0, 0);
						Vec3 toOwner = owner.position().subtract(livingTarget.position());
						double distance = toOwner.length();
						if (distance <= 1.0) {
							releaseHook(owner, target);
						}
						if (distance > 0.1) {
							int pullTicks = this.entityData.get(DATA_pullTicks) + 1;
							this.entityData.set(DATA_pullTicks, pullTicks);
							double strength = Math.min(1.2, 0.4 + (pullTicks * 0.012));
							Vec3 pull = toOwner.normalize().scale(strength);
							livingTarget.setDeltaMovement(pull);
							livingTarget.hurtMarked = true;
						}
					}
				} catch (IllegalArgumentException ignored) {
				}
			}
		}
	}

	@Override
	public void leashTooFarBehaviour() {
		// Prevent the leash from snapping due to distance.
	}

	@Override
	public void onLeashRemoved() {
		// Prevent lead item drop and leash break sound spam.
	}

	@Override
	public double leashSnapDistance() {
		return 1024.0;
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
		builder = builder.add(Attributes.FLYING_SPEED, 0.3);
		return builder;
	}

	private void releaseHook(Entity owner, Entity target) {
		if (!(this.level() instanceof ServerLevel _level) || owner == null)
			return;
		if (owner instanceof LivingEntity livingOwner) {
			ItemStack main = livingOwner.getMainHandItem();
			ItemStack off = livingOwner.getOffhandItem();
			if (main.is(MinigamesModItems.GRAPPLING_HOOK.get())) {
				main.shrink(1);
			} else if (off.is(MinigamesModItems.GRAPPLING_HOOK.get())) {
				off.shrink(1);
			}
			this.level().playSound(null, livingOwner.getX(), livingOwner.getY(), livingOwner.getZ(), SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
		}

		GrappleEntity grapple = _level.getEntitiesOfClass(GrappleEntity.class, this.getBoundingBox().inflate(128)).stream()
				.filter(g -> g.getOwner() != null && g.getOwner().getStringUUID().equals(owner.getStringUUID())).findFirst().orElse(null);
		if (grapple != null) {
			grapple.discard();
		}

		if (target != null) {
			Vec3 toOwner = owner.position().subtract(target.position());
			int pullTicks = this.entityData.get(DATA_pullTicks);
			double launchSpeed = Math.min(1.6, 0.3 + (pullTicks * 0.02));
			target.setDeltaMovement(toOwner.normalize().scale(launchSpeed));
			target.hurtMarked = true;
		}
		this.discard();
	}
}
