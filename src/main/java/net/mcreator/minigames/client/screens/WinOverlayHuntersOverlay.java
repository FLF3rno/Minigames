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
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.procedures.*;

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

			if (DisplayEntityProcedure.execute(world) instanceof LivingEntity livingEntity) {
				InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), w / 2 + -1001, h / 2 + -949, w / 2 + 999, h / 2 + 1051, 40, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
			}
			if (DisplayEntity2Procedure.execute(world) instanceof LivingEntity livingEntity) {
				InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), w / 2 + -1070, h / 2 + -952, w / 2 + 930, h / 2 + 1048, 36, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
			}
			if (DisplayEntity3Procedure.execute(world) instanceof LivingEntity livingEntity) {
				InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), w / 2 + -929, h / 2 + -952, w / 2 + 1071, h / 2 + 1048, 36, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
			}
			if (DisplayEntity5Procedure.execute(world) instanceof LivingEntity livingEntity) {
				InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), w / 2 + -843, h / 2 + -957, w / 2 + 1157, h / 2 + 1043, 30, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
			}
			if (DisplayEntity4Procedure.execute(world) instanceof LivingEntity livingEntity) {
				InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), w / 2 + -1147, h / 2 + -957, w / 2 + 853, h / 2 + 1043, 30, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
			}
			if (DisplayEntity6Procedure.execute(world) instanceof LivingEntity livingEntity) {
				InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), w / 2 + -811, h - 981, w / 2 + 1189, h - -1019, 25, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
			}
		}
	}
}
