package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModBlocks;

public class DungeonCheckProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double neighbour = 0;
		double spawnRoomX = 0;
		double spawnRoomZ = 0;
		boolean specialRoom = false;
		if (MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.x() <= MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.y()
				&& MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.z() >= MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.y() && MinigamesModVariables.MapVariables.get(world).dungeonSpawn == 1
				&& MinigamesModVariables.MapVariables.get(world).dungeonBoss == 1 && MinigamesModVariables.MapVariables.get(world).minibossRoomsDungeon == MinigamesModVariables.MapVariables.get(world).minibossRoomPlacedDungeon
				&& MinigamesModVariables.MapVariables.get(world).lootRoomsDungeon == MinigamesModVariables.MapVariables.get(world).lootRoomPlacedDungeon) {
			spawnRoomX = MinigamesModVariables.MapVariables.get(world).dungeonSize.x();
			spawnRoomZ = MinigamesModVariables.MapVariables.get(world).dungeonSize.z();
			for (int index0 = 0; index0 < (int) MinigamesModVariables.MapVariables.get(world).dungeonSize.z(); index0++) {
				spawnRoomX = MinigamesModVariables.MapVariables.get(world).dungeonSize.x();
				for (int index1 = 0; index1 < (int) MinigamesModVariables.MapVariables.get(world).dungeonSize.x(); index1++) {
					specialRoom = false;
					neighbour = 0;
					if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ))).getBlock() == MinigamesModBlocks.EMPTY_GRID_BLOCK.get()) {
						if ((world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
							if ((world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(ResourceLocation.parse("minigames:special_room")))) {
								specialRoom = true;
							}
							neighbour = neighbour + 1;
						}
						if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
							if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(ResourceLocation.parse("minigames:special_room")))) {
								specialRoom = true;
							}
							neighbour = neighbour + 1;
						}
						if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ + 1))).is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
							if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ + 1))).is(BlockTags.create(ResourceLocation.parse("minigames:special_room")))) {
								specialRoom = true;
							}
							neighbour = neighbour + 1;
						}
						if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ - 1))).is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
							if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ - 1))).is(BlockTags.create(ResourceLocation.parse("minigames:special_room")))) {
								specialRoom = true;
							}
							neighbour = neighbour + 1;
						}
						if (neighbour >= 1) {
							if (MinigamesModVariables.MapVariables.get(world).secretRoomPlacedDungeon < MinigamesModVariables.MapVariables.get(world).secretRoomDungeon) {
								if (Math.random() < 0.4) {
									if (specialRoom == false) {
										world.setBlock(BlockPos.containing(spawnRoomX, 300, spawnRoomZ), MinigamesModBlocks.SECRET_ROOM_GRID_BLOCK.get().defaultBlockState(), 3);
										MinigamesModVariables.MapVariables.get(world).secretRoomPlacedDungeon = MinigamesModVariables.MapVariables.get(world).secretRoomPlacedDungeon + 1;
										MinigamesModVariables.MapVariables.get(world).markSyncDirty();
									}
								}
							}
						}
					}
					spawnRoomX = spawnRoomX - 1;
				}
				spawnRoomZ = spawnRoomZ - 1;
			}
			if (MinigamesModVariables.MapVariables.get(world).secretRoomDungeon == MinigamesModVariables.MapVariables.get(world).secretRoomPlacedDungeon) {
				SpawnFloorProcedure.execute(world, x, y, z);
			} else {
				SpawnGridProcedure.execute(world, x, y, z, MinigamesModVariables.MapVariables.get(world).lootRoomsDungeon, MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.z(),
						MinigamesModVariables.MapVariables.get(world).minibossRoomsDungeon, MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.x(), MinigamesModVariables.MapVariables.get(world).dungeonSize.x(),
						MinigamesModVariables.MapVariables.get(world).dungeonSize.z(), MinigamesModVariables.MapVariables.get(world).secretRoomDungeon);
			}
		} else {
			SpawnGridProcedure.execute(world, x, y, z, MinigamesModVariables.MapVariables.get(world).lootRoomsDungeon, MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.z(),
					MinigamesModVariables.MapVariables.get(world).minibossRoomsDungeon, MinigamesModVariables.MapVariables.get(world).roomLimitDungeon.x(), MinigamesModVariables.MapVariables.get(world).dungeonSize.x(),
					MinigamesModVariables.MapVariables.get(world).dungeonSize.z(), MinigamesModVariables.MapVariables.get(world).secretRoomDungeon);
		}
	}
}