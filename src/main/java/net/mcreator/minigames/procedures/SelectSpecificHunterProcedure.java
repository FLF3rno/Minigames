package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class SelectSpecificHunterProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (world instanceof ServerLevel level) {
			if (level != level.getServer().getLevel(Level.OVERWORLD))
				return;
		}
		if (MinigamesModVariables.MapVariables.get(world).ShowOptInAchievement && MinigamesModVariables.MapVariables.get(world).AchievementHunterOptInTimer > 0) {
			MinigamesModVariables.MapVariables.get(world).AchievementHunterOptInTimer = MinigamesModVariables.MapVariables.get(world).AchievementHunterOptInTimer - 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else if (MinigamesModVariables.MapVariables.get(world).ShowOptInAchievement && MinigamesModVariables.MapVariables.get(world).AchievementHunterOptInTimer <= 0) {
			MinigamesModVariables.MapVariables.get(world).ShowOptInAchievement = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			StartAchievementRunProcedure.execute(world, 0, 0, 0);
		}
	}
}