package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.items.ItemHandlerHelper;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SymmetricalShovelItemInInventoryTickProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).playingSpleef) {
			if (MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef == 1) {
				itemstack.shrink(1);
				if (entity instanceof Player _player) {
					ItemStack _setstack = new ItemStack(
							(BuiltInRegistries.ITEM.getRandomElementOf(ItemTags.create(ResourceLocation.parse("minigames:spleef_powerup")), RandomSource.create()).orElseGet(() -> BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR)).value())).copy();
					_setstack.setCount(1);
					ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
				}
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("\u00A76Your \u00A7fSymmetrical Shovel \u00A76is now useless and was transformed into another item"), false);
			}
		}
	}
}