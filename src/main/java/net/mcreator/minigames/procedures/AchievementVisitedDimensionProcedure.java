package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class AchievementVisitedDimensionProcedure {
	@SubscribeEvent
	public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).playingAchievement) {
			if (entity instanceof Player) {
				if ((entity.level().dimension()) == ResourceKey.create(Registries.DIMENSION, Identifier.parse("minigames:achievement_nether"))) {
					MinigamesModVariables.MapVariables.get(world).visitedNetherAchievement = true;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				} else if ((entity.level().dimension()) == ResourceKey.create(Registries.DIMENSION, Identifier.parse("minigames:achievement_end"))) {
					MinigamesModVariables.MapVariables.get(world).visitedEndAchievement = true;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
			}
		}
	}
}