package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class ChooseAchievementProcedure {
	public static void execute(LevelAccessor world) {
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.wantsToReroll = false;
				_vars.markSyncDirty();
			}
		}
		MinigamesModVariables.MapVariables.get(world).rerollingPlayers = 0;
		MinigamesModVariables.MapVariables.get(world).p1state = 0;
		MinigamesModVariables.MapVariables.get(world).p2state = 0;
		MinigamesModVariables.MapVariables.get(world).p3state = 0;
		MinigamesModVariables.MapVariables.get(world).p4state = 0;
		MinigamesModVariables.MapVariables.get(world).p5state = 0;
		MinigamesModVariables.MapVariables.get(world).p6state = 0;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (MinigamesModVariables.MapVariables.get(world).achievmentType == 0) {
			MinigamesModVariables.MapVariables.get(world).achievement = Mth.nextInt(RandomSource.create(), 61, 77);
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if (MinigamesModVariables.MapVariables.get(world).achievmentType == 1) {
			MinigamesModVariables.MapVariables.get(world).achievement = Mth.nextInt(RandomSource.create(), 30, 60);
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if (MinigamesModVariables.MapVariables.get(world).achievmentType == 2) {
			MinigamesModVariables.MapVariables.get(world).achievement = Mth.nextInt(RandomSource.create(), 14, 29);
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if (MinigamesModVariables.MapVariables.get(world).achievmentType == 3) {
			MinigamesModVariables.MapVariables.get(world).achievement = Mth.nextInt(RandomSource.create(), 0, 13);
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		AchievementStasisProcedure.execute(world);
	}
}