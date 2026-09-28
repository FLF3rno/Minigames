package net.mcreator.minigames.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerSkin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class AccountSwapClient {
	public record SwappedIdentity(String swappedName, UUID skinOwnerUUID, String skinOwnerName) {}

	private static final Map<UUID, SwappedIdentity> SWAPPED_PLAYERS = new ConcurrentHashMap<>();
	private static final Map<UUID, Supplier<PlayerSkin>> SKIN_LOOKUP_CACHE = new ConcurrentHashMap<>();
	private static final ThreadLocal<Boolean> RESOLVING_ORIGINAL = ThreadLocal.withInitial(() -> Boolean.FALSE);

	public static void handleSync(UUID playerUUID, String swappedName, UUID skinOwnerUUID, String skinOwnerName, boolean isReset) {
		if (isReset) {
			SWAPPED_PLAYERS.remove(playerUUID);
		} else {
			SWAPPED_PLAYERS.put(playerUUID, new SwappedIdentity(
				(swappedName != null && !swappedName.isEmpty()) ? swappedName : null,
				skinOwnerUUID,
				skinOwnerName
			));
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.level != null) {
			Player p = mc.level.getPlayerByUUID(playerUUID);
			if (p != null) {
				p.refreshDisplayName();
			}
		}
		if (mc.player != null && mc.player.getUUID().equals(playerUUID)) {
			mc.player.refreshDisplayName();
		}
	}

	public static String getSwappedName(UUID playerUUID) {
		if (playerUUID == null) {
			return null;
		}
		SwappedIdentity identity = SWAPPED_PLAYERS.get(playerUUID);
		if (identity != null && identity.swappedName() != null) {
			return identity.swappedName();
		}
		return net.mcreator.minigames.AccountSwapManager.getSwappedName(playerUUID);
	}

	public static PlayerSkin getSkin(UUID playerUUID) {
		if (playerUUID == null) {
			return null;
		}
		if (RESOLVING_ORIGINAL.get()) {
			return null;
		}
		UUID skinOwnerUUID = null;
		String skinOwnerName = null;
		SwappedIdentity identity = SWAPPED_PLAYERS.get(playerUUID);
		if (identity != null && identity.skinOwnerUUID() != null) {
			skinOwnerUUID = identity.skinOwnerUUID();
			skinOwnerName = identity.skinOwnerName();
		} else {
			net.mcreator.minigames.AccountSwapManager.SwapRecord record = net.mcreator.minigames.AccountSwapManager.getRecord(playerUUID);
			if (record != null && record.swappedSkinUUID != null) {
				skinOwnerUUID = record.swappedSkinUUID;
				skinOwnerName = record.swappedSkinName;
			}
		}
		if (skinOwnerUUID == null) {
			return null;
		}
		try {
			RESOLVING_ORIGINAL.set(Boolean.TRUE);
			return resolveSkin(skinOwnerUUID, skinOwnerName);
		} finally {
			RESOLVING_ORIGINAL.set(Boolean.FALSE);
		}
	}

	private static PlayerSkin resolveSkin(UUID targetUUID, String targetName) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.getConnection() != null) {
			PlayerInfo info = mc.getConnection().getPlayerInfo(targetUUID);
			if (info != null) {
				return info.getSkin();
			}
		}
		Supplier<PlayerSkin> lookup = SKIN_LOOKUP_CACHE.computeIfAbsent(targetUUID, uuid -> {
			GameProfile profile = new GameProfile(uuid, targetName != null ? targetName : "");
			return mc.getSkinManager().createLookup(profile, false);
		});
		PlayerSkin result = lookup.get();
		if (result != null) {
			return result;
		}
		return DefaultPlayerSkin.get(targetUUID);
	}

	public static Component getSwappedTabName(UUID playerUUID) {
		String name = getSwappedName(playerUUID);
		if (name == null || name.isEmpty()) {
			return null;
		}
		return Component.literal(name);
	}

	public static void clearAll() {
		SWAPPED_PLAYERS.clear();
		SKIN_LOOKUP_CACHE.clear();
	}
}
