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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.entity.*;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public class MinigamesModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, MinigamesMod.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<GoldenSpiderEntity>> GOLDEN_SPIDER = register("golden_spider",
			EntityType.Builder.<GoldenSpiderEntity>of(GoldenSpiderEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().notInPeaceful().sized(0.6f, 1f));
	public static final DeferredHolder<EntityType<?>, EntityType<IceDartProjectileEntity>> ICE_DART_PROJECTILE = register("ice_dart_projectile",
			EntityType.Builder.<IceDartProjectileEntity>of(IceDartProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<SnowbombProjectileEntity>> SNOWBOMB_PROJECTILE = register("snowbomb_projectile",
			EntityType.Builder.<SnowbombProjectileEntity>of(SnowbombProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<GrappleEntity>> GRAPPLE = register("grapple",
			EntityType.Builder.<GrappleEntity>of(GrappleEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<MagmaHitboxEntity>> MAGMA_HITBOX = register("magma_hitbox",
			EntityType.Builder.<MagmaHitboxEntity>of(MagmaHitboxEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().ridingOffset(-0.6f).notInPeaceful().sized(0.6f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<MagmaDartProjectileEntity>> MAGMA_DART_PROJECTILE = register("magma_dart_projectile",
			EntityType.Builder.<MagmaDartProjectileEntity>of(MagmaDartProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<GlueProjectileEntity>> GLUE_PROJECTILE = register("glue_projectile",
			EntityType.Builder.<GlueProjectileEntity>of(GlueProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<SpleefPodiumPlayerEntity>> SPLEEF_PODIUM_PLAYER = register("spleef_podium_player",
			EntityType.Builder.<SpleefPodiumPlayerEntity>of(SpleefPodiumPlayerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).ridingOffset(-0.6f).notInPeaceful().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<StunnedEffectEntity>> STUNNED_EFFECT = register("stunned_effect",
			EntityType.Builder.<StunnedEffectEntity>of(StunnedEffectEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().notInPeaceful().sized(0.6f, 0.2f));
	public static final DeferredHolder<EntityType<?>, EntityType<WorshipperEntity>> WORSHIPPER = register("worshipper",
			EntityType.Builder.<WorshipperEntity>of(WorshipperEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(26).setUpdateInterval(3).notInPeaceful().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<CandleheadEntity>> CANDLEHEAD = register("candlehead",
			EntityType.Builder.<CandleheadEntity>of(CandleheadEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setUpdateInterval(3).fireImmune().notInPeaceful().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<ShieldAngelEntity>> SHIELD_ANGEL = register("shield_angel",
			EntityType.Builder.<ShieldAngelEntity>of(ShieldAngelEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setUpdateInterval(3).fireImmune().notInPeaceful().sized(1.6f, 2.2f));
	public static final DeferredHolder<EntityType<?>, EntityType<MovingBlockEntity>> MOVING_BLOCK = register("moving_block",
			EntityType.Builder.<MovingBlockEntity>of(MovingBlockEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(1f, 1f));
	public static final DeferredHolder<EntityType<?>, EntityType<VolcanicSpewEntity>> VOLCANIC_SPEW = register("volcanic_spew",
			EntityType.Builder.<VolcanicSpewEntity>of(VolcanicSpewEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.2f, 0.2f));
	public static final DeferredHolder<EntityType<?>, EntityType<GravediggerEntity>> GRAVEDIGGER = register("gravedigger",
			EntityType.Builder.<GravediggerEntity>of(GravediggerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(26).setUpdateInterval(3).notInPeaceful().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<PreacherEntity>> PREACHER = register("preacher",
			EntityType.Builder.<PreacherEntity>of(PreacherEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(26).setUpdateInterval(3).notInPeaceful().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<PreachingShotEntity>> PREACHING_SHOT = register("preaching_shot",
			EntityType.Builder.<PreachingShotEntity>of(PreachingShotEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<VolleybombEntityEntity>> VOLLEYBOMB_ENTITY = register("volleybomb_entity",
			EntityType.Builder.<VolleybombEntityEntity>of(VolleybombEntityEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().notInPeaceful().sized(0.8f, 0.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<PlagueMiddleEntity>> PLAGUE_MIDDLE = register("plague_middle",
			EntityType.Builder.<PlagueMiddleEntity>of(PlagueMiddleEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().ridingOffset(-0.6f).notInPeaceful().sized(0.1f, 0.1f));
	public static final DeferredHolder<EntityType<?>, EntityType<BlessedArrowEntity>> BLESSED_ARROW = register("blessed_arrow",
			EntityType.Builder.<BlessedArrowEntity>of(BlessedArrowEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<DemonEntity>> DEMON = register("demon",
			EntityType.Builder.<DemonEntity>of(DemonEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(26).setUpdateInterval(3).notInPeaceful().sized(1.8f, 2.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<SculklingEntity>> SCULKLING = register("sculkling",
			EntityType.Builder.<SculklingEntity>of(SculklingEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(26).setUpdateInterval(3).notInPeaceful().sized(0.7f, 0.9f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlavioEntity>> FLAVIO = register("flavio",
			EntityType.Builder.<FlavioEntity>of(FlavioEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(0.8f, 2.4f));
	public static final DeferredHolder<EntityType<?>, EntityType<BlessingDispenserEntity>> BLESSING_DISPENSER = register("blessing_dispenser",
			EntityType.Builder.<BlessingDispenserEntity>of(BlessingDispenserEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(0.8f, 3.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlavioTrapdoorEntity>> FLAVIO_TRAPDOOR = register("flavio_trapdoor",
			EntityType.Builder.<FlavioTrapdoorEntity>of(FlavioTrapdoorEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().notInPeaceful().sized(1f, 0.99f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlavioTeslaCoilEntity>> FLAVIO_TESLA_COIL = register("flavio_tesla_coil",
			EntityType.Builder.<FlavioTeslaCoilEntity>of(FlavioTeslaCoilEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(1.35f, 4.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlavioAntennaEntity>> FLAVIO_ANTENNA = register("flavio_antenna",
			EntityType.Builder.<FlavioAntennaEntity>of(FlavioAntennaEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(2f, 5.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<SpikeTrapEntity>> SPIKE_TRAP = register("spike_trap",
			EntityType.Builder.<SpikeTrapEntity>of(SpikeTrapEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(1.4f, 0.9f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlavioClockCannonEntity>> FLAVIO_CLOCK_CANNON = register("flavio_clock_cannon",
			EntityType.Builder.<FlavioClockCannonEntity>of(FlavioClockCannonEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(1f, 4.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<CannonballEntity>> CANNONBALL = register("cannonball",
			EntityType.Builder.<CannonballEntity>of(CannonballEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.6f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlavioSweeperEntity>> FLAVIO_SWEEPER = register("flavio_sweeper",
			EntityType.Builder.<FlavioSweeperEntity>of(FlavioSweeperEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(0.7f, 2f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlavioTrapdoor2Entity>> FLAVIO_TRAPDOOR_2 = register("flavio_trapdoor_2",
			EntityType.Builder.<FlavioTrapdoor2Entity>of(FlavioTrapdoor2Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().notInPeaceful().sized(1f, 0.99f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlavioTrapdoor3Entity>> FLAVIO_TRAPDOOR_3 = register("flavio_trapdoor_3",
			EntityType.Builder.<FlavioTrapdoor3Entity>of(FlavioTrapdoor3Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().notInPeaceful().sized(1f, 0.99f));
	public static final DeferredHolder<EntityType<?>, EntityType<PlayerCageEntity>> PLAYER_CAGE = register("player_cage",
			EntityType.Builder.<PlayerCageEntity>of(PlayerCageEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(1.2f, 2.2f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlavioOmegaLaserEntity>> FLAVIO_OMEGA_LASER = register("flavio_omega_laser",
			EntityType.Builder.<FlavioOmegaLaserEntity>of(FlavioOmegaLaserEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).notInPeaceful().sized(1.35f, 4.5f));
	// Start of user code block custom entities
	public static final DeferredHolder<EntityType<?>, EntityType<GrapplingHitboxEntity>> GRAPPLING_HITBOX = register("grappling_hitbox",
			EntityType.Builder.<GrapplingHitboxEntity>of(GrapplingHitboxEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.1f, 0.1f));

	// End of user code block custom entities
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, registryname))));
	}

	@SubscribeEvent
	public static void init(RegisterSpawnPlacementsEvent event) {
		GoldenSpiderEntity.init(event);
		MagmaHitboxEntity.init(event);
		SpleefPodiumPlayerEntity.init(event);
		StunnedEffectEntity.init(event);
		WorshipperEntity.init(event);
		CandleheadEntity.init(event);
		ShieldAngelEntity.init(event);
		MovingBlockEntity.init(event);
		GravediggerEntity.init(event);
		PreacherEntity.init(event);
		VolleybombEntityEntity.init(event);
		PlagueMiddleEntity.init(event);
		DemonEntity.init(event);
		SculklingEntity.init(event);
		FlavioEntity.init(event);
		BlessingDispenserEntity.init(event);
		FlavioTrapdoorEntity.init(event);
		FlavioTeslaCoilEntity.init(event);
		FlavioAntennaEntity.init(event);
		SpikeTrapEntity.init(event);
		FlavioClockCannonEntity.init(event);
		FlavioSweeperEntity.init(event);
		FlavioTrapdoor2Entity.init(event);
		FlavioTrapdoor3Entity.init(event);
		PlayerCageEntity.init(event);
		FlavioOmegaLaserEntity.init(event);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(GOLDEN_SPIDER.get(), GoldenSpiderEntity.createAttributes().build());
		event.put(MAGMA_HITBOX.get(), MagmaHitboxEntity.createAttributes().build());
		event.put(SPLEEF_PODIUM_PLAYER.get(), SpleefPodiumPlayerEntity.createAttributes().build());
		event.put(STUNNED_EFFECT.get(), StunnedEffectEntity.createAttributes().build());
		event.put(WORSHIPPER.get(), WorshipperEntity.createAttributes().build());
		event.put(CANDLEHEAD.get(), CandleheadEntity.createAttributes().build());
		event.put(SHIELD_ANGEL.get(), ShieldAngelEntity.createAttributes().build());
		event.put(MOVING_BLOCK.get(), MovingBlockEntity.createAttributes().build());
		event.put(GRAVEDIGGER.get(), GravediggerEntity.createAttributes().build());
		event.put(PREACHER.get(), PreacherEntity.createAttributes().build());
		event.put(VOLLEYBOMB_ENTITY.get(), VolleybombEntityEntity.createAttributes().build());
		event.put(PLAGUE_MIDDLE.get(), PlagueMiddleEntity.createAttributes().build());
		event.put(DEMON.get(), DemonEntity.createAttributes().build());
		event.put(SCULKLING.get(), SculklingEntity.createAttributes().build());
		event.put(FLAVIO.get(), FlavioEntity.createAttributes().build());
		event.put(BLESSING_DISPENSER.get(), BlessingDispenserEntity.createAttributes().build());
		event.put(FLAVIO_TRAPDOOR.get(), FlavioTrapdoorEntity.createAttributes().build());
		event.put(FLAVIO_TESLA_COIL.get(), FlavioTeslaCoilEntity.createAttributes().build());
		event.put(FLAVIO_ANTENNA.get(), FlavioAntennaEntity.createAttributes().build());
		event.put(SPIKE_TRAP.get(), SpikeTrapEntity.createAttributes().build());
		event.put(FLAVIO_CLOCK_CANNON.get(), FlavioClockCannonEntity.createAttributes().build());
		event.put(FLAVIO_SWEEPER.get(), FlavioSweeperEntity.createAttributes().build());
		event.put(FLAVIO_TRAPDOOR_2.get(), FlavioTrapdoor2Entity.createAttributes().build());
		event.put(FLAVIO_TRAPDOOR_3.get(), FlavioTrapdoor3Entity.createAttributes().build());
		event.put(PLAYER_CAGE.get(), PlayerCageEntity.createAttributes().build());
		event.put(FLAVIO_OMEGA_LASER.get(), FlavioOmegaLaserEntity.createAttributes().build());
	}
}