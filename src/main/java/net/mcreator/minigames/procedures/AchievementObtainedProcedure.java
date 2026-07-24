package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.advancements.AdvancementHolder;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class AchievementObtainedProcedure {
	@SubscribeEvent
	public static void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		if (MinigamesModVariables.MapVariables.get(world).playingAchievement) {
			if (entity instanceof ServerPlayer _plr0 && _plr0.level() instanceof ServerLevel _serverLevel0) {
				String achievementPath = GetAchievementProcedure.execute(
						MinigamesModVariables.MapVariables.get(world).AchievementCategory,
						MinigamesModVariables.MapVariables.get(world).Achievement
				);

				if (achievementPath != null) {
					AdvancementHolder targetAdvancement = _serverLevel0.getServer()
							.getAdvancements()
							.get(Identifier.parse("minecraft:" + achievementPath));
					
					if (targetAdvancement != null && _plr0.getAdvancements().getOrStartProgress(targetAdvancement).isDone()) {
						if (!MinigamesModVariables.MapVariables.get(world).achievementHunterMode
								|| entity.getStringUUID().equals(MinigamesModVariables.MapVariables.get(world).hunterAchievementUUID)) {
							AchievementGameEndProcedure.execute(world, x, y, z, entity);
						}
					}
				}
			}
		}
	}
}