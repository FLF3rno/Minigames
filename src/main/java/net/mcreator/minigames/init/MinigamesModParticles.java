/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minigames.client.particle.*;

@EventBusSubscriber(Dist.CLIENT)
public class MinigamesModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(MinigamesModParticleTypes.RED_EXPLOSION.get(), RedExplosionParticle::provider);
		event.registerSpriteSet(MinigamesModParticleTypes.POISON.get(), PoisonParticle::provider);
		event.registerSpriteSet(MinigamesModParticleTypes.BLOOD.get(), BloodParticle::provider);
		event.registerSpriteSet(MinigamesModParticleTypes.CONFETTI.get(), ConfettiParticle::provider);
		event.registerSpriteSet(MinigamesModParticleTypes.BLESSED_PARTICLE.get(), BlessedParticleParticle::provider);
		event.registerSpriteSet(MinigamesModParticleTypes.CURSED_PARTICLE.get(), CursedParticleParticle::provider);
	}
}