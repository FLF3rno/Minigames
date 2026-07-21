/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.world.inventory.*;
import net.mcreator.minigames.network.MenuStateUpdateMessage;
import net.mcreator.minigames.MinigamesMod;

import java.util.Map;

public class MinigamesModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, MinigamesMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<StartGameMenu>> START_GAME = REGISTRY.register("start_game", () -> IMenuTypeExtension.create(StartGameMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<MinigameGUIAchievementRunMenu>> MINIGAME_GUI_ACHIEVEMENT_RUN = REGISTRY.register("minigame_gui_achievement_run", () -> IMenuTypeExtension.create(MinigameGUIAchievementRunMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<MinigameGUIAchievementHuntMenu>> MINIGAME_GUI_ACHIEVEMENT_HUNT = REGISTRY.register("minigame_gui_achievement_hunt", () -> IMenuTypeExtension.create(MinigameGUIAchievementHuntMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<CustomizeGUIMenu>> CUSTOMIZE_GUI = REGISTRY.register("customize_gui", () -> IMenuTypeExtension.create(CustomizeGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<MinigameGUICrownHuntMenu>> MINIGAME_GUI_CROWN_HUNT = REGISTRY.register("minigame_gui_crown_hunt", () -> IMenuTypeExtension.create(MinigameGUICrownHuntMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<MinigameGUISpleefMenu>> MINIGAME_GUI_SPLEEF = REGISTRY.register("minigame_gui_spleef", () -> IMenuTypeExtension.create(MinigameGUISpleefMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<MapGUISpleefMenu>> MAP_GUI_SPLEEF = REGISTRY.register("map_gui_spleef", () -> IMenuTypeExtension.create(MapGUISpleefMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<DungeonInventoryMenu>> DUNGEON_INVENTORY = REGISTRY.register("dungeon_inventory", () -> IMenuTypeExtension.create(DungeonInventoryMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<BattleBoxMenu>> BATTLE_BOX = REGISTRY.register("battle_box", () -> IMenuTypeExtension.create(BattleBoxMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SelectCategoryAchievementMenu>> SELECT_CATEGORY_ACHIEVEMENT = REGISTRY.register("select_category_achievement", () -> IMenuTypeExtension.create(SelectCategoryAchievementMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<DisplayAchievmenMenu>> DISPLAY_ACHIEVMEN = REGISTRY.register("display_achievmen", () -> IMenuTypeExtension.create(DisplayAchievmenMenu::new));

	public interface MenuAccessor {
		Map<String, Object> getMenuState();

		Map<Integer, Slot> getSlots();

		default void sendMenuStateUpdate(Player player, int elementType, String name, Object elementState, boolean needClientUpdate) {
			getMenuState().put(elementType + ":" + name, elementState);
			if (player instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, new MenuStateUpdateMessage(elementType, name, elementState));
			} else if (player.level().isClientSide()) {
				if (Minecraft.getInstance().screen instanceof MinigamesModScreens.ScreenAccessor accessor && needClientUpdate)
					accessor.updateMenuState(elementType, name, elementState);
				ClientPacketDistributor.sendToServer(new MenuStateUpdateMessage(elementType, name, elementState));
			}
		}

		default <T> T getMenuState(int elementType, String name, T defaultValue) {
			try {
				return (T) getMenuState().getOrDefault(elementType + ":" + name, defaultValue);
			} catch (ClassCastException e) {
				return defaultValue;
			}
		}
	}
}