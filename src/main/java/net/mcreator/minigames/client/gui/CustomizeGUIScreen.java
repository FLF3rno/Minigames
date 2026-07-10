package net.mcreator.minigames.client.gui;

import net.mcreator.minigames.init.MinigamesModScreens;
import net.mcreator.minigames.world.inventory.CustomizeGUIMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CustomizeGUIScreen extends AbstractContainerScreen<CustomizeGUIMenu> implements MinigamesModScreens.ScreenAccessor {
	public CustomizeGUIScreen(CustomizeGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
	}
}
