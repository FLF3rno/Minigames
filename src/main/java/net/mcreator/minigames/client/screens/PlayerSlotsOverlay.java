package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.AnimationOverlay;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;

import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.client.gui.DungeonInventoryScreen;
import net.mcreator.minigames.network.MinigamesModVariables;

import java.lang.reflect.Field;
import java.util.Locale;

@EventBusSubscriber(Dist.CLIENT)
public class PlayerSlotsOverlay {
    private static final Identifier HOTBAR_SLOT = Identifier.parse("minigames:textures/screens/hotbar.png");
    private static final Identifier SELECTED_SLOT = Identifier.parse("minigames:textures/screens/hotbar_selection.png");
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
            boolean playingDungeons = MinigamesModVariables.MapVariables.get(entity.level()).playingDungeons;
            double playerSlots = vars.playerSlots;
            boolean showOnlyHearts = vars.showOnlyHearts;
            String path = event.getName().getPath();
            String layerName = event.getName().toString().toLowerCase(Locale.ROOT);
            boolean dungeonInventoryOpen = mc.screen instanceof DungeonInventoryScreen;
            ItemStack selectedStack = entity.getInventory().getItem(getSelectedSlot(entity.getInventory()));
            boolean selectedDungeonItem = !selectedStack.isEmpty() && DungeonItemAccess.isDungeonItem(selectedStack);

            if (dungeonInventoryOpen && (path.contains("hotbar") || path.equals("selected_item_tooltip"))) {
                event.setCanceled(true);
            }
            if (playingDungeons && path.equals("selected_item_name")) {
                event.setCanceled(true);
            }
            if (playerSlots < 9) {
                if (path.contains("hotbar")) {
                    event.setCanceled(true);
                }
            }

            if (showOnlyHearts) {
                if (path.equals("food_level") || path.equals("armor_level") || path.equals("air_level") || path.equals("experience_bar")
                        || path.equals("experience_level") || path.contains("experience") || path.contains("exp_bar") || layerName.contains("experience") || layerName.contains("exp_bar")
                        || layerName.contains("xp_bar") || path.equals("jump_bar") || path.equals("vehicle_health")) {
                    event.setCanceled(true);
                }

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
            boolean playingDungeons = MinigamesModVariables.MapVariables.get(entity.level()).playingDungeons;
            ItemStack selectedStack = entity.getInventory().getItem(getSelectedSlot(entity.getInventory()));
            boolean selectedDungeonItem = !selectedStack.isEmpty() && DungeonItemAccess.isDungeonItem(selectedStack);
            if (playingDungeons && selectedDungeonItem) {
                renderColoredSelectedItemName(event.getGuiGraphics(), entity, selectedStack);
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
                                int x = gui.getLeftPos() + slot.x;
                                int y = gui.getTopPos() + slot.y;
                                event.getGuiGraphics().fill(x, y, x + 16, y + 16, 0xAA000000);
                            }
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {

        if (AnimationOverlay.isAnimationPlaying()) {
            return;
        }

        if (event.getScreen() instanceof AbstractContainerScreen<?> gui) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            if (player != null) {
                if (player.isSpectator()) {
                    return;
                }

                double playerSlots =
                        player.getData(
                                MinigamesModVariables.PLAYER_VARIABLES
                        ).playerSlots;

                int nSlots =
                        Math.max(
                                0,
                                Math.min(9, (int) playerSlots)
                        );

                if (nSlots < 9) {
                    for (Slot slot : gui.getMenu().slots) {
                        if (slot.container instanceof Inventory) {
                            int slotIndex = slot.getContainerSlot();

                            if (slotIndex >= nSlots && slotIndex < 9) {
                                int sx = gui.getLeftPos() + slot.x;
                                int sy = gui.getTopPos() + slot.y;

                                event.getGuiGraphics().fill(
                                        sx,
                                        sy,
                                        sx + 16,
                                        sy + 16,
                                        0xAA000000
                                );
                            }
                        }
                    }
                }
            }
        }
    }
    private static void renderCustomHotbar(
            GuiGraphicsExtractor guiGraphics,
            int nSlots) {

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null
                || guiGraphics == null
                || mc.options.hideGui
                || AnimationOverlay.isAnimationPlaying()) {
            return;
        }

        int w = guiGraphics.guiWidth();
        int h = guiGraphics.guiHeight();

        int slotWidth = 20;
        int totalWidth = nSlots * slotWidth;
        int startX = w / 2 - totalWidth / 2;
        int hotbarY = h - 22;

        for (int i = 0; i < nSlots; i++) {
            int x = startX + i * slotWidth;

            guiGraphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    HOTBAR_SLOT,
                    x, hotbarY,
                    0, 0,
                    20, 22,
                    20, 22
            );
        }

        int selectedSlot = getSelectedSlot(player.getInventory());

        if (selectedSlot >= 0 && selectedSlot < nSlots) {
            int x = startX + selectedSlot * slotWidth - 2;

            guiGraphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    SELECTED_SLOT,
                    x, hotbarY - 1,
                    0, 0,
                    24, 24,
                    24, 24
            );
        }

        for (int i = 0; i < nSlots; i++) {
            int itemX = startX + i * slotWidth + 2;
            int itemY = hotbarY + 3;

            ItemStack stack = player.getInventory().getItem(i);

            if (!stack.isEmpty()) {
                guiGraphics.item(stack, itemX, itemY);
                guiGraphics.itemDecorations(
                        mc.font,
                        stack,
                        itemX,
                        itemY
                );
            }
        }
    }

    private static void renderColoredSelectedItemName(
            GuiGraphicsExtractor guiGraphics,
            Player player,
            ItemStack stack) {

        if (guiGraphics == null
                || player == null
                || stack == null
                || stack.isEmpty()
                || AnimationOverlay.isAnimationPlaying()) {
            return;
        }

        ClassInfo classInfo = getClassInfo(stack);
        MutableComponent displayName = stack.getHoverName().copy();
        if (classInfo != null) {
            displayName = displayName.withStyle(style -> style.withColor(classInfo.color()).withBold(false));
        }

        Minecraft mc = Minecraft.getInstance();
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0.0F, 2.0F);
        guiGraphics.pose().scale(0.96F, 0.96F);
        int x = Math.round((guiGraphics.guiWidth() / 2.134F) / 0.9F);
        int y = Math.round(((guiGraphics.guiHeight() - 74) / 0.9F));
        guiGraphics.centeredText(mc.font, displayName, x, y, 0xFFFFFFFF);
        guiGraphics.pose().popMatrix();
    }

    private static ClassInfo getClassInfo(ItemStack stack) {
        if (stack.is(DungeonItemAccess.DUNGEON_WARRIOR)) {
            return new ClassInfo(0xFF5555);
        }
        if (stack.is(DungeonItemAccess.DUNGEON_THIEF)) {
            return new ClassInfo(0xFFAA00);
        }
        if (stack.is(DungeonItemAccess.DUNGEON_SUPPORT)) {
            return new ClassInfo(0x55FFFF);
        }
        if (stack.is(DungeonItemAccess.DUNGEON_MAGE)) {
            return new ClassInfo(0xFF55FF);
        }
        return null;
    }

    private record ClassInfo(int color) {
    }
}