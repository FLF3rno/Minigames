package net.mcreator.minigames.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;

import net.mcreator.minigames.procedures.MovingBlockTickProcedure;
import net.mcreator.minigames.procedures.MovingBlockRightclickedProcedure;

public class MovingBlockEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(MovingBlockEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(MovingBlockEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<String> DATA_block_id = SynchedEntityData.defineId(MovingBlockEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Boolean> DATA_breakable = SynchedEntityData.defineId(MovingBlockEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> DATA_interactable = SynchedEntityData.defineId(MovingBlockEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> DATA_movingX = SynchedEntityData.defineId(MovingBlockEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_movingY = SynchedEntityData.defineId(MovingBlockEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_movingZ = SynchedEntityData.defineId(MovingBlockEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> DATA_friction = SynchedEntityData.defineId(MovingBlockEntity.class, EntityDataSerializers.FLOAT);

	public MovingBlockEntity(EntityType<MovingBlockEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(true);
		setPersistenceRequired();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "crown");
		builder.define(ANIM, 0);
		builder.define(DATA_block_id, "minecraft:stone");
		builder.define(DATA_breakable, false);
		builder.define(DATA_interactable, false);
		builder.define(DATA_movingX, 0);
		builder.define(DATA_movingY, 0);
		builder.define(DATA_movingZ, 0);
		builder.define(DATA_friction, 0.91f);
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
		valueOutput.putString("Datablock_id", this.entityData.get(DATA_block_id));
		valueOutput.putBoolean("Databreakable", this.entityData.get(DATA_breakable));
		valueOutput.putBoolean("Datainteractable", this.entityData.get(DATA_interactable));
		valueOutput.putInt("DatamovingX", this.entityData.get(DATA_movingX));
		valueOutput.putInt("DatamovingY", this.entityData.get(DATA_movingY));
		valueOutput.putInt("DatamovingZ", this.entityData.get(DATA_movingZ));
		valueOutput.putFloat("Datafriction", this.entityData.get(DATA_friction));
	}

	@Override
	public void readAdditionalSaveData(ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "crown"));
		this.entityData.set(DATA_block_id, valueInput.getStringOr("Datablock_id", ""));
		this.entityData.set(DATA_breakable, valueInput.getBooleanOr("Databreakable", false));
		this.entityData.set(DATA_interactable, valueInput.getBooleanOr("Datainteractable", false));
		this.entityData.set(DATA_movingX, valueInput.getIntOr("DatamovingX", 0));
		this.entityData.set(DATA_movingY, valueInput.getIntOr("DatamovingY", 0));
		this.entityData.set(DATA_movingZ, valueInput.getIntOr("DatamovingZ", 0));
		this.entityData.set(DATA_friction, valueInput.getFloatOr("Datafriction", 0.91f));
	}

	@Override
	public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
		ItemStack itemstack = sourceentity.getItemInHand(hand);
		InteractionResult retval = InteractionResult.SUCCESS;
		super.mobInteract(sourceentity, hand);
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		Entity entity = this;
		Level world = this.level();

		MovingBlockRightclickedProcedure.execute(entity, sourceentity);
		return retval;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		MovingBlockTickProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return true;
	}

	@Override
	public boolean canBeCollidedWith(Entity sourceentity) {
		return true;
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 1000);
		builder = builder.add(Attributes.ARMOR, 100);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 100);
		return builder;
	}
}
