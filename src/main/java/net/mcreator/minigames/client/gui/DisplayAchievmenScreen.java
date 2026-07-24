package net.mcreator.minigames.client.gui;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.util.FormattedCharSequence;
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

import net.mcreator.minigames.world.inventory.DisplayAchievmenMenu;
import net.mcreator.minigames.procedures.*;
import net.mcreator.minigames.network.DisplayAchievmenButtonMessage;
import net.mcreator.minigames.init.MinigamesModScreens;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Arrays;

import com.mojang.blaze3d.platform.InputConstants;

public class DisplayAchievmenScreen extends AbstractContainerScreen<DisplayAchievmenMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Button button_im_ready;
	private ImageButton imagebutton_reroll;
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/achievement_run/achievement_background.png");
	private static final Identifier BACKGROUND = Identifier.parse("minigames:textures/screens/diamondwindow.png");
	private static final Identifier SETTINGS = Identifier.parse("minigames:textures/screens/settings.png");
	private static final Identifier NIGHT = Identifier.parse("minigames:textures/screens/achievement_run/always_night.png");
	private static final Identifier PVP = Identifier.parse("minigames:textures/screens/achievement_run/pvp_in_5min.png");
	private static final Identifier THUNDER = Identifier.parse("minigames:textures/screens/achievement_run/thunder.png");
	private static final Identifier HUNTED = Identifier.parse("minigames:textures/font/hunted.png");

	private static final Identifier SPOTLIGHT = Identifier.parse("minigames:textures/screens/achievement_run/spotlight.png");
	public DisplayAchievmenScreen(DisplayAchievmenMenu container, Inventory inventory, Component text) {
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
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos + -102, this.topPos + -29, 0, 0, 384, 384, 384, 384);
		if (MinigamesModVariables.MapVariables.get(world).AchievementModifier == 1) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, NIGHT, this.leftPos - 45, this.topPos + 123, 0, 0, 50, 50, 50, 50);
		} if (MinigamesModVariables.MapVariables.get(world).AchievementModifier == 2) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, THUNDER, this.leftPos - 45, this.topPos + 123, 0, 0, 50, 50, 50, 50);
		}
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, PVP, this.leftPos - 90, this.topPos + 117, 0, 0, 50, 50, 50, 50);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SETTINGS, this.leftPos + 217, this.topPos + 123, 0, 0, 50, 50, 50, 50);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + -75, this.topPos + 11, 0, 0, 54, 54, 54, 54);
		advancementIcon(guiGraphics, this.leftPos + -75, this.topPos + 11);
	}
	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (mouseX > leftPos + -78 && mouseX < leftPos + -37 && mouseY > topPos + 125 && mouseY < topPos + 164) {
			String hoverText = DisplayBottomLeftDescriptionProcedure.execute(world);
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
			guiGraphics.setTooltipForNextFrame(font, Component.translatable("gui.minigames.display_achievmen.tooltip_reroll_achievement"), mouseX, mouseY);
		}
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		//render all players evenly spread
		playersRender(guiGraphics,(this.width / 8) * 3, (int) ((this.width / 8) * 5.5f), world.players().size());
	}
	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		int x;
		int y;
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().scale(1.3F, 1.3F);
		x = Math.round(-72/1.3f) - 4;
		y = Math.round(81/1.3f) + 7;
		Component text = Component.literal(MinigamesModVariables.MapVariables.get(world).AchievementDescription);
		List<FormattedCharSequence> lines = this.font.split(text, 240);
		for (int i = 0; i < lines.size() && i < 2; i++) {
			guiGraphics.text(this.font, lines.get(i), x + 2, (y + i * 10) - 5 + 5 * i, 0xFF000000, false);
		}
		guiGraphics.pose().scale(2F, 2F);
		Component text2 = Component.literal(MinigamesModVariables.MapVariables.get(world).AchievementTitle);
		List<FormattedCharSequence> lines2 = this.font.split(text2, 100);
		for (int i = 0; i < lines2.size() && i < 2; i++) {
			int lineCount = Math.min(lines2.size(), 2);
			int totalHeight = lineCount * this.font.lineHeight;
			int centerY = 15;
			int startY = centerY - totalHeight / 2;
			guiGraphics.text(this.font, lines2.get(i), -5, startY + i * this.font.lineHeight, 0xFF000000, false);
		}
		guiGraphics.pose().popMatrix();

	}
	private void playersRender(GuiGraphicsExtractor guiGraphics, int fromX, int toX, int amount)
	{
		int line, gap;
		line = toX - fromX;
		gap = line / (amount + 1);
		for (int pN = 1; pN < (amount + 1); pN++)
		{
			Player foundPlayer = null;
			for (Player iteratePlayer : world.players()) {
				if (iteratePlayer.getData(MinigamesModVariables.PLAYER_VARIABLES).team == pN) {
					foundPlayer = iteratePlayer;
					break;
				}
			}
			if (foundPlayer != null) {
				renderSpecificPlayer(guiGraphics, foundPlayer, fromX + gap * pN, foundPlayer);
			}
		}
	}
	private void renderSpecificPlayer(GuiGraphicsExtractor guiGraphics, LivingEntity entity, int x, Player player)
	{
		int boxWidth = 25;
		int x0 = x - boxWidth / 2;
		int x1 = x + boxWidth / 2;
		int y0 = this.topPos + 140;
		InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, x0, y0 , x1, this.topPos + 140 + 100, 25, -entity.getBbHeight() / (2.0f * entity.getScale()), 0f, 0, entity);

		if (Objects.equals(player.getData(MinigamesModVariables.PLAYER_VARIABLES).AchievementLobbyState, "Ready")) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPOTLIGHT, x0-4, y0+6, 0, 0, 33, 50, 33, 50, 0xFF05fa1b);
		}
		else if (Objects.equals(player.getData(MinigamesModVariables.PLAYER_VARIABLES).AchievementLobbyState, "Rerolling")) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SPOTLIGHT, x0-4, y0+6, 0, 0, 33, 50, 33, 50, 0xFF0e56f1);
		}

		if ((player.getStringUUID()).equals(MinigamesModVariables.MapVariables.get(world).hunterAchievementUUID) && MinigamesModVariables.MapVariables.get(world).achievementHunterMode
				&& MinigamesModVariables.MapVariables.get(world).playingAchievement)
		{
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HUNTED, x0+4, y0-17, 0, 0, 16, 16, 16, 16);
		}
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			return true;
		}
		return super.keyPressed(event);
	}
	private void advancementIcon(GuiGraphicsExtractor guiGraphics, int x, int y)
	{
		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().scale(2.5F, 2.5F);
		x = Math.round(x / 2.5f) + 2;
		y = Math.round(y / 2.5f) + 3;
		guiGraphics.item(MinigamesModVariables.MapVariables.get(world).AchievementIcon, x, y);
		guiGraphics.pose().popMatrix();
	}

	@Override
	public void init() {
		super.init();
		button_im_ready = Button.builder(Component.translatable("gui.minigames.display_achievmen.button_im_ready"), e -> {
			int x = DisplayAchievmenScreen.this.x;
			int y = DisplayAchievmenScreen.this.y;
			if (!Objects.equals(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).AchievementLobbyState, "Ready")) {
				ClientPacketDistributor.sendToServer(new DisplayAchievmenButtonMessage(0, x, y, z));
				DisplayAchievmenButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + -84, this.topPos + 169, 72, 20).build();
		this.addRenderableWidget(button_im_ready);
		imagebutton_reroll = new ImageButton(this.leftPos + 249, this.topPos + -25, 20, 20, new WidgetSprites(Identifier.parse("minigames:textures/screens/reroll.png"), Identifier.parse("minigames:textures/screens/rerollhover.png")), e -> {
			int x = DisplayAchievmenScreen.this.x;
			int y = DisplayAchievmenScreen.this.y;
			if (!Objects.equals(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).AchievementLobbyState, "Rerolling")) {
				ClientPacketDistributor.sendToServer(new DisplayAchievmenButtonMessage(1, x, y, z));
				DisplayAchievmenButtonMessage.handleButtonAction(entity, 1, x, y, z);
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
		this.button_im_ready.visible = !Objects.equals(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).AchievementLobbyState, "Ready");
	}
}