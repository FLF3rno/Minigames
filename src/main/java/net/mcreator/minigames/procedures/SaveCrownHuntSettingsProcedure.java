package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMenus;

public class SaveCrownHuntSettingsProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && _player.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu)
			_menu.sendMenuStateUpdate(_player, 0, "minutes", (new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).crownMinutes)), true);
		if (entity instanceof Player _player && _player.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu)
			_menu.sendMenuStateUpdate(_player, 0, "grace", (new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).graceMinutes)), true);
	}
}