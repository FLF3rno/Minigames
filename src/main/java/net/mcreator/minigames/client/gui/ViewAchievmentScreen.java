package net.mcreator.minigames.client.gui;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.minigames.world.inventory.ViewAchievmentMenu;
import net.mcreator.minigames.procedures.*;
import net.mcreator.minigames.network.ViewAchievmentButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

import java.util.stream.Collectors;
import java.util.Arrays;

import com.mojang.blaze3d.platform.InputConstants;

public class ViewAchievmentScreen extends AbstractContainerScreen<ViewAchievmentMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Button button_im_ready;
	private ImageButton imagebutton_reroll;
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/lowerhalf.png");
	private static final Identifier SPRITE_0 = Identifier.parse("minigames:textures/screens/diamondwindow.png");
	private static final Identifier SPRITE_1 = Identifier.parse("minigames:textures/screens/achievements.png");
	private static final Identifier SPRITE_2 = Identifier.parse("minigames:textures/screens/modifiers.png");
	private static final Identifier SPRITE_3 = Identifier.parse("minigames:textures/screens/modifiers.png");
	private static final Identifier SPRITE_4 = Identifier.parse("minigames:textures/screens/modifiers.png");
	private static final Identifier SPRITE_5 = Identifier.parse("minigames:textures/screens/lowerhalf.png");
	private static final Identifier SPRITE_6 = Identifier.parse("minigames:textures/screens/blackhead.png");
	private static final Identifier SPRITE_7 = Identifier.parse("minigames:textures/screens/blackhead.png");
	private static final Identifier SPRITE_8 = Identifier.parse("minigames:textures/screens/blackhead.png");
	private static final Identifier SPRITE_9 = Identifier.parse("minigames:textures/screens/blackhead.png");
	private static final Identifier SPRITE_10 = Identifier.parse("minigames:textures/screens/blackhead.png");
	private static final Identifier SPRITE_11 = Identifier.parse("minigames:textures/screens/blackhead.png");
	private static final Identifier SPRITE_12 = Identifier.parse("minigames:textures/screens/clock.png");

	public ViewAchievmentScreen(ViewAchievmentMenu container, Inventory inventory, Component text) {
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
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (mouseX > leftPos + -78 && mouseX < leftPos + -37 && mouseY > topPos + 125 && mouseY < topPos + 164) {
			String hoverText = DisplayBottomLeftDescriptionProcedure.execute(world);
			if (hoverText != null) {
				guiGraphics.setComponentTooltipForNextFrame(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
		}
		if (DisplayTooltipModifierProcedure.execute(world))
			if (mouseX > leftPos + -34 && mouseX < leftPos + 8 && mouseY > topPos + 125 && mouseY < topPos + 164) {
				String hoverText = TooltipModifierProcedure.execute(world);
				if (hoverText != null) {
					guiGraphics.setComponentTooltipForNextFrame(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
				}
			}
		if (mouseX > leftPos + 226 && mouseX < leftPos + 258 && mouseY > topPos + 131 && mouseY < topPos + 163) {
			String hoverText = SettingsDisplayProcedure.execute(world);
			if (hoverText != null) {
				guiGraphics.setComponentTooltipForNextFrame(font, Arrays.stream(hoverText.split("\n")).map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
			}
		}
		if (mouseX > leftPos + 246 && mouseX < leftPos + 270 && mouseY > topPos + -27 && mouseY < topPos + -3) {
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.view_achievment.tooltip_reroll_achievement"), mouseX, mouseY);
		}
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		if (DisplayEntityProcedure.execute(world) instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + -984, this.topPos + -811, this.leftPos + 1016, this.topPos + 1189, 25, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
		}
		if (DisplayEntity2Procedure.execute(world) instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + -948, this.topPos + -811, this.leftPos + 1052, this.topPos + 1189, 25, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
		}
		if (DisplayEntity3Procedure.execute(world) instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + -912, this.topPos + -811, this.leftPos + 1088, this.topPos + 1189, 25, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
		}
		if (DisplayEntity4Procedure.execute(world) instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + -876, this.topPos + -811, this.leftPos + 1124, this.topPos + 1189, 25, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
		}
		if (DisplayEntity5Procedure.execute(world) instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + -840, this.topPos + -811, this.leftPos + 1160, this.topPos + 1189, 25, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
		}
		if (DisplayEntity6Procedure.execute(world) instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + -804, this.topPos + -811, this.leftPos + 1196, this.topPos + 1189, 25, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_1, this.leftPos + -102, this.topPos + -29, 0, Mth.clamp((int) DisplayAchievementProcedure.execute(world) * 100, 0, 7700), 384, 100, 384, 7800);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_2, this.leftPos + 218, this.topPos + 123, 0, 50, 50, 50, 50, 250);
		if (DisplayPVPTimeProcedure.execute(world)) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_3, this.leftPos + -84, this.topPos + 117, 0, 100, 50, 50, 50, 250);
		}
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_4, this.leftPos + -41, this.topPos + 121, 0, Mth.clamp((int) DisplayThunderProcedure.execute(world) * 50, 0, 200), 50, 50, 50, 250);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_5, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_6, this.leftPos + 0, this.topPos + 146, 0, Mth.clamp((int) DisplayEntityReadyProcedure.execute(world) * 44, 0, 132), 33, 44, 33, 176);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_7, this.leftPos + 36, this.topPos + 146, 0, Mth.clamp((int) DisplayEntityReady2Procedure.execute(world) * 44, 0, 132), 33, 44, 33, 176);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_8, this.leftPos + 72, this.topPos + 146, 0, Mth.clamp((int) DisplayEntityReady3Procedure.execute(world) * 44, 0, 132), 33, 44, 33, 176);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_9, this.leftPos + 108, this.topPos + 146, 0, Mth.clamp((int) DisplayEntityReady4Procedure.execute(world) * 44, 0, 132), 33, 44, 33, 176);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_10, this.leftPos + 144, this.topPos + 146, 0, Mth.clamp((int) DisplayEntityReady5Procedure.execute(world) * 44, 0, 132), 33, 44, 33, 176);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_11, this.leftPos + 180, this.topPos + 146, 0, Mth.clamp((int) DisplayEntityReady6Procedure.execute(world) * 44, 0, 132), 33, 44, 33, 176);
		if (DisplayHeadStartProcedure.execute(world)) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPRITE_12, this.leftPos + -73, this.topPos + 129, 0, 0, 32, 32, 32, 32);
		}
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
		guiGraphics.text(this.font, AchievementDescriptionProcedure.execute(world), -71, 81, -11842741, false);
		guiGraphics.text(this.font, AchievementDescriptionProcedure.execute(world), -72, 81, -11842741, false);
		guiGraphics.text(this.font, AchievementDescriptionProcedure.execute(world), -72, 80, -16777216, false);
	}

	@Override
	public void init() {
		super.init();
		button_im_ready = Button.builder(Component.translatable("gui.minigames.view_achievment.button_im_ready"), e -> {
			int x = ViewAchievmentScreen.this.x;
			int y = ViewAchievmentScreen.this.y;
			if (ReadyDisplayProcedure.execute(entity)) {
				ClientPacketDistributor.sendToServer(new ViewAchievmentButtonMessage(0, x, y, z));
				ViewAchievmentButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + -84, this.topPos + 169, 72, 20).build();
		this.addRenderableWidget(button_im_ready);
		imagebutton_reroll = new ImageButton(this.leftPos + 249, this.topPos + -25, 20, 20, new WidgetSprites(Identifier.parse("minigames:textures/screens/reroll.png"), Identifier.parse("minigames:textures/screens/rerollhover.png")), e -> {
			int x = ViewAchievmentScreen.this.x;
			int y = ViewAchievmentScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new ViewAchievmentButtonMessage(1, x, y, z));
				ViewAchievmentButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
				guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		this.addRenderableWidget(imagebutton_reroll);
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		this.button_im_ready.visible = ReadyDisplayProcedure.execute(entity);
	}
}