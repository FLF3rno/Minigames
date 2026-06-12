package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.init.MinigamesModItems;

public class SpawnDungeonItemProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		ItemStack item = ItemStack.EMPTY;
		String itemtype = "";
		double itemtyperng = 0;
		itemtyperng = Mth.nextInt(RandomSource.create(), 1, 100);
		if (itemtyperng <= 30) {
			itemtype = "relic";
		} else if (itemtyperng <= 60) {
			itemtype = "weapon";
		} else {
			itemtype = "utility";
		}
		while (!item.is(ItemTags.create(ResourceLocation.parse((("minigames:dungeon_type_" + itemtype)).toLowerCase(java.util.Locale.ENGLISH))))) {
			item = new ItemStack((BuiltInRegistries.ITEM.getRandomElementOf(ItemTags.create(ResourceLocation.parse((("minigames:dungeon_" + itemtype)).toLowerCase(java.util.Locale.ENGLISH))), RandomSource.create())
					.orElseGet(() -> BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR)).value())).copy();
		}
		if (item.getItem() == MinigamesModItems.BLACKSMITH_HAMMER.get()) {
			{
				final String _tagName = "isForged";
				final boolean _tagValue = true;
				CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean(_tagName, _tagValue));
			}
		}
		if (item.getItem() == MinigamesModItems.GHOSTIFIER.get()) {
			{
				final String _tagName = "phantom";
				final boolean _tagValue = true;
				CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean(_tagName, _tagValue));
			}
		}
		if (item.getItem() == MinigamesModItems.GHOSTIFIER.get()) {
			{
				final String _tagName = "blessed";
				final boolean _tagValue = true;
				CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean(_tagName, _tagValue));
			}
		}
		if (world instanceof ServerLevel _level) {
			ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, item);
			entityToSpawn.setPickUpDelay(10);
			entityToSpawn.setUnlimitedLifetime();
			_level.addFreshEntity(entityToSpawn);
		}
	}
}