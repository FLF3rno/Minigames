package net.mcreator.minigames.client.gui;

import io.netty.buffer.Unpooled;
import net.mcreator.minigames.init.MinigamesModSounds;
import net.mcreator.minigames.network.OpenDisplayAchievementMessage;
import net.mcreator.minigames.procedures.*;
import net.mcreator.minigames.world.inventory.DisplayAchievmenMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.mcreator.minigames.world.inventory.SelectCategoryAchievementMenu;
import net.mcreator.minigames.init.MinigamesModScreens;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.Random;

public class SelectCategoryAchievementScreen extends AbstractContainerScreen<SelectCategoryAchievementMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/emptywindow.png");
	private float animationState;
	private int animationStateBreak;
	private int category = 1;
	private int breakStage = 0;
	private boolean animation2started = false;

	public SelectCategoryAchievementScreen(SelectCategoryAchievementMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 366, 191);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		renderCategory(guiGraphics,  GetCategoryTextureProcedure.execute(category));
		if (animationStateBreak > 0) { renderBreak(guiGraphics); }
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -6, this.topPos + -13, 0, 0, 384, 384, 384, 384);
	}

	private void renderCategory(GuiGraphicsExtractor guiGraphics, String category) {

		Identifier BLOCK = Identifier.parse(category);
		for (int x = leftPos; x < leftPos + imageWidth; x += 16) {
			for (int y = topPos; y < topPos + imageHeight; y += 16) {
				guiGraphics.blit(
						RenderPipelines.GUI_TEXTURED,
						BLOCK,
						x,
						y,
						0,
						0,
						16,
						16,
						16,
						16
				);
			}
		}
	}

	private void renderBreak(GuiGraphicsExtractor guiGraphics)
	{
		Identifier BLOCK = Identifier.parse("minecraft:textures/block/destroy_stage_" + breakStage + ".png");
		for (int x = leftPos; x < leftPos + imageWidth; x += 16) {
			for (int y = topPos; y < topPos + imageHeight; y += 16) {
				guiGraphics.blit(
						RenderPipelines.GUI_TEXTURED,
						BLOCK,
						x,
						y,
						0,
						0,
						16,
						16,
						16,
						16
				);
			}
		}
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
    	if (event.key() == InputConstants.KEY_ESCAPE) {
        	return true;
    	}

    	return super.keyPressed(event);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		float size = 1.75F;
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().scale(size, size);
		int x = Math.round(6 / size);
		int y = Math.round(-3 / size);
		guiGraphics.text(this.font, GetCategoryProcedure.execute(category), x, y, -16777216, false);
		guiGraphics.pose().popMatrix();
	}

	@Override
	public void init() {
		super.init();
		animationState = 300;
		animationStateBreak = 0;
		breakStage = 0;
		animation2started = false;
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		animationState *= 0.96f;
		if (animation2started) {
			animation2Tick();
		} else {
			if (animationState < 6) {
				Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING, 2.0F));
				category = (int) MinigamesModVariables.MapVariables.get(world).AchievementCategory;
				animation2started = true;
			} else if (Math.round(animationState) % 5 == 0) {
				Minecraft.getInstance().getSoundManager().play(
						SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING, 2.0F)
				);
				if (category == 5) {
					category = 1;
				} else {
					category++;
				}
				animationState--;
			}
		}
	}

	private void animation2Tick()
	{
		animationStateBreak ++;
		if (animationStateBreak % 3 == 0) {
			breakStage ++;
			if (category == 1 || category == 3 || category == 4)
			{Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.STONE_HIT, 1.0F));}
			else if (category == 2)
			{Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NETHERRACK_HIT, 1.0F));}
			else if (category == 5)
			{Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.GRAVEL_HIT, 1.0F));}
		}
		if  (breakStage == 9) {
			ClientPacketDistributor.sendToServer(new OpenDisplayAchievementMessage(""));
			if (category == 1 || category == 3 || category == 4)
			{Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.STONE_BREAK, 1.0F));}
			else if (category == 2)
			{Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NETHERRACK_BREAK, 1.0F));}
			else if (category == 5)
			{Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.GRAVEL_BREAK, 1.0F));}
		}
	}
}