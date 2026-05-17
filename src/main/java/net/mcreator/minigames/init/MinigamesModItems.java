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
import net.minecraft.resources.ResourceLocation;

import net.mcreator.minigames.item.inventory.GameCompassInventoryCapability;
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
	}

	// Start of user code block custom items
	// End of user code block custom items
	private static <I extends Item> DeferredItem<I> register(String name, Function<Item.Properties, ? extends I> supplier) {
		return REGISTRY.registerItem(name, supplier, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.registerItem(block.getId().getPath(), prop -> new BlockItem(block.get(), prop), properties);
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) -> new GameCompassInventoryCapability(stack), GAME_COMPASS.get());
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class ItemsClientSideHandler {
		@SubscribeEvent
		public static void registerItemModelProperties(RegisterRangeSelectItemModelPropertyEvent event) {
			event.register(ResourceLocation.parse("minigames:magma_dart/shockwave"), MagmaDartItem.ShockwaveProperty.MAP_CODEC);
		}
	}
}