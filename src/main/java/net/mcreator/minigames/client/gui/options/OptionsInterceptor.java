package net.mcreator.minigames.client.gui.options;

import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(Dist.CLIENT)
public class OptionsInterceptor {
	public static boolean allowVanillaOptions = false;

	@SubscribeEvent
	public static void onScreenOpening(ScreenEvent.Opening event) {
		if (event.getNewScreen() != null && event.getNewScreen().getClass() == OptionsScreen.class) {
			if (allowVanillaOptions) {
				allowVanillaOptions = false;
				return;
			}
			Screen current = event.getCurrentScreen();
			if (current != null && !(current instanceof TitleScreen || current instanceof PauseScreen)) {
				return;
			}
			OptionsScreen optionsScreen = (OptionsScreen) event.getNewScreen();
			Screen parent = current != null ? current : optionsScreen.getLastScreen();
			event.setNewScreen(new OptionsSectionScreen(parent, optionsScreen));
		}
	}
}
