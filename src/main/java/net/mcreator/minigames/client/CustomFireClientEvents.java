package net.mcreator.minigames.client;

import net.mcreator.minigames.CustomFireHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractBlockScreenEffectEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class CustomFireClientEvents {

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void onRegisterRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
		Class rawRenderer = EntityRenderer.class;
		event.registerEntityModifier(rawRenderer, (entity, state) -> {
			int color = CustomFireHelper.getFireColor((Entity) entity);
			if (color != 0) {
				((EntityRenderState) state).setRenderData(CustomFireHelper.FIRE_COLOR_KEY, color);
			}
		});
	}

	@SubscribeEvent
	public static void onRenderScreenOverlay(ExtractBlockScreenEffectEvent event) {
		if (event.getOverlayType() == ExtractBlockScreenEffectEvent.OverlayType.FIRE) {
			Player player = event.getPlayer();
			int color = CustomFireHelper.getFireColor(player);
			boolean lowFire = CustomFireHelper.isLowFire();
			if (color != 0 || lowFire) {
				event.setCustomRenderer((playerRenderState, cameraRenderState, submitNodeCollector, poseStack, sprites, partialTicks, hideGui) -> {
					CustomFireHelper.renderFirstPersonFire(poseStack, submitNodeCollector, sprites, color);
					return true;
				});
			}
		}
	}
}
