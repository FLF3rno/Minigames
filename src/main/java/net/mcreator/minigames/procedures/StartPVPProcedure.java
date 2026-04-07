package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;

public class StartPVPProcedure {
	public static void execute(LevelAccessor world) {
		if (world instanceof ServerLevel _origLevel) {
			LevelAccessor _worldorig = world;
			world = _origLevel.getServer().getLevel(Level.OVERWORLD);
			if (world != null) {
				MinigamesModVariables.MapVariables.get(world).pvpAnimationStart = true;
				MinigamesModVariables.MapVariables.get(world).pvpAnimationTick = 0;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			world = _worldorig;
		}
	}
}