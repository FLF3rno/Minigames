package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

public class NextLayerDestroyedInDisplayProcedure {
	public static String execute(LevelAccessor world) {
		return "The top layer is conquered in " + new java.text.DecimalFormat("##").format((400 - MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef) / 20) + "s";
	}
}