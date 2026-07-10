package net.mcreator.minigames.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class GrapplingHitboxEntity extends Monster {
	public static final EntityDataAccessor<String> DATA_owner = SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<String> DATA_target = SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> DATA_pullTicks = SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.INT);
	private String texture = "empty";

	public GrapplingHitboxEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_owner, "");
		builder.define(DATA_target, "");
		builder.define(DATA_pullTicks, 0);
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	@Override
	public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putString("Texture", texture);
	}

	@Override
	public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
		super.readAdditionalSaveData(input);
		texture = input.getStringOr("Texture", texture);
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes();
	}
}
