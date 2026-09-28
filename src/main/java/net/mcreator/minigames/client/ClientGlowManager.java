package net.mcreator.minigames.client;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.mcreator.minigames.client.gui.options.MinigamesSettingsConfig;
import net.mcreator.minigames.entity.CandleheadEntity;
import net.minecraft.network.chat.TextColor;
import net.mcreator.minigames.entity.DemonEntity;
import net.mcreator.minigames.entity.GravediggerEntity;
import net.mcreator.minigames.entity.GravediggerMinibossEntity;
import net.mcreator.minigames.entity.LaserStatueEntity;
import net.mcreator.minigames.entity.PreacherEntity;
import net.mcreator.minigames.entity.SculklingEntity;
import net.mcreator.minigames.entity.ShieldAngelEntity;
import net.mcreator.minigames.entity.WorshipperEntity;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.procedures.CheckRelicProcedure;

public class ClientGlowManager {
	private static final Map<Integer, GlowEntry> GLOWING_ENTITIES = new ConcurrentHashMap<>();
	private static volatile boolean cachedHasBatEssence = false;
	private static volatile int cachedMinLight = 0;
	private static volatile int cachedMaxLight = 15;
	private static int lastEffectiveMinLight = -999;
	private static int lastEffectiveMaxLight = -999;

	private static class GlowEntry {
		final int color;
		int remainingTicks;

		GlowEntry(int color, int remainingTicks) {
			this.color = color;
			this.remainingTicks = remainingTicks;
		}
	}

	public static void addGlow(int entityId, int color, int ticks) {
		if (ticks <= 0) {
			GLOWING_ENTITIES.remove(entityId);
			return;
		}
		GLOWING_ENTITIES.put(entityId, new GlowEntry(color, ticks));
	}

	public static void removeGlow(int entityId) {
		GLOWING_ENTITIES.remove(entityId);
	}

	public static boolean hasExplicitGlow(int entityId) {
		return GLOWING_ENTITIES.containsKey(entityId);
	}

	public static boolean isSuppressed(int entityId) {
		GlowEntry entry = GLOWING_ENTITIES.get(entityId);
		return entry != null && entry.color == -2;
	}

	public static boolean isForceDisableGlowingActive() {
		Minecraft mc = Minecraft.getInstance();
		if (mc == null || mc.level == null) {
			return false;
		}
		try {
			return MinigamesModVariables.MapVariables.get(mc.level).forceDisableGlowingPlayerSetting;
		} catch (Throwable ignored) {
			return false;
		}
	}

	public static boolean isRoguelikePlayerGlowingSettingEnabled() {
		return MinigamesSettingsConfig.getBoolean("roguelikePlayerGlowing",
				MinigamesSettingsConfig.getBoolean("playerGlowing", true));
	}

	public static boolean isRoguelikePlayerGlowingActive() {
		if (isForceDisableGlowingActive()) {
			return false;
		}
		return isRoguelikePlayerGlowingSettingEnabled();
	}

	public static int getPlayerChosenColor(Player player) {
		if (player == null) {
			return 0xFFFFFF;
		}
		try {
			MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
			if (vars != null && vars.color != null && !vars.color.isBlank()) {
				int parsed = parseColor(vars.color);
				if (parsed >= 0) {
					return parsed;
				}
			}
		} catch (Throwable ignored) {
		}
		try {
			if (player.getDisplayName() != null && player.getDisplayName().getStyle() != null) {
				TextColor textColor = player.getDisplayName().getStyle().getColor();
				if (textColor != null) {
					return textColor.getValue();
				}
			}
		} catch (Throwable ignored) {
		}
		return 0xFFFFFF;
	}

	public static boolean isGlowing(Entity entity) {
		if (entity == null || !entity.level().isClientSide()) {
			return false;
		}
		GlowEntry entry = GLOWING_ENTITIES.get(entity.getId());
		if (entry != null) {
			return entry.color != -2;
		}
		if (isRoguelikePlayerGlowingActive() && entity instanceof Player player) {
			if (!player.isRemoved() && !player.isSpectator() && !player.isInvisible()) {
				return true;
			}
		}
		if (isBatEssenceActive()) {
			if (entity.isRemoved() || entity.isInvisible()) {
				return false;
			}
			if (entity instanceof Player || entity instanceof ItemEntity) {
				return true;
			}
			if (entity instanceof Enemy || entity instanceof Monster) {
				return isInSameRoom(entity);
			}
		}
		return false;
	}

