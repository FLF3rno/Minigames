package net.mcreator.minigames.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.procedures.DarkBackgroundProcedure;
import net.mcreator.minigames.procedures.HunterWinOverlayDisplayProcedure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(Dist.CLIENT)
public class WinOverlayHuntersOverlay {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
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
		if (HunterWinOverlayDisplayProcedure.execute(world)) {

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, ResourceLocation.parse("minigames:textures/screens/background.png"), w / 2 + -501, h / 2 + -531, 0, Mth.clamp((int) DarkBackgroundProcedure.execute(world) * 1000, 0, 4002),
					1000, 1000, 1000, 5002);

			List<LivingEntity> winners = getHunterWinners();
			renderDynamicWinners(event, winners, w, h);
		}
	}

	private static List<LivingEntity> getHunterWinners() {
		List<LivingEntity> winners = new ArrayList<>();
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.getConnection() == null || minecraft.level == null) {
			return winners;
		}
		List<PlayerInfo> infos = new ArrayList<>(minecraft.getConnection().getListedOnlinePlayers());
		infos.sort(Comparator.comparing(info -> info.getProfile().getName(), String.CASE_INSENSITIVE_ORDER));
		for (PlayerInfo info : infos) {
			if (winners.size() >= 6) {
				break;
			}
			UUID id = info.getProfile().getId();
			Player localPlayer = minecraft.level.getPlayerByUUID(id);
			if (localPlayer != null) {
				if (localPlayer.getData(MinigamesModVariables.PLAYER_VARIABLES).winner && localPlayer.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 1 && !localPlayer.isSpectator()
						&& localPlayer.isAlive()) {
					winners.add(localPlayer);
				}
			}
		}
		return winners;
	}

	private static void renderWinner(RenderGuiEvent.Pre event, int x0, int y0, int x1, int y1, int scale, LivingEntity entity) {
		InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), x0, y0, x1, y1, scale, -entity.getBbHeight() / (2.0f * entity.getScale()), 0f, 0, entity);
	}

	private static void renderDynamicWinners(RenderGuiEvent.Pre event, List<LivingEntity> winners, int w, int h) {
		if (winners.isEmpty()) {
			return;
		}
		int count = winners.size();
		int centerX = w / 2;
		int centerY = h / 2 + 30;

		double radiusX = Math.min(w * 0.34, 90 + count * 10);
		double radiusY = Math.min(h * 0.22, 52 + count * 4);
		int scale = Math.max(13, Math.min(42, (int) Math.round(52.0 - (count * 1.8))));
		double halfSpan = 1000.0;

		if (count == 1) {
			LivingEntity entity = winners.get(0);
			InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), centerX - 1000, centerY - 1000, centerX + 1000, centerY + 1000, scale, -entity.getBbHeight() / (2.0f * entity.getScale()), 0f, 0, entity);
			return;
		}

		double arcStart = Math.toRadians(210);
		double arcEnd = Math.toRadians(330);
		for (int i = 0; i < count; i++) {
			double t = count == 1 ? (arcStart + arcEnd) / 2.0 : arcStart + ((arcEnd - arcStart) * i / (count - 1.0));
			int px = centerX + (int) Math.round(Math.cos(t) * radiusX);
			int py = centerY + (int) Math.round(Math.sin(t) * radiusY);
			LivingEntity entity = winners.get(i);
			InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), (int) (px - halfSpan), (int) (py - halfSpan), (int) (px + halfSpan), (int) (py + halfSpan), scale,
					-entity.getBbHeight() / (2.0f * entity.getScale()), 0f, 0, entity);
		}
	}
}
