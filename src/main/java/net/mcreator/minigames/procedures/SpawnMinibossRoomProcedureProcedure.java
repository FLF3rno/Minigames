package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SpawnMinibossRoomProcedureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		double structureX = 0;
		double structureZ = 0;
		double rng = 0;
		double structureY = 0;
		String spawnRoomName = "";
		String rotation = "";
		String prefix = "";
		if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _withbp2 ? blockstate.setValue(_withbp2, true) : blockstate)) {
			structureX = x;
			structureY = y;
			structureZ = z;
			if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _withbp6 ? blockstate.setValue(_withbp6, true) : blockstate)) {
				structureX = x - MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x();
			} else if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _withbp11 ? blockstate.setValue(_withbp11, true) : blockstate)) {
				structureX = x + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x();
			} else if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("flipped") instanceof BooleanProperty _withbp16 ? blockstate.setValue(_withbp16, true) : blockstate)) {
				structureZ = z - MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z();
			} else {
				structureZ = z + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z();
			}
			SpawnLootRoomProcedureProcedure.execute(world, structureX, structureY, structureZ, blockstate);
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			prefix = "dungeon_miniboss_";
			if ((MinigamesModVariables.MapVariables.get(world).floorTypeDungeon).equals("church")) {
				rng = Mth.nextInt(RandomSource.create(), 1, 1);
			}
			structureX = x;
			structureY = y;
			structureZ = z;
			spawnRoomName = prefix + "" + MinigamesModVariables.MapVariables.get(world).floorTypeDungeon + "_" + new java.text.DecimalFormat("##").format(rng);
			rotation = "none";
			if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _withbp24 ? blockstate.setValue(_withbp24, true) : blockstate)) {
				structureX = x + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x() - 1;
				rotation = "clockwise_90";
			} else if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _withbp29 ? blockstate.setValue(_withbp29, true) : blockstate)) {
				rotation = "counterclockwise_90";
				structureZ = z + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z() - 1;
			} else if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("flipped") instanceof BooleanProperty _withbp34 ? blockstate.setValue(_withbp34, true) : blockstate)) {
				structureX = x + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x() - 1;
				structureZ = z + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z() - 1;
				rotation = "180";
			}
			SpawnStructureDungeonProcedure.execute(world, Mth.nextInt(RandomSource.create(), 1, 999999999), structureX, structureY, structureZ, rotation, spawnRoomName);
		}
	}
}