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
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.client.model.animations.stunnedAnimation;

public class StunnedEffectEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(StunnedEffectEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(StunnedEffectEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> PARENT_ID = SynchedEntityData.defineId(StunnedEffectEntity.class, EntityDataSerializers.INT);

	public final AnimationState animationState0 = new AnimationState();
	private Entity parentEntity = null;

	public StunnedEffectEntity(EntityType<StunnedEffectEntity> type, Level world) {
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
		builder.define(TEXTURE, "stunned");
		builder.define(ANIM, 0);
		builder.define(PARENT_ID, -1);
	}

	public void setParent(Entity parent) {
		this.parentEntity = parent;
		this.entityData.set(PARENT_ID, parent.getId());
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 3) {
			return;
		}
		super.handleEntityEvent(id);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		this.setYRot(this.getYRot() + 15);
		this.setYHeadRot(this.getYRot());
		this.setYBodyRot(this.getYRot());
		this.yRotO = this.getYRot();

		if (this.parentEntity == null || !this.parentEntity.isAlive()) {
			int id = this.entityData.get(PARENT_ID);
			if (id != -1) {
				this.parentEntity = this.level().getEntity(id);
			}
		}

		if (this.parentEntity != null && this.parentEntity.isAlive()) {
			if (!this.level().isClientSide() && this.tickCount > 10) {
				if (this.parentEntity instanceof LivingEntity living && !living.hasEffect(MinigamesModMobEffects.STUNNED)) {
					this.discard();
					return;
				}
			}
			this.setPos(parentEntity.getX(), parentEntity.getY() + parentEntity.getBbHeight() + 0.5, parentEntity.getZ());
			this.getAttribute(Attributes.SCALE).setBaseValue(parentEntity.getBbWidth() + 0.4);
		} else if (!this.level().isClientSide() && this.tickCount > 40) {
			this.discard();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			if (this.animationState0.isStarted()) {
				float elapsedSeconds = this.animationState0.getTimeInMillis(this.tickCount) / 1000.0F;
				if (elapsedSeconds >= stunnedAnimation.idle.lengthInSeconds()) {
					if (!stunnedAnimation.idle.looping())
						this.animationState0.stop();
					else
						this.animationState0.start(this.tickCount);
				}
			}
		}
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
	public boolean causeFallDamage(double l, float d, DamageSource source) {
		return false;
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damagesource, float amount) {
		return false;
	}

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}

	@Override
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
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
		this.move(MoverType.SELF, this.getDeltaMovement());
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
	}

	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.0);
		builder = builder.add(Attributes.MAX_HEALTH, 1000);
		builder = builder.add(Attributes.FLYING_SPEED, 0.0);
		builder = builder.add(Attributes.SCALE, 1.0);
		return builder;
	}
}
