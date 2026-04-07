package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisplaySnowballCounterProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		if (MinigamesModVariables.MapVariables.get(world).playingSpleef && MinigamesModVariables.MapVariables.get(world).spleefPowerups && !(entity instanceof Player _plr0 && _plr0.gameMode() == GameType.SPECTATOR)) {
			return true;
		}
		return false;
	}
}