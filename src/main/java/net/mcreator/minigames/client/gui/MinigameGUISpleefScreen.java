package net.mcreator.minigames.client.gui;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.minigames.world.inventory.MinigameGUISpleefMenu;
import net.mcreator.minigames.procedures.KeepInventoryCheckedProcedure;
import net.mcreator.minigames.procedures.HideNoMapsSelectedProcedure;
import net.mcreator.minigames.network.MinigameGUISpleefButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

public class MinigameGUISpleefScreen extends AbstractContainerScreen<MinigameGUISpleefMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Checkbox powerup;
	private Button button_start_normal;
	private Button button_modify;
	private ImageButton imagebutton_compass_16;
	private ImageButton imagebutton_bucket;
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/spleefwindow.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("minigames:textures/screens/symmetrical_shovel.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("minigames:textures/screens/halfextraslot.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("minigames:textures/screens/halfextraslot.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("minigames:textures/screens/map_icon.png");

	public MinigameGUISpleefScreen(MinigameGUISpleefMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		if (elementType == 1 && elementState instanceof Boolean logicState) {
			if (name.equals("powerup")) {
				if (powerup.selected() != logicState)
					powerup.onPress();
			}
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		boolean customTooltipShown = false;
		if (mouseX > leftPos + 69 && mouseX < leftPos + 114 && mouseY > topPos + 45 && mouseY < topPos + 115) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_spleef.tooltip_keep_inventory"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 168 && mouseX < leftPos + 213 && mouseY > topPos + 41 && mouseY < topPos + 115) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_spleef.tooltip_modify_the_map_pool"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (!customTooltipShown)
			this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 74, this.topPos + 47, 0, 0, 32, 32, 32, 32);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + -97, this.topPos + 168, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, this.leftPos + 231, this.topPos + 167, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_4, this.leftPos + 175, this.topPos + 48, 0, 0, 32, 32, 32, 32);
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.minigame_gui_spleef.label_achievement_run"), -90, -20, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.minigame_gui_spleef.label_powerups"), 69, 33, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.minigame_gui_spleef.label_maps"), 180, 35, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		button_start_normal = Button.builder(Component.translatable("gui.minigames.minigame_gui_spleef.button_start_normal"), e -> {
			int x = MinigameGUISpleefScreen.this.x;
			int y = MinigameGUISpleefScreen.this.y;
			if (HideNoMapsSelectedProcedure.execute(world, entity)) {
				ClientPacketDistributor.sendToServer(new MinigameGUISpleefButtonMessage(0, x, y, z));
				MinigameGUISpleefButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 38, this.topPos + 163, 98, 20).build();
		this.addRenderableWidget(button_start_normal);
		button_modify = Button.builder(Component.translatable("gui.minigames.minigame_gui_spleef.button_modify"), e -> {
			int x = MinigameGUISpleefScreen.this.x;
			int y = MinigameGUISpleefScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MinigameGUISpleefButtonMessage(1, x, y, z));
				MinigameGUISpleefButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}).bounds(this.leftPos + 169, this.topPos + 87, 43, 20).build();
		this.addRenderableWidget(button_modify);
		imagebutton_compass_16 = new ImageButton(this.leftPos + -95, this.topPos + 182, 36, 36,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/compass_16.png"), ResourceLocation.parse("minigames:textures/screens/selectedgamecompass.png")), e -> {
					int x = MinigameGUISpleefScreen.this.x;
					int y = MinigameGUISpleefScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new MinigameGUISpleefButtonMessage(2, x, y, z));
						MinigameGUISpleefButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_compass_16);
		imagebutton_bucket = new ImageButton(this.leftPos + 235, this.topPos + 181, 32, 32, new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/bucket.png"), ResourceLocation.parse("minigames:textures/screens/selectcolor.png")),
				e -> {
					int x = MinigameGUISpleefScreen.this.x;
					int y = MinigameGUISpleefScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new MinigameGUISpleefButtonMessage(3, x, y, z));
						MinigameGUISpleefButtonMessage.handleButtonAction(entity, 3, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_bucket);
		boolean powerupSelected = KeepInventoryCheckedProcedure.execute();
		powerup = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_spleef.powerup"), this.font).pos(this.leftPos + 80, this.topPos + 92).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "powerup", value, false);
		}).selected(powerupSelected).build();
		if (powerupSelected)
			menu.sendMenuStateUpdate(entity, 1, "powerup", true, false);
		this.addRenderableWidget(powerup);
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		this.button_start_normal.visible = HideNoMapsSelectedProcedure.execute(world, entity);
	}
}