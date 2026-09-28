package net.mcreator.minigames.procedures;

import net.mcreator.minigames.client.ClientGlowManager;
import net.mcreator.minigames.network.ClientGlowSyncMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.network.PacketDistributor;

public class DisplayClientsideGlowProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity target, double ticks, String color) {
		if (target == null) {
			return;
		}

		int tickDuration = (int) Math.round(ticks);
		int parsedColor = ClientGlowManager.parseColor(color);

		if (world != null && world.isClientSide()) {
			if (tickDuration <= 0) {
				ClientGlowManager.removeGlow(target.getId());
			} else {
				ClientGlowManager.addGlow(target.getId(), parsedColor, tickDuration);
			}
			return;
		}

		if (!target.level().isClientSide()) {
			ClientGlowSyncMessage message = new ClientGlowSyncMessage(target.getId(), parsedColor, tickDuration);
			if (entity instanceof ServerPlayer viewer) {
				PacketDistributor.sendToPlayer(viewer, message);
			} else {
				PacketDistributor.sendToPlayersTrackingEntityAndSelf(target, message);
			}
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, double ticks, String color) {
		execute(world, x, y, z, null, entity, ticks, color);
	}

	public static void execute(Entity entity, Entity target, double ticks, String color) {
		if (target != null) {
			execute(target.level(), target.getX(), target.getY(), target.getZ(), entity, target, ticks, color);
		}
	}

	public static void execute(LevelAccessor world, Entity entity, Entity target, double ticks, String color) {
		if (target != null) {
			execute(world, target.getX(), target.getY(), target.getZ(), entity, target, ticks, color);
		}
	}

	public static void execute(Entity entity, double ticks, String color) {
		if (entity != null) {
			execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), null, entity, ticks, color);
		}
	}

	public static void execute(LevelAccessor world, Entity entity, double ticks, String color) {
		if (entity != null) {
			execute(world, entity.getX(), entity.getY(), entity.getZ(), null, entity, ticks, color);
		}
	}
}