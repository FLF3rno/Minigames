package net.mcreator.minigames.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class StunnedEffectEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(StunnedEffectEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> PARENT_ID = SynchedEntityData.defineId(StunnedEffectEntity.class, EntityDataSerializers.INT);
	public final AnimationState animationState0 = new AnimationState();

	public StunnedEffectEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "empty");
		builder.define(PARENT_ID, 0);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public int getParentId() {
		return this.entityData.get(PARENT_ID);
	}

	public void setParent(net.minecraft.world.entity.Entity entity) {
		this.entityData.set(PARENT_ID, entity.getId());
	}

	public static void init(RegisterSpawnPlacementsEvent event) {}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes();
	}
}
