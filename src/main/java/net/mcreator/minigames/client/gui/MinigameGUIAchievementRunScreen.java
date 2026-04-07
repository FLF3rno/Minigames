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
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.world.inventory.MinigameGUIAchievementRunMenu;
import net.mcreator.minigames.procedures.NightVisionCheckedProcedure;
import net.mcreator.minigames.procedures.KeepInventoryCheckedProcedure;
import net.mcreator.minigames.network.MinigameGUIAchievementRunButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

public class MinigameGUIAchievementRunScreen extends AbstractContainerScreen<MinigameGUIAchievementRunMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private EditBox hp;
	private Checkbox nightvision;
	private Checkbox keepinventory;
	private Checkbox minimap;
	private Checkbox spawn;
	private Button button_start_normal;
	private ImageButton imagebutton_compass_16;
	private ImageButton imagebutton_bucket;
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/diamondwindow.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("minigames:textures/screens/health_boost.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("minigames:textures/screens/night_vision.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("minigames:textures/screens/keepinventory.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("minigames:textures/screens/crown.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("minigames:textures/screens/compass_00.png");
	private static final ResourceLocation IMAGE_6 = ResourceLocation.parse("minigames:textures/screens/halfextraslot.png");
	private static final ResourceLocation IMAGE_7 = ResourceLocation.parse("minigames:textures/screens/halfextraslot.png");

	public MinigameGUIAchievementRunScreen(MinigameGUIAchievementRunMenu container, Inventory inventory, Component text) {
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
		if (elementType == 0 && elementState instanceof String stringState) {
			if (name.equals("hp"))
				hp.setValue(stringState);
		}
		if (elementType == 1 && elementState instanceof Boolean logicState) {
			if (name.equals("nightvision")) {
				if (nightvision.selected() != logicState)
					nightvision.onPress();
			} else if (name.equals("keepinventory")) {
				if (keepinventory.selected() != logicState)
					keepinventory.onPress();
			} else if (name.equals("minimap")) {
				if (minimap.selected() != logicState)
					minimap.onPress();
			} else if (name.equals("spawn")) {
				if (spawn.selected() != logicState)
					spawn.onPress();
			}
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		hp.render(guiGraphics, mouseX, mouseY, partialTicks);
		boolean customTooltipShown = false;
		if (mouseX > leftPos + -57 && mouseX < leftPos + -5 && mouseY > topPos + 40 && mouseY < topPos + 103) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_run.tooltip_night_vision"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 67 && mouseX < leftPos + 113 && mouseY > topPos + 41 && mouseY < topPos + 103) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_run.tooltip_keep_inventory"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 181 && mouseX < leftPos + 235 && mouseY > topPos + 36 && mouseY < topPos + 105) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_run.tooltip_health_amount"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 12 && mouseX < leftPos + 52 && mouseY > topPos + 43 && mouseY < topPos + 103) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_run.tooltip_last_winner_takes_15x_damage"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 126 && mouseX < leftPos + 169 && mouseY > topPos + 40 && mouseY < topPos + 104) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_run.tooltip_dont_reset_world_on_game_start"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (!customTooltipShown)
			this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 184, this.topPos + 35, 0, 0, 46, 46, 46, 46);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + -58, this.topPos + 32, 0, 0, 54, 54, 54, 54);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, this.leftPos + 68, this.topPos + 36, 0, 0, 48, 48, 48, 48);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_4, this.leftPos + 8, this.topPos + 36, 0, 0, 48, 48, 48, 48);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_5, this.leftPos + 124, this.topPos + 35, 0, 0, 48, 48, 48, 48);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_6, this.leftPos + -97, this.topPos + 168, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_7, this.leftPos + 231, this.topPos + 167, 0, 0, 41, 51, 41, 51);
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (hp.isFocused())
			return hp.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String hpValue = hp.getValue();
		super.resize(minecraft, width, height);
		hp.setValue(hpValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.minigame_gui_achievement_run.label_achievement_run"), -90, -20, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		hp = new EditBox(this.font, this.leftPos + 182, this.topPos + 83, 47, 18, Component.translatable("gui.minigames.minigame_gui_achievement_run.hp"));
		hp.setMaxLength(8192);
		hp.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "hp", content, false);
		});
		hp.setHint(Component.translatable("gui.minigames.minigame_gui_achievement_run.hp"));
		this.addWidget(this.hp);
		button_start_normal = Button.builder(Component.translatable("gui.minigames.minigame_gui_achievement_run.button_start_normal"), e -> {
			int x = MinigameGUIAchievementRunScreen.this.x;
			int y = MinigameGUIAchievementRunScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MinigameGUIAchievementRunButtonMessage(0, x, y, z));
				MinigameGUIAchievementRunButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 38, this.topPos + 163, 98, 20).build();
		this.addRenderableWidget(button_start_normal);
		imagebutton_compass_16 = new ImageButton(this.leftPos + -95, this.topPos + 182, 36, 36,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/compass_16.png"), ResourceLocation.parse("minigames:textures/screens/selectedgamecompass.png")), e -> {
					int x = MinigameGUIAchievementRunScreen.this.x;
					int y = MinigameGUIAchievementRunScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new MinigameGUIAchievementRunButtonMessage(1, x, y, z));
						MinigameGUIAchievementRunButtonMessage.handleButtonAction(entity, 1, x, y, z);
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
					int x = MinigameGUIAchievementRunScreen.this.x;
					int y = MinigameGUIAchievementRunScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new MinigameGUIAchievementRunButtonMessage(2, x, y, z));
						MinigameGUIAchievementRunButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_bucket);
		boolean nightvisionSelected = NightVisionCheckedProcedure.execute(world);
		nightvision = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_run.nightvision"), this.font).pos(this.leftPos + -41, this.topPos + 83).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "nightvision", value, false);
		}).selected(nightvisionSelected).build();
		if (nightvisionSelected)
			menu.sendMenuStateUpdate(entity, 1, "nightvision", true, false);
		this.addRenderableWidget(nightvision);
		boolean keepinventorySelected = KeepInventoryCheckedProcedure.execute();
		keepinventory = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_run.keepinventory"), this.font).pos(this.leftPos + 82, this.topPos + 83).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "keepinventory", value, false);
		}).selected(keepinventorySelected).build();
		if (keepinventorySelected)
			menu.sendMenuStateUpdate(entity, 1, "keepinventory", true, false);
		this.addRenderableWidget(keepinventory);
		minimap = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_run.minimap"), this.font).pos(this.leftPos + 23, this.topPos + 83).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "minimap", value, false);
		}).build();
		this.addRenderableWidget(minimap);
		spawn = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_run.spawn"), this.font).pos(this.leftPos + 138, this.topPos + 83).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "spawn", value, false);
		}).build();
		this.addRenderableWidget(spawn);
	}
}