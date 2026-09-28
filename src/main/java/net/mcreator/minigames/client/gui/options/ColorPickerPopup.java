package net.mcreator.minigames.client.gui.options;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ColorPickerPopup {
	private static final Identifier COLOR_SELECTOR = Identifier.fromNamespaceAndPath("minigames", "textures/screens/colorselector.png");

	private final MinigamesSettingsScreen parent;
	private final MinigamesSettingsHelper.ColorSettingRow row;
	private final Runnable onColorChanged;

	private float selectedHue = 0.0f;
	private float selectedSaturation = 1.0f;
	private float selectedValue = 1.0f;
	private int activeDragTarget = 0;
	private boolean isUpdatingInternally = false;

	private final EditBox hexEditBox;
	private final Button copyButton;
	private final Button pasteButton;
	private final Button doneButton;

	public ColorPickerPopup(MinigamesSettingsScreen parent, MinigamesSettingsHelper.ColorSettingRow row, Runnable onColorChanged) {
		this.parent = parent;
		this.row = row;
		this.onColorChanged = onColorChanged;
		loadColor(row.getValue());

		Minecraft mc = Minecraft.getInstance();
		int dx = getDialogX();
		int dy = getDialogY();
		int infoX = dx + 226;

		this.hexEditBox = new EditBox(mc.font, infoX, dy + 62, 70, 16, Component.literal("Hex"));
		this.hexEditBox.setMaxLength(7);
		this.hexEditBox.setValue(getFormattedHex());
		this.hexEditBox.setResponder(text -> {
			if (isUpdatingInternally) {
				return;
			}
			if (text != null && text.matches("^#?[0-9a-fA-F]{6}$")) {
				String norm = text.startsWith("#") ? text.substring(1) : text;
				try {
					int rgb = Integer.parseInt(norm, 16);
					float[] hsb = java.awt.Color.RGBtoHSB((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255, null);
					selectedHue = hsb[0];
					selectedSaturation = hsb[1];
					selectedValue = hsb[2];
					applyColor();
				} catch (Throwable ignored) {
				}
			}
		});

		this.copyButton = Button.builder(Component.literal("Copy"), b -> copyHex()).bounds(infoX, dy + 82, 33, 16).build();
		this.pasteButton = Button.builder(Component.literal("Paste"), b -> pasteHex()).bounds(infoX + 37, dy + 82, 33, 16).build();
		this.doneButton = Button.builder(Component.literal("Done"), b -> close()).bounds(dx + (getDialogW() - 100) / 2, dy + 122, 100, 20).build();
	}

	private String getFormattedHex() {
		int rgb = java.awt.Color.HSBtoRGB(selectedHue, selectedSaturation, selectedValue) & 0xFFFFFF;
		return String.format(java.util.Locale.ROOT, "#%06X", rgb);
	}

	private void copyHex() {
		Minecraft.getInstance().keyboardHandler.setClipboard(getFormattedHex());
	}

	private void pasteHex() {
		String clip = Minecraft.getInstance().keyboardHandler.getClipboard();
		if (clip != null) {
			String trimmed = clip.trim();
			if (trimmed.matches("^#?[0-9a-fA-F]{6}$")) {
				if (!trimmed.startsWith("#")) {
					trimmed = "#" + trimmed;
				}
				hexEditBox.setValue(trimmed);
			}
		}
	}

	private void loadColor(String hex) {
		if (hex != null && hex.matches("^#?[0-9a-fA-F]{6}$")) {
			String norm = hex.startsWith("#") ? hex.substring(1) : hex;
			try {
				int rgb = Integer.parseInt(norm, 16);
				float[] hsb = java.awt.Color.RGBtoHSB((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255, null);
				selectedHue = hsb[0];
				selectedSaturation = hsb[1];
				selectedValue = hsb[2];
			} catch (Throwable ignored) {
			}
		}
	}

	private void applyColor() {
		String hex = getFormattedHex();
		row.setValue(hex);
		if (onColorChanged != null) {
			onColorChanged.run();
		}
	}

	public void close() {
		parent.closeColorPicker();
	}

	public int getDialogW() {
		return 310;
	}

	public int getDialogH() {
		return 152;
	}

	public int getDialogX() {
		return (parent.width - getDialogW()) / 2;
	}

	public int getDialogY() {
		return (parent.height - getDialogH()) / 2;
	}

	public boolean keyPressed(KeyEvent event) {
		if (hexEditBox.isFocused()) {
			int key = InputConstants.getKey(event).getValue();
			if (key == 256) {
				hexEditBox.setFocused(false);
				return true;
			}
			return hexEditBox.keyPressed(event);
		}
		return false;
	}

	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int dx = getDialogX();
		int dy = getDialogY();
		int dw = getDialogW();
		int dh = getDialogH();
		double mouseX = event.x();
		double mouseY = event.y();

		if (mouseX < dx || mouseX >= dx + dw || mouseY < dy || mouseY >= dy + dh) {
			close();
			return true;
		}

		if (event.button() == 0) {
			if (this.hexEditBox.mouseClicked(event, doubleClick)) {
				return true;
			}
			if (this.copyButton.mouseClicked(event, doubleClick)) {
				return true;
			}
			if (this.pasteButton.mouseClicked(event, doubleClick)) {
				return true;
			}
			if (this.doneButton.mouseClicked(event, doubleClick)) {
				return true;
			}

			int mapX = dx + 14;
			int mapY = dy + 24;
			int mapW = 180;
			int mapH = 80;
			if (mouseX >= mapX && mouseX < mapX + mapW && mouseY >= mapY && mouseY < mapY + mapH) {
				hexEditBox.setFocused(false);
				activeDragTarget = 1;
				updateDrag(mouseX, mouseY);
				return true;
			}

			int hueX = dx + 202;
			int hueY = dy + 24;
			int hueW = 16;
			int hueH = 80;
			if (mouseX >= hueX && mouseX < hueX + hueW && mouseY >= hueY && mouseY < hueY + hueH) {
				hexEditBox.setFocused(false);
				activeDragTarget = 2;
				updateDrag(mouseX, mouseY);
				return true;
			}
		}
		return true;
	}

	public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
		if (event.button() == 0 && activeDragTarget != 0) {
			updateDrag(event.x(), event.y());
			return true;
		}
		return true;
	}

	public boolean mouseReleased(MouseButtonEvent event) {
		if (event.button() == 0) {
			activeDragTarget = 0;
		}
		return true;
	}

	private void updateDrag(double mouseX, double mouseY) {
		int dx = getDialogX();
		int dy = getDialogY();
		int mapX = dx + 14;
		int mapY = dy + 24;
		int mapW = 180;
		int mapH = 80;

		int hueY = dy + 24;
		int hueH = 80;

		if (activeDragTarget == 1) {
			selectedSaturation = (float) ((mouseX - mapX) / (double) (mapW - 1));
			selectedValue = 1f - (float) ((mouseY - mapY) / (double) (mapH - 1));
			selectedSaturation = Math.max(0f, Math.min(1f, selectedSaturation));
			selectedValue = Math.max(0f, Math.min(1f, selectedValue));
			applyColor();
			syncEditBoxText();
		} else if (activeDragTarget == 2) {
			selectedHue = (float) ((mouseY - hueY) / (double) (hueH - 1));
			selectedHue = Math.max(0f, Math.min(1f, selectedHue));
			applyColor();
			syncEditBoxText();
		}
	}

	private void syncEditBoxText() {
		isUpdatingInternally = true;
		hexEditBox.setValue(getFormattedHex());
		isUpdatingInternally = false;
	}

	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		guiGraphics.fill(0, 0, parent.width, parent.height, 0x88000000);

		int dx = getDialogX();
		int dy = getDialogY();
		int dw = getDialogW();
		int dh = getDialogH();

		guiGraphics.fill(dx, dy, dx + dw, dy + dh, 0xF0181820);
		drawMinecraftFrame(guiGraphics, dx, dy, dw, dh);

		Minecraft mc = Minecraft.getInstance();
		guiGraphics.centeredText(mc.font, row.getLabel(), dx + dw / 2, dy + 8, 0xFFFFFFFF);

		int mapX = dx + 14;
		int mapY = dy + 24;
		int mapW = 180;
		int mapH = 80;
		for (int px = 0; px < mapW; px++) {
			float sat = px / (float) (mapW - 1);
			int topRgb = java.awt.Color.HSBtoRGB(selectedHue, sat, 1.0f) & 0xFFFFFF;
			guiGraphics.fillGradient(mapX + px, mapY, mapX + px + 1, mapY + mapH, 0xFF000000 | topRgb, 0xFF000000);
		}
		drawMinecraftFrame(guiGraphics, mapX, mapY, mapW, mapH);

		int hueX = dx + 202;
		int hueY = dy + 24;
		int hueW = 16;
		int hueH = 80;
		int[] hues = new int[] { 0xFF0000, 0xFFFF00, 0x00FF00, 0x00FFFF, 0x0000FF, 0xFF00FF, 0xFF0000 };
		int segmentHeight = Math.max(1, hueH / 6);
		for (int i = 0; i < 6; i++) {
			int yStart = hueY + (i * segmentHeight);
			int yEnd = (i == 5) ? (hueY + hueH) : (yStart + segmentHeight);
			guiGraphics.fillGradient(hueX, yStart, hueX + hueW, yEnd, 0xFF000000 | hues[i], 0xFF000000 | hues[i + 1]);
		}
		drawMinecraftFrame(guiGraphics, hueX, hueY, hueW, hueH);

		int infoX = dx + 226;
		int swatchY = dy + 24;
		int swatchW = 70;
		int swatchH = 20;
		int rgb = java.awt.Color.HSBtoRGB(selectedHue, selectedSaturation, selectedValue) & 0xFFFFFF;
		guiGraphics.fill(infoX, swatchY, infoX + swatchW, swatchY + swatchH, 0xFF000000 | rgb);
		drawMinecraftFrame(guiGraphics, infoX, swatchY, swatchW, swatchH);

		String playerName = mc.player != null ? mc.player.getGameProfile().name() : "Player";
		guiGraphics.centeredText(mc.font, Component.literal(playerName), infoX + swatchW / 2, swatchY + swatchH + 4, 0xFF000000 | rgb);

		hexEditBox.setPosition(infoX, dy + 62);
		hexEditBox.setWidth(swatchW);
		hexEditBox.setHeight(16);
		hexEditBox.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTicks);

		copyButton.setPosition(infoX, dy + 82);
		copyButton.setWidth(33);
		copyButton.setHeight(16);
		copyButton.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);

		pasteButton.setPosition(infoX + 37, dy + 82);
		pasteButton.setWidth(33);
		pasteButton.setHeight(16);
		pasteButton.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);

		int doneX = dx + (dw - 100) / 2;
		int doneY = dy + 122;
		doneButton.setPosition(doneX, doneY);
		doneButton.setWidth(100);
		doneButton.setHeight(20);
		doneButton.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);

		int selX = mapX + Math.round(selectedSaturation * (mapW - 1));
		int selY = mapY + Math.round((1f - selectedValue) * (mapH - 1));
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, COLOR_SELECTOR, selX - 4, selY - 4, 0, 0, 8, 8, 8, 8);

		int hueSelY = hueY + Math.round(selectedHue * (hueH - 1));
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, COLOR_SELECTOR, hueX + (hueW - 8) / 2, hueSelY - 4, 0, 0, 8, 8, 8, 8);
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
}
