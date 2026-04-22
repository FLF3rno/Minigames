package net.mcreator.minigames.client.screens;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber(Dist.CLIENT)
public class TeammateOverlayOverlay {
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/teammateoverlay.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("minigames:textures/screens/health_boost.png");
	private static final int ROW_X = 0;
	private static final int ROW_Y = 3;
	private static final int ROW_HEIGHT = 61;
	private static final int ROW_SPACING = 2;
	private static final int NAME_X = 4;
	private static final int NAME_Y = 9;
	private static final int CLASS_X = 4;
	private static final int CLASS_Y = 20;
	private static final int ICON_X = 3;
	private static final int ICON_Y = 35;
	private static final int HEALTH_BAR_X = 31;
	private static final int HEALTH_BAR_Y = 39;
	private static final int HEALTH_BAR_W = 117;
	private static final int HEALTH_BAR_H = 8;
	private static final int HEALTH_TEXT_Y = 50;
	private static final int DEFAULT_TEXT_COLOR = 0xFFFFFF;
	private static final int UNKNOWN_CLASS_COLOR = 0xAAAAAA;
	private static final int WARRIOR_COLOR = 0xFF5555;
	private static final int THIEF_COLOR = 0xFFAA00;
	private static final int SUPPORT_COLOR = 0x55FFFF;
	private static final int MAGE_COLOR = 0xFF55FF;
	private static final int HEALTH_BG_COLOR = 0xAA141414;
	private static final int HEALTH_BORDER_COLOR = 0xFF2D2D2D;
	private static final int HEALTH_NORMAL_COLOR = 0xFFE34B4B;
	private static final int HEALTH_POISON_COLOR = 0xFF49C13D;
	private static final int HEALTH_WITHER_COLOR = 0xFF2E2E2E;
	private static final int HEALTH_ABSORPTION_COLOR = 0xFFE8C24A;
	private static final int HEALTH_TEXT_COLOR = 0xFFEDEDED;

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
			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, ROW_X, rowY, 0, 0, 154, 61, 154, 61);
			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, ICON_X, rowY + (ICON_Y - ROW_Y), 0, 0, 23, 23, 23, 23);

			int playerNameColor = getPlayerNameColor(teammate);
			ClassDisplay classDisplay = getClassDisplay(teammate.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon);

			event.getGuiGraphics().drawString(minecraft.font, Component.literal(teammate.getGameProfile().getName()), NAME_X, rowY + (NAME_Y - ROW_Y), playerNameColor, false);
			event.getGuiGraphics().drawString(minecraft.font, Component.literal(classDisplay.label()), CLASS_X, rowY + (CLASS_Y - ROW_Y), classDisplay.color(), false);
			drawHealthBar(event, teammate, rowY);
			rowIndex++;
		}
	}

	private static void drawHealthBar(RenderGuiEvent.Pre event, Player teammate, int rowY) {
		int barX = HEALTH_BAR_X;
		int barY = rowY + (HEALTH_BAR_Y - ROW_Y);

		float maxHealth = Math.max(1.0F, teammate.getMaxHealth());
		float health = Math.max(0.0F, Math.min(maxHealth, teammate.getHealth()));
		float absorption = Math.max(0.0F, teammate.getAbsorptionAmount());

		float healthRatio = health / maxHealth;
		float absorptionRatio = Math.min(1.0F, (health + absorption) / maxHealth);

		int healthFill = Math.round(HEALTH_BAR_W * healthRatio);
		int totalFill = Math.round(HEALTH_BAR_W * absorptionRatio);
		int absorptionFill = Math.max(0, totalFill - healthFill);

		event.getGuiGraphics().fill(barX - 1, barY - 1, barX + HEALTH_BAR_W + 1, barY + HEALTH_BAR_H + 1, HEALTH_BORDER_COLOR);
		event.getGuiGraphics().fill(barX, barY, barX + HEALTH_BAR_W, barY + HEALTH_BAR_H, HEALTH_BG_COLOR);

		if (healthFill > 0) {
			event.getGuiGraphics().fill(barX, barY, barX + healthFill, barY + HEALTH_BAR_H, getHealthFillColor(teammate));
		}

		if (absorptionFill > 0) {
			event.getGuiGraphics().fill(barX + healthFill, barY, barX + healthFill + absorptionFill, barY + HEALTH_BAR_H, HEALTH_ABSORPTION_COLOR);
		}

		String healthText = Math.round(health) + "/" + Math.round(maxHealth);
		event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.literal(healthText), barX, rowY + (HEALTH_TEXT_Y - ROW_Y), HEALTH_TEXT_COLOR, false);
	}

	private static int getHealthFillColor(Player player) {
		if (player.hasEffect(MobEffects.WITHER)) {
			return HEALTH_WITHER_COLOR;
		}
		if (player.hasEffect(MobEffects.POISON)) {
			return HEALTH_POISON_COLOR;
		}

		// Fallback for future effects: harmful effects use a darker red tint.
		for (var effectInstance : player.getActiveEffects()) {
			if (effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
				return 0xFFB03A3A;
			}
		}
		return HEALTH_NORMAL_COLOR;
	}

	private static int getPlayerNameColor(Player player) {
		PlayerTeam team = player.level().getScoreboard().getPlayersTeam(player.getScoreboardName());
		if (team == null || team.getColor() == null || team.getColor().getColor() == null) {
			return DEFAULT_TEXT_COLOR;
		}
		return team.getColor().getColor();
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
