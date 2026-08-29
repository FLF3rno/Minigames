/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minigames.client.model.*;

@EventBusSubscriber(Dist.CLIENT)
public class MinigamesModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelsculklings.LAYER_LOCATION, Modelsculklings::createBodyLayer);
		event.registerLayerDefinition(Modelboard.LAYER_LOCATION, Modelboard::createBodyLayer);
		event.registerLayerDefinition(Modelcrown.LAYER_LOCATION, Modelcrown::createBodyLayer);
		event.registerLayerDefinition(Modelpewseat.LAYER_LOCATION, Modelpewseat::createBodyLayer);
		event.registerLayerDefinition(Modelplayer_cage.LAYER_LOCATION, Modelplayer_cage::createBodyLayer);
		event.registerLayerDefinition(Modelarrowmodel.LAYER_LOCATION, Modelarrowmodel::createBodyLayer);
		event.registerLayerDefinition(Modelspike_trap.LAYER_LOCATION, Modelspike_trap::createBodyLayer);
		event.registerLayerDefinition(Modelintersecting_end_rod.LAYER_LOCATION, Modelintersecting_end_rod::createBodyLayer);
		event.registerLayerDefinition(Modelchain.LAYER_LOCATION, Modelchain::createBodyLayer);
		event.registerLayerDefinition(Modelworshipper.LAYER_LOCATION, Modelworshipper::createBodyLayer);
		event.registerLayerDefinition(Modelpewseat_left.LAYER_LOCATION, Modelpewseat_left::createBodyLayer);
		event.registerLayerDefinition(Modeltesla_coil_flavio.LAYER_LOCATION, Modeltesla_coil_flavio::createBodyLayer);
		event.registerLayerDefinition(Modelblessing_dispenser.LAYER_LOCATION, Modelblessing_dispenser::createBodyLayer);
		event.registerLayerDefinition(Modelcandlehead.LAYER_LOCATION, Modelcandlehead::createBodyLayer);
		event.registerLayerDefinition(Modelhalfblock.LAYER_LOCATION, Modelhalfblock::createBodyLayer);
		event.registerLayerDefinition(Modelflavio_sweeper.LAYER_LOCATION, Modelflavio_sweeper::createBodyLayer);
		event.registerLayerDefinition(Modelwinnercrown.LAYER_LOCATION, Modelwinnercrown::createBodyLayer);
		event.registerLayerDefinition(Modelpewseat_right.LAYER_LOCATION, Modelpewseat_right::createBodyLayer);
		event.registerLayerDefinition(Modelflavio_antenna.LAYER_LOCATION, Modelflavio_antenna::createBodyLayer);
		event.registerLayerDefinition(Modelgrapple.LAYER_LOCATION, Modelgrapple::createBodyLayer);
		event.registerLayerDefinition(Modelshortboard.LAYER_LOCATION, Modelshortboard::createBodyLayer);
		event.registerLayerDefinition(Modelflavio.LAYER_LOCATION, Modelflavio::createBodyLayer);
		event.registerLayerDefinition(Modelcannonball.LAYER_LOCATION, Modelcannonball::createBodyLayer);
		event.registerLayerDefinition(Modelvolleybomb.LAYER_LOCATION, Modelvolleybomb::createBodyLayer);
		event.registerLayerDefinition(Modelshieldagent.LAYER_LOCATION, Modelshieldagent::createBodyLayer);
		event.registerLayerDefinition(Modelflavio_omega_laser.LAYER_LOCATION, Modelflavio_omega_laser::createBodyLayer);
		event.registerLayerDefinition(Modelflavio_trapdoor.LAYER_LOCATION, Modelflavio_trapdoor::createBodyLayer);
		event.registerLayerDefinition(Modeldemon.LAYER_LOCATION, Modeldemon::createBodyLayer);
		event.registerLayerDefinition(Modelflavio_clock_cannon.LAYER_LOCATION, Modelflavio_clock_cannon::createBodyLayer);
		event.registerLayerDefinition(Modelpreacher.LAYER_LOCATION, Modelpreacher::createBodyLayer);
		event.registerLayerDefinition(Modelstunned.LAYER_LOCATION, Modelstunned::createBodyLayer);
		event.registerLayerDefinition(Modelgravedigger.LAYER_LOCATION, Modelgravedigger::createBodyLayer);
		event.registerLayerDefinition(Modelspider.LAYER_LOCATION, Modelspider::createBodyLayer);
		event.registerLayerDefinition(Modeldart.LAYER_LOCATION, Modeldart::createBodyLayer);
		event.registerLayerDefinition(Modelhalfblock_barred.LAYER_LOCATION, Modelhalfblock_barred::createBodyLayer);
	}
}