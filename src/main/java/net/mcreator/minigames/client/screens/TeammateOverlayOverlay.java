package net.mcreator.minigames.client.screens;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber(Dist.CLIENT)
public class TeammateOverlayOverlay {
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/teammateoverlay.png");
	private static final ResourceLocation HEART_CONTAINER = ResourceLocation.withDefaultNamespace("hud/heart/container");
	private static final ResourceLocation HEART_FULL = ResourceLocation.withDefaultNamespace("hud/heart/full");
	private static final ResourceLocation HEART_HALF = ResourceLocation.withDefaultNamespace("hud/heart/half");
	private static final ResourceLocation HEART_POISONED_FULL = ResourceLocation.withDefaultNamespace("hud/heart/poisoned_full");
	private static final ResourceLocation HEART_POISONED_HALF = ResourceLocation.withDefaultNamespace("hud/heart/poisoned_half");
	private static final ResourceLocation HEART_WITHERED_FULL = ResourceLocation.withDefaultNamespace("hud/heart/withered_full");
	private static final ResourceLocation HEART_WITHERED_HALF = ResourceLocation.withDefaultNamespace("hud/heart/withered_half");
	private static final ResourceLocation HEART_ABSORBING_FULL = ResourceLocation.withDefaultNamespace("hud/heart/absorbing_full");
	private static final ResourceLocation HEART_ABSORBING_HALF = ResourceLocation.withDefaultNamespace("hud/heart/absorbing_half");
	private static final int ROW_X = 0;
	private static final int ROW_Y = 3;
	private static final int ROW_WIDTH = 141;
	private static final int ROW_HEIGHT = 48;
	private static final int ROW_SPACING = 2;
	private static final int NAME_Y = 15;
	private static final int CLASS_Y = 25;
	private static final int HEAD_SIZE = 8;
	private static final int HEAD_GAP = 4;
	private static final int HEALTH_BAR_X = 12;
	private static final int HEALTH_BAR_Y = 36;
	private static final int HEART_SIZE = 9;
	private static final int BASE_HEART_ICONS = 10;
	private static final int MAX_HEART_ICONS = 13;
	private static final int HEALTH_BAR_WIDTH = BASE_HEART_ICONS * HEART_SIZE;
	private static final int DEFAULT_TEXT_COLOR = 0xFFFFFF;
	private static final int UNKNOWN_CLASS_COLOR = 0xAAAAAA;
	private static final int WARRIOR_COLOR = 0xFF5555;
	private static final int THIEF_COLOR = 0xFFAA00;
	private static final int SUPPORT_COLOR = 0x55FFFF;
	private static final int MAGE_COLOR = 0xFF55FF;

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		Minecraft minecraft = Minecraft.getInstance();
		Player self = minecraft.player;
		if (self == null || self.level() == null) {
			return;
		}
		if (!MinigamesModVariables.MapVariables.get(self.level()).playingDungeons) {
			return;
		}

		int rowIndex = 0;
		for (Player teammate : self.level().players()) {
			if (teammate == self) {
				continue;
			}

			int rowY = ROW_Y + rowIndex * (ROW_HEIGHT + ROW_SPACING);
			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, ROW_X, rowY, 0, 0, ROW_WIDTH, ROW_HEIGHT, ROW_WIDTH, ROW_HEIGHT);

