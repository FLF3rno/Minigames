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

import net.mcreator.minigames.world.inventory.MinigameGUICrownHuntMenu;
import net.mcreator.minigames.procedures.NightVisionCheckedProcedure;
import net.mcreator.minigames.procedures.KeepInventoryCheckedProcedure;
import net.mcreator.minigames.network.MinigameGUICrownHuntButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

public class MinigameGUICrownHuntScreen extends AbstractContainerScreen<MinigameGUICrownHuntMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private EditBox grace;
	private EditBox minutes;
	private Checkbox nightvision;
	private Checkbox keepinventory;
	private Checkbox returncrown;
	private Button button_start_normal;
	private ImageButton imagebutton_compass_16;
	private ImageButton imagebutton_bucket;
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/goldwindow.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("minigames:textures/screens/clock.png");
	private static final ResourceLocation IMAGE_2 = ResourceLocation.parse("minigames:textures/screens/night_vision.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("minigames:textures/screens/keepinventory.png");
	private static final ResourceLocation IMAGE_4 = ResourceLocation.parse("minigames:textures/screens/crown_head.png");
	private static final ResourceLocation IMAGE_5 = ResourceLocation.parse("minigames:textures/screens/halfextraslot.png");
	private static final ResourceLocation IMAGE_6 = ResourceLocation.parse("minigames:textures/screens/halfextraslot.png");

	public MinigameGUICrownHuntScreen(MinigameGUICrownHuntMenu container, Inventory inventory, Component text) {
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
			if (name.equals("grace"))
				grace.setValue(stringState);
			else if (name.equals("minutes"))
				minutes.setValue(stringState);
		}
		if (elementType == 1 && elementState instanceof Boolean logicState) {
			if (name.equals("nightvision")) {
				if (nightvision.selected() != logicState)
					nightvision.onPress();
			} else if (name.equals("keepinventory")) {
				if (keepinventory.selected() != logicState)
					keepinventory.onPress();
			} else if (name.equals("returncrown")) {
				if (returncrown.selected() != logicState)
					returncrown.onPress();
			}
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		grace.render(guiGraphics, mouseX, mouseY, partialTicks);
		minutes.render(guiGraphics, mouseX, mouseY, partialTicks);
		boolean customTooltipShown = false;
		if (mouseX > leftPos + -57 && mouseX < leftPos + -5 && mouseY > topPos + 41 && mouseY < topPos + 104) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_crown_hunt.tooltip_night_vision"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 69 && mouseX < leftPos + 115 && mouseY > topPos + 41 && mouseY < topPos + 103) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_crown_hunt.tooltip_keep_inventory"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 155 && mouseX < leftPos + 209 && mouseY > topPos + 36 && mouseY < topPos + 82) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_crown_hunt.tooltip_health_amount"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 14 && mouseX < leftPos + 52 && mouseY > topPos + 39 && mouseY < topPos + 104) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_crown_hunt.tooltip_last_winner_takes_15x_damage"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 154 && mouseX < leftPos + 178 && mouseY > topPos + 81 && mouseY < topPos + 105) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_crown_hunt.tooltip_grace_period_minutes"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (mouseX > leftPos + 186 && mouseX < leftPos + 210 && mouseY > topPos + 82 && mouseY < topPos + 106) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_crown_hunt.tooltip_crown_timer"), mouseX, mouseY);
			customTooltipShown = true;
		}
		if (!customTooltipShown)
			this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 166, this.topPos + 43, 0, 0, 32, 32, 32, 32);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + -58, this.topPos + 33, 0, 0, 54, 54, 54, 54);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, this.leftPos + 70, this.topPos + 34, 0, 0, 48, 48, 48, 48);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_4, this.leftPos + 16, this.topPos + 39, 0, 0, 33, 37, 33, 37);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_5, this.leftPos + -96, this.topPos + 168, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_6, this.leftPos + 231, this.topPos + 168, 0, 0, 41, 51, 41, 51);
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (grace.isFocused())
			return grace.keyPressed(key, b, c);
		if (minutes.isFocused())
			return minutes.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String graceValue = grace.getValue();
		String minutesValue = minutes.getValue();
		super.resize(minecraft, width, height);
		grace.setValue(graceValue);
		minutes.setValue(minutesValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.minigames.minigame_gui_crown_hunt.label_achievement_run"), -90, -20, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		grace = new EditBox(this.font, this.leftPos + 155, this.topPos + 83, 23, 18, Component.translatable("gui.minigames.minigame_gui_crown_hunt.grace"));
		grace.setMaxLength(8192);
		grace.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "grace", content, false);
		});
		grace.setHint(Component.translatable("gui.minigames.minigame_gui_crown_hunt.grace"));
		this.addWidget(this.grace);
		minutes = new EditBox(this.font, this.leftPos + 186, this.topPos + 83, 23, 18, Component.translatable("gui.minigames.minigame_gui_crown_hunt.minutes"));
		minutes.setMaxLength(8192);
		minutes.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "minutes", content, false);
		});
		minutes.setHint(Component.translatable("gui.minigames.minigame_gui_crown_hunt.minutes"));
		this.addWidget(this.minutes);
		button_start_normal = Button.builder(Component.translatable("gui.minigames.minigame_gui_crown_hunt.button_start_normal"), e -> {
			int x = MinigameGUICrownHuntScreen.this.x;
			int y = MinigameGUICrownHuntScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MinigameGUICrownHuntButtonMessage(0, x, y, z));
				MinigameGUICrownHuntButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 38, this.topPos + 163, 98, 20).build();
		this.addRenderableWidget(button_start_normal);
		imagebutton_compass_16 = new ImageButton(this.leftPos + -94, this.topPos + 182, 36, 36,
				new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/compass_16.png"), ResourceLocation.parse("minigames:textures/screens/selectedgamecompass.png")), e -> {
					int x = MinigameGUICrownHuntScreen.this.x;
					int y = MinigameGUICrownHuntScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new MinigameGUICrownHuntButtonMessage(1, x, y, z));
						MinigameGUICrownHuntButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_compass_16);
		imagebutton_bucket = new ImageButton(this.leftPos + 235, this.topPos + 182, 32, 32, new WidgetSprites(ResourceLocation.parse("minigames:textures/screens/bucket.png"), ResourceLocation.parse("minigames:textures/screens/selectcolor.png")),
				e -> {
					int x = MinigameGUICrownHuntScreen.this.x;
					int y = MinigameGUICrownHuntScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new MinigameGUICrownHuntButtonMessage(2, x, y, z));
						MinigameGUICrownHuntButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_bucket);
		boolean nightvisionSelected = NightVisionCheckedProcedure.execute(world);
		nightvision = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_crown_hunt.nightvision"), this.font).pos(this.leftPos + -41, this.topPos + 83).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "nightvision", value, false);
		}).selected(nightvisionSelected).build();
		if (nightvisionSelected)
			menu.sendMenuStateUpdate(entity, 1, "nightvision", true, false);
		this.addRenderableWidget(nightvision);
		boolean keepinventorySelected = KeepInventoryCheckedProcedure.execute();
		keepinventory = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_crown_hunt.keepinventory"), this.font).pos(this.leftPos + 82, this.topPos + 83).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "keepinventory", value, false);
		}).selected(keepinventorySelected).build();
		if (keepinventorySelected)
			menu.sendMenuStateUpdate(entity, 1, "keepinventory", true, false);
		this.addRenderableWidget(keepinventory);
		returncrown = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_crown_hunt.returncrown"), this.font).pos(this.leftPos + 23, this.topPos + 83).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "returncrown", value, false);
		}).build();
		this.addRenderableWidget(returncrown);
	}
}