	public static int getGlowColor(Entity entity) {
		if (entity == null || !entity.level().isClientSide()) {
			return -1;
		}
		GlowEntry entry = GLOWING_ENTITIES.get(entity.getId());
		if (entry != null) {
			return entry.color == -2 ? -1 : entry.color;
		}
		if (isRoguelikePlayerGlowingActive() && entity instanceof Player player) {
			return getPlayerChosenColor(player);
		}
		if (isBatEssenceActive()) {
			if (entity instanceof Player) {
				return 0x55FFFF;
			}
			if (entity instanceof ItemEntity) {
				return 0xFFFFFF;
			}
			if (entity instanceof Enemy || entity instanceof Monster) {
				if (isInSameRoom(entity)) {
					return 0xFF5555;
				}
			}
		}
		return -1;
	}

	public static boolean isInSameRoom(Entity entity) {
		if (entity == null) {
			return false;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc == null || mc.player == null || mc.level == null) {
			return false;
		}
		int playerRoomId = (int) MinigamesModVariables.MapVariables.get(mc.level).currentRoomID;
		int entityRoomId = getEntityRoomId(entity);
		if (entityRoomId > 0) {
			return playerRoomId > 0 && entityRoomId == playerRoomId;
		}
		if (playerRoomId == 0) {
			return !MinigamesModVariables.MapVariables.get(mc.level).playingDungeons;
		}
		int playerGridX = (int) Math.floor(mc.player.getX() / 29.0);
		int playerGridZ = (int) Math.floor(mc.player.getZ() / 29.0);
		int entityGridX = (int) Math.floor(entity.getX() / 29.0);
		int entityGridZ = (int) Math.floor(entity.getZ() / 29.0);
		return playerGridX == entityGridX && playerGridZ == entityGridZ;
	}

	public static int getEntityRoomId(Entity entity) {
		if (entity == null) {
			return 0;
		}
		if (entity instanceof CandleheadEntity e) {
			return e.getEntityData().get(CandleheadEntity.DATA_ID);
		}
		if (entity instanceof DemonEntity e) {
			return e.getEntityData().get(DemonEntity.DATA_ID);
		}
		if (entity instanceof GravediggerEntity e) {
			return e.getRoomID();
		}
		if (entity instanceof GravediggerMinibossEntity e) {
			return e.getRoomID();
		}
		if (entity instanceof LaserStatueEntity e) {
			return e.getRoomID();
		}
		if (entity instanceof PreacherEntity e) {
			return e.getEntityData().get(PreacherEntity.DATA_ID);
		}
		if (entity instanceof SculklingEntity e) {
			return e.getEntityData().get(SculklingEntity.DATA_ID);
		}
		if (entity instanceof ShieldAngelEntity e) {
			return e.getEntityData().get(ShieldAngelEntity.DATA_ID);
		}
		if (entity instanceof WorshipperEntity e) {
			return e.getEntityData().get(WorshipperEntity.DATA_ID);
		}
		int id = entity.getPersistentData().getIntOr("DataID", 0);
		if (id != 0) {
			return id;
		}
		return (int) entity.getPersistentData().getDoubleOr("DataID", 0);
	}

	public static int getMinLight() {
		return cachedMinLight;
	}

	public static int getMaxLight() {
		return cachedMaxLight;
	}

	public static boolean isBatEssenceActive() {
		return cachedHasBatEssence;
	}

	public static boolean hasBatEssence(Player player) {
		if (player == null) {
			return false;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc != null && mc.player == player) {
			return cachedHasBatEssence;
		}
		return computeHasBatEssence(player);
	}

