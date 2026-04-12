package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class SpleefSettingsChosenProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		SaveSpleefSettingsProcedure.execute(world, entity);
		if (entity instanceof Player _player)
			_player.closeContainer();
		StartSpleefProcedure.execute(world, x, y, z);
	}
}