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

import net.mcreator.minigames.world.inventory.CustomizeGUIMenu;
import net.mcreator.minigames.procedures.*;
import net.mcreator.minigames.network.CustomizeGUIButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

public class CustomizeGUIScreen extends AbstractContainerScreen<CustomizeGUIMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private ImageButton imagebutton_colorbutton;
	private ImageButton imagebutton_colorbutton1;
	private ImageButton imagebutton_colorbutton2;
	private ImageButton imagebutton_colorbutton3;
	private ImageButton imagebutton_colorbutton4;
	private ImageButton imagebutton_colorbutton5;
	private ImageButton imagebutton_colorbutton6;
	private ImageButton imagebutton_colorbutton7;
	private ImageButton imagebutton_colorbutton8;
	private ImageButton imagebutton_colorbutton9;
	private ImageButton imagebutton_colorbutton10;
	private ImageButton imagebutton_colorbutton11;
	private ImageButton imagebutton_colorbutton12;
	private ImageButton imagebutton_compass_16;
	private static final ResourceLocation BACKGROUND = ResourceLocation.parse("minigames:textures/screens/customize_gui.png");
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/halfextraslot.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("minigames:textures/screens/selectedextraslot.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("minigames:textures/screens/bucket.png");
	private static final ResourceLocation SPRITE_0 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_1 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_2 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_3 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_4 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_5 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_6 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_7 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_8 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_9 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_10 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_11 = ResourceLocation.parse("minigames:textures/screens/colors.png");
	private static final ResourceLocation SPRITE_12 = ResourceLocation.parse("minigames:textures/screens/colors.png");

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
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
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
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, this.leftPos + 15, this.topPos + 46, 0, 260, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, this.leftPos + 60, this.topPos + 29, 0, 0, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_2, this.leftPos + 100, this.topPos + 29, 0, 52, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_3, this.leftPos + 139, this.topPos + 29, 0, 26, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_4, this.leftPos + 181, this.topPos + 29, 0, 78, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_5, this.leftPos + 223, this.topPos + 29, 0, 182, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_6, this.leftPos + 264, this.topPos + 29, 0, 104, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_7, this.leftPos + 100, this.topPos + 63, 0, 130, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_8, this.leftPos + 60, this.topPos + 63, 0, 208, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_9, this.leftPos + 139, this.topPos + 63, 0, 234, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_10, this.leftPos + 223, this.topPos + 63, 0, 286, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_11, this.leftPos + 264, this.topPos + 63, 0, 156, 26, 26, 26, 338);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_12, this.leftPos + 181, this.topPos + 63, 0, 312, 26, 26, 26, 338);
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
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.customize_gui.label_customize_player_name"), 5, 5, -12829636, false);
		if (IsNameWhiteProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -1, false);
		if (IsNameAquaProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -11207428, false);
		if (IsNameBlueProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -11250436, false);
		if (IsNameDarkAquaProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -16734040, false);
		if (IsNameDarkBlueProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -16777048, false);
		if (IsNameDarkGreenProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -16734208, false);
		if (IsNameDarkPurpleProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -5767000, false);
		if (IsNameDarkRedProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -5767168, false);
		if (IsNameGoldProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -219136, false);
		if (IsNameGreenProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -11207596, false);
		if (IsNameLightPurpleProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -240388, false);
		if (IsNameRedProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -240556, false);
		if (IsNameYellowProcedure.execute(entity))
			guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 6, 98, -197548, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_colorbutton = new ImageButton(this.leftPos + 15, this.topPos + 46, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotWhiteProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(0, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotWhiteProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton);
		imagebutton_colorbutton1 = new ImageButton(this.leftPos + 60, this.topPos + 29, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotAquaProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(1, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotAquaProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton1);
		imagebutton_colorbutton2 = new ImageButton(this.leftPos + 100, this.topPos + 29, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotDarkAquaProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(2, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotDarkAquaProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton2);
		imagebutton_colorbutton3 = new ImageButton(this.leftPos + 139, this.topPos + 29, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotBlueProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(3, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 3, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotBlueProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton3);
		imagebutton_colorbutton4 = new ImageButton(this.leftPos + 181, this.topPos + 29, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotDarkBlueProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(4, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 4, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotDarkBlueProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton4);
		imagebutton_colorbutton5 = new ImageButton(this.leftPos + 223, this.topPos + 29, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotGreenProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(5, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 5, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotGreenProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton5);
		imagebutton_colorbutton6 = new ImageButton(this.leftPos + 264, this.topPos + 29, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotDarkGreenProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(6, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 6, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotDarkGreenProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton6);
		imagebutton_colorbutton7 = new ImageButton(this.leftPos + 60, this.topPos + 63, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotLightPurpleProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(7, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 7, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotLightPurpleProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton7);
		imagebutton_colorbutton8 = new ImageButton(this.leftPos + 100, this.topPos + 63, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotDarkPurpleProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(8, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 8, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotDarkPurpleProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton8);
		imagebutton_colorbutton9 = new ImageButton(this.leftPos + 139, this.topPos + 63, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotRedProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(9, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 9, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotRedProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton9);
		imagebutton_colorbutton10 = new ImageButton(this.leftPos + 181, this.topPos + 63, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotDarkRedProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(10, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 10, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotDarkRedProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton10);
		imagebutton_colorbutton11 = new ImageButton(this.leftPos + 223, this.topPos + 63, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotYellowProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(11, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 11, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotYellowProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton11);
		imagebutton_colorbutton12 = new ImageButton(this.leftPos + 264, this.topPos + 63, 26, 26,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/colorbutton.png"), ResourceLocation.parse("minigames:textures/screens/colorhover.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (IsNameNotGoldProcedure.execute(entity)) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(12, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 12, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				int x = CustomizeGUIScreen.this.x;
				int y = CustomizeGUIScreen.this.y;
				if (IsNameNotGoldProcedure.execute(entity))
					guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_colorbutton12);
		imagebutton_compass_16 = new ImageButton(this.leftPos + 6, this.topPos + 113, 36, 36,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/compass_16.png"), ResourceLocation.parse("minigames:textures/screens/selectedgamecompass.png")), e -> {
					int x = CustomizeGUIScreen.this.x;
					int y = CustomizeGUIScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new CustomizeGUIButtonMessage(13, x, y, z));
						CustomizeGUIButtonMessage.handleButtonAction(entity, 13, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_compass_16);
	}
}