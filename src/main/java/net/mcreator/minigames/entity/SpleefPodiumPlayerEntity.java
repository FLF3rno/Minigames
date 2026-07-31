package net.mcreator.minigames.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import javax.annotation.Nullable;
import java.util.UUID;

public class SpleefPodiumPlayerEntity extends Monster {
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(SpleefPodiumPlayerEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(SpleefPodiumPlayerEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> DATA_position = SynchedEntityData.defineId(SpleefPodiumPlayerEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<String> DATA_display_uuid = SynchedEntityData.defineId(SpleefPodiumPlayerEntity.class, EntityDataSerializers.STRING);

	public SpleefPodiumPlayerEntity(EntityType<SpleefPodiumPlayerEntity> type, Level level) {
		super(type, level);
		this.xpReward = 0;
		setNoAi(true);
		setPersistenceRequired();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TEXTURE, "empty");
		builder.define(ANIM, 0);
		builder.define(DATA_position, 0);
		builder.define(DATA_display_uuid, "");
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	public int getPosition() {
		return this.entityData.get(DATA_position);
	}

	public void setPosition(int position) {
		this.entityData.set(DATA_position, position);
	}

	@Nullable
	public UUID getDisplayUuid() {
		String raw = this.entityData.get(DATA_display_uuid);
		if (raw == null || raw.isBlank()) {
			return null;
		}
		try {
			String trimmed = raw.trim();
			if (trimmed.startsWith("[I;") && trimmed.endsWith("]")) {
				String body = trimmed.substring(3, trimmed.length() - 1).trim();
				String[] parts = body.split(",");
				if (parts.length == 4) {
					int a = Integer.parseInt(parts[0].trim());
					int b = Integer.parseInt(parts[1].trim());
					int c = Integer.parseInt(parts[2].trim());
					int d = Integer.parseInt(parts[3].trim());
					long most = ((long) a << 32) | (b & 0xffffffffL);
					long least = ((long) c << 32) | (d & 0xffffffffL);
					return new UUID(most, least);
				}
			}
			return UUID.fromString(trimmed);
		} catch (IllegalArgumentException e) {
			return null;
		}
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
	public SoundEvent getHurtSound(DamageSource damageSource) {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.death"));
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
		return super.finalizeSpawn(level, difficulty, reason, spawnData);
	}

	@Override
	public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput valueOutput) {
		super.addAdditionalSaveData(valueOutput);
		valueOutput.putString("Texture", this.getTexture());
		valueOutput.putInt("Dataposition", this.getPosition());
		valueOutput.putString("Datadisplay_uuid", this.entityData.get(DATA_display_uuid));
	}

	@Override
	public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput valueInput) {
		super.readAdditionalSaveData(valueInput);
		this.setTexture(valueInput.getStringOr("Texture", "empty"));
		this.setPosition(valueInput.getIntOr("Dataposition", 0));
		this.entityData.set(DATA_display_uuid, valueInput.getStringOr("Datadisplay_uuid", ""));
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.3)
				.add(Attributes.MAX_HEALTH, 10.0)
				.add(Attributes.ARMOR, 0.0)
				.add(Attributes.ATTACK_DAMAGE, 3.0)
				.add(Attributes.FOLLOW_RANGE, 16.0)
				.add(Attributes.STEP_HEIGHT, 0.6);
	}
}