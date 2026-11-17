package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

@EventBusSubscriber
public class HitArmorStandProcedure {
	private static final String CROWN_STAND_CLAIMED_KEY = "minigames.crown_stand_claimed";

	@SubscribeEvent
	public static void onLeftClickEntity(AttackEntityEvent event) {
		handleCrownStand(event, event.getEntity(), event.getTarget());
	}

	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		handleCrownStand(event, event.getEntity(), event.getTarget());
	}

	@SubscribeEvent
	public static void onRightClickEntitySpecific(PlayerInteractEvent.EntityInteractSpecific event) {
		handleCrownStand(event, event.getEntity(), event.getTarget());
	}

	private static void handleCrownStand(ICancellableEvent event, Entity entity, Entity target) {
		if (!(entity instanceof Player player) || !(target instanceof ArmorStand stand)) {
			return;
		}
		Level world = player.level();
		if (world.isClientSide()) {
			return;
		}
		if (stand.getPersistentData().getBoolean(CROWN_STAND_CLAIMED_KEY).orElse(false)) {
			return;
		}
		if (stand.getItemBySlot(EquipmentSlot.HEAD).getItem() != MinigamesModItems.CROWN_HELMET_HELMET.get()) {
			return;
		}

		event.setCanceled(true);

		if (!MinigamesModVariables.MapVariables.get(world).canGrabCrown) {
			return;
		}

		stand.getPersistentData().putBoolean(CROWN_STAND_CLAIMED_KEY, true);
		stand.discard();

		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		vars.helmet = player.getItemBySlot(EquipmentSlot.HEAD).copy();
		vars.markSyncDirty();

		player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MinigamesModItems.CROWN_HELMET_HELMET.get()));

		Scoreboard scoreboard = world.getScoreboard();
		PlayerTeam standTeam = scoreboard.getPlayersTeam(stand.getStringUUID());
		PlayerTeam playerTeam = scoreboard.getPlayersTeam(player.getGameProfile().name());

		if (standTeam != null && standTeam == playerTeam) {
			RepickAnimateCrownProcedure.execute(world, player.getX(), player.getY(), player.getZ());
			broadcastCrownMessage(world, player.getDisplayName().getString() + " recovered the crown!");
		} else {
			AnimateCrownPickupProcedure.execute(world);
			broadcastCrownMessage(world, player.getDisplayName().getString() + " stole the crown!");
		}
	}

	private static void broadcastCrownMessage(Level world, String message) {
		if (world instanceof ServerLevel serverLevel) {
			serverLevel.getServer().getPlayerList().broadcastSystemMessage(
					Component.literal(message).withColor(0xecb25d),
					false
			);
		}
	}
}
