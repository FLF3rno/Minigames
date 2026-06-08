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
	public static final DeferredBlock<Block> SPAWN_GRAVEDIGGER;
	public static final DeferredBlock<Block> EMPTY_COARSE_DIRT;
	public static final DeferredBlock<Block> INTERSECTING_END_RODS;
	public static final DeferredBlock<Block> SULFUR_BLOCK;
	public static final DeferredBlock<Block> POTENT_SULFUR;
	public static final DeferredBlock<Block> POLISHED_SULFUR;
	public static final DeferredBlock<Block> CHISELED_SULFUR;
	public static final DeferredBlock<Block> SULFUR_BRICKS;
	public static final DeferredBlock<Block> SULFUR_SLAB;
	public static final DeferredBlock<Block> SULFUR_STAIRS;
	public static final DeferredBlock<Block> SULFUR_WALL;
	public static final DeferredBlock<Block> POLISHED_SULFUR_SLAB;
	public static final DeferredBlock<Block> POLISHED_SULFUR_STAIRS;
	public static final DeferredBlock<Block> POLISHED_SULFUR_WALL;
	public static final DeferredBlock<Block> SULFUR_BRICK_SLAB;
	public static final DeferredBlock<Block> SULFUR_BRICK_STAIRS;
	public static final DeferredBlock<Block> SULFUR_BRICK_WALL;
	public static final DeferredBlock<Block> CINNABAR;
	public static final DeferredBlock<Block> POLISHED_CINNABAR;
	public static final DeferredBlock<Block> CHISELED_CINNABAR;
	public static final DeferredBlock<Block> CINNABAR_BRICKS;
	public static final DeferredBlock<Block> CINNABAR_SLAB;
	public static final DeferredBlock<Block> CINNABAR_STAIRS;
	public static final DeferredBlock<Block> CINNABAR_WALL;
	public static final DeferredBlock<Block> POLISHED_CINNABAR_SLAB;
	public static final DeferredBlock<Block> POLISHED_CINNABAR_STAIRS;
	public static final DeferredBlock<Block> POLISHED_CINNABAR_WALL;
	public static final DeferredBlock<Block> CINNABAR_BRICK_SLAB;
	public static final DeferredBlock<Block> CINNABAR_BRICK_STAIRS;
	public static final DeferredBlock<Block> CINNABAR_BRICK_WALL;
	public static final DeferredBlock<Block> FIGHT_DOORS_BLOCKED;
	public static final DeferredBlock<Block> LOOT_DOORS;
	public static final DeferredBlock<Block> MINIBOSS_DOORS;
	public static final DeferredBlock<Block> BOSS_DOORS;
	public static final DeferredBlock<Block> SPAWN_PREACHER;
	public static final DeferredBlock<Block> SPAWN_DEMON;
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
		SPAWN_GRAVEDIGGER = register("spawn_gravedigger", SpawnGravediggerBlock::new);
		EMPTY_COARSE_DIRT = register("empty_coarse_dirt", EmptyCoarseDirtBlock::new);
		INTERSECTING_END_RODS = register("intersecting_end_rods", IntersectingEndRodsBlock::new);
		SULFUR_BLOCK = register("sulfur_block", SulfurBlockBlock::new);
		POTENT_SULFUR = register("potent_sulfur", PotentSulfurBlock::new);
		POLISHED_SULFUR = register("polished_sulfur", PolishedSulfurBlock::new);
		CHISELED_SULFUR = register("chiseled_sulfur", ChiseledSulfurBlock::new);
		SULFUR_BRICKS = register("sulfur_bricks", SulfurBricksBlock::new);
		SULFUR_SLAB = register("sulfur_slab", SulfurSlabBlock::new);
		SULFUR_STAIRS = register("sulfur_stairs", SulfurStairsBlock::new);
		SULFUR_WALL = register("sulfur_wall", SulfurWallBlock::new);
		POLISHED_SULFUR_SLAB = register("polished_sulfur_slab", PolishedSulfurSlabBlock::new);
		POLISHED_SULFUR_STAIRS = register("polished_sulfur_stairs", PolishedSulfurStairsBlock::new);
		POLISHED_SULFUR_WALL = register("polished_sulfur_wall", PolishedSulfurWallBlock::new);
		SULFUR_BRICK_SLAB = register("sulfur_brick_slab", SulfurBrickSlabBlock::new);
		SULFUR_BRICK_STAIRS = register("sulfur_brick_stairs", SulfurBrickStairsBlock::new);
		SULFUR_BRICK_WALL = register("sulfur_brick_wall", SulfurBrickWallBlock::new);
		CINNABAR = register("cinnabar", CinnabarBlock::new);
		POLISHED_CINNABAR = register("polished_cinnabar", PolishedCinnabarBlock::new);
		CHISELED_CINNABAR = register("chiseled_cinnabar", ChiseledCinnabarBlock::new);
		CINNABAR_BRICKS = register("cinnabar_bricks", CinnabarBricksBlock::new);
		CINNABAR_SLAB = register("cinnabar_slab", CinnabarSlabBlock::new);
		CINNABAR_STAIRS = register("cinnabar_stairs", CinnabarStairsBlock::new);
		CINNABAR_WALL = register("cinnabar_wall", CinnabarWallBlock::new);
		POLISHED_CINNABAR_SLAB = register("polished_cinnabar_slab", PolishedCinnabarSlabBlock::new);
		POLISHED_CINNABAR_STAIRS = register("polished_cinnabar_stairs", PolishedCinnabarStairsBlock::new);
		POLISHED_CINNABAR_WALL = register("polished_cinnabar_wall", PolishedCinnabarWallBlock::new);
		CINNABAR_BRICK_SLAB = register("cinnabar_brick_slab", CinnabarBrickSlabBlock::new);
		CINNABAR_BRICK_STAIRS = register("cinnabar_brick_stairs", CinnabarBrickStairsBlock::new);
		CINNABAR_BRICK_WALL = register("cinnabar_brick_wall", CinnabarBrickWallBlock::new);
		FIGHT_DOORS_BLOCKED = register("fight_doors_blocked", FightDoorsBlockedBlock::new);
		LOOT_DOORS = register("loot_doors", LootDoorsBlock::new);
		MINIBOSS_DOORS = register("miniboss_doors", MinibossDoorsBlock::new);
		BOSS_DOORS = register("boss_doors", BossDoorsBlock::new);
		SPAWN_PREACHER = register("spawn_preacher", SpawnPreacherBlock::new);
		SPAWN_DEMON = register("spawn_demon", SpawnDemonBlock::new);
	}

	// Start of user code block custom blocks
	// End of user code block custom blocks
	private static <B extends Block> DeferredBlock<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> supplier) {
		return REGISTRY.registerBlock(name, supplier);
	}
}