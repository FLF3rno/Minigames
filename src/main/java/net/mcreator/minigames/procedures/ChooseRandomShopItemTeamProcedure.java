package net.mcreator.minigames.procedures;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

public class ChooseRandomShopItemTeamProcedure {
	public static ItemStack execute() {
		ItemStack item = ItemStack.EMPTY;
		return new ItemStack((BuiltInRegistries.ITEM.getRandomElementOf(ItemTags.create(Identifier.parse("minigames:dungeon_shop_team")), RandomSource.create()).orElseGet(() -> BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR)).value()));
	}
}