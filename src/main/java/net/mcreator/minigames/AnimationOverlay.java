package net.mcreator.minigames;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(
		modid = "minigames",
		value = Dist.CLIENT
)
public class AnimationOverlay {

	private static final List<AnimationManager> ACTIVE_MANAGERS =
			new ArrayList<>();

	private static boolean animationPlaying = false;

	public static void addManager(AnimationManager manager) {
		ACTIVE_MANAGERS.add(manager);
		animationPlaying = true;
	}

	public static boolean isAnimationPlaying() {
		return animationPlaying;
	}

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAboveAll(
				Identifier.fromNamespaceAndPath("minigames", "animation"),
				(GuiLayer) (graphics, deltaTracker) -> {

					if (ACTIVE_MANAGERS.isEmpty()) {
						animationPlaying = false;
						return;
					}

					int sw = graphics.guiWidth();
					int sh = graphics.guiHeight();

					for (AnimationManager manager :
							new ArrayList<>(ACTIVE_MANAGERS)) {

						if (!manager.isFinished()) {
							manager.render(graphics, sw, sh);
						}
					}

					ACTIVE_MANAGERS.removeIf(AnimationManager::isFinished);

					animationPlaying = ACTIVE_MANAGERS.stream()
							.anyMatch(manager -> !manager.isFinished());
				}
		);
	}

	public static void tickAll() {
		for (AnimationManager manager :
				new ArrayList<>(ACTIVE_MANAGERS)) {

			if (!manager.isFinished()) {
				manager.tick();
			}
		}
	}
}