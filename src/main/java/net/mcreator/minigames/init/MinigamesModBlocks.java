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
	static {
		CROWN_HUNT_CAPTURE = register("crown_hunt_capture", CrownHuntCaptureBlock::new);
		CASTLE_BRICKS = register("castle_bricks", CastleBricksBlock::new);
		CASTLE_STAIRS = register("castle_stairs", CastleStairsBlock::new);
		SPREADING_ICE = register("spreading_ice", SpreadingIceBlock::new);
		INFLATABLE_WALL_BLOCK = register("inflatable_wall_block", InflatableWallBlockBlock::new);
		EXPLODING_MAGMA = register("exploding_magma", ExplodingMagmaBlock::new);
	}

	// Start of user code block custom blocks
	// End of user code block custom blocks
	private static <B extends Block> DeferredBlock<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> supplier) {
		return REGISTRY.registerBlock(name, supplier);
	}
}