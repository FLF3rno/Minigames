package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.init.MinigamesModItems;

public class SpawnDungeonItemProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, String className) {
		if (className == null)
			return;
		ItemStack item = ItemStack.EMPTY;
		item = ChooseRandomDungeonItemProcedure.execute(className).copy();
		if (item.getItem() == MinigamesModItems.BLACKSMITH_HAMMER.get()) {
			{
				final String _tagName = "isForged";
				final boolean _tagValue = true;
				CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean(_tagName, _tagValue));
			}
		}
		if (item.getItem() == MinigamesModItems.GHOSTIFIER.get() || item.getItem() == MinigamesModItems.PHASE_CLOAK.get()) {
			{
				final String _tagName = "phantom";
				final boolean _tagValue = true;
				CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean(_tagName, _tagValue));
			}
		}
		if (item.getItem() == MinigamesModItems.GHOSTIFIER.get() || item.getItem() == MinigamesModItems.PHASE_CLOAK.get()) {
			{
				final String _tagName = "blessed";
				final boolean _tagValue = true;
				CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean(_tagName, _tagValue));
			}
		}
		if (world instanceof ServerLevel _level) {
			ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(Blocks.AIR));
			entityToSpawn.setPickUpDelay(10);
			entityToSpawn.setUnlimitedLifetime();
			_level.addFreshEntity(entityToSpawn);
		}
		if (world instanceof ServerLevel _level) {
			ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, item);
			entityToSpawn.setDeltaMovement(0, 0, 0);
			entityToSpawn.setPickUpDelay(10);
			entityToSpawn.setUnlimitedLifetime();
			_level.addFreshEntity(entityToSpawn);
		}
	}
}