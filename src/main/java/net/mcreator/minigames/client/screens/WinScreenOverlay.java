package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.DisplayYourselfProcedure;

import java.util.UUID;

@EventBusSubscriber(Dist.CLIENT)
public class WinScreenOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		if (world != null && MinigamesModVariables.MapVariables.get(world).showWinscreen) {
			event.getGuiGraphics().fill(0, 0, event.getGuiGraphics().guiWidth(), event.getGuiGraphics().guiHeight(), 0x80000000);
			int gap;
			gap = w / (MinigamesModVariables.MapVariables.get(world).WinnerList.size() + 1);
			for (int pN = 0; pN < (MinigamesModVariables.MapVariables.get(world).WinnerList.size()); pN++) {
			renderPlayers(event, String.valueOf(MinigamesModVariables.MapVariables.get(world).WinnerList.get(pN)), world, gap * (pN+ 1)); }

		}
	}

	private static void renderPlayers(RenderGuiEvent.Pre event, String uuid, Level world, int pos)
	{
		Entity player = null;

		try {
			UUID id = UUID.fromString(uuid);

			if (world instanceof net.minecraft.server.level.ServerLevel serverLevel) {
				player = serverLevel.getPlayerByUUID(id);
			}

			if (player == null && Minecraft.getInstance().level != null) {
				player = Minecraft.getInstance().level.getPlayerByUUID(id);
			}

		} catch (IllegalArgumentException e) {
			System.out.println("Invalid UUID in WinnerList: " + uuid);
			return;
		}
		int h = event.getGuiGraphics().guiHeight();
		int scale = 100 - 5 * (MinigamesModVariables.MapVariables.get(world).WinnerList.size());
		if (scale < 1) scale = 3;
		int x0 = pos - scale / 2;
		int x1 = pos  + scale / 2;
		if (player instanceof LivingEntity livingEntity) {
				InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), x0, h / 2 + -932, x1, h / 2 + 1068, scale, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
		}
	}

}