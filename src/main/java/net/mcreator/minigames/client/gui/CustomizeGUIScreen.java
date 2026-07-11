package net.mcreator.minigames.client.gui;

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

import net.mcreator.minigames.init.MinigamesModScreens;
import net.mcreator.minigames.network.CustomizeGUIButtonMessage;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.world.inventory.CustomizeGUIMenu;

import com.mojang.blaze3d.platform.InputConstants;

public class CustomizeGUIScreen extends AbstractContainerScreen<CustomizeGUIMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private static final Identifier BACKGROUND = Identifier.parse("minigames:textures/screens/customize_gui.png");
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/halfextraslot.png");
	private static final Identifier IMAGE_1 = Identifier.parse("minigames:textures/screens/selectedextraslot.png");
	private static final Identifier IMAGE_2 = Identifier.parse("minigames:textures/screens/bucket.png");
	private static final Identifier IMAGE_3 = Identifier.parse("minigames:textures/screens/compass_16.png");
	private static final Identifier IMAGE_4 = Identifier.parse("minigames:textures/screens/selectedgamecompass.png");
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

		this.addRenderableWidget(new ImageButton(this.leftPos + 64, this.topPos + 177, 18, 18,
				new net.minecraft.client.gui.components.WidgetSprites(IMAGE_4, IMAGE_4), e -> {
					ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(13, x, y, z));
					CustomizeGUIButtonMessage.handleButtonAction(entity, 13, x, y, z);
				}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
				guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, 0xFF3A5A7A);
				int border = isHoveredOrFocused() ? 0xFFFFFFFF : 0xAA000000;
				guiGraphics.fill(getX(), getY(), getX() + width, getY() + 1, border);
				guiGraphics.fill(getX(), getY() + height - 1, getX() + width, getY() + height, border);
				guiGraphics.fill(getX(), getY(), getX() + 1, getY() + height, border);
				guiGraphics.fill(getX() + width - 1, getY(), getX() + width, getY() + height, border);
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
	}

	@Override
	public void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.literal("Customize your name!"), 5, 5, -12829636, false);
		guiGraphics.text(this.font, resolveDisplayName(), 6, 98, resolveGuiDisplayTextColor(), true);
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
