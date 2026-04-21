package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModBlocks;

public class EndRoomGridBlockBlockAddedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (z == MinigamesModVariables.MapVariables.get(world).dungeonSize.z() && MinigamesModVariables.MapVariables.get(world).dungeonSpawn == 0) {
			world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.STARTING_ROOM_GRID_BLOCK.get().defaultBlockState(), 3);
			MinigamesModVariables.MapVariables.get(world).dungeonSpawn = MinigamesModVariables.MapVariables.get(world).dungeonSpawn + 1;
			MinigamesModVariables.MapVariables.get(world).dungeonStartLocation = new Vec3(x, 300, z);
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (z == 1 && MinigamesModVariables.MapVariables.get(world).dungeonBoss == 0) {
			world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.BOSS_ROOM_GRID_BLOCK.get().defaultBlockState(), 3);
			MinigamesModVariables.MapVariables.get(world).dungeonBoss = MinigamesModVariables.MapVariables.get(world).dungeonBoss + 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (MinigamesModVariables.MapVariables.get(world).lootRoomPlacedDungeon < MinigamesModVariables.MapVariables.get(world).lootRoomsDungeon) {
			if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == MinigamesModBlocks.END_ROOM_GRID_BLOCK.get()) {
				MinigamesModVariables.MapVariables.get(world).lootRoomPlacedDungeon = MinigamesModVariables.MapVariables.get(world).lootRoomPlacedDungeon + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.LOOT_ROOM_GRID_BLOCK.get().defaultBlockState(), 3);
			}
		} else {
			if (MinigamesModVariables.MapVariables.get(world).minibossRoomPlacedDungeon < MinigamesModVariables.MapVariables.get(world).minibossRoomsDungeon) {
				if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == MinigamesModBlocks.END_ROOM_GRID_BLOCK.get()) {
					MinigamesModVariables.MapVariables.get(world).minibossRoomPlacedDungeon = MinigamesModVariables.MapVariables.get(world).minibossRoomPlacedDungeon + 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.MINIBOSS_ROOM_GRID_BLOCK.get().defaultBlockState(), 3);
				}
			}
		}
	}
}