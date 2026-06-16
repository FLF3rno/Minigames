/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import net.mcreator.minigames.client.renderer.*;

@EventBusSubscriber(Dist.CLIENT)
public class MinigamesModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(MinigamesModEntities.GOLDEN_SKELETON.get(), GoldenSkeletonRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.GOLDEN_SPIDER.get(), GoldenSpiderRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.GOLDEN_ZOMBIE.get(), GoldenZombieRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.ICE_DART_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.SNOWBOMB_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.GRAPPLE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.GRAPPLING_HITBOX.get(), GrapplingHitboxRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.MAGMA_HITBOX.get(), MagmaHitboxRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.MAGMA_DART_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.GLUE_PROJECTILE.get(), GlueProjectileRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.SPLEEF_PODIUM_PLAYER.get(), SpleefPodiumPlayerRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.STUNNED_EFFECT.get(), StunnedEffectRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.WORSHIPPER.get(), WorshipperRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.CANDLEHEAD.get(), CandleheadRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.SHIELD_ANGEL.get(), ShieldAngelRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.MOVING_BLOCK.get(), MovingBlockRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.VOLCANIC_SPEW.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.GRAVEDIGGER.get(), GravediggerRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.PREACHER.get(), PreacherRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.PREACHING_SHOT.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.VOLLEYBOMB_ENTITY.get(), VolleybombEntityRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.PLAGUE_MIDDLE.get(), PlagueMiddleRenderer::new);
		event.registerEntityRenderer(MinigamesModEntities.BLESSED_ARROW.get(), BlessedArrowRenderer::new);
	}
}