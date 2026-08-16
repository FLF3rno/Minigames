package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMenus;

public class MinigameGUICrownHuntThisGUIIsClosedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (parseDouble((entity instanceof Player _entity0 && _entity0.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(0, "minutes", "") : "") <= 0) {
			MinigamesModVariables.MapVariables.get(world).crownMinutes = 8;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if (parseDouble((entity instanceof Player _entity1 && _entity1.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu1) ? _menu1.getMenuState(0, "minutes", "") : "") > 59) {
			MinigamesModVariables.MapVariables.get(world).crownMinutes = 3;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).crownMinutes = Math
					.round(parseDouble((entity instanceof Player _entity2 && _entity2.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu2) ? _menu2.getMenuState(0, "minutes", "") : ""));
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (parseDouble((entity instanceof Player _entity3 && _entity3.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu3) ? _menu3.getMenuState(0, "grace", "") : "") <= 59
				&& parseDouble((entity instanceof Player _entity4 && _entity4.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu4) ? _menu4.getMenuState(0, "grace", "") : "") > 0) {
			MinigamesModVariables.MapVariables.get(world).graceMinutes = Math
					.round(parseDouble((entity instanceof Player _entity5 && _entity5.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu5) ? _menu5.getMenuState(0, "grace", "") : ""));
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).graceMinutes = Math
					.round(parseDouble((entity instanceof Player _entity6 && _entity6.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu6) ? _menu6.getMenuState(0, "grace", "") : ""));
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (((entity instanceof Player _entity7 && _entity7.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu7) && _menu7.getMenuState(1, "nightvision", false)) == true) {
			MinigamesModVariables.MapVariables.get(world).nightVision = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "effect clear @a night_vision");
			MinigamesModVariables.MapVariables.get(world).nightVision = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (((entity instanceof Player _entity9 && _entity9.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu9) && _menu9.getMenuState(1, "keepinventory", false)) == true) {
			if (world instanceof ServerLevel _origLevel) {
				LevelAccessor _switchworld11 = _origLevel.getServer().getLevel(Level.OVERWORLD);
				if (_switchworld11 != null) {
					worldSwitch11(_switchworld11, x, y, z, entity);
				}
			}
		} else {
			if (world instanceof ServerLevel _origLevel) {
				LevelAccessor _switchworld13 = _origLevel.getServer().getLevel(Level.OVERWORLD);
				if (_switchworld13 != null) {
					worldSwitch13(_switchworld13, x, y, z, entity);
				}
			}
		}
		if (((entity instanceof Player _entity14 && _entity14.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu14) && _menu14.getMenuState(1, "returncrown", false)) == true) {
			MinigamesModVariables.MapVariables.get(world).returnToCastle = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).returnToCastle = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (entity instanceof Player _player)
			_player.closeContainer();
		StartVoteProcedure.execute(world, entity, entity, "crown hunt");
	}

	private static void worldSwitch11(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.KEEP_INVENTORY, true, world.getServer());
	}

	private static void worldSwitch13(LevelAccessor world, double x, double y, double z, Entity entity) {
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