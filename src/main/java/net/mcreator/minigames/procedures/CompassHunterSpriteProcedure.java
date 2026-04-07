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
public class CompassHunterSpriteProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static double execute(LevelAccessor world, Entity entity) {
		return execute(null, world, entity);
	}

	private static double execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return 0;
		if ((entity.level().dimension()) == Level.OVERWORLD) {
			return Math.round(
					((((Math.atan2(MinigamesModVariables.MapVariables.get(world).overwoldHuntedX - entity.getX(), MinigamesModVariables.MapVariables.get(world).overworldHuntedZ - entity.getZ()) * 57.2958 * (-1) - entity.getYRot()) % 360 + 360) % 360)
							/ 360) * 32);
		}
		return Math.round(
				((((Math.atan2(MinigamesModVariables.MapVariables.get(world).netherHuntedX - entity.getX(), MinigamesModVariables.MapVariables.get(world).netherHuntedZ - entity.getZ()) * 57.2958 * (-1) - entity.getYRot()) % 360 + 360) % 360) / 360)
						* 32);
	}
}