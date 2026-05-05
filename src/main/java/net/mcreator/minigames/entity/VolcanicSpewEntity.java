package net.mcreator.minigames.entity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import java.util.List;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder.Reference;

import net.mcreator.minigames.procedures.VolcanicSpewFlyingProcedure;
import net.mcreator.minigames.init.MinigamesModEntities;

import javax.annotation.Nullable;

public class VolcanicSpewEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(Blocks.AIR);
	private int knockback = 0;

	public VolcanicSpewEntity(EntityType<? extends VolcanicSpewEntity> type, Level world) {
		super(type, world);
	}

	public VolcanicSpewEntity(EntityType<? extends VolcanicSpewEntity> type, double x, double y, double z, Level world, @Nullable ItemStack firedFromWeapon) {
		super(type, x, y, z, world, PROJECTILE_ITEM, firedFromWeapon);
		if (firedFromWeapon != null)
			setKnockback(EnchantmentHelper.getItemEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), firedFromWeapon));
	}

	public VolcanicSpewEntity(EntityType<? extends VolcanicSpewEntity> type, LivingEntity entity, Level world, @Nullable ItemStack firedFromWeapon) {
		super(type, entity, world, PROJECTILE_ITEM, firedFromWeapon);
		if (firedFromWeapon != null)
			setKnockback(EnchantmentHelper.getItemEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), firedFromWeapon));
	}

	@Override
	public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(Blocks.AIR);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	public void setKnockback(int knockback) {
		this.knockback = knockback;
	}

	@Override
	protected void doKnockback(LivingEntity livingEntity, DamageSource damageSource) {
		if (knockback > 0.0) {
			double d1 = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
			Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(knockback * 0.6 * d1);
			if (vec3.lengthSqr() > 0.0) {
				livingEntity.push(vec3.x, 0.1, vec3.z);
			}
		} else { 
			super.doKnockback(livingEntity, damageSource);
		}
	}

	@Override
    public void tick() {
        super.tick();
        VolcanicSpewFlyingProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ());

        if (this.isInGround()) {
            if (!this.level().isClientSide && this.tickCount % 5 == 0) {
                
                // This creates a 3x2x3 block area centered on the projectile
                AABB damageZone = new AABB(
                    this.getX() - 0.25, this.getY() - 0.25, this.getZ() - 0.25,
                    this.getX() + 0.25, this.getY() + 0.25, this.getZ() + 0.25
                );

                List<Player> players = this.level().getEntitiesOfClass(Player.class, damageZone);

                for (Player player : players) {
                    if (player.isAlive() && !player.isCreative() && !player.isSpectator()) {
                        player.hurt(this.damageSources().hotFloor(), 1.0F);
                        player.igniteForSeconds(2);
                    }
                }
            }

            // Ensure it disappears after 10-15 seconds
            if (this.tickCount > 200) {
                this.discard();
            }
        }
    }
	public static VolcanicSpewEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 5, 5);
	}

	public static VolcanicSpewEntity shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
		return shoot(world, entity, source, pullingPower * 1f, 5, 5);
	}

	public static VolcanicSpewEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		VolcanicSpewEntity entityarrow = new VolcanicSpewEntity(MinigamesModEntities.VOLCANIC_SPEW.get(), entity, world, null);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		return entityarrow;
	}

	public static VolcanicSpewEntity shoot(LivingEntity entity, LivingEntity target) {
		VolcanicSpewEntity entityarrow = new VolcanicSpewEntity(MinigamesModEntities.VOLCANIC_SPEW.get(), entity, entity.level(), null);
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(5);
		entityarrow.setKnockback(5);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		return entityarrow;
	}

	@Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("minigames:volcanic_spew_land")).map(Reference::value).orElse(SoundEvents.ARROW_HIT);
    }
}