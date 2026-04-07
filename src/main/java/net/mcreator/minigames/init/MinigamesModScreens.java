/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minigames.client.gui.*;

@EventBusSubscriber(Dist.CLIENT)
public class MinigamesModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(MinigamesModMenus.VIEW_ACHIEVMENT.get(), ViewAchievmentScreen::new);
		event.register(MinigamesModMenus.START_GAME.get(), StartGameScreen::new);
		event.register(MinigamesModMenus.MINIGAME_GUI_ACHIEVEMENT_RUN.get(), MinigameGUIAchievementRunScreen::new);
		event.register(MinigamesModMenus.MINIGAME_GUI_ACHIEVEMENT_HUNT.get(), MinigameGUIAchievementHuntScreen::new);
		event.register(MinigamesModMenus.VIEW_ACHIEVMENT_HUNTER.get(), ViewAchievmentHunterScreen::new);
		event.register(MinigamesModMenus.CUSTOMIZE_GUI.get(), CustomizeGUIScreen::new);
		event.register(MinigamesModMenus.MINIGAME_GUI_CROWN_HUNT.get(), MinigameGUICrownHuntScreen::new);
	}

	public interface ScreenAccessor {
		void updateMenuState(int elementType, String name, Object elementState);
	}
}