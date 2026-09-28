package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.PacketDistributor;

import net.mcreator.minigames.client.ClientPlayerTrailManager;
import net.mcreator.minigames.network.PlayerTrailMessage;

public class PlayerTrailProcedure {
	public static void execute(LevelAccessor world, Entity entity, boolean fadeOut, double cloneDuration, double opacity, double ticks, String color) {
		if (entity == null)
			return;
		if (world == null)
			world = entity.level();
		if (color == null || color.isBlank())
			color = "rainbow";

		if (world.isClientSide()) {
			ClientPlayerTrailManager.startTrail(entity.getId(), fadeOut, cloneDuration, opacity, ticks, color);
			return;
		}

		PlayerTrailMessage message = new PlayerTrailMessage(entity.getId(), fadeOut, cloneDuration, opacity, ticks, color);
		if (entity != null) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
		} else if (world instanceof ServerLevel serverLevel) {
			PacketDistributor.sendToPlayersNear(serverLevel, null, entity.getX(), entity.getY(), entity.getZ(), 64.0, message);
		}
	}

	public static void execute(Entity entity, boolean fadeOut, double cloneDuration, double opacity, double ticks, String color) {
		if (entity != null) {
			execute(entity.level(), entity, fadeOut, cloneDuration, opacity, ticks, color);
		}
	}
}