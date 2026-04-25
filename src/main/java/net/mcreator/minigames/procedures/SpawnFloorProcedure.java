package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SpawnFloorProcedure {
	public static void execute(LevelAccessor world) {
		String spawnRoomName = "";
		double structureX = 0;
		double structureZ = 0;
		spawnRoomName = "dungeon_start_generic";
		structureX = 0;
		structureZ = 0;
		if ((world.getBlockState(BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x() - 1, 300, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z())))
				.is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
			structureZ = 28;
		} else if ((world.getBlockState(BlockPos.containing(MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.x() + 1, 300, MinigamesModVariables.MapVariables.get(world).dungeonStartLocation.z())))
				.is(BlockTags.create(ResourceLocation.parse("minigames:room")))) {
			structureX = 28;
		}
		SpawnStructureDungeonProcedure.execute(world, structureX, 100, structureZ, spawnRoomName);
	}
}