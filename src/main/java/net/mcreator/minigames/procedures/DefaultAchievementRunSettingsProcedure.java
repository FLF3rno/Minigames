package net.mcreator.minigames.procedures;

import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMenus;

public class DefaultAchievementRunSettingsProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && _player.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu)
			_menu.sendMenuStateUpdate(_player, 0, "pvp", (new java.text.DecimalFormat("##").format(300)), true);
		if (entity instanceof Player _player && _player.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu)
			_menu.sendMenuStateUpdate(_player, 1, "keepinventory", (world instanceof ServerLevel _serverLevelGR1 && _serverLevelGR1.getGameRules().get(GameRules.KEEP_INVENTORY)), true);
		if (entity instanceof Player _player && _player.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu)
			_menu.sendMenuStateUpdate(_player, 0, "hp", (new java.text.DecimalFormat("##").format(MinigamesModVariables.health)), true);
	}
}