package net.mcreator.minigames.item;

import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.init.MinigamesModItems;

@EventBusSubscriber
public class TNTRoulettePickaxeItem extends Item {
	private static final ToolMaterial TOOL_MATERIAL = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 0, 4f, 0, 2, TagKey.create(Registries.ITEM, Identifier.parse("minigames:tnt_roulette_pickaxe_repair_items")));

	public TNTRoulettePickaxeItem(Item.Properties properties) {
		super(properties.pickaxe(TOOL_MATERIAL, -1f, -3f).fireResistant());
	}

	@SubscribeEvent
	public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
		event.modify(MinigamesModItems.TNT_ROULETTE_PICKAXE.get(), (builder, _, _) -> builder.set(DataComponents.MAX_DAMAGE, null));
	}
}