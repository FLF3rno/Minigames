package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Holder;

import java.util.Optional;

public class Debugphase2Procedure {
	public static void execute(LevelAccessor world) {
		if (world instanceof ServerLevel _level) {
			ServerClockManager _clockManager = _level.getServer().clockManager();
			Optional<Holder<WorldClock>> _clock = _level.dimensionType().defaultClock();
			if (_clock.isPresent())
				_clockManager.setTotalTicks(_clock.get(), 1);
		}
		net.mcreator.minigames.FlavioFightManager.phase = 1;
		net.mcreator.minigames.FlavioFightManager.nextPhase(world);
	}
}