package net.mcreator.minigames.procedures;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.AccountSwapManager;

public class SwapAccountProcedure {
	public static void execute(LevelAccessor world, Entity p1, Entity p2, boolean swapClass, boolean swapEffects, boolean swapHearts, boolean swapInventory, boolean swapName, boolean swapSkin) {
		if (world == null || world.isClientSide()) {
			return;
		}
		if (p1 == null || p2 == null) {
			return;
		}
		if (!(p1 instanceof ServerPlayer player1 && p2 instanceof ServerPlayer player2)) {
			return;
		}
		AccountSwapManager.swapAccounts(world, player1, player2, swapClass, swapEffects, swapHearts, swapInventory, swapName, swapSkin);
	}
}