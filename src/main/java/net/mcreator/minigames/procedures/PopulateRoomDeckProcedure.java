package net.mcreator.minigames.procedures;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PopulateRoomDeckProcedure {
	private static MinecraftServer server;
	public static void execute(LevelAccessor world) {
		int index;
		int amount;
		MinecraftServer server = null;
		if (world instanceof ServerLevel level) server = level.getServer();
		if (server == null) {return;}


		String prefix = MinigamesModVariables.MapVariables.get(world).floorTypeDungeon;
		MinigamesModVariables.MapVariables.get(world).roguelike_two.clear();
		MinigamesModVariables.MapVariables.get(world).roguelike_one.clear();
		MinigamesModVariables.MapVariables.get(world).roguelike_four.clear();
		MinigamesModVariables.MapVariables.get(world).roguelike_loot.clear();
		MinigamesModVariables.MapVariables.get(world).roguelike_miniboss.clear();
		amount = server.getResourceManager()
				.listResources("structure", id ->
						id.getNamespace().equals("minigames")
								&& id.getPath().startsWith("structure/dungeon_two_" + prefix + "_")
								&& id.getPath().endsWith(".nbt")).size();
		for (index = 1; index < amount + 1; index++)
		{
			MinigamesModVariables.MapVariables.get(world).roguelike_two.add("dungeon_two_" + prefix + "_" + index);
		}

			amount = server.getResourceManager()
					.listResources("structure", id ->
							id.getNamespace().equals("minigames")
									&& id.getPath().startsWith("structure/dungeon_one_" + prefix + "_")
									&& id.getPath().endsWith(".nbt")).size();
		for (index = 1; index < amount + 1; index++)
		{
			MinigamesModVariables.MapVariables.get(world).roguelike_one.add("dungeon_one_" + prefix + "_" + index);
		}

			amount = server.getResourceManager()
					.listResources("structure", id ->
							id.getNamespace().equals("minigames")
									&& id.getPath().startsWith("structure/dungeon_four_" + prefix + "_")
									&& id.getPath().endsWith(".nbt")).size();
		for (index = 1; index < amount + 1; index++)
		{
			MinigamesModVariables.MapVariables.get(world).roguelike_four.add("dungeon_four_" + prefix + "_" + index);
		}

			amount = server.getResourceManager()
					.listResources("structure", id ->
							id.getNamespace().equals("minigames")
									&& id.getPath().startsWith("structure/dungeon_loot_" + prefix + "_")
									&& id.getPath().endsWith(".nbt")).size();
		for (index = 1; index < amount + 1; index++)
		{
			MinigamesModVariables.MapVariables.get(world).roguelike_loot.add("dungeon_loot_" + prefix + "_" + index);
		}

			amount = server.getResourceManager()
					.listResources("structure", id ->
							id.getNamespace().equals("minigames")
									&& id.getPath().startsWith("structure/dungeon_miniboss_" + prefix + "_")
									&& id.getPath().endsWith(".nbt")).size();
		for (index = 1; index < amount + 1; index++)
		{
			MinigamesModVariables.MapVariables.get(world).roguelike_miniboss.add("dungeon_miniboss_" + prefix + "_" + index);
		}

		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}