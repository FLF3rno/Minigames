/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

import net.mcreator.minigames.block.*;
import net.mcreator.minigames.MinigamesMod;

import java.util.function.Function;

public class MinigamesModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(MinigamesMod.MODID);
	public static final DeferredBlock<Block> CROWN_HUNT_CAPTURE;
	public static final DeferredBlock<Block> CASTLE_BRICKS;
	public static final DeferredBlock<Block> CASTLE_STAIRS;
	public static final DeferredBlock<Block> SPREADING_ICE;
	public static final DeferredBlock<Block> INFLATABLE_WALL_BLOCK;
	public static final DeferredBlock<Block> EXPLODING_MAGMA;
	public static final DeferredBlock<Block> SPREADING_GLUE;
	public static final DeferredBlock<Block> EMPTY_GRID_BLOCK;
	public static final DeferredBlock<Block> ROOM_GRID_BLOCK;
	public static final DeferredBlock<Block> END_ROOM_GRID_BLOCK;
	public static final DeferredBlock<Block> STARTING_ROOM_GRID_BLOCK;
	public static final DeferredBlock<Block> LOOT_ROOM_GRID_BLOCK;
	public static final DeferredBlock<Block> MINIBOSS_ROOM_GRID_BLOCK;
	public static final DeferredBlock<Block> BOSS_ROOM_GRID_BLOCK;
	public static final DeferredBlock<Block> SECRET_ROOM_GRID_BLOCK;
	static {
		CROWN_HUNT_CAPTURE = register("crown_hunt_capture", CrownHuntCaptureBlock::new);
		CASTLE_BRICKS = register("castle_bricks", CastleBricksBlock::new);
		CASTLE_STAIRS = register("castle_stairs", CastleStairsBlock::new);
		SPREADING_ICE = register("spreading_ice", SpreadingIceBlock::new);
		INFLATABLE_WALL_BLOCK = register("inflatable_wall_block", InflatableWallBlockBlock::new);
		EXPLODING_MAGMA = register("exploding_magma", ExplodingMagmaBlock::new);
		SPREADING_GLUE = register("spreading_glue", SpreadingGlueBlock::new);
		EMPTY_GRID_BLOCK = register("empty_grid_block", EmptyGridBlockBlock::new);
		ROOM_GRID_BLOCK = register("room_grid_block", RoomGridBlockBlock::new);
		END_ROOM_GRID_BLOCK = register("end_room_grid_block", EndRoomGridBlockBlock::new);
		STARTING_ROOM_GRID_BLOCK = register("starting_room_grid_block", StartingRoomGridBlockBlock::new);
		LOOT_ROOM_GRID_BLOCK = register("loot_room_grid_block", LootRoomGridBlockBlock::new);
		MINIBOSS_ROOM_GRID_BLOCK = register("miniboss_room_grid_block", MinibossRoomGridBlockBlock::new);
		BOSS_ROOM_GRID_BLOCK = register("boss_room_grid_block", BossRoomGridBlockBlock::new);
		SECRET_ROOM_GRID_BLOCK = register("secret_room_grid_block", SecretRoomGridBlockBlock::new);
	}

	// Start of user code block custom blocks
	// End of user code block custom blocks
	private static <B extends Block> DeferredBlock<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> supplier) {
		return REGISTRY.registerBlock(name, supplier);
	}
}