package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ClosingHunterAnimationProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team == MinigamesModVariables.MapVariables.get(world).displayHunterPlayerAnimation) {
			MinigamesModVariables.MapVariables.get(world).hunterAchievement = entity.getDisplayName().getString();
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}