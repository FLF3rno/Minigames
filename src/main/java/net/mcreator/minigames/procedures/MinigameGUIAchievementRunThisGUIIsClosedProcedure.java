package net.mcreator.minigames.procedures;

import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMenus;

public class MinigameGUIAchievementRunThisGUIIsClosedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		MinigamesModVariables.MapVariables.get(world).WhenPVPActive = Math.round(parseDouble((entity instanceof Player _entity0 && _entity0.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(0, "pvp", "") : ""));
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (((entity instanceof Player _entity1 && _entity1.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu1) && _menu1.getMenuState(1, "keepinventory", false)) == true) {
			if (world instanceof ServerLevel _origLevel) {
				LevelAccessor _switchworld3 = _origLevel.getServer().getLevel(Level.OVERWORLD);
				if (_switchworld3 != null) {
					worldSwitch3(_switchworld3, entity);
				}
			}
		} else {
			if (world instanceof ServerLevel _origLevel) {
				LevelAccessor _switchworld5 = _origLevel.getServer().getLevel(Level.OVERWORLD);
				if (_switchworld5 != null) {
					worldSwitch5(_switchworld5, entity);
				}
			}
		}
		if (parseDouble((entity instanceof Player _entity6 && _entity6.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu6) ? _menu6.getMenuState(0, "hp", "") : "") == 0) {
			MinigamesModVariables.health = 20;
		} else {
			MinigamesModVariables.health = Math.round(parseDouble((entity instanceof Player _entity7 && _entity7.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu7) ? _menu7.getMenuState(0, "hp", "") : ""));
		}
		StartVoteProcedure.execute(world, entity, entity, "achievement run");
		if (entity instanceof Player _player)
			_player.closeContainer();
	}

	private static void worldSwitch3(LevelAccessor world, Entity entity) {
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.KEEP_INVENTORY, true, world.getServer());
	}

	private static void worldSwitch5(LevelAccessor world, Entity entity) {
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.KEEP_INVENTORY, false, world.getServer());
	}

	private static double parseDouble(String s) {
		try {
			return Double.parseDouble(s.trim());
		} catch (Exception e) {
			return 0;
		}
	}
}