package net.mcreator.minigames.procedures;

import net.minecraft.client.Minecraft;

public class UpdateWorldRendererProcedure {
	public static void execute() {
		Minecraft.getInstance().levelRenderer.allChanged();
	}
}