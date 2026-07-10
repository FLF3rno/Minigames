package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ItemDroppedDungeonProcedure {
	@SubscribeEvent
	public static void onGemDropped(ItemTossEvent event) {
		execute(event, event.getPlayer().level(), event.getPlayer(), event.getEntity().getItem());
	}

	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		execute(null, world, entity, itemstack);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).playingDungeons) {
			if (CheckRelicProcedure.execute(entity, new ItemStack(MinigamesModItems.BLACKSMITH_HAMMER.get()))) {
				if (!itemstack.is(ItemTags.create(Identifier.parse("minigames:dungeon_type_relic")))) {
					{
						final String _tagName = "forged";
						final double _tagValue = (itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("forged", 0) - 10);
						CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putDouble(_tagName, _tagValue));
					}
				}
			}
		}
	}
}