	private static boolean computeHasBatEssence(Player player) {
		if (player == null) {
			return false;
		}
		try {
			if (CheckRelicProcedure.execute(player, new ItemStack(MinigamesModItems.BAT_ESSENCE.get()))) {
				return true;
			}
		} catch (Throwable ignored) {
		}
		try {
			var inventory = player.getInventory();
			if (inventory != null) {
				ItemStack s34 = inventory.getItem(34);
				if (!s34.isEmpty() && s34.is(MinigamesModItems.BAT_ESSENCE.get())) {
					return true;
				}
				ItemStack s35 = inventory.getItem(35);
				if (!s35.isEmpty() && s35.is(MinigamesModItems.BAT_ESSENCE.get())) {
					return true;
				}
			}
		} catch (Throwable ignored) {
		}
		return false;
	}

	public static int parseColor(String color) {
		if (color == null || color.isBlank()) {
			return 0xFFFFFF;
		}
		String s = color.trim().toLowerCase(java.util.Locale.ROOT);
		switch (s) {
			case "none":
			case "off":
			case "hide":
			case "hidden":
			case "clear":
			case "disable":
			case "disabled":
				return -2;
			case "red": return 0xFF5555;
			case "dark_red": return 0xAA0000;
			case "green": return 0x55FF55;
			case "dark_green": return 0x00AA00;
			case "blue": return 0x5555FF;
			case "dark_blue": return 0x0000AA;
			case "yellow": return 0xFFFF55;
			case "gold":
			case "orange": return 0xFFAA00;
			case "aqua":
			case "cyan": return 0x55FFFF;
			case "dark_aqua": return 0x00AAAA;
			case "purple":
			case "dark_purple": return 0xAA00AA;
			case "pink":
			case "light_purple": return 0xFF55FF;
			case "white": return 0xFFFFFF;
			case "black": return 0x000000;
			case "gray":
			case "grey": return 0xAAAAAA;
			case "dark_gray":
			case "dark_grey": return 0x555555;
		}
		if (s.startsWith("#")) {
			s = s.substring(1);
		} else if (s.startsWith("0x")) {
			s = s.substring(2);
		}
		if (s.matches("^[0-9a-f]{6}$")) {
			try {
				return Integer.parseInt(s, 16);
			} catch (NumberFormatException ignored) {
			}
		}
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException ignored) {
		}
		return 0xFFFFFF;
	}

	public static void tick() {
		tickLighting();
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			GLOWING_ENTITIES.clear();
			return;
		}
		if (GLOWING_ENTITIES.isEmpty()) {
			return;
		}
		Iterator<Map.Entry<Integer, GlowEntry>> it = GLOWING_ENTITIES.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, GlowEntry> entry = it.next();
			GlowEntry data = entry.getValue();
			data.remainingTicks--;
			if (data.remainingTicks <= 0) {
				it.remove();
			}
		}
	}

	public static void tickLighting() {
		Minecraft mc = Minecraft.getInstance();
		if (mc == null || mc.level == null || mc.player == null) {
			cachedHasBatEssence = false;
			cachedMinLight = 0;
			cachedMaxLight = 15;
			lastEffectiveMinLight = -999;
			lastEffectiveMaxLight = -999;
			return;
		}
		cachedHasBatEssence = computeHasBatEssence(mc.player);
		int min;
		int max;
		if (cachedHasBatEssence) {
			min = 0;
			max = 0;
		} else {
			try {
				var variables = mc.player.getData(MinigamesModVariables.PLAYER_VARIABLES);
				min = Mth.clamp((int) variables.minimumLightLevel, 0, 15);
				max = Mth.clamp((int) variables.maximumLightLevel, 0, 15);
				if (min > max) {
					min = max;
				}
			} catch (Throwable t) {
				min = 0;
				max = 15;
			}
		}
		cachedMinLight = min;
		cachedMaxLight = max;
		if (mc.levelRenderer != null) {
			if (lastEffectiveMinLight != -999) {
				if (min != lastEffectiveMinLight || max != lastEffectiveMaxLight) {
					mc.levelRenderer.allChanged();
				}
			} else {
				if (min != 0 || max != 15) {
					mc.levelRenderer.allChanged();
				}
			}
		}
		lastEffectiveMinLight = min;
		lastEffectiveMaxLight = max;
	}

	@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
	public static class ClientTickEvents {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			ClientGlowManager.tick();
		}
	}
}
