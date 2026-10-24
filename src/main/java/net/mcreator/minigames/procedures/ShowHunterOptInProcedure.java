package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ShowHunterOptInProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!MinigamesModVariables.MapVariables.get(world).wantsToBeHuntedAchievement.contains(entity) && MinigamesModVariables.MapVariables.get(world).ShowOptInAchievement) {
			MinigamesModVariables.MapVariables.get(world).wantsToBeHuntedAchievement.add(entity);
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}