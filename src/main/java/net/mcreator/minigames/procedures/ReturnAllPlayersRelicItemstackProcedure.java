package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public class ReturnAllPlayersRelicItemstackProcedure {
	public static ItemStack execute(LevelAccessor world, ItemStack itemstack) {
		return CheckAllPlayersRelicProcedure.getRelicStack(itemstack, world);
	}

	public static ItemStack execute(ItemStack itemstack, LevelAccessor world) {
		return CheckAllPlayersRelicProcedure.getRelicStack(itemstack, world);
	}
}
