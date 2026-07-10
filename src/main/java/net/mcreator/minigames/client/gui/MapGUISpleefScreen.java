package net.mcreator.minigames.client.gui;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.minigames.world.inventory.MapGUISpleefMenu;
import net.mcreator.minigames.procedures.SteampunkSelectedProcedure;
import net.mcreator.minigames.procedures.SolarSystemSelectedProcedure;
import net.mcreator.minigames.procedures.ChristmasSelectedProcedure;
import net.mcreator.minigames.procedures.BalloonsSelectedProcedure;
import net.mcreator.minigames.network.MapGUISpleefButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class MapGUISpleefScreen extends AbstractContainerScreen<MapGUISpleefMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Button button_start_normal;
	private ImageButton imagebutton_maphovered;
	private ImageButton imagebutton_nothing;
	private ImageButton imagebutton_nothing1;
	private ImageButton imagebutton_nothing2;
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/spleefwindow.png");
	private static final Identifier IMAGE_1 = Identifier.parse("minigames:textures/screens/selectedoverlay.png");
	private static final Identifier IMAGE_2 = Identifier.parse("minigames:textures/screens/hotairballoons.png");
	private static final Identifier IMAGE_3 = Identifier.parse("minigames:textures/screens/selectedoverlay.png");
	private static final Identifier IMAGE_4 = Identifier.parse("minigames:textures/screens/solarsystemmap.png");
	private static final Identifier IMAGE_5 = Identifier.parse("minigames:textures/screens/selectedoverlay.png");
	private static final Identifier IMAGE_6 = Identifier.parse("minigames:textures/screens/selectedoverlay.png");
	private static final Identifier IMAGE_7 = Identifier.parse("minigames:textures/screens/christmas.png");
	private static final Identifier IMAGE_8 = Identifier.parse("minigames:textures/screens/steampunk.png");
	private static final Identifier IMAGE_9 = Identifier.parse("minigames:textures/screens/emptymap.png");
	private static final Identifier IMAGE_10 = Identifier.parse("minigames:textures/screens/emptymap.png");

	public MapGUISpleefScreen(MapGUISpleefMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 180, 168);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -100, this.topPos + -28, 0, 0, 384, 384, 384, 384);
		if (BalloonsSelectedProcedure.execute(world)) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 36, this.topPos + 0, 0, 0, 107, 75, 107, 75);
		}
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + 42, this.topPos + 7, 0, 0, 95, 62, 95, 62);
		if (SolarSystemSelectedProcedure.execute(world)) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, this.leftPos + 36, this.topPos + 78, 0, 0, 107, 75, 107, 75);
		}
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_4, this.leftPos + 42, this.topPos + 85, 0, 0, 95, 62, 95, 62);
		if (SteampunkSelectedProcedure.execute(world)) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_5, this.leftPos + -75, this.topPos + 1, 0, 0, 107, 75, 107, 75);
		}
		if (ChristmasSelectedProcedure.execute(world)) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_6, this.leftPos + -75, this.topPos + 79, 0, 0, 107, 75, 107, 75);
		}
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_7, this.leftPos + -70, this.topPos + 85, 0, 0, 95, 62, 95, 62);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_8, this.leftPos + -70, this.topPos + 7, 0, 0, 95, 62, 95, 62);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_9, this.leftPos + 154, this.topPos + 85, 0, 0, 95, 62, 95, 62);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_10, this.leftPos + 154, this.topPos + 7, 0, 0, 95, 62, 95, 62);
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

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.translatable("gui.minigames.map_gui_spleef.label_achievement_run"), -88, -19, -12829636, false);
		guiGraphics.text(this.font, Component.translatable("gui.minigames.map_gui_spleef.label_hot_air_balloons"), 48, 71, -16777216, false);
		guiGraphics.text(this.font, Component.translatable("gui.minigames.map_gui_spleef.label_solar_system"), 59, 149, -16777216, false);
		guiGraphics.text(this.font, Component.translatable("gui.minigames.map_gui_spleef.label_steampunk"), -47, 71, -16777216, false);
		guiGraphics.text(this.font, Component.translatable("gui.minigames.map_gui_spleef.label_christmas"), -48, 149, -16777216, false);
	}

	@Override
	public void init() {
		super.init();
		button_start_normal = Button.builder(Component.translatable("gui.minigames.map_gui_spleef.button_start_normal"), e -> {
			int x = MapGUISpleefScreen.this.x;
			int y = MapGUISpleefScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MapGUISpleefButtonMessage(0, x, y, z));
				MapGUISpleefButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 40, this.topPos + 164, 98, 20).build();
		this.addRenderableWidget(button_start_normal);
		imagebutton_maphovered = new ImageButton(this.leftPos + 46, this.topPos + 11, 87, 54, new WidgetSprites(Identifier.parse("minigames:textures/screens/nothing.png"), Identifier.parse("minigames:textures/screens/nothing.png")), e -> {
			int x = MapGUISpleefScreen.this.x;
			int y = MapGUISpleefScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MapGUISpleefButtonMessage(1, x, y, z));
				MapGUISpleefButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_maphovered);
		imagebutton_nothing = new ImageButton(this.leftPos + 46, this.topPos + 89, 87, 54, new WidgetSprites(Identifier.parse("minigames:textures/screens/nothing.png"), Identifier.parse("minigames:textures/screens/nothing.png")), e -> {
			int x = MapGUISpleefScreen.this.x;
			int y = MapGUISpleefScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MapGUISpleefButtonMessage(2, x, y, z));
				MapGUISpleefButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_nothing);
		imagebutton_nothing1 = new ImageButton(this.leftPos + -66, this.topPos + 11, 87, 54, new WidgetSprites(Identifier.parse("minigames:textures/screens/nothing.png"), Identifier.parse("minigames:textures/screens/nothing.png")), e -> {
			int x = MapGUISpleefScreen.this.x;
			int y = MapGUISpleefScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MapGUISpleefButtonMessage(3, x, y, z));
				MapGUISpleefButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_nothing1);
		imagebutton_nothing2 = new ImageButton(this.leftPos + -66, this.topPos + 89, 87, 54, new WidgetSprites(Identifier.parse("minigames:textures/screens/nothing.png"), Identifier.parse("minigames:textures/screens/nothing.png")), e -> {
			int x = MapGUISpleefScreen.this.x;
			int y = MapGUISpleefScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MapGUISpleefButtonMessage(4, x, y, z));
				MapGUISpleefButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_nothing2);
	}
}