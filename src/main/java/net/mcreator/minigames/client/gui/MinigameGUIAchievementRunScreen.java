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

import net.mcreator.minigames.world.inventory.MinigameGUIAchievementRunMenu;
import net.mcreator.minigames.procedures.KeepInventoryCheckedProcedure;
import net.mcreator.minigames.network.MinigameGUIAchievementRunButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class MinigameGUIAchievementRunScreen extends AbstractContainerScreen<MinigameGUIAchievementRunMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private EditBox hp;
	private EditBox pvp;
	private Checkbox keepinventory;
	private Button button_start_normal;
	private ImageButton imagebutton_compass_16;
	private ImageButton imagebutton_bucket;
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/diamondwindow.png");
	private static final Identifier IMAGE_1 = Identifier.parse("minigames:textures/screens/health_boost.png");
	private static final Identifier IMAGE_2 = Identifier.parse("minigames:textures/screens/pvp.png");
	private static final Identifier IMAGE_3 = Identifier.parse("minigames:textures/screens/keepinventory.png");
	private static final Identifier IMAGE_4 = Identifier.parse("minigames:textures/screens/halfextraslot.png");
	private static final Identifier IMAGE_5 = Identifier.parse("minigames:textures/screens/halfextraslot.png");

	public MinigameGUIAchievementRunScreen(MinigameGUIAchievementRunMenu container, Inventory inventory, Component text) {
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
			else if (name.equals("pvp"))
				pvp.setValue(stringState);
		}
		if (elementType == 1 && elementState instanceof Boolean logicState) {
			if (name.equals("keepinventory")) {
				if (keepinventory.selected() != logicState)
					keepinventory.onPress(null);
			}
		}
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (mouseX > leftPos + -57 && mouseX < leftPos + -5 && mouseY > topPos + 40 && mouseY < topPos + 103) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_run.tooltip_night_vision"), mouseX, mouseY);
		}
		if (mouseX > leftPos + 67 && mouseX < leftPos + 113 && mouseY > topPos + 41 && mouseY < topPos + 103) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_run.tooltip_keep_inventory"), mouseX, mouseY);
		}
		if (mouseX > leftPos + 181 && mouseX < leftPos + 235 && mouseY > topPos + 36 && mouseY < topPos + 105) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.minigame_gui_achievement_run.tooltip_health_amount"), mouseX, mouseY);
		}
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		hp.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		pvp.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 184, this.topPos + 35, 0, 0, 46, 46, 46, 46);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_2, this.leftPos + -55, this.topPos + 34, 0, 0, 48, 48, 48, 48);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, this.leftPos + 68, this.topPos + 36, 0, 0, 48, 48, 48, 48);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_4, this.leftPos + -97, this.topPos + 168, 0, 0, 41, 51, 41, 51);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_5, this.leftPos + 231, this.topPos + 167, 0, 0, 41, 51, 41, 51);
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
		if (pvp.isFocused())
			return pvp.keyPressed(event);
		return super.keyPressed(event);
	}

	@Override
	public void resize(int width, int height) {
		String hpValue = hp.getValue();
		String pvpValue = pvp.getValue();
		super.resize(width, height);
		hp.setValue(hpValue);
		pvp.setValue(pvpValue);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.translatable("gui.minigames.minigame_gui_achievement_run.label_achievement_run"), -90, -20, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		hp = new EditBox(this.font, this.leftPos + 181, this.topPos + 82, 49, 20, Component.translatable("gui.minigames.minigame_gui_achievement_run.hp"));
		hp.setMaxLength(8192);
		hp.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "hp", content, false);
		});
		hp.setHint(Component.translatable("gui.minigames.minigame_gui_achievement_run.hp"));
		this.addWidget(this.hp);
		pvp = new EditBox(this.font, this.leftPos + -55, this.topPos + 82, 44, 20, Component.translatable("gui.minigames.minigame_gui_achievement_run.pvp"));
		pvp.setMaxLength(8192);
		pvp.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "pvp", content, false);
		});
		pvp.setHint(Component.translatable("gui.minigames.minigame_gui_achievement_run.pvp"));
		this.addWidget(this.pvp);
		button_start_normal = Button.builder(Component.translatable("gui.minigames.minigame_gui_achievement_run.button_start_normal"), e -> {
			int x = MinigameGUIAchievementRunScreen.this.x;
			int y = MinigameGUIAchievementRunScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MinigameGUIAchievementRunButtonMessage(0, x, y, z));
				MinigameGUIAchievementRunButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 38, this.topPos + 163, 98, 20).build();
		this.addRenderableWidget(button_start_normal);
		imagebutton_compass_16 = new ImageButton(this.leftPos + -95, this.topPos + 182, 36, 36, new WidgetSprites(Identifier.parse("minigames:textures/screens/compass_16.png"), Identifier.parse("minigames:textures/screens/selectedgamecompass.png")),
				e -> {
					int x = MinigameGUIAchievementRunScreen.this.x;
					int y = MinigameGUIAchievementRunScreen.this.y;
					if (true) {
						ClientPacketDistributor.sendToServer(new MinigameGUIAchievementRunButtonMessage(1, x, y, z));
						MinigameGUIAchievementRunButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_compass_16);
		imagebutton_bucket = new ImageButton(this.leftPos + 235, this.topPos + 181, 32, 32, new WidgetSprites(Identifier.parse("minigames:textures/screens/bucket.png"), Identifier.parse("minigames:textures/screens/selectcolor.png")), e -> {
			int x = MinigameGUIAchievementRunScreen.this.x;
			int y = MinigameGUIAchievementRunScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new MinigameGUIAchievementRunButtonMessage(2, x, y, z));
				MinigameGUIAchievementRunButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_bucket);
		boolean keepinventorySelected = KeepInventoryCheckedProcedure.execute(world);
		keepinventory = Checkbox.builder(Component.translatable("gui.minigames.minigame_gui_achievement_run.keepinventory"), this.font).pos(this.leftPos + 82, this.topPos + 83).onValueChange((checkbox, value) -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 1, "keepinventory", value, false);
		}).selected(keepinventorySelected).build();
		if (keepinventorySelected)
			menu.sendMenuStateUpdate(entity, 1, "keepinventory", true, false);
		this.addRenderableWidget(keepinventory);
	}
}