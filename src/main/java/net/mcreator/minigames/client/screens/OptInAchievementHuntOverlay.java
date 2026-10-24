package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.client.AccountSwapClient;
import net.mcreator.minigames.init.MinigamesModKeyMappings;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(Dist.CLIENT)
public class OptInAchievementHuntOverlay {
	private static final Identifier IMAGE_0 = Identifier.parse("minigames:textures/screens/voteyes.png");
	private static final Identifier IMAGE_1 = Identifier.parse("minigames:textures/font/hunted.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
		}
		if (MinigamesModVariables.MapVariables.get(world).ShowOptInAchievement) {
			Minecraft minecraft = Minecraft.getInstance();
			Component voteYesKeybind = MinigamesModKeyMappings.VOTE_YES.getTranslatedKeyMessage();

			int timerTicks = (int) MinigamesModVariables.MapVariables.get(world).AchievementHunterOptInTimer;
			int seconds = (int) Math.ceil(timerTicks / 20.0);
			String timerText = seconds + "s";
			int timerWidth = minecraft.font.width(timerText);
			int timerX = (w - timerWidth) / 2;
			int timerY = h / 2 - 70;

			event.getGuiGraphics().text(minecraft.font, Component.literal(timerText), timerX, timerY, 0xFFFFAA00, true);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, w / 2 - 8, h / 2 - 58, 0, 0, 16, 16, 16, 16);

			event.getGuiGraphics().text(Minecraft.getInstance().font, Component.translatable("gui.minigames.opt_in_achievement_hunt.label_do_you_want_to_be_hunted"), w / 2 + -65, h / 2 + -39, -65536, true);

			event.getGuiGraphics().blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, w / 2 - 14, h / 2 - 22, 0, 0, 28, 16, 28, 16);

			event.getGuiGraphics().text(minecraft.font, voteYesKeybind, w / 2 - 14 + (28 - minecraft.font.width(voteYesKeybind)) / 2, h / 2 - 22 + 6, 0xFF000000, false);

			List<Object> rawList = MinigamesModVariables.MapVariables.get(world).wantsToBeHuntedAchievement;
			List<PlayerSkin> votedSkins = new ArrayList<>();
			if (rawList != null && !rawList.isEmpty()) {
				for (Object obj : rawList) {
					PlayerSkin skin = resolvePlayerSkin(obj, world);
					if (skin != null) {
						votedSkins.add(skin);
					}
				}
			}

			if (!votedSkins.isEmpty()) {
				int headSize = 16;
				int gap = 3;
				int totalWidth = (votedSkins.size() * headSize) + ((votedSkins.size() - 1) * gap);
				int startX = (w - totalWidth) / 2;
				int headY = h / 2 - 2;

				for (int i = 0; i < votedSkins.size(); i++) {
					PlayerSkin skin = votedSkins.get(i);
					int headX = startX + i * (headSize + gap);
					PlayerFaceExtractor.extractRenderState(event.getGuiGraphics(), skin, headX, headY, headSize);
				}
			}
		}
	}

	private static PlayerSkin resolvePlayerSkin(Object obj, Level world) {
		if (obj == null) {
			return null;
		}
		Minecraft minecraft = Minecraft.getInstance();
		if (obj instanceof Player player) {
			return getPlayerSkin(player, player.getUUID());
		}
		if (obj instanceof UUID uuid) {
			Player p = world != null ? world.getPlayerByUUID(uuid) : null;
			return getPlayerSkin(p, uuid);
		}
		String str = String.valueOf(obj);
		if (str.isBlank()) {
			return null;
		}
		try {
			UUID uuid = UUID.fromString(str);
			Player p = world != null ? world.getPlayerByUUID(uuid) : null;
			return getPlayerSkin(p, uuid);
		} catch (IllegalArgumentException ignored) {
		}
		String targetName = str;
		int first = str.indexOf('\'');
		int second = str.indexOf('\'', first + 1);
		if (first != -1 && second != -1) {
			targetName = str.substring(first + 1, second);
		}
		if (minecraft.getConnection() != null) {
			for (PlayerInfo info : minecraft.getConnection().getListedOnlinePlayers()) {
				if (info.getProfile().name().equalsIgnoreCase(targetName)) {
					return info.getSkin();
				}
			}
		}
		if (world != null) {
			for (Player p : world.players()) {
				if (p.getGameProfile().name().equalsIgnoreCase(targetName)) {
					return getPlayerSkin(p, p.getUUID());
				}
			}
		}
		return null;
	}

	private static PlayerSkin getPlayerSkin(Player player, UUID fallbackId) {
		if (player != null) {
			PlayerSkin swapped = AccountSwapClient.getSkin(player.getUUID());
			if (swapped != null) {
				return swapped;
			}
			if (player instanceof AbstractClientPlayer clientPlayer) {
				return clientPlayer.getSkin();
			}
		}
		if (fallbackId != null) {
			return DefaultPlayerSkin.get(fallbackId);
		}
		return DefaultPlayerSkin.getDefaultSkin();
	}
}
