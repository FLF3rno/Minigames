package net.mcreator.minigames.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class ShieldAngelEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(ShieldAngelEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> DATA_timeSinceLastHit = SynchedEntityData.defineId(ShieldAngelEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_cooldown = SynchedEntityData.defineId(ShieldAngelEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_HP = SynchedEntityData.defineId(ShieldAngelEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_ID = SynchedEntityData.defineId(ShieldAngelEntity.class, EntityDataSerializers.INT);

	public ShieldAngelEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "empty");
		builder.define(DATA_timeSinceLastHit, 0);
		builder.define(DATA_cooldown, 0);
		builder.define(DATA_HP, 3);
		builder.define(DATA_ID, 0);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public int getParentId() {
		return this.entityData.get(DATA_ID);
	}

	public void setParentId(int id) {
		this.entityData.set(DATA_ID, id);
	}

	public static void init(RegisterSpawnPlacementsEvent event) {}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes();
	}
}
