/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public class MinigamesModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MinigamesMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DUNGEON_ITEMS = REGISTRY.register("dungeon_items",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.minigames.dungeon_items")).icon(() -> new ItemStack(MinigamesModItems.BLANK_LONG_SWORD.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MinigamesModItems.BLANK_DAGGER.get());
				tabData.accept(MinigamesModItems.BLANK_SWORD.get());
				tabData.accept(MinigamesModItems.BLANK_LONG_SWORD.get());
			}).withSearchBar().build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CUSTOM_BLOCKS = REGISTRY.register("custom_blocks",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.minigames.custom_blocks")).icon(() -> new ItemStack(MinigamesModBlocks.CHISELED_QUARTZ_WALL.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MinigamesModBlocks.QUARTZ_CHAINS.get().asItem());
				tabData.accept(MinigamesModBlocks.CHISELED_QUARTZ_WALL.get().asItem());
				tabData.accept(MinigamesModBlocks.SPRUCE_BOARD.get().asItem());
				tabData.accept(MinigamesModBlocks.SPRUCE_PEW.get().asItem());
				tabData.accept(MinigamesModBlocks.SPRUCE_PEW_RIGHT.get().asItem());
				tabData.accept(MinigamesModBlocks.SPRUCE_PEW_LEFT.get().asItem());
				tabData.accept(MinigamesModBlocks.SPRUCE_SHORT_BOARD.get().asItem());
			}).withSearchBar().withTabsBefore(DUNGEON_ITEMS.getId()).build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CUSTOM_MOBS = REGISTRY.register("custom_mobs",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.minigames.custom_mobs")).icon(() -> new ItemStack(MinigamesModBlocks.SPAWN_WORSHIPPER.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MinigamesModBlocks.SPAWN_WORSHIPPER.get().asItem());
				tabData.accept(MinigamesModBlocks.SPAWN_CANDLEHEAD.get().asItem());
				tabData.accept(MinigamesModBlocks.SPAWN_SHIELD_ANGEL.get().asItem());
				tabData.accept(MinigamesModBlocks.FIGHT_DOORS.get().asItem());
			}).withSearchBar().withTabsBefore(CUSTOM_BLOCKS.getId()).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
			if (tabData.hasPermissions()) {
				tabData.accept(MinigamesModItems.GAME_COMPASS.get());
				tabData.accept(MinigamesModBlocks.MOVING_BLOCK_SPAWN.get().asItem());
			}
		}
	}
}