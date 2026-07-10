package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;

public class StartPVPProcedure {
	public static void execute(LevelAccessor world) {
		if (world instanceof ServerLevel _origLevel) {
			LevelAccessor _switchworld0 = _origLevel.getServer().getLevel(Level.OVERWORLD);
			if (_switchworld0 != null) {
				worldSwitch0(world);
			}
		}
	}

	private static void worldSwitch0(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).pvpAnimationStart = true;
		MinigamesModVariables.MapVariables.get(world).pvpAnimationTick = 0;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}