			int playerNameColor = getPlayerNameColor(teammate);
			ClassDisplay classDisplay = getClassDisplay(teammate.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon);
			String playerName = teammate.getGameProfile().getName();
			String classLabel = classDisplay.label();
			drawCenteredHeader(event, teammate, playerName, classLabel, withFullAlpha(playerNameColor), withFullAlpha(classDisplay.color()), rowY);
			drawHealthBar(event, teammate, rowY);
			rowIndex++;
		}
	}

	private static void drawCenteredHeader(RenderGuiEvent.Pre event, Player teammate, String playerName, String classLabel, int nameColor, int classColor, int rowY) {
		Minecraft minecraft = Minecraft.getInstance();
		int nameWidth = minecraft.font.width(playerName);
		int classWidth = minecraft.font.width(classLabel);
		int nameRowWidth = HEAD_SIZE + HEAD_GAP + nameWidth;
		int centerX = HEALTH_BAR_X + HEALTH_BAR_WIDTH / 2;
		int nameRowX = centerX - nameRowWidth / 2;
		int classX = centerX - classWidth / 2;

		int headX = nameRowX;
		int nameX = headX + HEAD_SIZE + HEAD_GAP;
		int headY = rowY + ((NAME_Y + CLASS_Y - HEAD_SIZE) / 2) - ROW_Y - 1;

		PlayerFaceRenderer.draw(event.getGuiGraphics(), getPlayerSkin(teammate), headX, headY, HEAD_SIZE);
		event.getGuiGraphics().drawString(minecraft.font, Component.literal(playerName), nameX, rowY + (NAME_Y - ROW_Y), nameColor, false);
		event.getGuiGraphics().drawString(minecraft.font, Component.literal(classLabel), classX, rowY + (CLASS_Y - ROW_Y), classColor, false);
	}

	private static PlayerSkin getPlayerSkin(Player player) {
		if (player instanceof AbstractClientPlayer clientPlayer) {
			return clientPlayer.getSkin();
		}
		return DefaultPlayerSkin.get(player.getUUID());
	}

	private static void drawHealthBar(RenderGuiEvent.Pre event, Player teammate, int rowY) {
		int heartsX = HEALTH_BAR_X;
		int heartsY = rowY + (HEALTH_BAR_Y - ROW_Y);

		int maxHalfHearts = Math.max(2, (int) Math.ceil(Math.max(1.0F, teammate.getMaxHealth())));
		int healthHalfHearts = Math.max(0, Math.min(maxHalfHearts, (int) Math.ceil(teammate.getHealth() * 2.0F)));
		int absorptionHalfHearts = Math.max(0, (int) Math.ceil(teammate.getAbsorptionAmount() * 2.0F));
		int totalIcons = Math.min(MAX_HEART_ICONS, Math.max(1, (int) Math.ceil((maxHalfHearts + absorptionHalfHearts) / 2.0)));

		ResourceLocation fullHeart = getNormalHeartFull(teammate);
		ResourceLocation halfHeart = getNormalHeartHalf(teammate);

		for (int i = 0; i < totalIcons; i++) {
			int x = heartsX + i * HEART_SIZE;
			event.getGuiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED, HEART_CONTAINER, x, heartsY, HEART_SIZE, HEART_SIZE);

			int slotStart = i * 2;
			int healthInSlot = Math.max(0, Math.min(2, healthHalfHearts - slotStart));
			if (healthInSlot >= 2) {
				event.getGuiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED, fullHeart, x, heartsY, HEART_SIZE, HEART_SIZE);
			} else if (healthInSlot == 1) {
				event.getGuiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED, halfHeart, x, heartsY, HEART_SIZE, HEART_SIZE);
			}

			int absorbInSlot = Math.max(0, Math.min(2, absorptionHalfHearts - Math.max(0, slotStart - maxHalfHearts)));
			if (slotStart >= maxHalfHearts && absorbInSlot > 0) {
				event.getGuiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED, absorbInSlot >= 2 ? HEART_ABSORBING_FULL : HEART_ABSORBING_HALF, x, heartsY, HEART_SIZE, HEART_SIZE);
			}
		}

	}

	private static ResourceLocation getNormalHeartFull(Player player) {
		if (player.hasEffect(MobEffects.WITHER)) {
			return HEART_WITHERED_FULL;
		}
		if (player.hasEffect(MobEffects.POISON)) {
			return HEART_POISONED_FULL;
		}

		// Fallback for future effects: harmful effects use a darker red tint.
		for (var effectInstance : player.getActiveEffects()) {
			if (effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
				return HEART_WITHERED_FULL;
			}
		}
		return HEART_FULL;
	}

	private static ResourceLocation getNormalHeartHalf(Player player) {
		if (player.hasEffect(MobEffects.WITHER)) {
			return HEART_WITHERED_HALF;
		}
		if (player.hasEffect(MobEffects.POISON)) {
			return HEART_POISONED_HALF;
		}

		for (var effectInstance : player.getActiveEffects()) {
			if (effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
				return HEART_WITHERED_HALF;
			}
		}
		return HEART_HALF;
	}

	private static int getPlayerNameColor(Player player) {
		PlayerTeam team = player.level().getScoreboard().getPlayersTeam(player.getScoreboardName());
		if (team == null || team.getColor() == null || team.getColor().getColor() == null) {
			return DEFAULT_TEXT_COLOR;
		}
		return team.getColor().getColor();
	}

	private static int withFullAlpha(int color) {
		return (color & 0x00FFFFFF) | 0xFF000000;
	}

	private static ClassDisplay getClassDisplay(String rawClassName) {
		String className = rawClassName == null ? "" : rawClassName.trim().toLowerCase();
		return switch (className) {
		case "warrior" -> new ClassDisplay("Warrior", WARRIOR_COLOR);
		case "thief" -> new ClassDisplay("Thief", THIEF_COLOR);
		case "support" -> new ClassDisplay("Support", SUPPORT_COLOR);
		case "mage" -> new ClassDisplay("Mage", MAGE_COLOR);
		default -> new ClassDisplay("No Class", UNKNOWN_CLASS_COLOR);
		};
	}

	private record ClassDisplay(String label, int color) {
	}
}
