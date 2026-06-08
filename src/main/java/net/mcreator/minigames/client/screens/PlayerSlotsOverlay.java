package net.mcreator.minigames.client.screens;

import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.minigames.client.gui.DungeonInventoryScreen;
import net.mcreator.minigames.network.MinigamesModVariables;

import java.lang.reflect.Field;
import java.util.Locale;

@EventBusSubscriber(Dist.CLIENT)
public class PlayerSlotsOverlay {
	private static final ResourceLocation HOTBAR_SLOT = ResourceLocation.parse("minigames:textures/screens/hotbar.png");
	private static final ResourceLocation SELECTED_SLOT = ResourceLocation.parse("minigames:textures/screens/hotbar_selection.png");
	private static Field selectedField;

	static {
		try {
			selectedField = Inventory.class.getDeclaredField("selected");
			selectedField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			try {
				selectedField = Inventory.class.getDeclaredField("selectedSlot");
				selectedField.setAccessible(true);
			} catch (NoSuchFieldException e2) {
			}
		}
	}

	private static int getSelectedSlot(Inventory inventory) {
		try {
			if (selectedField != null) {
				return selectedField.getInt(inventory);
			}
		} catch (IllegalAccessException e) {
		}
		return 0;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void eventHandler(RenderGuiLayerEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		Player entity = mc.player;
		if (entity != null) {
			if (entity.isSpectator()) {
				return;
			}
			MinigamesModVariables.PlayerVariables vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			double playerSlots = vars.playerSlots;
			boolean showOnlyHearts = vars.showOnlyHearts;
			String path = event.getName().getPath();
			String layerName = event.getName().toString().toLowerCase(Locale.ROOT);
			boolean dungeonInventoryOpen = mc.screen instanceof DungeonInventoryScreen;

			if (dungeonInventoryOpen && (path.contains("hotbar") || path.equals("selected_item_name") || path.equals("selected_item_tooltip"))) {
				event.setCanceled(true);
			}

			// 1. Handle Hotbar Hiding (if slots < 9)
			if (playerSlots < 9) {
				if (path.contains("hotbar")) {
					event.setCanceled(true);
				}
			}

			// 2. Handle showOnlyHearts HUD changes
			if (showOnlyHearts) {
				// Hide Food, Armor, XP, Air, and Vehicle Health
				if (path.equals("food_level") || path.equals("armor_level") || path.equals("air_level") || path.equals("experience_bar")
						|| path.equals("experience_level") || path.contains("experience") || path.contains("exp_bar") || layerName.contains("experience") || layerName.contains("exp_bar")
						|| layerName.contains("xp_bar") || path.equals("jump_bar") || path.equals("vehicle_health")) {
					event.setCanceled(true);
				}

				// Center Health Bar
				if (path.equals("player_health")) {
					event.getGuiGraphics().pose().pushMatrix();
					event.getGuiGraphics().pose().translate(51, 0);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void eventHandlerPost(RenderGuiLayerEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		Player entity = mc.player;
		if (entity != null) {
			if (entity.isSpectator()) {
				return;
			}
			MinigamesModVariables.PlayerVariables vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			boolean showOnlyHearts = vars.showOnlyHearts;
			String path = event.getName().getPath();

			if (showOnlyHearts && path.equals("player_health")) {
				event.getGuiGraphics().pose().popMatrix();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRenderGuiPost(RenderGuiEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		Player entity = mc.player;
		if (entity != null) {
			if (entity.isSpectator()) {
				return;
			}
			if (mc.options.hideGui) {
				return;
			}
			if (!(mc.screen instanceof AbstractContainerScreen)) {
				double playerSlots = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots;
				int nSlots = Math.max(0, Math.min(9, (int) playerSlots));
				if (nSlots < 9 && nSlots > 0) {
					renderCustomHotbar(event.getGuiGraphics(), nSlots);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onScreenRenderBackground(ScreenEvent.Render.Background event) {
		if (event.getScreen() instanceof AbstractContainerScreen<?>) {
			if (event.getScreen() instanceof DungeonInventoryScreen) {
				return;
			}
			Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;
			if (player != null) {
				if (player.isSpectator()) {
					return;
				}
				if (mc.options.hideGui) {
					return;
				}
				double playerSlots = player.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots;
				int nSlots = Math.max(0, Math.min(9, (int) playerSlots));
				if (nSlots < 9) {
					renderCustomHotbar(event.getGuiGraphics(), nSlots);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onScreenRender(ScreenEvent.Render.Post event) {
		if (event.getScreen() instanceof AbstractContainerScreen<?> gui) {
			Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;
			if (player != null) {
				if (player.isSpectator()) {
					return;
				}
				double playerSlots = player.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots;
				int nSlots = Math.max(0, Math.min(9, (int) playerSlots));
				if (nSlots < 9) {
					for (Slot slot : gui.getMenu().slots) {
						if (slot.container instanceof Inventory) {
							int slotIndex = slot.getContainerSlot();
							if (slotIndex >= nSlots && slotIndex < 9) {
								int x = gui.getGuiLeft() + slot.x;
								int y = gui.getGuiTop() + slot.y;
								event.getGuiGraphics().fill(x, y, x + 16, y + 16, 0xAA000000);
							}
						}
					}
				}
			}
		}
	}

	private static void renderCustomHotbar(GuiGraphics guiGraphics, int nSlots) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null || guiGraphics == null || mc.options.hideGui) return;

		int w = guiGraphics.guiWidth();
		int h = guiGraphics.guiHeight();
		
		int slotWidth = 20; 
		int totalWidth = nSlots * slotWidth;
		int startX = w / 2 - totalWidth / 2;
		int hotbarY = h - 22;

		for (int i = 0; i < nSlots; i++) {
			int x = startX + i * slotWidth;
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HOTBAR_SLOT, x, hotbarY, 0, 0, 20, 22, 20, 22);
		}

		int selectedSlot = getSelectedSlot(player.getInventory());
		if (selectedSlot >= 0 && selectedSlot < nSlots) {
			int x = startX + selectedSlot * slotWidth - 2;
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SELECTED_SLOT, x, hotbarY - 1, 0, 0, 24, 24, 24, 24);
		}

		for (int i = 0; i < nSlots; i++) {
			int x = startX + i * slotWidth + 2;
			int itemY = hotbarY + 3;
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty()) {
				guiGraphics.renderItem(stack, x, itemY);
				guiGraphics.renderItemDecorations(mc.font, stack, x, itemY);
			}
		}
	}
}
