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
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.minigames.world.inventory.MinigameGUISpleefMenu;
import net.mcreator.minigames.procedures.PowerupChecklistProcedure;
import net.mcreator.minigames.procedures.HideNoMapsSelectedProcedure;
import net.mcreator.minigames.network.MinigameGUISpleefButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class MinigameGUISpleefScreen extends AbstractContainerScreen<MinigameGUISpleefMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private EditBox passive;
	private Checkbox powerup;
	private Button button_start_normal;
	private Button button_modify;
	private ImageButton imagebutton_compass_16;
	private ImageButton imagebutton_bucket;
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/spleefwindow.png");
	private static final Identifier IMAGE_1 = Identifier.parse("minigames:textures/screens/symmetrical_shovel.png");
	private static final Identifier IMAGE_2 = Identifier.parse("minigames:textures/screens/halfextraslot.png");
	private static final Identifier IMAGE_3 = Identifier.parse("minigames:textures/screens/halfextraslot.png");
	private static final Identifier IMAGE_4 = Identifier.parse("minigames:textures/screens/map_icon.png");
	private static final Identifier IMAGE_5 = Identifier.parse("minigames:textures/screens/snowball2x.png");
	private static final Identifier IMAGE_6 = Identifier.parse("minigames:textures/screens/clock.png");

	public MinigameGUISpleefScreen(MinigameGUISpleefMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 176, 166);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		if (elementType == 0 && elementState instanceof String stringState) {
			if (name.equals("passive"))
				passive.setValue(stringState);
		}
		if (elementType == 1 && elementState instanceof Boolean logicState) {
			if (name.equals("powerup")) {
				if (powerup.selected() != logicState)
					powerup.onPress(null);
			}
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (mouseX > leftPos + 69 && mouseX < leftPos + 114 && mouseY > topPos + 45 && mouseY < topPos + 115) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_spleef.tooltip_keep_inventory"), mouseX, mouseY);
		}
		if (mouseX > leftPos + 168 && mouseX < leftPos + 213 && mouseY > topPos + 41 && mouseY < topPos + 115) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_spleef.tooltip_modify_the_map_pool"), mouseX, mouseY);
		}
		if (mouseX > leftPos + -52 && mouseX < leftPos + 36 && mouseY > topPos + 47 && mouseY < topPos + 124) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_spleef.tooltip_passive_snowball_gain_per_second"), mouseX, mouseY);
		}
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		passive.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 74, this.topPos + 49, 0, 0, 32, 32, 32, 32);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + -97, this.topPos + 168, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, this.leftPos + 231, this.topPos + 167, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_4, this.leftPos + 175, this.topPos + 48, 0, 0, 32, 32, 32, 32);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_5, this.leftPos + -28, this.topPos + 50, 0, 0, 32, 32, 32, 32);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_6, this.leftPos + -12, this.topPos + 65, 0, 0, 16, 16, 16, 16);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (passive.isFocused())
			return passive.keyPressed(event);
		return super.keyPressed(event);
	}

	@Override
	public void resize(int width, int height) {
		String passiveValue = passive.getValue();
		super.resize(width, height);
		passive.setValue(passiveValue);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.translatable("gui.minigames.minigame_gui_spleef.label_achievement_run"), -90, -20, -12829636, false);
		guiGraphics.text(this.font, Component.translatable("gui.minigames.minigame_gui_spleef.label_powerups"), 69, 33, -12829636, false);
		guiGraphics.text(this.font, Component.translatable("gui.minigames.minigame_gui_spleef.label_maps"), 180, 35, -12829636, false);
		guiGraphics.text(this.font, Component.translatable("gui.minigames.minigame_gui_spleef.label_passive_snowballs"), -53, 36, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		passive = new EditBox(this.font, this.leftPos + -27, this.topPos + 87, 33, 20, Component.translatable("gui.minigames.minigame_gui_spleef.passive"));
		passive.setMaxLength(8192);
		passive.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "passive", content, false);
		});
		passive.setHint(Component.translatable("gui.minigames.minigame_gui_spleef.passive"));
		this.addWidget(this.passive);
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
		imagebutton_compass_16 = new ImageButton(this.leftPos + -95, this.topPos + 182, 36, 36, new WidgetSprites(Identifier.parse("minigames:textures/screens/compass_16.png"), Identifier.parse("minigames:textures/screens/selectedgamecompass.png")),
				e -> {
					int x = MinigameGUISpleefScreen.this.x;
					int y = MinigameGUISpleefScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new MinigameGUISpleefButtonMessage(2, x, y, z));
						MinigameGUISpleefButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_compass_16);
		imagebutton_bucket = new ImageButton(this.leftPos + 235, this.topPos + 181, 32, 32, new WidgetSprites(Identifier.parse("minigames:textures/screens/bucket.png"), Identifier.parse("minigames:textures/screens/selectcolor.png")), e -> {
			int x = MinigameGUISpleefScreen.this.x;
			int y = MinigameGUISpleefScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MinigameGUISpleefButtonMessage(3, x, y, z));
				MinigameGUISpleefButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_bucket);
		boolean powerupSelected = PowerupChecklistProcedure.execute(world);
		powerup = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_spleef.powerup"), this.font).pos(this.leftPos + 80, this.topPos + 87).onValueChange((checkbox, value) -> {
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