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
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.minigames.world.inventory.RoguelikeStartGameMenu;
import net.mcreator.minigames.network.RoguelikeStartGameButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class RoguelikeStartGameScreen extends AbstractContainerScreen<RoguelikeStartGameMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_achievementrunicon;
	private static final Identifier BACKGROUND = Identifier.parse("minigames:textures/screens/roguelike_start_game.png");
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/roguelike_menu.png");

	public RoguelikeStartGameScreen(RoguelikeStartGameMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 421, 198);
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
		if (mouseX > leftPos + 188 && mouseX < leftPos + 227 && mouseY > topPos + 82 && mouseY < topPos + 119) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.roguelike_start_game.tooltip_achievement_run"), mouseX, mouseY);
		}
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -27, this.topPos + -58, 0, 0, 475, 312, 475, 312);
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
		guiGraphics.text(this.font, Component.translatable("gui.minigames.roguelike_start_game.label_achievement"), 184, -15, -1, true);
		guiGraphics.text(this.font, Component.translatable("gui.minigames.roguelike_start_game.label_still_pretty_alpha"), 156, -2, -65536, true);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_achievementrunicon = new ImageButton(this.leftPos + 192, this.topPos + 84, 32, 32, new WidgetSprites(Identifier.parse("minigames:textures/screens/roguelike.png"), Identifier.parse("minigames:textures/screens/roguelike.png")),
				e -> {
					int x = RoguelikeStartGameScreen.this.x;
					int y = RoguelikeStartGameScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new RoguelikeStartGameButtonMessage(0, x, y, z));
						RoguelikeStartGameButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_achievementrunicon);
	}
}