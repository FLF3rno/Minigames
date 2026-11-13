package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class IndividualPriceAlterationProcedure {
	public static void execute(ItemStack itemstack) {
		net.mcreator.minigames.DungeonItemAccess.setItemAttribute(itemstack, "minigames:price",
				(net.mcreator.minigames.procedures.GetItemAttributeProcedure.execute(itemstack, "minigames:price") * Mth.nextInt(RandomSource.create(), (int) 0.8, (int) 1.2)), "MAINHAND", "ADD_VALUE");
	}
}