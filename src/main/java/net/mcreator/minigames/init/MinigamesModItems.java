/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.item.inventory.GameCompassInventoryCapability;
import net.mcreator.minigames.item.inventory.DungeonCompassInventoryCapability;
import net.mcreator.minigames.item.*;
import net.mcreator.minigames.MinigamesMod;

import java.util.function.Function;

@EventBusSubscriber
public class MinigamesModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(MinigamesMod.MODID);
	public static final DeferredItem<Item> GAME_COMPASS;
	public static final DeferredItem<Item> CROWN_HELMET_HELMET;
	public static final DeferredItem<Item> CROWN_HUNT_CAPTURE;
	public static final DeferredItem<Item> CASTLE_BRICKS;
	public static final DeferredItem<Item> CASTLE_STAIRS;
	public static final DeferredItem<Item> SPLEEF_SHOVEL;
	public static final DeferredItem<Item> ICE_DART;
	public static final DeferredItem<Item> THRUSTERS;
	public static final DeferredItem<Item> INFLATABLE_WALL;
	public static final DeferredItem<Item> SNOWBOMB;
	public static final DeferredItem<Item> GRAPPLING_HOOK;
	public static final DeferredItem<Item> REPLAY_SPLEEF;
	public static final DeferredItem<Item> SYMMETRICAL_SHOVEL;
	public static final DeferredItem<Item> MAGMA_DART;
	public static final DeferredItem<Item> SNOW_SHOVEL;
	public static final DeferredItem<Item> HYPNOTIC_PENDULUM;
	public static final DeferredItem<Item> GLUE_DART;
	public static final DeferredItem<Item> BLANK_DAGGER;
	public static final DeferredItem<Item> BLANK_SWORD;
	public static final DeferredItem<Item> BLANK_LONG_SWORD;
	public static final DeferredItem<Item> QUARTZ_CHAINS;
	public static final DeferredItem<Item> CHISELED_QUARTZ_WALL;
	public static final DeferredItem<Item> SPRUCE_BOARD;
	public static final DeferredItem<Item> SPRUCE_PEW;
	public static final DeferredItem<Item> SPRUCE_PEW_RIGHT;
	public static final DeferredItem<Item> SPRUCE_PEW_LEFT;
	public static final DeferredItem<Item> SPRUCE_SHORT_BOARD;
	public static final DeferredItem<Item> SPAWN_WORSHIPPER;
	public static final DeferredItem<Item> SPAWN_CANDLEHEAD;
	public static final DeferredItem<Item> SPAWN_SHIELD_ANGEL;
	public static final DeferredItem<Item> MOVING_BLOCK_SPAWN;
	public static final DeferredItem<Item> FIGHT_DOORS;
	public static final DeferredItem<Item> FLOOR_DOORS;
	public static final DeferredItem<Item> SPAWN_TP_MARKER_BLOCK;
	public static final DeferredItem<Item> SPAWN_GRAVEDIGGER;
	public static final DeferredItem<Item> EMPTY_COARSE_DIRT;
	public static final DeferredItem<Item> INTERSECTING_END_RODS;
	public static final DeferredItem<Item> SULFUR_BLOCK;
	public static final DeferredItem<Item> POTENT_SULFUR;
	public static final DeferredItem<Item> POLISHED_SULFUR;
	public static final DeferredItem<Item> CHISELED_SULFUR;
	public static final DeferredItem<Item> SULFUR_BRICKS;
	public static final DeferredItem<Item> SULFUR_SLAB;
	public static final DeferredItem<Item> SULFUR_STAIRS;
	public static final DeferredItem<Item> SULFUR_WALL;
	public static final DeferredItem<Item> POLISHED_SULFUR_SLAB;
	public static final DeferredItem<Item> POLISHED_SULFUR_STAIRS;
	public static final DeferredItem<Item> POLISHED_SULFUR_WALL;
	public static final DeferredItem<Item> SULFUR_BRICK_SLAB;
	public static final DeferredItem<Item> SULFUR_BRICK_STAIRS;
	public static final DeferredItem<Item> SULFUR_BRICK_WALL;
	public static final DeferredItem<Item> CINNABAR;
	public static final DeferredItem<Item> POLISHED_CINNABAR;
	public static final DeferredItem<Item> CHISELED_CINNABAR;
	public static final DeferredItem<Item> CINNABAR_BRICKS;
	public static final DeferredItem<Item> CINNABAR_SLAB;
	public static final DeferredItem<Item> CINNABAR_STAIRS;
	public static final DeferredItem<Item> CINNABAR_WALL;
	public static final DeferredItem<Item> POLISHED_CINNABAR_SLAB;
	public static final DeferredItem<Item> POLISHED_CINNABAR_STAIRS;
	public static final DeferredItem<Item> POLISHED_CINNABAR_WALL;
	public static final DeferredItem<Item> CINNABAR_BRICK_SLAB;
	public static final DeferredItem<Item> CINNABAR_BRICK_STAIRS;
	public static final DeferredItem<Item> CINNABAR_BRICK_WALL;
	public static final DeferredItem<Item> FIGHT_DOORS_BLOCKED;
	public static final DeferredItem<Item> LOOT_DOORS;
	public static final DeferredItem<Item> MINIBOSS_DOORS;
	public static final DeferredItem<Item> BOSS_DOORS;
	public static final DeferredItem<Item> SPAWN_PREACHER;
	public static final DeferredItem<Item> SPAWN_DEMON;
	public static final DeferredItem<Item> VOLLEY_BOMB;
	public static final DeferredItem<Item> PLAGUE_STARTER;
	public static final DeferredItem<Item> GHOSTIFIER;
	public static final DeferredItem<Item> CRYSTALLIZED_POTION;
	public static final DeferredItem<Item> BLACKSMITH_HAMMER;
	public static final DeferredItem<Item> PHASE_CLOAK;
	public static final DeferredItem<Item> WIND_SCYTHE;
	public static final DeferredItem<Item> BLESSED_CURSED_CROSSBOW;
	public static final DeferredItem<Item> DUNGEON_COMPASS;
	public static final DeferredItem<Item> COMBO_DAGGER;
	public static final DeferredItem<Item> WARRIOR_ITEM_PEDESTAL;
	public static final DeferredItem<Item> SUPPORT_ITEM_PEDESTAL;
	public static final DeferredItem<Item> THIEF_ITEM_PEDESTAL;
	public static final DeferredItem<Item> MAGE_ITEM_PEDESTAL;
	public static final DeferredItem<Item> TUFF_PEDESTAL;
	public static final DeferredItem<Item> BLANK_PEDESTAL_P_1;
	public static final DeferredItem<Item> BLANK_PEDESTAL_P_2;
	public static final DeferredItem<Item> BLANK_PEDESTAL_P_3;
	public static final DeferredItem<Item> BLANK_PEDESTAL_P_4;
	public static final DeferredItem<Item> SPAWN_SCULKLING;
	public static final DeferredItem<Item> STOP_SIGN;
	public static final DeferredItem<Item> HAMMER;
	public static final DeferredItem<Item> THE_FINISHER;
	public static final DeferredItem<Item> HUMAN_CANNONBALL;
	public static final DeferredItem<Item> TIME_CLOCK;
	public static final DeferredItem<Item> SNATCHING_CLAW;
	public static final DeferredItem<Item> PLUG;
	public static final DeferredItem<Item> CLUSTER_BOMB;
	public static final DeferredItem<Item> REACTION_TIME;
	public static final DeferredItem<Item> SILENT_ASSASSIN;
	public static final DeferredItem<Item> POWER_HARVESTER;
	public static final DeferredItem<Item> PARASITE_SCYTHE;
	public static final DeferredItem<Item> HORSESHOE;
	public static final DeferredItem<Item> SLAM;
	public static final DeferredItem<Item> BOX_OF_FUN;
	static {
		GAME_COMPASS = register("game_compass", GameCompassItem::new);
		CROWN_HELMET_HELMET = register("crown_helmet_helmet", CrownHelmetItem.Helmet::new);
		CROWN_HUNT_CAPTURE = block(MinigamesModBlocks.CROWN_HUNT_CAPTURE);
		CASTLE_BRICKS = block(MinigamesModBlocks.CASTLE_BRICKS);
		CASTLE_STAIRS = block(MinigamesModBlocks.CASTLE_STAIRS);
		SPLEEF_SHOVEL = register("spleef_shovel", SpleefShovelItem::new);
		ICE_DART = register("ice_dart", IceDartItem::new);
		THRUSTERS = register("thrusters", ThrustersItem::new);
		INFLATABLE_WALL = register("inflatable_wall", InflatableWallItem::new);
		SNOWBOMB = register("snowbomb", SnowbombItem::new);
		GRAPPLING_HOOK = register("grappling_hook", GrapplingHookItem::new);
		REPLAY_SPLEEF = register("replay_spleef", ReplaySpleefItem::new);
		SYMMETRICAL_SHOVEL = register("symmetrical_shovel", SymmetricalShovelItem::new);
		MAGMA_DART = register("magma_dart", MagmaDartItem::new);
		SNOW_SHOVEL = register("snow_shovel", SnowShovelItem::new);
		HYPNOTIC_PENDULUM = register("hypnotic_pendulum", HypnoticPendulumItem::new);
		GLUE_DART = register("glue_dart", GlueDartItem::new);
		BLANK_DAGGER = register("blank_dagger", BlankDaggerItem::new);
		BLANK_SWORD = register("blank_sword", BlankSwordItem::new);
		BLANK_LONG_SWORD = register("blank_long_sword", BlankLongSwordItem::new);
		QUARTZ_CHAINS = block(MinigamesModBlocks.QUARTZ_CHAINS);
		CHISELED_QUARTZ_WALL = block(MinigamesModBlocks.CHISELED_QUARTZ_WALL);
		SPRUCE_BOARD = block(MinigamesModBlocks.SPRUCE_BOARD);
		SPRUCE_PEW = block(MinigamesModBlocks.SPRUCE_PEW);
		SPRUCE_PEW_RIGHT = block(MinigamesModBlocks.SPRUCE_PEW_RIGHT);
		SPRUCE_PEW_LEFT = block(MinigamesModBlocks.SPRUCE_PEW_LEFT);
		SPRUCE_SHORT_BOARD = block(MinigamesModBlocks.SPRUCE_SHORT_BOARD);
		SPAWN_WORSHIPPER = block(MinigamesModBlocks.SPAWN_WORSHIPPER);
		SPAWN_CANDLEHEAD = block(MinigamesModBlocks.SPAWN_CANDLEHEAD);
		SPAWN_SHIELD_ANGEL = block(MinigamesModBlocks.SPAWN_SHIELD_ANGEL);
		MOVING_BLOCK_SPAWN = block(MinigamesModBlocks.MOVING_BLOCK_SPAWN, new Item.Properties().rarity(Rarity.EPIC));
		FIGHT_DOORS = block(MinigamesModBlocks.FIGHT_DOORS);
		FLOOR_DOORS = block(MinigamesModBlocks.FLOOR_DOORS);
		SPAWN_TP_MARKER_BLOCK = block(MinigamesModBlocks.SPAWN_TP_MARKER_BLOCK);
		SPAWN_GRAVEDIGGER = block(MinigamesModBlocks.SPAWN_GRAVEDIGGER);
		EMPTY_COARSE_DIRT = block(MinigamesModBlocks.EMPTY_COARSE_DIRT);
		INTERSECTING_END_RODS = block(MinigamesModBlocks.INTERSECTING_END_RODS);
		SULFUR_BLOCK = block(MinigamesModBlocks.SULFUR_BLOCK);
		POTENT_SULFUR = block(MinigamesModBlocks.POTENT_SULFUR);
		POLISHED_SULFUR = block(MinigamesModBlocks.POLISHED_SULFUR);
		CHISELED_SULFUR = block(MinigamesModBlocks.CHISELED_SULFUR);
		SULFUR_BRICKS = block(MinigamesModBlocks.SULFUR_BRICKS);
		SULFUR_SLAB = block(MinigamesModBlocks.SULFUR_SLAB);
		SULFUR_STAIRS = block(MinigamesModBlocks.SULFUR_STAIRS);
		SULFUR_WALL = block(MinigamesModBlocks.SULFUR_WALL);
		POLISHED_SULFUR_SLAB = block(MinigamesModBlocks.POLISHED_SULFUR_SLAB);
		POLISHED_SULFUR_STAIRS = block(MinigamesModBlocks.POLISHED_SULFUR_STAIRS);
		POLISHED_SULFUR_WALL = block(MinigamesModBlocks.POLISHED_SULFUR_WALL);
		SULFUR_BRICK_SLAB = block(MinigamesModBlocks.SULFUR_BRICK_SLAB);
		SULFUR_BRICK_STAIRS = block(MinigamesModBlocks.SULFUR_BRICK_STAIRS);
		SULFUR_BRICK_WALL = block(MinigamesModBlocks.SULFUR_BRICK_WALL);
		CINNABAR = block(MinigamesModBlocks.CINNABAR);
		POLISHED_CINNABAR = block(MinigamesModBlocks.POLISHED_CINNABAR);
		CHISELED_CINNABAR = block(MinigamesModBlocks.CHISELED_CINNABAR);
		CINNABAR_BRICKS = block(MinigamesModBlocks.CINNABAR_BRICKS);
		CINNABAR_SLAB = block(MinigamesModBlocks.CINNABAR_SLAB);
		CINNABAR_STAIRS = block(MinigamesModBlocks.CINNABAR_STAIRS);
		CINNABAR_WALL = block(MinigamesModBlocks.CINNABAR_WALL);
		POLISHED_CINNABAR_SLAB = block(MinigamesModBlocks.POLISHED_CINNABAR_SLAB);
		POLISHED_CINNABAR_STAIRS = block(MinigamesModBlocks.POLISHED_CINNABAR_STAIRS);
		POLISHED_CINNABAR_WALL = block(MinigamesModBlocks.POLISHED_CINNABAR_WALL);
		CINNABAR_BRICK_SLAB = block(MinigamesModBlocks.CINNABAR_BRICK_SLAB);
		CINNABAR_BRICK_STAIRS = block(MinigamesModBlocks.CINNABAR_BRICK_STAIRS);
		CINNABAR_BRICK_WALL = block(MinigamesModBlocks.CINNABAR_BRICK_WALL);
		FIGHT_DOORS_BLOCKED = block(MinigamesModBlocks.FIGHT_DOORS_BLOCKED);
		LOOT_DOORS = block(MinigamesModBlocks.LOOT_DOORS);
		MINIBOSS_DOORS = block(MinigamesModBlocks.MINIBOSS_DOORS);
		BOSS_DOORS = block(MinigamesModBlocks.BOSS_DOORS);
		SPAWN_PREACHER = block(MinigamesModBlocks.SPAWN_PREACHER);
		SPAWN_DEMON = block(MinigamesModBlocks.SPAWN_DEMON);
		VOLLEY_BOMB = register("volley_bomb", VolleyBombItem::new);
		PLAGUE_STARTER = register("plague_starter", PlagueStarterItem::new);
		GHOSTIFIER = register("ghostifier", GhostifierItem::new);
		CRYSTALLIZED_POTION = register("crystallized_potion", CrystallizedPotionItem::new);
		BLACKSMITH_HAMMER = register("blacksmith_hammer", BlacksmithHammerItem::new);
		PHASE_CLOAK = register("phase_cloak", PhaseCloakItem::new);
		WIND_SCYTHE = register("wind_scythe", WindScytheItem::new);
		BLESSED_CURSED_CROSSBOW = register("blessed_cursed_crossbow", BlessedCursedCrossbowItem::new);
		DUNGEON_COMPASS = register("dungeon_compass", DungeonCompassItem::new);
		COMBO_DAGGER = register("combo_dagger", ComboDaggerItem::new);
		WARRIOR_ITEM_PEDESTAL = block(MinigamesModBlocks.WARRIOR_ITEM_PEDESTAL);
		SUPPORT_ITEM_PEDESTAL = block(MinigamesModBlocks.SUPPORT_ITEM_PEDESTAL);
		THIEF_ITEM_PEDESTAL = block(MinigamesModBlocks.THIEF_ITEM_PEDESTAL);
		MAGE_ITEM_PEDESTAL = block(MinigamesModBlocks.MAGE_ITEM_PEDESTAL);
		TUFF_PEDESTAL = block(MinigamesModBlocks.TUFF_PEDESTAL);
		BLANK_PEDESTAL_P_1 = block(MinigamesModBlocks.BLANK_PEDESTAL_P_1);
		BLANK_PEDESTAL_P_2 = block(MinigamesModBlocks.BLANK_PEDESTAL_P_2);
		BLANK_PEDESTAL_P_3 = block(MinigamesModBlocks.BLANK_PEDESTAL_P_3);
		BLANK_PEDESTAL_P_4 = block(MinigamesModBlocks.BLANK_PEDESTAL_P_4);
		SPAWN_SCULKLING = block(MinigamesModBlocks.SPAWN_SCULKLING);
		STOP_SIGN = register("stop_sign", StopSignItem::new);
		HAMMER = register("hammer", HammerItem::new);
		THE_FINISHER = register("the_finisher", TheFinisherItem::new);
		HUMAN_CANNONBALL = register("human_cannonball", HumanCannonballItem::new);
		TIME_CLOCK = register("time_clock", TimeClockItem::new);
		SNATCHING_CLAW = register("snatching_claw", SnatchingClawItem::new);
		PLUG = register("plug", PlugItem::new);
		CLUSTER_BOMB = register("cluster_bomb", ClusterBombItem::new);
		REACTION_TIME = register("reaction_time", ReactionTimeItem::new);
		SILENT_ASSASSIN = register("silent_assassin", SilentAssassinItem::new);
		POWER_HARVESTER = register("power_harvester", PowerHarvesterItem::new);
		PARASITE_SCYTHE = register("parasite_scythe", ParasiteScytheItem::new);
		HORSESHOE = register("horseshoe", HorseshoeItem::new);
		SLAM = register("slam", SlamItem::new);
		BOX_OF_FUN = register("box_of_fun", BoxOfFunItem::new);
	}

	// Start of user code block custom items
	// End of user code block custom items
	private static <I extends Item> DeferredItem<I> register(String name, Function<Item.Properties, ? extends I> supplier) {
		return REGISTRY.registerItem(name, supplier, Item.Properties::new);
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.registerItem(block.getId().getPath(), prop -> new BlockItem(block.get(), prop), () -> properties);
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.Item.ITEM, (stack, access) -> new GameCompassInventoryCapability(access), GAME_COMPASS.get());
		event.registerItem(Capabilities.Item.ITEM, (stack, access) -> new DungeonCompassInventoryCapability(access), DUNGEON_COMPASS.get());
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class ItemsClientSideHandler {
		@SubscribeEvent
		public static void registerItemModelProperties(RegisterRangeSelectItemModelPropertyEvent event) {
			event.register(Identifier.parse("minigames:magma_dart/shockwave"), MagmaDartItem.ShockwaveProperty.MAP_CODEC);
			event.register(Identifier.parse("minigames:blessed_cursed_crossbow/loadstate"), BlessedCursedCrossbowItem.LoadstateProperty.MAP_CODEC);
		}
	}
}