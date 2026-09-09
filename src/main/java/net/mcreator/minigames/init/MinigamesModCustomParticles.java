package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.particle.CustomEnchantParticle;

@EventBusSubscriber(value = Dist.CLIENT, modid = MinigamesMod.MODID)
public class MinigamesModCustomParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(MinigamesModCustomParticleTypes.CUSTOM_ENCHANT.get(), CustomEnchantParticle::provider);
	}
}
