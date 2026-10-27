package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.client.HunterTrackerClient;
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
		String dimId = entity.level().dimension().identifier().toString();
		double[] coords = HunterTrackerClient.getCoordsForDimension(dimId);
		if (coords == null) {
			if (entity.level().dimension() == Level.OVERWORLD) {
				double ox = MinigamesModVariables.MapVariables.get(world).overwoldHuntedX;
				double oz = MinigamesModVariables.MapVariables.get(world).overworldHuntedZ;
				if (ox != 0 || oz != 0) {
					coords = new double[]{ox, oz};
				}
			} else if (entity.level().dimension() == Level.NETHER) {
				double nx = MinigamesModVariables.MapVariables.get(world).netherHuntedX;
				double nz = MinigamesModVariables.MapVariables.get(world).netherHuntedZ;
				if (nx != 0 || nz != 0) {
					coords = new double[]{nx, nz};
				}
			}
		}

		if (coords != null) {
			double dx = coords[0] - entity.getX();
			double dz = coords[1] - entity.getZ();
			double angle = (Math.atan2(dx, dz) * 57.29577951308232 * (-1) - entity.getYRot()) % 360;
			if (angle < 0) {
				angle += 360;
			}
			return Math.round((angle / 360.0) * 32.0) % 32;
		}

		return (entity.tickCount * 2) % 32;
	}
}
