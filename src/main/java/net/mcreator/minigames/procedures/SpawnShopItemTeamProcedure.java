package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;

public class SpawnShopItemTeamProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		ItemStack item = ItemStack.EMPTY;
		item = ChooseRandomShopItemTeamProcedure.execute().copy();
		if (world instanceof ServerLevel _level) {
			ItemEntity entityToSpawn = new ItemEntity(_level, x, (y - 10), z, new ItemStack(Blocks.AIR));
			entityToSpawn.setPickUpDelay(10);
			entityToSpawn.setUnlimitedLifetime();
			_level.addFreshEntity(entityToSpawn);
		}
		TeamPriceAlterationProcedure.execute(world, item);
		if (!world.isClientSide()) {
			if (world instanceof ServerLevel _level) {
				item = net.mcreator.minigames.DungeonItemAccess.applyDefaultDungeonNBT(item);
				ItemEntity entityToSpawn = new ItemEntity(_level, x + 0.5, y + 1, z + 0.5, item);
				entityToSpawn.setDeltaMovement(0, 0, 0);
				entityToSpawn.setPickUpDelay(10);
				entityToSpawn.setUnlimitedLifetime();
				_level.addFreshEntity(entityToSpawn);
			}
		}
	}
}