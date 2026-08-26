package net.mcreator.minigames.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;

import net.mcreator.minigames.procedures.SpikeTrapTickProcedure;

public class SpikeTrapEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE =
			SynchedEntityData.defineId(
					SpikeTrapEntity.class,
					EntityDataSerializers.STRING
			);

	public static final EntityDataAccessor<Integer> ANIM =
			SynchedEntityData.defineId(
					SpikeTrapEntity.class,
					EntityDataSerializers.INT
			);

	public SpikeTrapEntity(EntityType<SpikeTrapEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(true);
		setNoGravity(true);
		setPersistenceRequired();
		setDeltaMovement(0, 0, 0);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "spike_trap");
		builder.define(ANIM, 0);
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
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "spike_trap"));
	}

	@Override
	public void baseTick() {
		setDeltaMovement(0, 0, 0);

		super.baseTick();

		setDeltaMovement(0, 0, 0);

		SpikeTrapTickProcedure.execute(
				this.level(),
				this.getX(),
				this.getY(),
				this.getZ(),
				this
		);

		setDeltaMovement(0, 0, 0);
	}

	@Override
	public boolean hurtServer(
			ServerLevel level,
			DamageSource damagesource,
			float amount
	) {
		if (damagesource.is(DamageTypes.GENERIC_KILL)) {
			return super.hurtServer(level, damagesource, amount);
		}

		return false;
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
	public boolean isNoGravity() {
		return true;
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.0);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.0);
		return builder;
	}
}