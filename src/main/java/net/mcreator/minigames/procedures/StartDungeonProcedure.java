package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class StartDungeonProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		MinigamesModVariables.MapVariables.get(world).playingDungeons = true;
		MinigamesModVariables.MapVariables.get(world).minimap = false;
		MinigamesModVariables.MapVariables.get(world).waypoints = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.showOnlyHearts = true;
				_vars.playerInInventory = true;
				_vars.playerSlots = 4;
				_vars.backpackSlots = 3;
				_vars.markSyncDirty();
			}
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/effect give @a minecraft:saturation 10 100 true");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"execute in minigames:dungeon_dimension run forceload add 10 10 -10 -10");
		if (world instanceof ServerLevel _origLevel) {
			LevelAccessor _worldorig = world;
			world = _origLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minigames:dungeon_dimension")));
			if (world != null) {
				MinigamesModVariables.MapVariables.get(world).dungeonRoomSize = new Vec3((28 + 1), 20, (28 + 1));
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				SpawnGridProcedure.execute(world, x, y, z, 1, 13, 1, 9, 5, 5, 1);
			}
			world = _worldorig;
		}
	}
}