package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.server.ServerLifecycleHooks;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;

public class TeamPriceAlterationProcedure {
	public static void execute(LevelAccessor world, ItemStack itemstack) {
		net.mcreator.minigames.DungeonItemAccess.setItemAttribute(itemstack, "minigames:price",
				(net.mcreator.minigames.procedures.GetItemAttributeProcedure.execute(itemstack, "minigames:price") * Mth.nextInt(RandomSource.create(), (int) 0.8, (int) 1.2)), "MAINHAND", "ADD_VALUE");
		net.mcreator.minigames.DungeonItemAccess.setItemAttribute(itemstack, "minigames:price", (net.mcreator.minigames.procedures.GetItemAttributeProcedure.execute(itemstack, "minigames:price")
				* (world.isClientSide() ? Minecraft.getInstance().getConnection().getOnlinePlayers().size() : ServerLifecycleHooks.getCurrentServer().getPlayerCount())), "MAINHAND", "ADD_VALUE");
		net.mcreator.minigames.DungeonItemAccess.setItemAttribute(itemstack, "minigames:price", (net.mcreator.minigames.procedures.GetItemAttributeProcedure.execute(itemstack, "minigames:price") * 0.8), "MAINHAND", "ADD_VALUE");
	}
}