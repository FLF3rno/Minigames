package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

@EventBusSubscriber
public class AscendingActiveProcedure {
	private static Field selectedField;

	static {
		try {
			selectedField = Inventory.class.getDeclaredField("selected");
			selectedField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			try {
				selectedField = Inventory.class.getDeclaredField("selectedSlot");
				selectedField.setAccessible(true);
			} catch (NoSuchFieldException e2) {
				e2.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().getY(), event.getEntity());
	}

	public static void execute(double y, Entity entity) {
		execute(null, y, entity);
	}

	private static void execute(@Nullable Event event, double y, Entity entity) {
		if (entity == null)
			return;
            
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.ASCENDING)) {
			double targetX = entity.getPersistentData().getDoubleOr("immobileX", 0);
			double targetZ = entity.getPersistentData().getDoubleOr("immobileZ", 0);
            
			if (entity.getX() != targetX || entity.getZ() != targetZ) {
				entity.teleportTo(targetX, y, targetZ);
				if (entity instanceof ServerPlayer _serverPlayer)
					_serverPlayer.connection.teleport(targetX, y, targetZ, entity.getYRot(), entity.getXRot());
			}
			
			if (entity instanceof Player player) {
				try {
					if (selectedField != null) {
						selectedField.setInt(player.getInventory(), 8); 
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				if (player instanceof ServerPlayer serverPlayer) {
					serverPlayer.containerMenu.broadcastChanges();
				}
			}

			double amp = _livEnt0.getEffect(MinigamesModMobEffects.ASCENDING).getAmplifier();
			entity.setDeltaMovement(new Vec3(0, (amp / 100d), 0));
		}
	}
}
