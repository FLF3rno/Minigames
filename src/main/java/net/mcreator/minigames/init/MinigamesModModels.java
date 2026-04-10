/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minigames.client.model.Modelspider;
import net.mcreator.minigames.client.model.Modelgrapple;
import net.mcreator.minigames.client.model.Modeldart;
import net.mcreator.minigames.client.model.Modelcrown;

@EventBusSubscriber(Dist.CLIENT)
public class MinigamesModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelcrown.LAYER_LOCATION, Modelcrown::createBodyLayer);
		event.registerLayerDefinition(Modelgrapple.LAYER_LOCATION, Modelgrapple::createBodyLayer);
		event.registerLayerDefinition(Modelspider.LAYER_LOCATION, Modelspider::createBodyLayer);
		event.registerLayerDefinition(Modeldart.LAYER_LOCATION, Modeldart::createBodyLayer);
	}
}