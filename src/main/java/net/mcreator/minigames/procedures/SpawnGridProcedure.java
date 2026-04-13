package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;

public class SpawnGridProcedure {
	public static void execute(LevelAccessor world) {
		double roomX = 0;
		double roomZ = 0;
		double spawnRoomX = 0;
		double spawnRoomZ = 0;
		roomX = 5;
		roomZ = 5;
		spawnRoomX = roomX;
		spawnRoomZ = roomZ;
		for (int index0 = 0; index0 < (int) roomZ; index0++) {
			spawnRoomX = roomX;
			for (int index1 = 0; index1 < (int) roomX; index1++) {
				world.setBlock(BlockPos.containing(spawnRoomX, 300, spawnRoomZ), MinigamesModBlocks.EMPTY_GRID_BLOCK.get().defaultBlockState(), 3);
				spawnRoomX = spawnRoomX - 1;
			}
			spawnRoomZ = spawnRoomZ - 1;
		}
		world.setBlock(BlockPos.containing(Math.round(roomX / 2), 300, Math.round(roomZ / 2)), MinigamesModBlocks.ROOM_GRID_BLOCK.get().defaultBlockState(), 3);
	}
}