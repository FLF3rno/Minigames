package net.mcreator.minigames.client.gui;

import net.minecraft.client.gui.components.WidgetSprites;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import net.minecraft.client.input.MouseButtonEvent;
import net.mcreator.minigames.init.MinigamesModScreens;
import net.mcreator.minigames.network.CustomizeGUIButtonMessage;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.world.inventory.CustomizeGUIMenu;

import net.mcreator.minigames.procedures.CustomizeNameColorProcedure;
import com.mojang.blaze3d.platform.InputConstants;

import net.mcreator.minigames.network.NameColorPreferenceMessage;

public class CustomizeGUIScreen extends AbstractContainerScreen<CustomizeGUIMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private static final Identifier BACKGROUND = Identifier.parse("minigames:textures/screens/customize_gui.png");
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/halfextraslot.png");
	private static final Identifier IMAGE_1 = Identifier.parse("minigames:textures/screens/selectedextraslot.png");
	private static final Identifier IMAGE_2 = Identifier.parse("minigames:textures/screens/bucket.png");
	private static final Identifier GAME_COMPASS = Identifier.parse("minigames:textures/screens/compass_16.png");
	private static final Identifier IMAGE_4 = Identifier.parse("minigames:textures/screens/selectedgamecompass.png");
	private static final Identifier COLOR_SELECTOR = Identifier.parse("minigames:textures/screens/colorselector.png");
	private float selectedHue = 0.0f;
	private float selectedSaturation = 1.0f;
	private float selectedValue = 1.0f;

	public CustomizeGUIScreen(CustomizeGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 311, 112);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
	}

	@Override
	public void init() {
		super.init();
		loadCurrentColorIntoPicker();

		this.addRenderableWidget(new ImageButton(
				this.leftPos + 15,
				this.topPos + 123,
				18,
				18,
				new WidgetSprites(GAME_COMPASS, IMAGE_4),
				e -> {
					ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(13, x, y, z));
					CustomizeGUIButtonMessage.handleButtonAction(entity, 13, x, y, z);
				}
		) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
				int size = 36;

				guiGraphics.blit(
						net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
						isHoveredOrFocused() ? IMAGE_4 : GAME_COMPASS,
						getX() - (size - width) / 2,
						getY() - (size - height) / 2,
						0,
						0,
						size,
						size,
						size,
						size
				);
			}
		});
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + 4, this.topPos + 99, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 268, this.topPos + 99, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + 272, this.topPos + 113, 0, 0, 32, 32, 32, 32);
		renderHsvMap(guiGraphics);
		renderHueBar(guiGraphics);
		renderSelectedColorSwatch(guiGraphics);
		renderHsvBorders(guiGraphics);
		renderSelector(guiGraphics);
	}

	@Override
	public void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.literal("Customize your name!"), 5, 5, -12829636, false);
		guiGraphics.text(this.font, resolveDisplayName(), 6, 98, resolveGuiDisplayTextColor(), true);
	}

	@Override
public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
    if (event.button() == 0) {
        if (handleColorPicker(event.x(), event.y())) {
            return true;
        }
    }

    return super.mouseClicked(event, doubleClick);
}

