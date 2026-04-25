package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class StartDungeonProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		MinigamesModVariables.MapVariables.get(world).playingDungeons = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.showOnlyHearts = true;
				_vars.playerSlots = 4;
				_vars.backpackSlots = 3;
				_vars.markSyncDirty();
			}
		}
		if (world instanceof ServerLevel _origLevel) {
			LevelAccessor _worldorig = world;
			world = _origLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minigames:dungeon_dimension")));
			if (world != null) {
				SpawnGridProcedure.execute(world, x, y, z, 1, 13, 1, 9, 5, 5, 1);
			}
			world = _worldorig;
		}
	}
}