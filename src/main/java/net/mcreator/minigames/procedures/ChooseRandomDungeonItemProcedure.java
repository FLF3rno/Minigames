package net.mcreator.minigames.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

public class ChooseRandomDungeonItemProcedure {
	public static ItemStack execute(String className) {
		if (className == null)
			return ItemStack.EMPTY;
		ItemStack item = ItemStack.EMPTY;
		String itemtype = "";
		String stolenClass = "";
		String finalClass = "";
		double itemtyperng = 0;
		double stolenrng = 0;
		double stolenChance = 0;
		itemtyperng = Mth.nextInt(RandomSource.create(), 1, 100);
		stolenChance = 10;
		stolenrng = 100;
		if (itemtyperng <= 30) {
			itemtype = "relic";
		} else if (itemtyperng <= 60) {
			itemtype = "weapon";
		} else {
			itemtype = "utility";
		}
		finalClass = className;
		if ((className).equals("thief")) {
			stolenrng = Mth.nextInt(RandomSource.create(), 1, 100);
			if (stolenrng <= stolenChance / 2) {
				finalClass = "support";
			} else if (stolenrng <= stolenChance) {
				finalClass = "support";
			}
		}
		while (!item.is(ItemTags.create(ResourceLocation.parse((("minigames:dungeon_type_" + itemtype)).toLowerCase(java.util.Locale.ENGLISH))))) {
			item = new ItemStack((BuiltInRegistries.ITEM.getRandomElementOf(ItemTags.create(ResourceLocation.parse((("minigames:dungeon_" + finalClass)).toLowerCase(java.util.Locale.ENGLISH))), RandomSource.create())
					.orElseGet(() -> BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR)).value())).copy();
		}
		if (stolenrng <= stolenChance) {
			{
				final String _tagName = "stolen";
				final boolean _tagValue = true;
				CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean(_tagName, _tagValue));
			}
		}
		return item;
	}
}