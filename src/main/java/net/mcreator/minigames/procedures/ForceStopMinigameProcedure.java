package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ForceStopMinigameProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (PlayingMinigameProcedure.execute(world)) {
			if (MinigamesModVariables.MapVariables.get(world).playingAchievement && !MinigamesModVariables.MapVariables.get(world).achievementHunterMode) {
				AchievementGameEndProcedure.execute(world, x, y, z, entity);
			} else if (MinigamesModVariables.MapVariables.get(world).playingAchievement && MinigamesModVariables.MapVariables.get(world).achievementHunterMode) {
				AchievementGameEndHunterProcedure.execute(world, x, y, z);
			} else if (MinigamesModVariables.MapVariables.get(world).CrownHuntInGame) {
				OnWinCrownHuntProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).playingSpleef) {
				StopSpleefProcedure.execute(world);
			}
		} else {
			if (entity instanceof ServerPlayer _player)
				_player.sendSystemMessage(Component.literal("\u00A7cNo minigame currently active!"), false);
		}
		if (entity instanceof Player _player)
			_player.closeContainer();
	}
}