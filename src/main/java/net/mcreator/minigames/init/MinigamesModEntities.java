/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.entity.*;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public class MinigamesModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, MinigamesMod.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<GoldenSkeletonEntity>> GOLDEN_SKELETON = register("golden_skeleton",
			EntityType.Builder.<GoldenSkeletonEntity>of(GoldenSkeletonEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().ridingOffset(-0.6f).sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<GoldenSpiderEntity>> GOLDEN_SPIDER = register("golden_spider",
			EntityType.Builder.<GoldenSpiderEntity>of(GoldenSpiderEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune()

					.sized(0.6f, 1f));
	public static final DeferredHolder<EntityType<?>, EntityType<GoldenZombieEntity>> GOLDEN_ZOMBIE = register("golden_zombie",
			EntityType.Builder.<GoldenZombieEntity>of(GoldenZombieEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().ridingOffset(-0.6f).sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<IceDartProjectileEntity>> ICE_DART_PROJECTILE = register("ice_dart_projectile",
			EntityType.Builder.<IceDartProjectileEntity>of(IceDartProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<SnowbombProjectileEntity>> SNOWBOMB_PROJECTILE = register("snowbomb_projectile",
			EntityType.Builder.<SnowbombProjectileEntity>of(SnowbombProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<GrappleEntity>> GRAPPLE = register("grapple",
			EntityType.Builder.<GrappleEntity>of(GrappleEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<GrapplingHitboxEntity>> GRAPPLING_HITBOX = register("grappling_hitbox",
			EntityType.Builder.<GrapplingHitboxEntity>of(GrapplingHitboxEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().ridingOffset(-0.6f).sized(0.6f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<MagmaHitboxEntity>> MAGMA_HITBOX = register("magma_hitbox",
			EntityType.Builder.<MagmaHitboxEntity>of(MagmaHitboxEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().ridingOffset(-0.6f).sized(0.6f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<MagmaDartProjectileEntity>> MAGMA_DART_PROJECTILE = register("magma_dart_projectile",
			EntityType.Builder.<MagmaDartProjectileEntity>of(MagmaDartProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, registryname))));
	}

	@SubscribeEvent
	public static void init(RegisterSpawnPlacementsEvent event) {
		GoldenSkeletonEntity.init(event);
		GoldenSpiderEntity.init(event);
		GoldenZombieEntity.init(event);
		GrapplingHitboxEntity.init(event);
		MagmaHitboxEntity.init(event);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(GOLDEN_SKELETON.get(), GoldenSkeletonEntity.createAttributes().build());
		event.put(GOLDEN_SPIDER.get(), GoldenSpiderEntity.createAttributes().build());
		event.put(GOLDEN_ZOMBIE.get(), GoldenZombieEntity.createAttributes().build());
		event.put(GRAPPLING_HITBOX.get(), GrapplingHitboxEntity.createAttributes().build());
		event.put(MAGMA_HITBOX.get(), MagmaHitboxEntity.createAttributes().build());
	}
}