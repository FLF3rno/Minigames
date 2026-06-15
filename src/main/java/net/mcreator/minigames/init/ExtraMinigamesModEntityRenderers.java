package net.mcreator.minigames.init;

import net.mcreator.minigames.entity.BlessedArrowRenderer;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import net.mcreator.minigames.client.renderer.*;

@EventBusSubscriber(Dist.CLIENT)
public class ExtraMinigamesModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(MinigamesModEntities.BLESSED_ARROW.get(), BlessedArrowRenderer::new);
	}
}