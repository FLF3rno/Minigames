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
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.minigames.world.inventory.StartGameMenu;
import net.mcreator.minigames.network.StartGameButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

public class StartGameScreen extends AbstractContainerScreen<StartGameMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_achievementrunicon;
	private ImageButton imagebutton_achievementhunticon;
	private ImageButton imagebutton_settings;
	private ImageButton imagebutton_crownhunt;
	private ImageButton imagebutton_snowball;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("minigames:textures/screens/start_game.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/diamond_section.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("minigames:textures/screens/gold_section.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("minigames:textures/screens/blank_section.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("minigames:textures/screens/spleef_section.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("minigames:textures/screens/blank_section.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("minigames:textures/screens/selectedextraslot.png");
	private static final ResourceLocation IMAGE_6 = ResourceLocation.parse("minigames:textures/screens/halfextraslot.png");
	private static final ResourceLocation IMAGE_7 = ResourceLocation.parse("minigames:textures/screens/compass_16.png");

	public StartGameScreen(StartGameMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 421;
		this.imageHeight = 198;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		boolean customTooltipShown = false;
		if (mouseX > leftPos + 179 && mouseX < leftPos + 240 && mouseY > topPos + 56 && mouseY < topPos + 79) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.start_game.tooltip_achievement_run"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 179 && mouseX < leftPos + 240 && mouseY > topPos + 88 && mouseY < topPos + 129) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.start_game.tooltip_achievement_hunt"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 107 && mouseX < leftPos + 148 && mouseY > topPos + 49 && mouseY < topPos + 89) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.start_game.tooltip_steal_the_crown_from_other_playe"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 385 && mouseX < leftPos + 409 && mouseY > topPos + 199 && mouseY < topPos + 223) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.start_game.tooltip_change_player_name"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 278 && mouseX < leftPos + 309 && mouseY > topPos + 53 && mouseY < topPos + 83) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.start_game.tooltip_mine_blocks_under_opponents_to_e"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (!customTooltipShown)
			this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + 169, this.topPos + 1, 0, 0, 83, 197, 83, 197);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 86, this.topPos + 1, 0, 0, 83, 197, 83, 197);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + 3, this.topPos + 0, 0, 0, 83, 197, 83, 197);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, this.leftPos + 252, this.topPos + 1, 0, 0, 83, 197, 83, 197);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_4, this.leftPos + 335, this.topPos + 1, 0, 0, 83, 197, 83, 197);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_5, this.leftPos + 4, this.topPos + 186, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_6, this.leftPos + 377, this.topPos + 185, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_7, this.leftPos + 6, this.topPos + 200, 0, 0, 36, 36, 36, 36);
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
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.start_game.label_achievement"), 180, 16, -13027015, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.start_game.label_run"), 201, 25, -13027015, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.start_game.label_crown"), 113, 16, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.start_game.label_hunt"), 116, 25, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.start_game.label_spleef"), 278, 17, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_achievementrunicon = new ImageButton(this.leftPos + 177, this.topPos + 53, 66, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/achievementrunicon.png"), ResourceLocation.parse("minigames:textures/screens/selectedachievementrunicon.png")), e -> {
					int x = StartGameScreen.this.x;
					int y = StartGameScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new StartGameButtonMessage(0, x, y, z));
						StartGameButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_achievementrunicon);
		imagebutton_achievementhunticon = new ImageButton(this.leftPos + 177, this.topPos + 86, 68, 44,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/achievementhunticon.png"), ResourceLocation.parse("minigames:textures/screens/selectedachievementhunticon.png")), e -> {
					int x = StartGameScreen.this.x;
					int y = StartGameScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new StartGameButtonMessage(1, x, y, z));
						StartGameButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_achievementhunticon);
		imagebutton_settings = new ImageButton(this.leftPos + 381, this.topPos + 199, 32, 32, new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/bucket.png"), ResourceLocation.parse("minigames:textures/screens/selectcolor.png")),
				e -> {
					int x = StartGameScreen.this.x;
					int y = StartGameScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new StartGameButtonMessage(2, x, y, z));
						StartGameButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_settings);
		imagebutton_crownhunt = new ImageButton(this.leftPos + 107, this.topPos + 48, 38, 38,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/crownhunt.png"), ResourceLocation.parse("minigames:textures/screens/crownhuntselected.png")), e -> {
					int x = StartGameScreen.this.x;
					int y = StartGameScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new StartGameButtonMessage(3, x, y, z));
						StartGameButtonMessage.handleButtonAction(entity, 3, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_crownhunt);
		imagebutton_snowball = new ImageButton(this.leftPos + 278, this.topPos + 52, 32, 32,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/snowball2x.png"), ResourceLocation.parse("minigames:textures/screens/snowballselected.png")), e -> {
					int x = StartGameScreen.this.x;
					int y = StartGameScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new StartGameButtonMessage(4, x, y, z));
						StartGameButtonMessage.handleButtonAction(entity, 4, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_snowball);
	}
}