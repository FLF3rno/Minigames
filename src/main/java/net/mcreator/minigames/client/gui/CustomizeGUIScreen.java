package net.mcreator.minigames.client.gui;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.minigames.world.inventory.CustomizeGUIMenu;
import net.mcreator.minigames.client.NameColorPreferenceClient;
import net.mcreator.minigames.procedures.GetDisplayNameProcedure;
import net.mcreator.minigames.network.CustomizeGUIButtonMessage;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.network.NameColorPreferenceMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

public class CustomizeGUIScreen extends AbstractContainerScreen<CustomizeGUIMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private ImageButton imagebutton_compass_16;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("minigames:textures/screens/customize_gui.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/halfextraslot.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("minigames:textures/screens/selectedextraslot.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("minigames:textures/screens/bucket.png");
	private static final ResourceLocation COLOR_SELECTOR = ResourceLocation.parse("minigames:textures/screens/colorselector.png");
	private static final int HSV_X = 48;
	private static final int HSV_Y = 22;
	private static final int HSV_W = 220;
	private static final int HSV_H = 72;
	private static final int HUE_X = HSV_X + HSV_W + 10;
	private static final int HUE_Y = HSV_Y;
	private static final int HUE_W = 14;
	private static final int HUE_H = HSV_H;
	private static final int SWATCH_X = HSV_X - 26;
	private static final int SWATCH_Y = HSV_Y;
	private static final int SWATCH_W = 18;
	private static final int SWATCH_H = HSV_H;
	private static final int PREVIEW_ENTITY_X = 40;
	private static final int PREVIEW_ENTITY_Y = 78;
	private static final int PREVIEW_ENTITY_SCALE = 28;
	private float selectedHue = 0.0f;
	private float selectedSaturation = 1.0f;
	private float selectedValue = 1.0f;
	private boolean draggingMap = false;
	private boolean draggingHue = false;
	private long lastNetworkColorSendAt = 0L;
	private static final long DRAG_NETWORK_SEND_INTERVAL_MS = 120L;

	public CustomizeGUIScreen(CustomizeGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 311;
		this.imageHeight = 112;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + 4, this.topPos + 99, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 268, this.topPos + 99, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + 272, this.topPos + 113, 0, 0, 32, 32, 32, 32);
		renderHsvMap(guiGraphics);
		renderHueBar(guiGraphics);
		renderSelectedColorSwatch(guiGraphics);
		renderHsvBorders(guiGraphics);
		renderIndicators(guiGraphics);
		renderEntityPreview(guiGraphics);
		renderSelectedNamePreview(guiGraphics);
	}

	private void renderHsvBorders(GuiGraphics guiGraphics) {
		int mapX = this.leftPos + HSV_X;
		int mapY = this.topPos + HSV_Y;
		int hueX = this.leftPos + HUE_X;
		int hueY = this.topPos + HUE_Y;
		int swatchX = this.leftPos + SWATCH_X;
		int swatchY = this.topPos + SWATCH_Y;
		drawMinecraftFrame(guiGraphics, mapX, mapY, HSV_W, HSV_H);
		drawMinecraftFrame(guiGraphics, hueX, hueY, HUE_W, HUE_H);
		drawMinecraftFrame(guiGraphics, swatchX, swatchY, SWATCH_W, SWATCH_H);
	}

	private void renderSelectedColorSwatch(GuiGraphics guiGraphics) {
		int swatchX = this.leftPos + SWATCH_X;
		int swatchY = this.topPos + SWATCH_Y;
		int swatchColor = 0xFF000000 | (resolveGuiDisplayTextColor() & 0xFFFFFF);
		guiGraphics.fill(swatchX, swatchY, swatchX + SWATCH_W, swatchY + SWATCH_H, swatchColor);
	}

	private void drawMinecraftFrame(GuiGraphics guiGraphics, int x, int y, int w, int h) {
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

	private void renderEntityPreview(GuiGraphics guiGraphics) {
		if (this.minecraft == null || this.minecraft.player == null)
			return;
		InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, this.leftPos + PREVIEW_ENTITY_X, this.topPos + PREVIEW_ENTITY_Y, PREVIEW_ENTITY_SCALE, 0, 0, 0f, 0f, 0f, this.minecraft.player);
	}

	private void renderSelectedNamePreview(GuiGraphics guiGraphics) {
		int previewColor = resolveGuiDisplayTextColor();
		String previewName = (this.minecraft != null && this.minecraft.player != null) ? this.minecraft.player.getGameProfile().getName() : entity.getName().getString();
		int previewX = this.leftPos + 10;
		int previewY = this.topPos + 96;
		guiGraphics.drawString(this.font, previewName, previewX + 1, previewY + 1, previewColor, false);
		guiGraphics.drawString(this.font, previewName, previewX, previewY, previewColor, true);
	}

	private void renderHsvMap(GuiGraphics guiGraphics) {
		int x0 = this.leftPos + HSV_X;
		int y0 = this.topPos + HSV_Y;
		for (int px = 0; px < HSV_W; px++) {
			float sat = px / (float) (HSV_W - 1);
			int topRgb = java.awt.Color.HSBtoRGB(selectedHue, sat, 1.0f) & 0xFFFFFF;
			guiGraphics.fillGradient(x0 + px, y0, x0 + px + 1, y0 + HSV_H, 0xFF000000 | topRgb, 0xFF000000);
		}
	}

	private void renderHueBar(GuiGraphics guiGraphics) {
		int x0 = this.leftPos + HUE_X;
		int y0 = this.topPos + HUE_Y;
		int[] hues = new int[] { 0xFF0000, 0xFFFF00, 0x00FF00, 0x00FFFF, 0x0000FF, 0xFF00FF, 0xFF0000 };
		int segmentHeight = Math.max(1, HUE_H / 6);
		for (int i = 0; i < 6; i++) {
			int yStart = y0 + (i * segmentHeight);
			int yEnd = (i == 5) ? (y0 + HUE_H) : (yStart + segmentHeight);
			guiGraphics.fillGradient(x0, yStart, x0 + HUE_W, yEnd, 0xFF000000 | hues[i], 0xFF000000 | hues[i + 1]);
		}
	}

	private void renderIndicators(GuiGraphics guiGraphics) {
		int mapX = this.leftPos + HSV_X + Math.round(selectedSaturation * (HSV_W - 1));
		int mapY = this.topPos + HSV_Y + Math.round((1.0f - selectedValue) * (HSV_H - 1));
		int hueY = this.topPos + HUE_Y + Math.round(selectedHue * (HUE_H - 1));
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, COLOR_SELECTOR, mapX - 4, mapY - 4, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, COLOR_SELECTOR, this.leftPos + HUE_X + (HUE_W / 2) - 4, hueY - 4, 0, 0, 9, 9, 9, 9);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0 && tryPickColor(mouseX, mouseY)) {
			draggingMap = isInMap(mouseX, mouseY);
			draggingHue = isInHue(mouseX, mouseY);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (button == 0 && (draggingMap || draggingHue) && tryPickColor(mouseX, mouseY, true)) {
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0) {
			sendSelectedColor(false);
		}
		draggingMap = false;
		draggingHue = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	private boolean tryPickColor(double mouseX, double mouseY) {
		return tryPickColor(mouseX, mouseY, false);
	}

	private boolean tryPickColor(double mouseX, double mouseY, boolean fromDrag) {
		boolean inMap = isInMap(mouseX, mouseY) || draggingMap;
		boolean inHue = isInHue(mouseX, mouseY) || draggingHue;
		if (!inMap && !inHue)
			return false;
		if (inMap) {
			int localX = Math.max(0, Math.min(HSV_W - 1, (int) mouseX - this.leftPos - HSV_X));
			int localY = Math.max(0, Math.min(HSV_H - 1, (int) mouseY - this.topPos - HSV_Y));
			selectedSaturation = localX / (float) (HSV_W - 1);
			selectedValue = 1.0f - (localY / (float) (HSV_H - 1));
		}
		if (inHue) {
			int localY = Math.max(0, Math.min(HUE_H - 1, (int) mouseY - this.topPos - HUE_Y));
			selectedHue = localY / (float) (HUE_H - 1);
		}
		sendSelectedColor(fromDrag);
		return true;
	}

	private void sendSelectedColor(boolean fromDrag) {
		int rgb = java.awt.Color.HSBtoRGB(selectedHue, selectedSaturation, selectedValue) & 0xFFFFFF;
		String hex = String.format("#%06X", rgb);
		if (this.minecraft != null && this.minecraft.player != null) {
			MinigamesModVariables.PlayerVariables localVars = this.minecraft.player.getData(MinigamesModVariables.PLAYER_VARIABLES);
			localVars.color = hex;
			localVars.markSyncDirty();
			this.minecraft.player.setCustomName(Component.literal(this.minecraft.player.getName().getString()).withColor(previewRgbToHexInt(hex)));
			this.minecraft.player.setCustomNameVisible(true);
			long now = System.currentTimeMillis();
			if (!fromDrag || now - lastNetworkColorSendAt >= DRAG_NETWORK_SEND_INTERVAL_MS) {
				NameColorPreferenceClient.sendCurrentVariableColorNow(this.minecraft.player);
				lastNetworkColorSendAt = now;
			}
		}
	}

	private int previewRgbToHexInt(String hex) {
		String normalized = hex.startsWith("#") ? hex.substring(1) : hex;
		return Integer.parseInt(normalized, 16);
	}

	private boolean isInMap(double mouseX, double mouseY) {
		return mouseX >= this.leftPos + HSV_X && mouseX < this.leftPos + HSV_X + HSV_W && mouseY >= this.topPos + HSV_Y && mouseY < this.topPos + HSV_Y + HSV_H;
	}

	private boolean isInHue(double mouseX, double mouseY) {
		return mouseX >= this.leftPos + HUE_X && mouseX < this.leftPos + HUE_X + HUE_W && mouseY >= this.topPos + HUE_Y && mouseY < this.topPos + HUE_Y + HUE_H;
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			sendSelectedColor(false);
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.customize_gui.label_customize_player_name"), 5, 5, -12829636, false);
		int textColor = resolveGuiDisplayTextColor();
		String displayName = (this.minecraft != null && this.minecraft.player != null) ? this.minecraft.player.getGameProfile().getName() : GetDisplayNameProcedure.execute(entity);
		guiGraphics.drawString(this.font, displayName, 6, 98, textColor, true);
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

	@Override
	public void init() {
		super.init();
		loadCurrentColorIntoPicker();
		imagebutton_compass_16 = new ImageButton(this.leftPos + 6, this.topPos + 113, 36, 36,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/compass_16.png"), ResourceLocation.parse("minigames:textures/screens/selectedgamecompass.png")), e -> {
					ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(13, CustomizeGUIScreen.this.x, CustomizeGUIScreen.this.y, z));
					CustomizeGUIButtonMessage.handleButtonAction(entity, 13, CustomizeGUIScreen.this.x, CustomizeGUIScreen.this.y, z);
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_compass_16);
	}

	private void loadCurrentColorIntoPicker() {
		String color = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
		if (color == null || !color.matches("^#?[0-9a-fA-F]{6}$"))
			return;
		String normalized = color.startsWith("#") ? color.substring(1) : color;
		int rgb = Integer.parseInt(normalized, 16);
		float[] hsb = java.awt.Color.RGBtoHSB((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255, null);
		selectedHue = hsb[0];
		selectedSaturation = hsb[1];
		selectedValue = hsb[2];
	}
}
