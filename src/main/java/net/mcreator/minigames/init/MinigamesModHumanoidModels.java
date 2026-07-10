package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.client.model.Modelwinnercrown;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

@EventBusSubscriber(Dist.CLIENT)
public class MinigamesModHumanoidModels {
	public static PlayerModel CROWN;

	@SubscribeEvent
	public static void initModels(EntityRenderersEvent.AddLayers event) {
		Modelwinnercrown crown_temp = new Modelwinnercrown(Minecraft.getInstance().getEntityModels().bakeLayer(Modelwinnercrown.LAYER_LOCATION));
		CROWN = new PlayerModel(new ModelPart(Collections.emptyList(),
				Map.of("head", new ModelPart(Collections.emptyList(), Map.of("head", crown_temp.crown, "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()))), "body",
						getPlayerPart(new ModelPart(Collections.emptyList(), Collections.emptyMap()), "jacket"), "left_arm", getPlayerPart(new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_sleeve"), "right_arm",
						getPlayerPart(new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_sleeve"), "left_leg", getPlayerPart(new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_pants"), "right_leg",
						getPlayerPart(new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_pants"))),
				false);
	}

	private static ModelPart getPlayerPart(ModelPart modelPart, String child) {
		Map<String, ModelPart> oldChildren = modelPart.children;
		Map<String, ModelPart> newChildren = new HashMap<>(oldChildren);
		newChildren.put(child, new ModelPart(Collections.emptyList(), Collections.emptyMap()));
		modelPart.children = Collections.unmodifiableMap(newChildren);
		return modelPart;
	}
}