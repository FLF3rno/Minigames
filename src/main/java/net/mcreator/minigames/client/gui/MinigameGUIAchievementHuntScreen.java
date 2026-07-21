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

import net.mcreator.minigames.world.inventory.MinigameGUIAchievementHuntMenu;
import net.mcreator.minigames.procedures.NightVisionCheckedProcedure;
import net.mcreator.minigames.procedures.KeepInventoryCheckedProcedure;
import net.mcreator.minigames.network.MinigameGUIAchievementHuntButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class MinigameGUIAchievementHuntScreen extends AbstractContainerScreen<MinigameGUIAchievementHuntMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private EditBox hp;
	private EditBox hunter;
	private Checkbox nightvision;
	private Checkbox keepinventory;
	private Checkbox minimap;
	private Checkbox headstart;
	private Checkbox randomhunter;
	private Button button_start_normal;
	private ImageButton imagebutton_compass_16;
	private ImageButton imagebutton_bucket;
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/diamondwindow.png");
	private static final Identifier IMAGE_1 = Identifier.parse("minigames:textures/screens/health_boost.png");
	private static final Identifier IMAGE_2 = Identifier.parse("minigames:textures/screens/night_vision.png");
	private static final Identifier IMAGE_3 = Identifier.parse("minigames:textures/screens/keepinventory.png");
	private static final Identifier IMAGE_4 = Identifier.parse("minigames:textures/screens/crown.png");
	private static final Identifier IMAGE_5 = Identifier.parse("minigames:textures/screens/clock.png");
	private static final Identifier IMAGE_6 = Identifier.parse("minigames:textures/screens/diamond_sword.png");
	private static final Identifier IMAGE_7 = Identifier.parse("minigames:textures/screens/halfextraslot.png");
	private static final Identifier IMAGE_8 = Identifier.parse("minigames:textures/screens/halfextraslot.png");

	public MinigameGUIAchievementHuntScreen(MinigameGUIAchievementHuntMenu container, Inventory inventory, Component text) {
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
			if (name.equals("hp"))
				hp.setValue(stringState);
			else if (name.equals("hunter"))
				hunter.setValue(stringState);
		}
		if (elementType == 1 && elementState instanceof Boolean logicState) {
			if (name.equals("nightvision")) {
				if (nightvision.selected() != logicState)
					nightvision.onPress(null);
			} else if (name.equals("keepinventory")) {
				if (keepinventory.selected() != logicState)
					keepinventory.onPress(null);
			} else if (name.equals("minimap")) {
				if (minimap.selected() != logicState)
					minimap.onPress(null);
			} else if (name.equals("headstart")) {
				if (headstart.selected() != logicState)
					headstart.onPress(null);
			} else if (name.equals("randomhunter")) {
				if (randomhunter.selected() != logicState)
					randomhunter.onPress(null);
			}
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (mouseX > leftPos + -57 && mouseX < leftPos + -5 && mouseY > topPos + 29 && mouseY < topPos + 92) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.tooltip_night_vision"), mouseX, mouseY);
		}
		if (mouseX > leftPos + 70 && mouseX < leftPos + 116 && mouseY > topPos + 28 && mouseY < topPos + 90) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.tooltip_keep_inventory"), mouseX, mouseY);
		}
		if (mouseX > leftPos + 179 && mouseX < leftPos + 233 && mouseY > topPos + 26 && mouseY < topPos + 95) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.tooltip_health_amount"), mouseX, mouseY);
		}
		if (mouseX > leftPos + 16 && mouseX < leftPos + 56 && mouseY > topPos + 33 && mouseY < topPos + 93) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.tooltip_last_winner_takes_15x_damage"), mouseX, mouseY);
		}
		if (mouseX > leftPos + 126 && mouseX < leftPos + 169 && mouseY > topPos + 27 && mouseY < topPos + 91) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.tooltip_dont_reset_world_on_game_start"), mouseX, mouseY);
		}
		if (KeepInventoryCheckedProcedure.execute(world))
			if (mouseX > leftPos + -14 && mouseX < leftPos + 10 && mouseY > topPos + 120 && mouseY < topPos + 144) {
				guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.tooltip_randomize_hunter"), mouseX, mouseY);
			}
		if (mouseX > leftPos + 24 && mouseX < leftPos + 150 && mouseY > topPos + 119 && mouseY < topPos + 143) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.tooltip_name_of_the_hunter_disabled_if"), mouseX, mouseY);
		}
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		hp.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		hunter.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 184, this.topPos + 23, 0, 0, 46, 46, 46, 46);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + -58, this.topPos + 15, 0, 0, 54, 54, 54, 54);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, this.leftPos + 68, this.topPos + 22, 0, 0, 48, 48, 48, 48);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_4, this.leftPos + 12, this.topPos + 26, 0, 0, 48, 48, 48, 48);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_5, this.leftPos + 132, this.topPos + 31, 0, 0, 32, 32, 32, 32);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_6, this.leftPos + -80, this.topPos + 107, 0, 0, 48, 48, 48, 48);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_7, this.leftPos + -97, this.topPos + 168, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_8, this.leftPos + 231, this.topPos + 168, 0, 0, 41, 51, 41, 51);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (hp.isFocused())
			return hp.keyPressed(event);
		if (hunter.isFocused())
			return hunter.keyPressed(event);
		return super.keyPressed(event);
	}

	@Override
	public void resize(int width, int height) {
		String hpValue = hp.getValue();
		String hunterValue = hunter.getValue();
		super.resize(width, height);
		hp.setValue(hpValue);
		hunter.setValue(hunterValue);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.label_achievement_run"), -90, -20, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		hp = new EditBox(this.font, this.leftPos + 181, this.topPos + 71, 49, 20, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.hp"));
		hp.setMaxLength(8192);
		hp.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "hp", content, false);
		});
		hp.setHint(Component.translatable("gui.minigames.minigame_gui_achievement_hunt.hp"));
		this.addWidget(this.hp);
		hunter = new EditBox(this.font, this.leftPos + 26, this.topPos + 121, 120, 20, Component.translatable("gui.minigames.minigame_gui_achievement_hunt.hunter"));
		hunter.setMaxLength(8192);
		hunter.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "hunter", content, false);
		});
		this.addWidget(this.hunter);
		button_start_normal = Button.builder(Component.translatable("gui.minigames.minigame_gui_achievement_hunt.button_start_normal"), e -> {
			int x = MinigameGUIAchievementHuntScreen.this.x;
			int y = MinigameGUIAchievementHuntScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MinigameGUIAchievementHuntButtonMessage(0, x, y, z));
				MinigameGUIAchievementHuntButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 38, this.topPos + 163, 98, 20).build();
		this.addRenderableWidget(button_start_normal);
		imagebutton_compass_16 = new ImageButton(this.leftPos + -95, this.topPos + 182, 36, 36, new WidgetSprites(Identifier.parse("minigames:textures/screens/compass_16.png"), Identifier.parse("minigames:textures/screens/selectedgamecompass.png")),
				e -> {
					int x = MinigameGUIAchievementHuntScreen.this.x;
					int y = MinigameGUIAchievementHuntScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new MinigameGUIAchievementHuntButtonMessage(1, x, y, z));
						MinigameGUIAchievementHuntButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_compass_16);
		imagebutton_bucket = new ImageButton(this.leftPos + 235, this.topPos + 182, 32, 32, new WidgetSprites(Identifier.parse("minigames:textures/screens/bucket.png"), Identifier.parse("minigames:textures/screens/selectcolor.png")), e -> {
			int x = MinigameGUIAchievementHuntScreen.this.x;
			int y = MinigameGUIAchievementHuntScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MinigameGUIAchievementHuntButtonMessage(2, x, y, z));
				MinigameGUIAchievementHuntButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_bucket);
		boolean nightvisionSelected = NightVisionCheckedProcedure.execute(world);
		nightvision = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_hunt.nightvision"), this.font).pos(this.leftPos + -41, this.topPos + 72).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "nightvision", value, false);
		}).selected(nightvisionSelected).build();
		if (nightvisionSelected)
			menu.sendMenuStateUpdate(entity, 1, "nightvision", true, false);
		this.addRenderableWidget(nightvision);
		keepinventory = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_hunt.keepinventory"), this.font).pos(this.leftPos + 83, this.topPos + 72).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "keepinventory", value, false);
		}).build();
		this.addRenderableWidget(keepinventory);
		minimap = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_hunt.minimap"), this.font).pos(this.leftPos + 27, this.topPos + 72).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "minimap", value, false);
		}).build();
		this.addRenderableWidget(minimap);
		boolean headstartSelected = KeepInventoryCheckedProcedure.execute(world);
		headstart = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_hunt.headstart"), this.font).pos(this.leftPos + 138, this.topPos + 72).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "headstart", value, false);
		}).selected(headstartSelected).build();
		if (headstartSelected)
			menu.sendMenuStateUpdate(entity, 1, "headstart", true, false);
		this.addRenderableWidget(headstart);
		boolean randomhunterSelected = KeepInventoryCheckedProcedure.execute(world);
		randomhunter = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_hunt.randomhunter"), this.font).pos(this.leftPos + -12, this.topPos + 122).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "randomhunter", value, false);
		}).selected(randomhunterSelected).build();
		if (randomhunterSelected)
			menu.sendMenuStateUpdate(entity, 1, "randomhunter", true, false);
		this.addRenderableWidget(randomhunter);
	}
}