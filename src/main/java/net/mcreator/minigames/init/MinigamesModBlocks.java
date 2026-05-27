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
	public static final DeferredBlock<Block> SPAWN_STARTING_ROOM;
	public static final DeferredBlock<Block> SPAWN_LOOT_ROOM;
	public static final DeferredBlock<Block> SPAWN_MINIBOSS_ROOM;
	public static final DeferredBlock<Block> SPAWN_BOSS_ROOM;
	public static final DeferredBlock<Block> SPAWN_SECRET_ROOM;
	public static final DeferredBlock<Block> SPAWN_ONE_DOOR_ROOM;
	public static final DeferredBlock<Block> SPAWN_TWO_DOOR_ROOM;
	public static final DeferredBlock<Block> SPAWN_FOUR_DOOR_ROOM;
	public static final DeferredBlock<Block> QUARTZ_CHAINS;
	public static final DeferredBlock<Block> CHISELED_QUARTZ_WALL;
	public static final DeferredBlock<Block> SPRUCE_BOARD;
	public static final DeferredBlock<Block> SPRUCE_PEW;
	public static final DeferredBlock<Block> SPRUCE_PEW_RIGHT;
	public static final DeferredBlock<Block> SPRUCE_PEW_LEFT;
	public static final DeferredBlock<Block> SPRUCE_SHORT_BOARD;
	public static final DeferredBlock<Block> SPAWN_WORSHIPPER;
	public static final DeferredBlock<Block> SPAWN_CANDLEHEAD;
	public static final DeferredBlock<Block> SPAWN_SHIELD_ANGEL;
	public static final DeferredBlock<Block> MOVING_BLOCK_SPAWN;
	public static final DeferredBlock<Block> FIGHT_DOORS;
	public static final DeferredBlock<Block> FLOOR_DOORS;
	public static final DeferredBlock<Block> SPAWN_TP_MARKER_BLOCK;
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
		SPAWN_STARTING_ROOM = register("spawn_starting_room", SpawnStartingRoomBlock::new);
		SPAWN_LOOT_ROOM = register("spawn_loot_room", SpawnLootRoomBlock::new);
		SPAWN_MINIBOSS_ROOM = register("spawn_miniboss_room", SpawnMinibossRoomBlock::new);
		SPAWN_BOSS_ROOM = register("spawn_boss_room", SpawnBossRoomBlock::new);
		SPAWN_SECRET_ROOM = register("spawn_secret_room", SpawnSecretRoomBlock::new);
		SPAWN_ONE_DOOR_ROOM = register("spawn_one_door_room", SpawnOneDoorRoomBlock::new);
		SPAWN_TWO_DOOR_ROOM = register("spawn_two_door_room", SpawnTwoDoorRoomBlock::new);
		SPAWN_FOUR_DOOR_ROOM = register("spawn_four_door_room", SpawnFourDoorRoomBlock::new);
		QUARTZ_CHAINS = register("quartz_chains", QuartzChainsBlock::new);
		CHISELED_QUARTZ_WALL = register("chiseled_quartz_wall", ChiseledQuartzWallBlock::new);
		SPRUCE_BOARD = register("spruce_board", SpruceBoardBlock::new);
		SPRUCE_PEW = register("spruce_pew", SprucePewBlock::new);
		SPRUCE_PEW_RIGHT = register("spruce_pew_right", SprucePewRightBlock::new);
		SPRUCE_PEW_LEFT = register("spruce_pew_left", SprucePewLeftBlock::new);
		SPRUCE_SHORT_BOARD = register("spruce_short_board", SpruceShortBoardBlock::new);
		SPAWN_WORSHIPPER = register("spawn_worshipper", SpawnWorshipperBlock::new);
		SPAWN_CANDLEHEAD = register("spawn_candlehead", SpawnCandleheadBlock::new);
		SPAWN_SHIELD_ANGEL = register("spawn_shield_angel", SpawnShieldAngelBlock::new);
		MOVING_BLOCK_SPAWN = register("moving_block_spawn", MovingBlockSpawnBlock::new);
		FIGHT_DOORS = register("fight_doors", FightDoorsBlock::new);
		FLOOR_DOORS = register("floor_doors", FloorDoorsBlock::new);
		SPAWN_TP_MARKER_BLOCK = register("spawn_tp_marker_block", SpawnTPMarkerBlockBlock::new);
	}

	// Start of user code block custom blocks
	// End of user code block custom blocks
	private static <B extends Block> DeferredBlock<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> supplier) {
		return REGISTRY.registerBlock(name, supplier);
	}
}