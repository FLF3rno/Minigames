package net.mcreator.minigames.client.renderer;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minigames.init.MinigamesModEntities;

@EventBusSubscriber(value = Dist.CLIENT)
public class DartProjectileRendererOverride {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(MinigamesModEntities.ICE_DART_PROJECTILE.get(), IceDartProjectileRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.MAGMA_DART_PROJECTILE.get(), MagmaDartProjectileRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.GLUE_PROJECTILE.get(), GlueProjectileRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.GRAPPLE.get(), GrappleProjectileRenderer::new);
	}
}
