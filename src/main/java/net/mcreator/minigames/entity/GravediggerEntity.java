package net.mcreator.minigames.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class GravediggerEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(GravediggerEntity.class, EntityDataSerializers.STRING);
	public final AnimationState animationState1 = new AnimationState();
	public final AnimationState animationState2 = new AnimationState();
	public final AnimationState animationState3 = new AnimationState();
	private boolean digging;

	public GravediggerEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "gravedigger");
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public boolean isDigging() {
		return digging;
	}

	public void startAttackAnimation() {
		this.digging = true;
	}

	public void stopAttackAnimation() {
		this.digging = false;
	}

	public void doChargedAttack(net.minecraft.world.entity.player.Player player) {
		this.digging = false;
	}

	public void beginDigging(net.minecraft.core.BlockPos pos) {
		this.digging = true;
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes();
	}

	@Override
	public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putString("Texture", getTexture());
	}

	@Override
	public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
		super.readAdditionalSaveData(input);
		setTexture(input.getStringOr("Texture", "gravedigger"));
	}
}
