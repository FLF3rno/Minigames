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
import net.mcreator.minigames.procedures.WinOverlayDisplayProcedure;
import net.mcreator.minigames.procedures.DisplayWinnerCrownHuntProcedure;
import net.mcreator.minigames.procedures.DisplayBlankActuallyBlankProcedure;
import net.mcreator.minigames.procedures.DarkBackgroundProcedure;

import java.util.UUID;

@EventBusSubscriber(Dist.CLIENT)
public class WinOverlayOverlay {
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
		if (WinOverlayDisplayProcedure.execute(world)) {
			boolean showBlankSilhouetteOnly = DisplayBlankActuallyBlankProcedure.execute(world);
			if (showBlankSilhouetteOnly) {
				event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, ResourceLocation.parse("minigames:textures/screens/blank.png"), w / 2 + -213, h / 2 + -120, 0, 0, 427, 240, 427, 240);
			}

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, ResourceLocation.parse("minigames:textures/screens/background.png"), w / 2 + -501, h / 2 + -531, 0, Mth.clamp((int) DarkBackgroundProcedure.execute(world) * 1000, 0, 4002),
					1000, 1000,
					1000, 5002);

			if (!showBlankSilhouetteOnly) {
				LivingEntity winnerEntity = null;
				if (DisplayWinnerCrownHuntProcedure.execute(world) instanceof LivingEntity livingEntity) {
					winnerEntity = livingEntity;
				} else if (world != null && Minecraft.getInstance().getConnection() != null) {
					String winnerUUID = MinigamesModVariables.MapVariables.get(world).winnerUUID;
					if (winnerUUID != null && !winnerUUID.isBlank()) {
						try {
							UUID id = UUID.fromString(winnerUUID);
							PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(id);
							if (info != null) {
								winnerEntity = new RemotePlayer(Minecraft.getInstance().level, info.getProfile());
							}
						} catch (IllegalArgumentException ignored) {
						}
					}
				}
				if (winnerEntity != null) {
					LivingEntity livingEntity = winnerEntity;
					InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), w / 2 + -1000, h / 2 + -909, w / 2 + 1000, h / 2 + 1091, 100, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
				}
			}
		}
	}
}
