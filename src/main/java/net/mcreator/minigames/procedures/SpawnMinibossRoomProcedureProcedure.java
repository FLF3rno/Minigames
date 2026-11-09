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
			rng = Mth.nextInt(RandomSource.create(), 1, (int) MinigamesModVariables.MapVariables.get(world).roguelike_miniboss.size());
			structureX = x;
			structureY = y;
			structureZ = z;
			spawnRoomName = MinigamesModVariables.MapVariables.get(world).roguelike_miniboss.get((int) (rng - 1)) instanceof String _str23 ? _str23 : "";
			if (MinigamesModVariables.MapVariables.get(world).roguelike_miniboss.size() > 1) {
				MinigamesModVariables.MapVariables.get(world).roguelike_miniboss.remove((int) (rng - 1));
			}
			rotation = "none";
			if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _withbp28 ? blockstate.setValue(_withbp28, true) : blockstate)) {
				structureX = x + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x() - 1;
				rotation = "clockwise_90";
			} else if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _withbp33 ? blockstate.setValue(_withbp33, true) : blockstate)) {
				rotation = "counterclockwise_90";
				structureZ = z + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z() - 1;
			} else if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("flipped") instanceof BooleanProperty _withbp38 ? blockstate.setValue(_withbp38, true) : blockstate)) {
				structureX = x + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x() - 1;
				structureZ = z + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z() - 1;
				rotation = "180";
			}
			SpawnStructureDungeonProcedure.execute(world, Mth.nextInt(RandomSource.create(), 1, 999999999), structureX, structureY, structureZ, rotation, spawnRoomName);
			net.mcreator.minigames.procedures.SpawnMinibossProcedure.spawn(world, x + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x() / 2, 103, z + MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z() / 2, 0, 0);
		}
	}
}