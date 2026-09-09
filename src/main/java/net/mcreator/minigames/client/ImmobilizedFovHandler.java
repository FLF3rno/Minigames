package net.mcreator.minigames.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;

import net.mcreator.minigames.init.MinigamesModMobEffects;

@EventBusSubscriber(value = Dist.CLIENT)
public class ImmobilizedFovHandler {
	@SubscribeEvent
	public static void onComputeFovModifier(ComputeFovModifierEvent event) {
		if (event.getPlayer() != null && event.getPlayer().hasEffect(MinigamesModMobEffects.IMMOBILIZED)) {
			event.setNewFovModifier(1.0F);
		}
	}
}
