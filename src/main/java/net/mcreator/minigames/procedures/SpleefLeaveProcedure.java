package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SpleefLeaveProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).playingSpleef && entity instanceof Player _plr0 && _plr0.gameMode() == GameType.SURVIVAL) {
			MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers = MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers - 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}