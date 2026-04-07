package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class HuntedCoordsProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getDisplayName().getString()).equals(MinigamesModVariables.MapVariables.get(world).hunterAchievement) && MinigamesModVariables.MapVariables.get(world).achievement != -1) {
			if ((entity.level().dimension()) == Level.OVERWORLD) {
				MinigamesModVariables.MapVariables.get(world).overwoldHuntedX = entity.getX();
				MinigamesModVariables.MapVariables.get(world).overworldHuntedZ = entity.getZ();
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((entity.level().dimension()) == Level.NETHER) {
				MinigamesModVariables.MapVariables.get(world).netherHuntedX = entity.getX();
				MinigamesModVariables.MapVariables.get(world).netherHuntedZ = entity.getZ();
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
	}
}