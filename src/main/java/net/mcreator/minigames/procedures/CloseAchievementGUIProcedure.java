package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class CloseAchievementGUIProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (world.players().size() == MinigamesModVariables.MapVariables.get(world).playersReady) {
			MinigamesModVariables.MapVariables.get(world).openGameGUI = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (entity instanceof Player _player)
				_player.closeContainer();
		}
	}
}