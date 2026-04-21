package net.mcreator.minigames.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

public class BlankLongswordInHandProcedure {
	public static void execute(ItemStack itemstack) {
		{
			final String _tagName = "tintColor";
			final String _tagValue = "0xD0F3FF2A";
			CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putString(_tagName, _tagValue));
		}
	}
}