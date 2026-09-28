package net.mcreator.minigames.procedures;

import net.mcreator.minigames.client.ClientGlowManager;
import net.mcreator.minigames.client.ClientWooshManager;
import net.mcreator.minigames.network.CircularWooshSyncMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.network.PacketDistributor;

public class DisplayCircularWooshProcedure {

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, double size, double speed, double ticks, String color) {
		if (world == null) {
			if (entity != null) {
				world = entity.level();
			} else {
				return;
			}
		}

		double posX = x;
		double posY = y;
		double posZ = z;

		if (entity != null && posX == 0.0 && posY == 0.0 && posZ == 0.0) {
			posX = entity.getX();
			posY = entity.getY();
			posZ = entity.getZ();
		}

		float effectiveSize = size > 0 ? (float) size : 2.5f;
		float effectiveSpeed = speed > 0 ? (float) speed : 1.0f;
		int effectiveTicks = ticks > 0 ? (int) Math.round(ticks) : 15;
		int parsedColor = ClientGlowManager.parseColor(color);
		int entityId = entity != null ? entity.getId() : -1;

		if (world.isClientSide()) {
			ClientWooshManager.addWoosh(posX, posY, posZ, entityId, effectiveSize, effectiveSpeed, effectiveTicks, parsedColor);
			return;
		}

		CircularWooshSyncMessage message = new CircularWooshSyncMessage(posX, posY, posZ, entityId, effectiveSize, effectiveSpeed, effectiveTicks, parsedColor);
		if (entity != null) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
		} else if (world instanceof ServerLevel serverLevel) {
			PacketDistributor.sendToPlayersNear(serverLevel, null, posX, posY, posZ, 64.0, message);
		}
	}

	public static void execute(Entity entity, double size, double speed, double ticks, String color) {
		if (entity != null) {
			execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity, size, speed, ticks, color);
		}
	}

	public static void execute(LevelAccessor world, Entity entity, double size, double speed, double ticks, String color) {
		if (entity != null) {
			execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, size, speed, ticks, color);
		}
	}
}