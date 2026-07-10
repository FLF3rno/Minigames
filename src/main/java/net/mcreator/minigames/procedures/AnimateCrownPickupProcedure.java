package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;

public class AnimateCrownPickupProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (!world.isClientSide()) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 100000, 1);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 100000, 1, false);
				}
			}
		}
		MinigamesModVariables.MapVariables.get(world).MoveCrownTimer = true;
		MinigamesModVariables.MapVariables.get(world).ShowCrownTimer = true;
		MinigamesModVariables.MapVariables.get(world).gameTick = 0;
		MinigamesModVariables.MapVariables.get(world).gameSeconds = 1;
		MinigamesModVariables.MapVariables.get(world).gameMinutes = MinigamesModVariables.MapVariables.get(world).crownMinutes;
		MinigamesModVariables.MapVariables.get(world).gameHours = 0;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}