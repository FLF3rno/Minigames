package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMenus;

public class SaveSpleefSettingsProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (((entity instanceof Player _entity0 && _entity0.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu0) && _menu0.getMenuState(1, "powerup", false)) == true) {
			MinigamesModVariables.MapVariables.get(world).spleefPowerups = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).spleefPowerups = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		MinigamesModVariables.MapVariables.get(world).passiveSnowballsSpleef = ((entity instanceof Player _entity1 && _entity1.containerMenu instanceof MinigamesModMenus.MenuAccessor _menu1) ? _menu1.getMenuState(2, "passive", 0.0) : 0.0) / 20;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}