package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.MinigamesMod;

public class SpawnGridProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, double loot, double maximumRooms, double miniboss, double minimumRooms, double roomX, double roomZ, double secret) {
		double spawnRoomX = 0;
		double spawnRoomZ = 0;
		MinigamesModVariables.MapVariables.get(world).dungeonSize = new Vec3(roomX, 300, roomZ);
		MinigamesModVariables.MapVariables.get(world).lootRoomsDungeon = loot;
		MinigamesModVariables.MapVariables.get(world).minibossRoomsDungeon = miniboss;
		MinigamesModVariables.MapVariables.get(world).secretRoomDungeon = secret;
		MinigamesModVariables.MapVariables.get(world).roomLimitDungeon = new Vec3(minimumRooms, 0, maximumRooms);
		MinigamesModVariables.MapVariables.get(world).lootRoomPlacedDungeon = 0;
		MinigamesModVariables.MapVariables.get(world).minibossRoomPlacedDungeon = 0;
		MinigamesModVariables.MapVariables.get(world).dungeonSpawn = 0;
		MinigamesModVariables.MapVariables.get(world).dungeonBoss = 0;
		MinigamesModVariables.MapVariables.get(world).secretRoomPlacedDungeon = 0;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		spawnRoomX = MinigamesModVariables.MapVariables.get(world).dungeonSize.x();
		spawnRoomZ = MinigamesModVariables.MapVariables.get(world).dungeonSize.z();
		for (int index0 = 0; index0 < (int) MinigamesModVariables.MapVariables.get(world).dungeonSize.z(); index0++) {
			spawnRoomX = MinigamesModVariables.MapVariables.get(world).dungeonSize.x();
			for (int index1 = 0; index1 < (int) MinigamesModVariables.MapVariables.get(world).dungeonSize.x(); index1++) {
				world.setBlock(BlockPos.containing(spawnRoomX, 300, spawnRoomZ), MinigamesModBlocks.EMPTY_GRID_BLOCK.get().defaultBlockState(), 3);
				spawnRoomX = spawnRoomX - 1;
			}
			spawnRoomZ = spawnRoomZ - 1;
		}
		world.setBlock(BlockPos.containing(Math.round(MinigamesModVariables.MapVariables.get(world).dungeonSize.x() / 2), 300, Math.round(MinigamesModVariables.MapVariables.get(world).dungeonSize.z() / 2)),
				MinigamesModBlocks.ROOM_GRID_BLOCK.get().defaultBlockState(), 3);
		MinigamesMod.queueServerWork(1, () -> {
			DungeonCheckProcedure.execute(world, x, y, z);
		});
	}
}