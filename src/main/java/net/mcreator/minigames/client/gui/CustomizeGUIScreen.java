package net.mcreator.minigames.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.minigames.init.MinigamesModScreens;
import net.mcreator.minigames.network.CustomizeGUIButtonMessage;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.network.NameColorPreferenceMessage;
import net.mcreator.minigames.world.inventory.CustomizeGUIMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

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
	private int activeDragTarget = 0;
	private boolean isUpdatingInternally = false;

	private EditBox hexEditBox;
	private Button copyButton;
	private Button pasteButton;

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

		this.hexEditBox = new EditBox(this.font, this.leftPos + 226, this.topPos + 54, 68, 16, Component.literal("Hex"));
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
					updateCurrentColor();
				} catch (Throwable ignored) {
				}
			}
		});
		this.addWidget(this.hexEditBox);

		this.copyButton = Button.builder(Component.literal("Copy"), b -> copyHex()).bounds(this.leftPos + 226, this.topPos + 74, 32, 16).build();
		this.addRenderableWidget(this.copyButton);

		this.pasteButton = Button.builder(Component.literal("Paste"), b -> pasteHex()).bounds(this.leftPos + 262, this.topPos + 74, 32, 16).build();
		this.addRenderableWidget(this.pasteButton);

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
						RenderPipelines.GUI_TEXTURED,
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
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		if (this.hexEditBox != null) {
			this.hexEditBox.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + 4, this.topPos + 99, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 268, this.topPos + 99, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + 272, this.topPos + 113, 0, 0, 32, 32, 32, 32);
		renderHsvMap(guiGraphics);
		renderHueBar(guiGraphics);
		renderSelectedColorSwatch(guiGraphics);
		renderHsvBorders(guiGraphics);
		renderSelector(guiGraphics);
	}

	@Override
	public void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.literal("Customize your name!"), 18, 8, -12829636, false);
		Component name = resolveDisplayName();
		guiGraphics.centeredText(this.font, name, 260, 43, resolveGuiDisplayTextColor());
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == 0) {
			if (this.hexEditBox != null && this.hexEditBox.mouseClicked(event, doubleClick)) {
				return true;
			}
			if (tryStartColorPickerDrag(event.x(), event.y())) {
				if (this.hexEditBox != null) {
					this.hexEditBox.setFocused(false);
				}
				return true;
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
		if (event.button() == 0 && activeDragTarget != 0) {
			updateColorFromDrag(event.x(), event.y());
			return true;
		}
		return super.mouseDragged(event, dragX, dragY);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (event.button() == 0) {
			activeDragTarget = 0;
		}
		return super.mouseReleased(event);
	}

	private boolean tryStartColorPickerDrag(double mouseX, double mouseY) {
		int mapX = this.leftPos + 18;
		int mapY = this.topPos + 22;
		int mapW = 180;
		int mapH = 72;

		int hueX = this.leftPos + 204;
		int hueY = this.topPos + 22;
		int hueW = 16;
		int hueH = 72;

		if (mouseX >= mapX && mouseX < mapX + mapW && mouseY >= mapY && mouseY < mapY + mapH) {
			activeDragTarget = 1;
			updateColorFromDrag(mouseX, mouseY);
			return true;
		}

		if (mouseX >= hueX && mouseX < hueX + hueW && mouseY >= hueY && mouseY < hueY + hueH) {
			activeDragTarget = 2;
			updateColorFromDrag(mouseX, mouseY);
			return true;
		}

		return false;
	}

	private void updateColorFromDrag(double mouseX, double mouseY) {
		int mapX = this.leftPos + 18;
		int mapY = this.topPos + 22;
		int mapW = 180;
		int mapH = 72;

		int hueY = this.topPos + 22;
		int hueH = 72;

		if (activeDragTarget == 1) {
			selectedSaturation = (float) ((mouseX - mapX) / (double) (mapW - 1));
			selectedValue = 1f - (float) ((mouseY - mapY) / (double) (mapH - 1));

			selectedSaturation = Math.max(0f, Math.min(1f, selectedSaturation));
			selectedValue = Math.max(0f, Math.min(1f, selectedValue));

			updateCurrentColor();
			syncEditBoxText();
		} else if (activeDragTarget == 2) {
			selectedHue = (float) ((mouseY - hueY) / (double) (hueH - 1));
			selectedHue = Math.max(0f, Math.min(1f, selectedHue));

			updateCurrentColor();
			syncEditBoxText();
		}
	}

	private String getFormattedHex() {
		int rgb = java.awt.Color.HSBtoRGB(selectedHue, selectedSaturation, selectedValue) & 0xFFFFFF;
		return String.format(java.util.Locale.ROOT, "#%06X", rgb);
	}

	private void syncEditBoxText() {
		if (hexEditBox != null) {
			isUpdatingInternally = true;
			hexEditBox.setValue(getFormattedHex());
			isUpdatingInternally = false;
		}
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
				if (hexEditBox != null) {
					hexEditBox.setValue(trimmed);
				}
			}
		}
	}

	private void updateCurrentColor() {
		int rgb = java.awt.Color.HSBtoRGB(
				selectedHue,
				selectedSaturation,
				selectedValue);

		String hex = String.format("#%06X", rgb & 0xFFFFFF);
		if (this.minecraft != null && this.minecraft.player != null) {
			net.mcreator.minigames.AccountSwapManager.SwapRecord record = net.mcreator.minigames.AccountSwapManager.getRecord(this.minecraft.player.getUUID());
			if (record != null) {
				record.originalColor = hex;
			} else {
				this.minecraft.player.getData(MinigamesModVariables.PLAYER_VARIABLES).color = hex;
				if (entity != null) {
					entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color = hex;
				}
			}
			ClientPacketDistributor.sendToServer(new NameColorPreferenceMessage(hex));
			net.mcreator.minigames.client.NameColorPreferenceClient.writeClientColorNow(this.minecraft.player.getUUID(), hex);
		}
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (this.hexEditBox != null && this.hexEditBox.isFocused()) {
			int key = InputConstants.getKey(event).getValue();
			if (key == 256) {
				this.hexEditBox.setFocused(false);
				return true;
			}
			return this.hexEditBox.keyPressed(event);
		}
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(event);
	}

	private void renderHsvMap(GuiGraphicsExtractor guiGraphics) {
		int x0 = this.leftPos + 18;
		int y0 = this.topPos + 22;
		int w = 180;
		int h = 72;
		for (int px = 0; px < w; px++) {
			float sat = px / (float) (w - 1);
			int topRgb = java.awt.Color.HSBtoRGB(selectedHue, sat, 1.0f) & 0xFFFFFF;
			guiGraphics.fillGradient(x0 + px, y0, x0 + px + 1, y0 + h, 0xFF000000 | topRgb, 0xFF000000);
		}
	}

	private void renderHueBar(GuiGraphicsExtractor guiGraphics) {
		int x0 = this.leftPos + 204;
		int y0 = this.topPos + 22;
		int w = 16;
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
		int swatchColor = resolveGuiDisplayTextColor();
		guiGraphics.fill(this.leftPos + 226, this.topPos + 22, this.leftPos + 226 + 68, this.topPos + 22 + 18, swatchColor);
	}

	private void renderSelector(GuiGraphicsExtractor guiGraphics) {
		int mapX = this.leftPos + 18;
		int mapY = this.topPos + 22;
		int mapW = 180;
		int mapH = 72;

		int selectorX = mapX + Math.round(selectedSaturation * (mapW - 1));
		int selectorY = mapY + Math.round((1f - selectedValue) * (mapH - 1));

		guiGraphics.blit(
				RenderPipelines.GUI_TEXTURED,
				COLOR_SELECTOR,
				selectorX - 4,
				selectorY - 4,
				0, 0,
				8, 8,
				8, 8
		);

		int hueX = this.leftPos + 204;
		int hueY = this.topPos + 22;
		int hueW = 16;
		int hueH = 72;

		int hueSelectorY = hueY + Math.round(selectedHue * (hueH - 1));

		guiGraphics.blit(
				RenderPipelines.GUI_TEXTURED,
				COLOR_SELECTOR,
				hueX + (hueW - 8) / 2,
				hueSelectorY - 4,
				0, 0,
				8, 8,
				8, 8
		);
	}

	private void renderHsvBorders(GuiGraphicsExtractor guiGraphics) {
		drawMinecraftFrame(guiGraphics, this.leftPos + 18, this.topPos + 22, 180, 72);
		drawMinecraftFrame(guiGraphics, this.leftPos + 204, this.topPos + 22, 16, 72);
		drawMinecraftFrame(guiGraphics, this.leftPos + 226, this.topPos + 22, 68, 18);
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
		int rgb = java.awt.Color.HSBtoRGB(selectedHue, selectedSaturation, selectedValue) & 0xFFFFFF;
		return 0xFF000000 | rgb;
	}

	private void loadCurrentColorIntoPicker() {
		String color = null;
		if (this.minecraft != null && this.minecraft.player != null) {
			net.mcreator.minigames.AccountSwapManager.SwapRecord record = net.mcreator.minigames.AccountSwapManager.getRecord(this.minecraft.player.getUUID());
			if (record != null && record.originalColor != null && !record.originalColor.isEmpty()) {
				color = record.originalColor;
			} else {
				String diskColor = net.mcreator.minigames.client.NameColorPreferenceClient.readClientColor(this.minecraft.player.getUUID());
				if (diskColor != null && !diskColor.isEmpty()) {
					color = diskColor;
				} else {
					color = this.minecraft.player.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
				}
			}
		}
		if ((color == null || color.isEmpty()) && entity != null) {
			color = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
		}
		if (color == null || !color.matches("^#?[0-9a-fA-F]{6}$")) {
			return;
		}
		String normalized = color.startsWith("#") ? color.substring(1) : color;
		try {
			int rgb = Integer.parseInt(normalized, 16);
			float[] hsb = java.awt.Color.RGBtoHSB((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255, null);
			selectedHue = hsb[0];
			selectedSaturation = hsb[1];
			selectedValue = hsb[2];
		} catch (Throwable ignored) {
		}
	}

	private Component resolveDisplayName() {
		if (this.minecraft != null && this.minecraft.player != null) {
			return Component.literal(this.minecraft.player.getGameProfile().name());
		}
		if (entity != null) {
			return entity.getName();
		}
		return Component.literal("Player");
	}
}