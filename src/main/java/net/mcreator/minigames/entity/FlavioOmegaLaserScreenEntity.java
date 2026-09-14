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
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.registries.BuiltInRegistries;

public class FlavioOmegaLaserScreenEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(FlavioOmegaLaserScreenEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(FlavioOmegaLaserScreenEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> SCREEN_INDEX = SynchedEntityData.defineId(FlavioOmegaLaserScreenEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> LASER_ID = SynchedEntityData.defineId(FlavioOmegaLaserScreenEntity.class, EntityDataSerializers.INT);

	public FlavioOmegaLaserScreenEntity(EntityType<FlavioOmegaLaserScreenEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(true);
		setPersistenceRequired();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "schermo");
		builder.define(ANIM, 0);
		builder.define(SCREEN_INDEX, 0);
		builder.define(LASER_ID, -1);
	}

	public int getScreenIndex() {
		return this.entityData.get(SCREEN_INDEX);
	}

	public void setScreenIndex(int index) {
		this.entityData.set(SCREEN_INDEX, index);
	}

	public int getLaserId() {
		return this.entityData.get(LASER_ID);
	}

	public void setLaserId(int laserId) {
		this.entityData.set(LASER_ID, laserId);
	}

	@Override
	public net.minecraft.world.InteractionResult mobInteract(net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand) {
		if (hand == net.minecraft.world.InteractionHand.MAIN_HAND) {
			if (!this.level().isClientSide() && player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
				int laserId = getLaserId();
				net.minecraft.world.entity.Entity laserEntity = this.level().getEntity(laserId);
				if (laserEntity instanceof FlavioOmegaLaserEntity laser && laser.isAlive()) {
					laser.registerPlayerManned(serverPlayer);
					int requiredPlayers = laser.getRequiredPlayerCount();
					net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer, new net.mcreator.minigames.network.OpenOmegaLaserScreenMessage(laserId, getScreenIndex(), requiredPlayers));
					return net.minecraft.world.InteractionResult.SUCCESS;
				}
			}
			return net.minecraft.world.InteractionResult.SUCCESS;
		}
		return super.mobInteract(player, hand);
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
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.death"));
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource damagesource, float amount) {
		if (damagesource.is(DamageTypes.IN_FIRE))
			return false;
		return super.hurtServer(level, damagesource, amount);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
		valueOutput.putInt("ScreenIndex", this.getScreenIndex());
		valueOutput.putInt("LaserId", this.getLaserId());
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "schermo"));
		this.setScreenIndex(valueInput.getIntOr("ScreenIndex", 0));
		this.setLaserId(valueInput.getIntOr("LaserId", -1));
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
		return builder;
	}
}