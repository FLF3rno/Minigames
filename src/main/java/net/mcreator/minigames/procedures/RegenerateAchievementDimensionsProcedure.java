package net.mcreator.minigames.procedures;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.LevelResource;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.io.File;
import java.nio.file.Path;

public class RegenerateAchievementDimensionsProcedure {

	public static void execute(LevelAccessor world) {
		MinecraftServer server = world.getServer();

		if (server == null)
			return;

		MinigamesModVariables.MapVariables mapVariables = MinigamesModVariables.MapVariables.get(world);

		if (!mapVariables.achievementDimensionRegenQueued) {
			return;
		}


		Path worldFolder = server.getWorldPath(LevelResource.ROOT);

		Path overworld1Folder = worldFolder.resolve("dimensions")
				.resolve("minigames")
				.resolve("achievement_1");

		Path overworld2Folder = worldFolder.resolve("dimensions")
				.resolve("minigames")
				.resolve("achievement_2");

		Path netherFolder = worldFolder.resolve("dimensions")
				.resolve("minigames")
				.resolve("achievement_nether");

		Path endFolder = worldFolder.resolve("dimensions")
				.resolve("minigames")
				.resolve("achievement_end");

		File overworldToDelete;

		if (mapVariables.useOverworld1) {
			overworldToDelete = overworld2Folder.toFile();
		} else {
			overworldToDelete = overworld1Folder.toFile();
		}

		if (overworldToDelete.exists() && canRegenerateDimensions(server, world)) {
			deleteDimensionFolder(overworldToDelete);
			System.out.println(overworldToDelete.getAbsolutePath());

		}

		if (mapVariables.visitedNetherAchievement && canDeleteNether(server)) {
			File nether = netherFolder.toFile();

			if (nether.exists()) {
				deleteDimensionFolder(nether);
			}

			mapVariables.visitedNetherAchievement = false;
		}

		if (mapVariables.visitedEndAchievement && canDeleteEnd(server)) {
			File end = endFolder.toFile();

			if (end.exists()) {
				deleteDimensionFolder(end);
			}

			mapVariables.visitedEndAchievement = false;
		}


		mapVariables.achievementDimensionRegenQueued = false;
		mapVariables.markSyncDirty();
	}

	private static boolean canRegenerateDimensions(MinecraftServer server, LevelAccessor world) {
		boolean useOverworld1 = MinigamesModVariables.MapVariables.get(world).useOverworld1;

		for (ServerLevel level : server.getAllLevels()) {
			Identifier dimensionId = level.dimension().identifier();

			if (!"minigames".equals(dimensionId.getNamespace()))
				continue;

			String path = dimensionId.getPath();

			if (useOverworld1) {
				if (!"achievement_2".equals(path))
					continue;
			} else {
				if (!"achievement_1".equals(path))
					continue;
			}

			if (!level.players().isEmpty()) {
				return false;
			}
		}

		return true;
	}
	private static boolean canDeleteNether(MinecraftServer server) {
		for (ServerLevel level : server.getAllLevels()) {
			Identifier id = level.dimension().identifier();

			if ("minigames".equals(id.getNamespace())
					&& "achievement_nether".equals(id.getPath())
					&& !level.players().isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private static boolean canDeleteEnd(MinecraftServer server) {
		for (ServerLevel level : server.getAllLevels()) {
			Identifier id = level.dimension().identifier();

			if ("minigames".equals(id.getNamespace())
					&& "achievement_end".equals(id.getPath())
					&& !level.players().isEmpty()) {
				return false;
			}
		}

		return true;
	}
	public static void deleteDimensionFolder(File file) {

		System.out.println("ATTEMPTING TO DELETE:");
		System.out.println(file.getAbsolutePath());

		File[] files = file.listFiles();

		if (files != null) {
			for (File child : files) {
				deleteDimensionFolder(child);
			}
		}

		boolean success = file.delete();

		System.out.println("DELETE RESULT = " + success);
	}
}