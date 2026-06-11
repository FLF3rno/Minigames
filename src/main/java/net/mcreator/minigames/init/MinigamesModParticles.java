/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minigames.client.particle.RedExplosionParticle;
import net.mcreator.minigames.client.particle.PoisonParticle;

@EventBusSubscriber(Dist.CLIENT)
public class MinigamesModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(MinigamesModParticleTypes.RED_EXPLOSION.get(), RedExplosionParticle::provider);
		event.registerSpriteSet(MinigamesModParticleTypes.POISON.get(), PoisonParticle::provider);
	}
}