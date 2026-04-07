package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.resources.ResourceLocation;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber(Dist.CLIENT)
public class ModelCrownProcedure {
	@SubscribeEvent
	public static void afterModelRegistration(EntityRenderersEvent.AddLayers event) {
		execute(event);
	}

	public static void execute() {
		execute(null);
	}

	private static void execute(@Nullable Event event) {
		MinigamesModVariables.crown = ResourceLocation.fromNamespaceAndPath("minigames", "textures/entities/crown.png");
	}
}