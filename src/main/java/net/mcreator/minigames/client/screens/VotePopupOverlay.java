package net.mcreator.minigames.client.screens;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModKeyMappings;
import net.mcreator.minigames.procedures.VotingPlayerNameProcedure;
import net.mcreator.minigames.procedures.VotingMessageProcedure;
import net.mcreator.minigames.procedures.VoteIconProcedure;
import net.mcreator.minigames.procedures.ShowVoteProcedure;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(Dist.CLIENT)
public class VotePopupOverlay {
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/voteno.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("minigames:textures/screens/voteyes.png");
	private static final ResourceLocation SPRITE_0 = ResourceLocation.parse("minigames:textures/screens/voteicons.png");

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		Level world = null;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
		}
		if (ShowVoteProcedure.execute(world)) {
			Minecraft minecraft = Minecraft.getInstance();
			String playerName = VotingPlayerNameProcedure.execute();
			String voteMessage = VotingMessageProcedure.execute(world);
			Component voteNoKeybind = MinigamesModKeyMappings.VOTE_NO.getTranslatedKeyMessage();
			Component voteYesKeybind = MinigamesModKeyMappings.VOTE_YES.getTranslatedKeyMessage();
			int popupCenterY = 66;
			int popupLeftX = (w / 2) - 44;
			int iconY = popupCenterY - 65;
			int nameY = popupCenterY - 32;
			int voteLabelY = popupCenterY - 22;
			int messageY = popupCenterY - 16;
			int headsY = popupCenterY + 22;
			int headX = popupLeftX + 2;
			int playerNameColor = getVotingPlayerNameColor();
			int leftButtonX = popupLeftX;
			int rightButtonX = popupLeftX + 63;
			int nameRowWidth = 8 + 2 + minecraft.font.width(playerName);
			int nameRowX = (w - nameRowWidth) / 2;
			int nameBaseX = nameRowX + 10;
			headX = nameRowX;

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, leftButtonX, popupCenterY, 0, 0, 28, 16, 28, 16);
			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, rightButtonX, popupCenterY, 0, 0, 28, 16, 28, 16);
			int voteIconIndex = Math.max(0, Math.min(8, (int) VoteIconProcedure.execute(world)));
			int voteIconSize = 66;
			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, (w / 2) - (voteIconSize / 2), iconY, 0, voteIconIndex * voteIconSize, voteIconSize, voteIconSize, voteIconSize, voteIconSize * 9);
			PlayerFaceRenderer.draw(event.getGuiGraphics(), getVotingPlayerSkin(), headX, nameY - 1, 8);
			event.getGuiGraphics().drawString(minecraft.font, Component.literal(playerName), nameBaseX, nameY, playerNameColor, false);
			event.getGuiGraphics().drawString(minecraft.font, Component.literal(voteMessage), (w - minecraft.font.width(voteMessage)) / 2, messageY, 0xFF000000, false);
			event.getGuiGraphics().drawString(minecraft.font, Component.translatable("gui.minigames.vote_popup.label_proc_vote_yes_keybind"), leftButtonX + (28 - minecraft.font.width(voteYesKeybind)) / 2, voteLabelY, 0xFF000000, false);
			event.getGuiGraphics().drawString(minecraft.font, Component.translatable("gui.minigames.vote_popup.label_proc_vote_no_keybind"), rightButtonX + (28 - minecraft.font.width(voteNoKeybind)) / 2, voteLabelY, 0xFF000000, false);
			event.getGuiGraphics().drawString(minecraft.font, voteYesKeybind, leftButtonX + (28 - minecraft.font.width(voteYesKeybind)) / 2, popupCenterY + 5, 0xFF000000, false);
			event.getGuiGraphics().drawString(minecraft.font, voteNoKeybind, rightButtonX + (28 - minecraft.font.width(voteNoKeybind)) / 2, popupCenterY + 5, 0xFF000000, false);
			drawOtherPlayersVoteHeads(event, entity, headsY, w);
		}
	}

	private static void drawOtherPlayersVoteHeads(RenderGuiEvent.Pre event, Player self, int y, int screenWidth) {
		if (self == null || self.level() == null) {
			return;
		}
		List<Player> others = new ArrayList<>();
		Player caller = MinigamesModVariables.VotingEntity instanceof Player p ? p : null;
		for (Player player : self.level().players()) {
			if (caller != null) {
				if (player != caller) {
					others.add(player);
				}
			} else if (player != self) {
				others.add(player);
			}
		}
		if (others.isEmpty()) {
			return;
		}
		int votedSize = 16;
		int gap = 3;
		int totalWidth = (others.size() * votedSize) + ((others.size() - 1) * gap);
		int x = (screenWidth - totalWidth) / 2;
		for (Player p : others) {
			boolean voted = p.getData(MinigamesModVariables.PLAYER_VARIABLES).voted;
			boolean votedYes = p.getData(MinigamesModVariables.PLAYER_VARIABLES).votedYes;
			int size = votedSize;
			PlayerFaceRenderer.draw(event.getGuiGraphics(), getPlayerSkin(p), x, y, size);
			if (voted) {
				int tint = votedYes ? 0x6600FF00 : 0x66FF0000;
				event.getGuiGraphics().fill(x, y, x + size, y + size, tint);
			}
			x += size + gap;
		}
	}

	private static PlayerSkin getPlayerSkin(Player player) {
		if (player instanceof AbstractClientPlayer clientPlayer) {
			return clientPlayer.getSkin();
		}
		return DefaultPlayerSkin.get(player.getUUID());
	}

	private static PlayerSkin getVotingPlayerSkin() {
		if (MinigamesModVariables.VotingEntity instanceof AbstractClientPlayer clientPlayer) {
			return clientPlayer.getSkin();
		}
		if (MinigamesModVariables.VotingEntity != null) {
			return DefaultPlayerSkin.get(MinigamesModVariables.VotingEntity.getUUID());
		}
		return DefaultPlayerSkin.get(Minecraft.getInstance().player != null ? Minecraft.getInstance().player.getUUID() : new UUID(0L, 0L));
	}

	private static int getVotingPlayerNameColor() {
		if (MinigamesModVariables.VotingEntity instanceof Player votingPlayer) {
			String color = votingPlayer.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
			if (color != null && color.matches("^#?[0-9a-fA-F]{6}$")) {
				String normalized = color.startsWith("#") ? color.substring(1) : color;
				return (Integer.parseInt(normalized, 16) & 0x00FFFFFF) | 0xFF000000;
			}
		}
		return -1;
	}
}
