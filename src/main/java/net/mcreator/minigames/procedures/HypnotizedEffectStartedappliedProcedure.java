package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.init.MinigamesModItems;

public class HypnotizedEffectStartedappliedProcedure {
	public static void execute(LevelAccessor world) {
		if (world.isClientSide())
			Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(MinigamesModItems.HYPNOTIC_PENDULUM.get()));
	}
}