package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

public class SwapAccountsProcedure {
	public static void execute(LevelAccessor world, Entity p1, Entity p2, boolean swapClass, boolean swapEffects, boolean swapHearts, boolean swapInventory, boolean swapName, boolean swapSkin) {
		SwapAccountProcedure.execute(world, p1, p2, swapClass, swapEffects, swapHearts, swapInventory, swapName, swapSkin);
	}
}
