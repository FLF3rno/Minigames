package net.mcreator.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public class AnimationOverlay {

	private static final List<AnimationManager> ACTIVE_MANAGERS = new ArrayList<>();

	public static void addManager(AnimationManager manager) {
		ACTIVE_MANAGERS.add(manager);
	}
	
	@SubscribeEvent
	public static void onRenderGui(net.neoforged.neoforge.client.event.RenderGuiEvent.Post event) {
		if (ACTIVE_MANAGERS.isEmpty()) return;

		int sw = event.getGuiGraphics().guiWidth();
		int sh = event.getGuiGraphics().guiHeight();

		ACTIVE_MANAGERS.removeIf(AnimationManager::isFinished);

		for (AnimationManager manager : ACTIVE_MANAGERS) {
			manager.render(event.getGuiGraphics(), sw, sh);
		}
	}

	public static void tickAll() {
		for (AnimationManager manager : new ArrayList<>(ACTIVE_MANAGERS)) {
			if (!manager.isFinished()) {
				manager.tick();
			}
		}
	}
}