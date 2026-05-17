package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMenus;

import java.util.ArrayList;

public class AchievementHuntSettingsProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		String name = "";
		if ((entity instanceof Player _entity0 && _entity0.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu0) && _menu0.getMenuState(1, "randomhunter", false)) {
			if (entity instanceof Player _player && _player.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu)
				_menu.sendMenuStateUpdate(_player, 0, "hunter", "", true);
		} else {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.randomHunterCheckboxClicked = true;
				_vars.markSyncDirty();
			}
		}
		if (((entity instanceof Player _entity2 && _entity2.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu2) ? _menu2.getMenuState(0, "hunter", "") : "").equals("")
				&& entity.getData(MinigamesModVariables.PLAYER_VARIABLES).randomHunterCheckboxClicked) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.randomHunterCheckboxClicked = false;
				_vars.markSyncDirty();
			}
			name = "";
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (entityiterator instanceof Player) {
					if (Math.random() < 0.5) {
						name = entityiterator.getDisplayName().getString();
					}
				}
			}
			if ((name).equals("")) {
				name = entity.getDisplayName().getString();
			}
			if (entity instanceof Player _player && _player.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu)
				_menu.sendMenuStateUpdate(_player, 0, "hunter", "", true);
		}
	}
}