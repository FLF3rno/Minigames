package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.network.HunterTrackingSyncMessage;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber
public class HuntedCoordsProcedure {
	private static final Map<String, double[]> SERVER_COORDS = new ConcurrentHashMap<>();
	private static String serverCurrentDimension = "";
	private static int lastBroadcastTick = 0;

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null || world.isClientSide())
			return;
		if ((entity.getStringUUID()).equals(MinigamesModVariables.MapVariables.get(world).hunterAchievementUUID) && MinigamesModVariables.MapVariables.get(world).achievementHunterMode
				&& MinigamesModVariables.MapVariables.get(world).playingAchievement) {
			String dimId = entity.level().dimension().identifier().toString();
			double x = entity.getX();
			double z = entity.getZ();

			double[] prev = SERVER_COORDS.get(dimId);
			boolean changedDim = !dimId.equals(serverCurrentDimension);
			boolean moved = prev == null || Math.abs(prev[0] - x) > 0.5 || Math.abs(prev[1] - z) > 0.5;

			if (moved || changedDim || Math.abs(entity.tickCount - lastBroadcastTick) >= 20) {
				SERVER_COORDS.put(dimId, new double[]{x, z});
				serverCurrentDimension = dimId;
				lastBroadcastTick = entity.tickCount;

				PacketDistributor.sendToAllPlayers(new HunterTrackingSyncMessage(dimId, x, z, true));
			}

			if ((entity.level().dimension()) == Level.OVERWORLD) {
				MinigamesModVariables.MapVariables.get(world).overwoldHuntedX = x;
				MinigamesModVariables.MapVariables.get(world).overworldHuntedZ = z;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((entity.level().dimension()) == Level.NETHER) {
				MinigamesModVariables.MapVariables.get(world).netherHuntedX = x;
				MinigamesModVariables.MapVariables.get(world).netherHuntedZ = z;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
	}

	public static void syncAllToPlayer(ServerPlayer player) {
		for (Map.Entry<String, double[]> entry : SERVER_COORDS.entrySet()) {
			boolean isCurrent = entry.getKey().equals(serverCurrentDimension);
			PacketDistributor.sendToPlayer(player, new HunterTrackingSyncMessage(entry.getKey(), entry.getValue()[0], entry.getValue()[1], isCurrent));
		}
	}

	public static void reset() {
		SERVER_COORDS.clear();
		serverCurrentDimension = "";
		lastBroadcastTick = 0;
	}
}
