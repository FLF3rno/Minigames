package net.mcreator.minigames.procedures;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMenus;

public class MinigameGUIAchievementRunThisGUIIsClosedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (parseDouble((entity instanceof Player _entity0 && _entity0.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu0) ? _menu0.getMenuState(0, "hp", "") : "") == 0) {
			MinigamesModVariables.health = 20;
		} else {
			MinigamesModVariables.health = Math.round(parseDouble((entity instanceof Player _entity1 && _entity1.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu1) ? _menu1.getMenuState(0, "hp", "") : ""));
		}
		if (((entity instanceof Player _entity2 && _entity2.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu2) && _menu2.getMenuState(1, "nightvision", false)) == true) {
			MinigamesModVariables.MapVariables.get(world).nightVision = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"effect clear @a night_vision");
			MinigamesModVariables.MapVariables.get(world).nightVision = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (((entity instanceof Player _entity4 && _entity4.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu4) && _menu4.getMenuState(1, "keepinventory", false)) == true) {
			if (world instanceof ServerLevel _origLevel) {
				LevelAccessor _worldorig = world;
				world = _origLevel.getServer().getLevel(Level.OVERWORLD);
				if (world != null) {
					if (world instanceof ServerLevel _serverLevel)
						_serverLevel.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(true, world.getServer());
				}
				world = _worldorig;
			}
		} else {
			if (world instanceof ServerLevel _origLevel) {
				LevelAccessor _worldorig = world;
				world = _origLevel.getServer().getLevel(Level.OVERWORLD);
				if (world != null) {
					if (world instanceof ServerLevel _serverLevel)
						_serverLevel.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(false, world.getServer());
				}
				world = _worldorig;
			}
		}
		if (((entity instanceof Player _entity9 && _entity9.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu9) && _menu9.getMenuState(1, "minimap", false)) == true) {
			MinigamesModVariables.MapVariables.get(world).minimap = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).minimap = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (((entity instanceof Player _entity10 && _entity10.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu10) && _menu10.getMenuState(1, "spawn", false)) == true) {
			MinigamesModVariables.MapVariables.get(world).randomizeSpawn = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).randomizeSpawn = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (((entity instanceof Player _entity11 && _entity11.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu11) && _menu11.getMenuState(1, "randomhunter", false)) == true) {
			MinigamesModVariables.MapVariables.get(world).hunterAchievement = "";
			MinigamesModVariables.MapVariables.get(world).randomHunterAchievement = true;
			MinigamesModVariables.MapVariables.get(world).achievementHunterMode = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			if (((entity instanceof Player _entity12 && _entity12.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu12) ? _menu12.getMenuState(0, "hunter", "") : "").equals("")) {
				MinigamesModVariables.MapVariables.get(world).hunterAchievement = "";
				MinigamesModVariables.MapVariables.get(world).achievementHunterMode = false;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else {
				MinigamesModVariables.MapVariables.get(world).hunterAchievement = (entity instanceof Player _entity13 && _entity13.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu13) ? _menu13.getMenuState(0, "hunter", "") : "";
				MinigamesModVariables.MapVariables.get(world).achievementHunterMode = true;
				MinigamesModVariables.MapVariables.get(world).randomHunterAchievement = false;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
		if (((entity instanceof Player _entity14 && _entity14.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu14) && _menu14.getMenuState(1, "headstart", false)) == true) {
			MinigamesModVariables.MapVariables.get(world).headStart = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).headStart = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		if (MinigamesModVariables.MapVariables.get(world).achievementHunterMode) {
			StartVoteProcedure.execute(world, entity, entity, "achievement hunt");
		} else {
			StartVoteProcedure.execute(world, entity, entity, "achievement run");
		}
	}

	private static double parseDouble(String s) {
		try {
			return Double.parseDouble(s.trim());
		} catch (Exception e) {
			return 0;
		}
	}
}