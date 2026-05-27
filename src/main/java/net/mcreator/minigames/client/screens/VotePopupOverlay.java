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
import net.minecraft.client.multiplayer.PlayerInfo;
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
import java.util.Comparator;

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
			int voteIconRenderWidth = 66;
			int voteIconRenderHeight = 32;
			int voteIconFrameSize = 32;
			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, SPRITE_0, (w / 2) - (voteIconRenderWidth / 2), iconY, 0, voteIconIndex * voteIconFrameSize, voteIconRenderWidth, voteIconRenderHeight, voteIconFrameSize, voteIconFrameSize, voteIconFrameSize,
					voteIconFrameSize * 10);
			PlayerFaceRenderer.draw(event.getGuiGraphics(), getVotingPlayerSkin(), headX, nameY - 1, 8);
			event.getGuiGraphics().drawString(minecraft.font, Component.literal(playerName), nameBaseX, nameY, playerNameColor, false);
			int messageX = (w - minecraft.font.width(voteMessage)) / 2;
			Component voteMessageComponent = Component.literal(voteMessage);
			event.getGuiGraphics().drawString(minecraft.font, voteMessageComponent, messageX - 1, messageY, 0xFFFFFFFF, false);
			event.getGuiGraphics().drawString(minecraft.font, voteMessageComponent, messageX + 1, messageY, 0xFFFFFFFF, false);
			event.getGuiGraphics().drawString(minecraft.font, voteMessageComponent, messageX, messageY - 1, 0xFFFFFFFF, false);
			event.getGuiGraphics().drawString(minecraft.font, voteMessageComponent, messageX, messageY + 1, 0xFFFFFFFF, false);
			event.getGuiGraphics().drawString(minecraft.font, Component.literal(voteMessage), messageX, messageY, 0xFF000000, false);
			event.getGuiGraphics().drawString(minecraft.font, voteYesKeybind, leftButtonX + (28 - minecraft.font.width(voteYesKeybind)) / 2, popupCenterY + 5, 0xFF000000, false);
			event.getGuiGraphics().drawString(minecraft.font, voteNoKeybind, rightButtonX + (28 - minecraft.font.width(voteNoKeybind)) / 2, popupCenterY + 5, 0xFF000000, false);
			drawOtherPlayersVoteHeads(event, entity, headsY, w);
		}
	}

	private static void drawOtherPlayersVoteHeads(RenderGuiEvent.Pre event, Player self, int y, int screenWidth) {
		Minecraft minecraft = Minecraft.getInstance();
		if (self == null || minecraft.getConnection() == null) {
			return;
		}
		UUID callerId = MinigamesModVariables.VotingEntity != null ? MinigamesModVariables.VotingEntity.getUUID() : null;
		List<PlayerInfo> others = new ArrayList<>();
		for (PlayerInfo info : minecraft.getConnection().getListedOnlinePlayers()) {
			UUID id = info.getProfile().getId();
			if (callerId != null && callerId.equals(id)) {
				continue;
			}
			others.add(info);
		}
		others.sort(Comparator.comparing(info -> info.getProfile().getName(), String.CASE_INSENSITIVE_ORDER));
		if (others.isEmpty()) {
			return;
		}
		int votedSize = 16;
		int gap = 3;
		int totalWidth = (others.size() * votedSize) + ((others.size() - 1) * gap);
		int x = (screenWidth - totalWidth) / 2;
		for (PlayerInfo info : others) {
			UUID id = info.getProfile().getId();
			Player localEntity = self.level() != null ? self.level().getPlayerByUUID(id) : null;
			boolean voted = localEntity != null && localEntity.getData(MinigamesModVariables.PLAYER_VARIABLES).voted;
			boolean votedYes = localEntity != null && localEntity.getData(MinigamesModVariables.PLAYER_VARIABLES).votedYes;
			int size = votedSize;
			PlayerFaceRenderer.draw(event.getGuiGraphics(), getPlayerSkin(localEntity, id), x, y, size);
			if (voted) {
				int tint = votedYes ? 0x6600FF00 : 0x66FF0000;
				event.getGuiGraphics().fill(x, y, x + size, y + size, tint);
			}
			x += size + gap;
		}
	}

	private static PlayerSkin getPlayerSkin(Player player, UUID fallbackId) {
		if (player instanceof AbstractClientPlayer clientPlayer) {
			return clientPlayer.getSkin();
		}
		return DefaultPlayerSkin.get(fallbackId);
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
