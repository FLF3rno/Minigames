package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minigames.client.renderer.*;
import net.mcreator.minigames.client.renderer.block.SkyboxSimulationBlockEntityRenderer;

@EventBusSubscriber(Dist.CLIENT)
public class ExtraMinigamesModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(MinigamesModEntities.BLESSED_ARROW.get(), BlessedArrowRenderer::new);
		event.registerBlockEntityRenderer(MinigamesModBlockEntities.SKYBOX_SIMULATION.get(), SkyboxSimulationBlockEntityRenderer::new);
	}
}