@Override
public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
    if (event.button() == 0) {
        if (handleColorPicker(event.x(), event.y())) {
            return true;
        }
    }

    return super.mouseDragged(event, dragX, dragY);
}
private boolean handleColorPicker(double mouseX, double mouseY) {
    int mapX = this.leftPos + 48;
    int mapY = this.topPos + 22;
    int mapW = 220;
    int mapH = 72;

    int hueX = this.leftPos + 278;
    int hueY = this.topPos + 22;
    int hueW = 14;
    int hueH = 72;

    if (mouseX >= hueX && mouseX < hueX + hueW &&
        mouseY >= hueY && mouseY < hueY + hueH) {

        selectedHue = (float)((mouseY - hueY) / (double)(hueH - 1));
        selectedHue = Math.max(0f, Math.min(1f, selectedHue));

        updateCurrentColor();
        return true;
    }

    if (mouseX >= mapX && mouseX < mapX + mapW &&
        mouseY >= mapY && mouseY < mapY + mapH) {

        selectedSaturation = (float)((mouseX - mapX) / (double)(mapW - 1));
        selectedValue = 1f - (float)((mouseY - mapY) / (double)(mapH - 1));

        selectedSaturation = Math.max(0f, Math.min(1f, selectedSaturation));
        selectedValue = Math.max(0f, Math.min(1f, selectedValue));

        updateCurrentColor();
        return true;
    }

    return false;
}

	private void updateCurrentColor() {

    	int rgb = java.awt.Color.HSBtoRGB(
            selectedHue,
            selectedSaturation,
            selectedValue);

    	String hex = String.format("#%06X", rgb & 0xFFFFFF);
    	entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color = hex;

    if (minecraft.player != null) {
        minecraft.player.getData(MinigamesModVariables.PLAYER_VARIABLES).color = hex;
        ClientPacketDistributor.sendToServer(new NameColorPreferenceMessage(hex));
    	}

	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(event);
	}

	private void renderHsvMap(GuiGraphicsExtractor guiGraphics) {
		int x0 = this.leftPos + 48;
		int y0 = this.topPos + 22;
		int w = 220;
		int h = 72;
		for (int px = 0; px < w; px++) {
			float sat = px / (float) (w - 1);
			int topRgb = java.awt.Color.HSBtoRGB(selectedHue, sat, 1.0f) & 0xFFFFFF;
			guiGraphics.fillGradient(x0 + px, y0, x0 + px + 1, y0 + h, 0xFF000000 | topRgb, 0xFF000000);
		}
	}

	private void renderHueBar(GuiGraphicsExtractor guiGraphics) {
		int x0 = this.leftPos + 278;
		int y0 = this.topPos + 22;
		int w = 14;
		int h = 72;
		int[] hues = new int[] { 0xFF0000, 0xFFFF00, 0x00FF00, 0x00FFFF, 0x0000FF, 0xFF00FF, 0xFF0000 };
		int segmentHeight = Math.max(1, h / 6);
		for (int i = 0; i < 6; i++) {
			int yStart = y0 + (i * segmentHeight);
			int yEnd = (i == 5) ? (y0 + h) : (yStart + segmentHeight);
			guiGraphics.fillGradient(x0, yStart, x0 + w, yEnd, 0xFF000000 | hues[i], 0xFF000000 | hues[i + 1]);
		}
	}

	private void renderSelectedColorSwatch(GuiGraphicsExtractor guiGraphics) {
		int swatchColor = 0xFF000000 | resolveGuiDisplayTextColor();
		guiGraphics.fill(this.leftPos + 22, this.topPos + 22, this.leftPos + 40, this.topPos + 94, swatchColor);
	}

	private void renderSelector(GuiGraphicsExtractor guiGraphics) {

    int mapX = this.leftPos + 48;
    int mapY = this.topPos + 22;
    int mapW = 220;
    int mapH = 72;

    int selectorX = mapX + Math.round(selectedSaturation * (mapW - 1));
    int selectorY = mapY + Math.round((1f - selectedValue) * (mapH - 1));

    guiGraphics.blit(
        net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
        COLOR_SELECTOR,
        selectorX - 4,
        selectorY - 4,
        0,0,
        8,8,
        8,8
    );

    int hueX = this.leftPos + 278;
    int hueY = this.topPos + 22;
    int hueH = 72;

    int hueSelectorY = hueY + Math.round(selectedHue * (hueH - 1));

    guiGraphics.blit(
        net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
        COLOR_SELECTOR,
        hueX + 3,
        hueSelectorY - 4,
        0,0,
        8,8,
        8,8
    );
}

	private void renderHsvBorders(GuiGraphicsExtractor guiGraphics) {
		drawMinecraftFrame(guiGraphics, this.leftPos + 48, this.topPos + 22, 220, 72);
		drawMinecraftFrame(guiGraphics, this.leftPos + 278, this.topPos + 22, 14, 72);
		drawMinecraftFrame(guiGraphics, this.leftPos + 22, this.topPos + 22, 18, 72);
	}

	private void drawMinecraftFrame(GuiGraphicsExtractor guiGraphics, int x, int y, int w, int h) {
		int light = 0xFFB0B0B0;
		int dark = 0xFF555555;
		int edge = 0xFF2A2A2A;
		guiGraphics.fill(x - 2, y - 2, x + w + 2, y - 1, edge);
		guiGraphics.fill(x - 2, y + h + 1, x + w + 2, y + h + 2, edge);
		guiGraphics.fill(x - 2, y - 1, x - 1, y + h + 1, edge);
		guiGraphics.fill(x + w + 1, y - 1, x + w + 2, y + h + 1, edge);
		guiGraphics.fill(x - 1, y - 1, x + w + 1, y, light);
		guiGraphics.fill(x - 1, y, x, y + h + 1, light);
		guiGraphics.fill(x, y + h, x + w + 1, y + h + 1, dark);
		guiGraphics.fill(x + w, y, x + w + 1, y + h, dark);
	}

	private int resolveGuiDisplayTextColor() {
		String color = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
		if (this.minecraft != null && this.minecraft.player != null) {
			String localColor = this.minecraft.player.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
			if (localColor != null && !localColor.isEmpty()) {
				color = localColor;
			}
		}
		if (color != null && color.matches("^#?[0-9a-fA-F]{6}$")) {
			String normalized = color.startsWith("#") ? color.substring(1) : color;
			return Integer.parseInt(normalized, 16);
		}
		return java.awt.Color.HSBtoRGB(selectedHue, selectedSaturation, selectedValue) & 0xFFFFFF;
	}

	private void loadCurrentColorIntoPicker() {
		String color = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
		if (color == null || !color.matches("^#?[0-9a-fA-F]{6}$")) {
			return;
		}
		String normalized = color.startsWith("#") ? color.substring(1) : color;
		int rgb = Integer.parseInt(normalized, 16);
		float[] hsb = java.awt.Color.RGBtoHSB((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255, null);
		selectedHue = hsb[0];
		selectedSaturation = hsb[1];
		selectedValue = hsb[2];
	}

	private Component resolveDisplayName() {
		if (this.minecraft != null && this.minecraft.player != null) {
			return Component.literal(this.minecraft.player.getGameProfile().name());
		}
		return entity.getName();
	}
}
