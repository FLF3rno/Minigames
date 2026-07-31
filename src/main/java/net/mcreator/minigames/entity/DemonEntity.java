package net.mcreator.minigames.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.procedures.DemonTickProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;

public class DemonEntity extends Monster {
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(DemonEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> ANIM = SynchedEntityData.defineId(DemonEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_ID = SynchedEntityData.defineId(DemonEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_cooldown = SynchedEntityData.defineId(DemonEntity.class, EntityDataSerializers.INT);
    
    public final AnimationState animationState0 = new AnimationState();
    public final AnimationState animationState1 = new AnimationState();

    public DemonEntity(EntityType<DemonEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setPersistenceRequired();
        refreshDimensions();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (ANIM.equals(data)) {
            switch (this.entityData.get(ANIM)) {
                case -1 -> this.animationState0.stop();
                case -2 -> this.animationState1.stop();
                case 0 -> this.animationState0.start(this.tickCount);
                case 1 -> this.animationState1.start(this.tickCount);
            }
        }
        super.onSyncedDataUpdated(data);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TEXTURE, "demon");
        builder.define(ANIM, 0);
        builder.define(DATA_ID, 0);
        builder.define(DATA_cooldown, -40);
    }

    public void setTexture(String texture) {
        this.entityData.set(TEXTURE, texture);
    }

    public String getTexture() {
        return this.entityData.get(TEXTURE);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.warden.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.warden.death"));
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putString("Texture", this.getTexture());
        valueOutput.putInt("DataID", this.entityData.get(DATA_ID));
        valueOutput.putInt("Datacooldown", this.entityData.get(DATA_cooldown));
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.setTexture(valueInput.getStringOr("Texture", "demon"));
        this.entityData.set(DATA_ID, valueInput.getIntOr("DataID", 0));
        this.entityData.set(DATA_cooldown, valueInput.getIntOr("Datacooldown", 0));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            int currentAnim = this.entityData.get(ANIM);

            if (currentAnim == 0) {
                this.animationState0.startIfStopped(this.tickCount);
            } else if (currentAnim == 1) {
                this.animationState1.startIfStopped(this.tickCount);
            }
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        DemonTickProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return super.getDefaultDimensions(pose).scale(1.5f);
    }

    public static void init(RegisterSpawnPlacementsEvent event) {
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 75);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 10);
        builder = builder.add(MinigamesModAttributes.DROPPED_COINS, 10);

        return builder;
    }
}