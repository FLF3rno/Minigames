package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;

public class PlaceRandomMapSpleefProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double rng = 0;
		double selectedIndex = 0;
		double currentIndex = 0;
		selectedIndex = 0;
		if (((int) MinigamesModVariables.MapVariables.get(world).mapsSpleef & 1) == 1) {
			selectedIndex = selectedIndex + 1;
		}
		if (((int) MinigamesModVariables.MapVariables.get(world).mapsSpleef & 2) == 2) {
			selectedIndex = selectedIndex + 1;
		}
		rng = Mth.nextInt(RandomSource.create(), 1, (int) selectedIndex);
		if (selectedIndex == 0) {
			StopSpleefProcedure.execute(world);
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("No map was selected!").withColor(0xcc1919), false);
			}
		} else {
			if (((int) MinigamesModVariables.MapVariables.get(world).mapsSpleef & 1) == 1) {
				currentIndex = currentIndex + 1;
				if (rng == currentIndex) {
					PlaceBalloonsSpleefProcedure.execute(world, x, y, z);
				}
			}
			if (((int) MinigamesModVariables.MapVariables.get(world).mapsSpleef & 2) == 2) {
				currentIndex = currentIndex + 1;
				if (rng == currentIndex) {
					PlaceSolarSystemSpleefProcedure.execute(world, x, y, z);
				}
			}
		}
	}
}