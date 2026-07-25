package net.mcreator.minigames.entity;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.init.MinigamesModEntities;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * An invisible entity that:
 *  - Is leashed to the hook shooter (giving the free Minecraft rope visual from owner→hitbox)
 *  - Follows the GrappleEntity projectile while it is in flight (no target yet)
 *  - Once a target (player or FallingBlockEntity) is set, pulls that target toward the owner
 *    every tick with slowly accelerating speed, and snaps when close enough.
 */
public class GrapplingHitboxEntity extends Mob {

    // ── Synced data ──────────────────────────────────────────────────────────
    /** UUID string of the player who fired the hook. */
    public static final EntityDataAccessor<String> DATA_owner =
            SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.STRING);
    /** UUID string of the entity being pulled (player OR falling block). Empty = no target yet. */
    public static final EntityDataAccessor<String> DATA_target =
            SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.STRING);
    /** How many ticks the pull has been active — used to ramp up pull speed and launch velocity. */
    public static final EntityDataAccessor<Integer> DATA_pullTicks =
            SynchedEntityData.defineId(GrapplingHitboxEntity.class, EntityDataSerializers.INT);

    // ── Pull tuning ───────────────────────────────────────────────────────────
    /** Initial pull speed in blocks/tick. */
    private static final double PULL_SPEED_BASE  = 0.04;
    /** Speed increase per tick of pulling. */
    private static final double PULL_ACCEL        = 0.002;
    /** Maximum pull speed cap. */
    private static final double PULL_SPEED_MAX    = 0.5;
    /** Distance from owner at which the snap triggers (blocks). */
    private static final double SNAP_DISTANCE     = 2.5;

    // ──────────────────────────────────────────────────────────────────────────

    public GrapplingHitboxEntity(EntityType<GrapplingHitboxEntity> type, Level world) {
        super(type, world);
        setNoAi(true);
        setNoGravity(true);
        setInvisible(true);
        setPersistenceRequired();
        xpReward = 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_owner, "");
        builder.define(DATA_target, "");
        builder.define(DATA_pullTicks, 0);
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput out) {
        super.addAdditionalSaveData(out);
        out.putString("GrappleOwner",  entityData.get(DATA_owner));
        out.putString("GrappleTarget", entityData.get(DATA_target));
        out.putInt("GrapplePullTicks", entityData.get(DATA_pullTicks));
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput in) {
        super.readAdditionalSaveData(in);
        entityData.set(DATA_owner,     in.getStringOr("GrappleOwner", ""));
        entityData.set(DATA_target,    in.getStringOr("GrappleTarget", ""));
        entityData.set(DATA_pullTicks, in.getIntOr("GrapplePullTicks", 0));
    }

    // ── Tick ─────────────────────────────────────────────────────────────────

    @Override
    public void baseTick() {
        super.baseTick();
        if (level().isClientSide()) return;
        if (!(level() instanceof ServerLevel serverLevel)) return;

        String targetId = entityData.get(DATA_target);
        if (targetId.isEmpty()) return; // still in flight – projectile positions us

        String ownerId = entityData.get(DATA_owner);
        if (ownerId.isEmpty()) { discard(); return; }

        // Resolve owner
        net.minecraft.world.entity.Entity ownerEntity = null;
        try {
            ownerEntity = serverLevel.getEntity(UUID.fromString(ownerId));
        } catch (IllegalArgumentException ignored) {}
        if (ownerEntity == null || !ownerEntity.isAlive()) { discard(); return; }

        // Resolve target (player or falling block)
        net.minecraft.world.entity.Entity targetEntity = null;
        try {
            targetEntity = serverLevel.getEntity(UUID.fromString(targetId));
        } catch (IllegalArgumentException ignored) {}
        if (targetEntity == null || !targetEntity.isAlive()) { discard(); return; }

        // Increment pull ticks
        int pullTicks = entityData.get(DATA_pullTicks) + 1;
        entityData.set(DATA_pullTicks, pullTicks);

        // Compute pull vector and speed
        Vec3 ownerPos   = ownerEntity.position().add(0, ownerEntity.getEyeHeight() * 0.5, 0);
        Vec3 targetPos  = targetEntity.position().add(0, targetEntity.getBbHeight() * 0.5, 0);
        Vec3 toOwner    = ownerPos.subtract(targetPos);
        double distance = toOwner.length();

        // Snap check
        if (distance < SNAP_DISTANCE) {
            onTargetReached(ownerEntity, targetEntity, pullTicks);
            return;
        }

        double speed = Math.min(PULL_SPEED_MAX, PULL_SPEED_BASE + pullTicks * PULL_ACCEL);
        Vec3 pull = toOwner.normalize().scale(speed);

        // For FallingBlockEntity: override gravity + set movement
        if (targetEntity instanceof FallingBlockEntity fallingBlock) {
            fallingBlock.setNoGravity(true);
            fallingBlock.setDeltaMovement(pull);
            fallingBlock.hurtMarked = true;
        } else {
            // For a living entity: add pull impulse on top of existing motion
            Vec3 current = targetEntity.getDeltaMovement();
            targetEntity.setDeltaMovement(
                    current.x * 0.6 + pull.x,
                    current.y * 0.6 + pull.y,
                    current.z * 0.6 + pull.z
            );
            targetEntity.hurtMarked = true;
            if (targetEntity instanceof LivingEntity le) {
                le.fallDistance = 0f;
            }
        }

        // Keep hitbox at owner position (rope rendering)
        this.setPos(ownerEntity.getX(), ownerEntity.getY() + 1.0, ownerEntity.getZ());
    }

    // ── Snap / Release ────────────────────────────────────────────────────────

    /**
     * Called when target reaches the owner, or when the shooter releases early.
     * Launches the target toward the owner with speed based on pull duration.
     */
    public void onTargetReached(net.minecraft.world.entity.Entity owner,
                                net.minecraft.world.entity.Entity target,
                                int pullTicks) {
        if (!level().isClientSide()) {
            if (target instanceof FallingBlockEntity fallingBlock) {
                // Just discard the block — it's close enough; let it drop
                fallingBlock.setNoGravity(false);
                fallingBlock.setDeltaMovement(Vec3.ZERO);
            } else {
                // Launch the player toward the owner
                Vec3 toOwner = owner.position().subtract(target.position()).normalize();
                double launchSpeed = Math.min(1.6, 0.15 + pullTicks * 0.01);
                target.setDeltaMovement(toOwner.scale(launchSpeed));
                target.hurtMarked = true;
            }
            discard();
        }
    }

    /**
     * Early release triggered by right-clicking the hook again.
     * Calculates launch speed from current pull ticks.
     */
    public void earlyRelease() {
        if (level().isClientSide() || !(level() instanceof ServerLevel serverLevel)) return;

        String ownerId  = entityData.get(DATA_owner);
        String targetId = entityData.get(DATA_target);
        int pullTicks   = entityData.get(DATA_pullTicks);

        net.minecraft.world.entity.Entity owner = null;
        net.minecraft.world.entity.Entity target = null;
        try {
            if (!ownerId.isEmpty())  owner  = serverLevel.getEntity(UUID.fromString(ownerId));
            if (!targetId.isEmpty()) target = serverLevel.getEntity(UUID.fromString(targetId));
        } catch (IllegalArgumentException ignored) {}

        if (owner != null && target != null && target.isAlive()) {
            Vec3 toOwner = owner.position().subtract(target.position()).normalize();
            if (target instanceof FallingBlockEntity fallingBlock) {
                fallingBlock.setNoGravity(false);
                fallingBlock.setDeltaMovement(toOwner.scale(Math.min(1.4, 0.1 + pullTicks * 0.01)));
                fallingBlock.hurtMarked = true;
            } else {
                double launchSpeed = Math.min(1.6, 0.15 + pullTicks * 0.01);
                target.setDeltaMovement(toOwner.scale(launchSpeed));
                target.hurtMarked = true;
            }
        }
        discard();
    }

    // ── Required Mob overrides ────────────────────────────────────────────────

    @Override
    public boolean removeWhenFarAway(double d) { return false; }

    @Override
    public boolean isPushable() { return false; }

    @Override
    public boolean isAttackable() { return false; }

    @Override
    public ItemStack getPickResult() { return ItemStack.EMPTY; }

    public static void init(RegisterSpawnPlacementsEvent event) {}

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0);
    }
}
