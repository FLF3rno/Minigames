/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.mcreator.minigames.item.*;
import net.mcreator.minigames.MinigamesMod;

import java.util.function.Function;

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
}