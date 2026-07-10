/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.block.entity.*;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public class MinigamesModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MinigamesMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InflatableWallBlockBlockEntity>> INFLATABLE_WALL_BLOCK = register("inflatable_wall_block", MinigamesModBlocks.INFLATABLE_WALL_BLOCK, InflatableWallBlockBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExplodingMagmaBlockEntity>> EXPLODING_MAGMA = register("exploding_magma", MinigamesModBlocks.EXPLODING_MAGMA, ExplodingMagmaBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnStartingRoomBlockEntity>> SPAWN_STARTING_ROOM = register("spawn_starting_room", MinigamesModBlocks.SPAWN_STARTING_ROOM, SpawnStartingRoomBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnLootRoomBlockEntity>> SPAWN_LOOT_ROOM = register("spawn_loot_room", MinigamesModBlocks.SPAWN_LOOT_ROOM, SpawnLootRoomBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnMinibossRoomBlockEntity>> SPAWN_MINIBOSS_ROOM = register("spawn_miniboss_room", MinigamesModBlocks.SPAWN_MINIBOSS_ROOM, SpawnMinibossRoomBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnBossRoomBlockEntity>> SPAWN_BOSS_ROOM = register("spawn_boss_room", MinigamesModBlocks.SPAWN_BOSS_ROOM, SpawnBossRoomBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnSecretRoomBlockEntity>> SPAWN_SECRET_ROOM = register("spawn_secret_room", MinigamesModBlocks.SPAWN_SECRET_ROOM, SpawnSecretRoomBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnOneDoorRoomBlockEntity>> SPAWN_ONE_DOOR_ROOM = register("spawn_one_door_room", MinigamesModBlocks.SPAWN_ONE_DOOR_ROOM, SpawnOneDoorRoomBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnTwoDoorRoomBlockEntity>> SPAWN_TWO_DOOR_ROOM = register("spawn_two_door_room", MinigamesModBlocks.SPAWN_TWO_DOOR_ROOM, SpawnTwoDoorRoomBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnFourDoorRoomBlockEntity>> SPAWN_FOUR_DOOR_ROOM = register("spawn_four_door_room", MinigamesModBlocks.SPAWN_FOUR_DOOR_ROOM, SpawnFourDoorRoomBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<QuartzChainsBlockEntity>> QUARTZ_CHAINS = register("quartz_chains", MinigamesModBlocks.QUARTZ_CHAINS, QuartzChainsBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpruceBoardBlockEntity>> SPRUCE_BOARD = register("spruce_board", MinigamesModBlocks.SPRUCE_BOARD, SpruceBoardBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SprucePewBlockEntity>> SPRUCE_PEW = register("spruce_pew", MinigamesModBlocks.SPRUCE_PEW, SprucePewBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SprucePewRightBlockEntity>> SPRUCE_PEW_RIGHT = register("spruce_pew_right", MinigamesModBlocks.SPRUCE_PEW_RIGHT, SprucePewRightBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SprucePewLeftBlockEntity>> SPRUCE_PEW_LEFT = register("spruce_pew_left", MinigamesModBlocks.SPRUCE_PEW_LEFT, SprucePewLeftBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpruceShortBoardBlockEntity>> SPRUCE_SHORT_BOARD = register("spruce_short_board", MinigamesModBlocks.SPRUCE_SHORT_BOARD, SpruceShortBoardBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FightDoorsBlockEntity>> FIGHT_DOORS = register("fight_doors", MinigamesModBlocks.FIGHT_DOORS, FightDoorsBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FloorDoorsBlockEntity>> FLOOR_DOORS = register("floor_doors", MinigamesModBlocks.FLOOR_DOORS, FloorDoorsBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IntersectingEndRodsBlockEntity>> INTERSECTING_END_RODS = register("intersecting_end_rods", MinigamesModBlocks.INTERSECTING_END_RODS, IntersectingEndRodsBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FightDoorsBlockedBlockEntity>> FIGHT_DOORS_BLOCKED = register("fight_doors_blocked", MinigamesModBlocks.FIGHT_DOORS_BLOCKED, FightDoorsBlockedBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LootDoorsBlockEntity>> LOOT_DOORS = register("loot_doors", MinigamesModBlocks.LOOT_DOORS, LootDoorsBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MinibossDoorsBlockEntity>> MINIBOSS_DOORS = register("miniboss_doors", MinigamesModBlocks.MINIBOSS_DOORS, MinibossDoorsBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BossDoorsBlockEntity>> BOSS_DOORS = register("boss_doors", MinigamesModBlocks.BOSS_DOORS, BossDoorsBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WarriorItemPedestalBlockEntity>> WARRIOR_ITEM_PEDESTAL = register("warrior_item_pedestal", MinigamesModBlocks.WARRIOR_ITEM_PEDESTAL, WarriorItemPedestalBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SupportItemPedestalBlockEntity>> SUPPORT_ITEM_PEDESTAL = register("support_item_pedestal", MinigamesModBlocks.SUPPORT_ITEM_PEDESTAL, SupportItemPedestalBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ThiefItemPedestalBlockEntity>> THIEF_ITEM_PEDESTAL = register("thief_item_pedestal", MinigamesModBlocks.THIEF_ITEM_PEDESTAL, ThiefItemPedestalBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MageItemPedestalBlockEntity>> MAGE_ITEM_PEDESTAL = register("mage_item_pedestal", MinigamesModBlocks.MAGE_ITEM_PEDESTAL, MageItemPedestalBlockEntity::new);
	// Start of user code block custom block entities
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpreadingIceBlockEntity>> SPREADING_ICE = register("spreading_ice", MinigamesModBlocks.SPREADING_ICE, SpreadingIceBlockEntity::new);

	// End of user code block custom block entities
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> new BlockEntityType(supplier, block.get()));
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INFLATABLE_WALL_BLOCK.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EXPLODING_MAGMA.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPAWN_STARTING_ROOM.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPAWN_LOOT_ROOM.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPAWN_MINIBOSS_ROOM.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPAWN_BOSS_ROOM.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPAWN_SECRET_ROOM.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPAWN_ONE_DOOR_ROOM.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPAWN_TWO_DOOR_ROOM.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPAWN_FOUR_DOOR_ROOM.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, QUARTZ_CHAINS.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPRUCE_BOARD.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPRUCE_PEW.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPRUCE_PEW_RIGHT.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPRUCE_PEW_LEFT.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SPRUCE_SHORT_BOARD.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FIGHT_DOORS.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FLOOR_DOORS.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INTERSECTING_END_RODS.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FIGHT_DOORS_BLOCKED.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, LOOT_DOORS.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MINIBOSS_DOORS.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BOSS_DOORS.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, WARRIOR_ITEM_PEDESTAL.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SUPPORT_ITEM_PEDESTAL.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, THIEF_ITEM_PEDESTAL.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MAGE_ITEM_PEDESTAL.get(), SidedInvWrapper::new);
	}
}