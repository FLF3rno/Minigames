package net.mcreator.minigames.client.screens;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;
import com.mojang.authlib.GameProfile;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@EventBusSubscriber(Dist.CLIENT)
public class WinScreenOverlay {
	private static final Map<UUID, RemotePlayer> DUMMY_PLAYERS = new HashMap<>();

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
		}
		if (world != null && MinigamesModVariables.MapVariables.get(world).showWinscreen) {
			event.getGuiGraphics().fill(0, 0, event.getGuiGraphics().guiWidth(), event.getGuiGraphics().guiHeight(), 0x80000000);
			int gap = w / (MinigamesModVariables.MapVariables.get(world).WinnerList.size() + 1);
			for (int pN = 0; pN < MinigamesModVariables.MapVariables.get(world).WinnerList.size(); pN++) {
				renderPlayers(event, String.valueOf(MinigamesModVariables.MapVariables.get(world).WinnerList.get(pN)), world, gap * (pN + 1));
			}
		} else {
			if (!DUMMY_PLAYERS.isEmpty()) {
				DUMMY_PLAYERS.clear();
			}
		}
	}

	private static void renderPlayers(RenderGuiEvent.Pre event, String uuid, Level world, int pos) {
		Entity player = null;
		UUID id;
		try {
			id = UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			return;
		}

		if (world instanceof net.minecraft.server.level.ServerLevel serverLevel) {
			player = serverLevel.getPlayerByUUID(id);
		}
		if (player == null && Minecraft.getInstance().level != null) {
			player = Minecraft.getInstance().level.getPlayerByUUID(id);
		}
		if (player == null && Minecraft.getInstance().level != null) {
			player = DUMMY_PLAYERS.computeIfAbsent(id, k -> {
				GameProfile profile = null;
				if (Minecraft.getInstance().getConnection() != null) {
					PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(id);
					if (info != null) {
						profile = info.getProfile();
					}
				}
				if (profile == null) {
					profile = new GameProfile(id, "");
				}
				RemotePlayer dummy = new RemotePlayer(Minecraft.getInstance().level, profile);
				dummy.addEffect(new MobEffectInstance(MinigamesModMobEffects.CROWNED, 99999999, 0, false, false));
				return dummy;
			});
		}

		int h = event.getGuiGraphics().guiHeight();
		int scale = 100 - 5 * MinigamesModVariables.MapVariables.get(world).WinnerList.size();
		if (scale < 1) scale = 3;
		int x0 = pos - scale / 2;
		int x1 = pos + scale / 2;
		if (player instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventoryFollowsAngle(event.getGuiGraphics(), x0, h / 2 + -932, x1, h / 2 + 1068, scale, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), 0f, 0, livingEntity);
		}
	}
}