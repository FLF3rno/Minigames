package net.mcreator.minigames;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import net.mcreator.minigames.network.AccountSwapSyncMessage;
import net.mcreator.minigames.network.MinigamesModVariables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = MinigamesMod.MODID)
public class AccountSwapManager {
	public static class SwapRecord {
		public final UUID originalUUID;
		public final String originalName;
		public final UUID originalSkinUUID;
		public final String originalSkinName;
		public final String originalClassDungeon;
		public final String originalClassColor;
		public String originalColor;
		public final boolean originalShowCustomNameColor;
		public final double originalMaxHealth;
		public final float originalHealth;
		public final float originalAbsorption;
		public final List<ItemStack> originalItems;
		public final int originalSelectedSlot;
		public final List<MobEffectInstance> originalEffects;

		public UUID partnerUUID;
		public String swappedName;
		public String swappedColor;
		public UUID swappedSkinUUID;
		public String swappedSkinName;
		public double partnerMaxHealth;

		public boolean swapClass;
		public boolean swapEffects;
		public boolean swapHearts;
		public boolean swapInventory;
		public boolean swapName;
		public boolean swapSkin;

		public SwapRecord(
			UUID originalUUID,
			String originalName,
			UUID originalSkinUUID,
			String originalSkinName,
			String originalClassDungeon,
			String originalClassColor,
			String originalColor,
			boolean originalShowCustomNameColor,
			double originalMaxHealth,
			float originalHealth,
			float originalAbsorption,
			List<ItemStack> originalItems,
			int originalSelectedSlot,
			List<MobEffectInstance> originalEffects
		) {
			this.originalUUID = originalUUID;
			this.originalName = originalName;
			this.originalSkinUUID = originalSkinUUID;
			this.originalSkinName = originalSkinName;
			this.originalClassDungeon = originalClassDungeon;
			this.originalClassColor = originalClassColor;
			this.originalColor = originalColor;
			this.originalShowCustomNameColor = originalShowCustomNameColor;
			this.originalMaxHealth = originalMaxHealth;
			this.originalHealth = originalHealth;
			this.originalAbsorption = originalAbsorption;
			this.originalItems = originalItems;
			this.originalSelectedSlot = originalSelectedSlot;
			this.originalEffects = originalEffects;
		}
	}

	public static class SoulIdentity {
		public final UUID ownerUUID;
		public final String name;
		public final UUID skinUUID;
		public final String skinName;
		public final String color;
		public final String classDungeon;
		public final String classColor;
		public final double maxHealth;
		public final float health;
		public final float absorption;
		public final List<ItemStack> items;
		public final int selectedSlot;
		public final List<MobEffectInstance> effects;

		public SoulIdentity(
			UUID ownerUUID,
			String name,
			UUID skinUUID,
			String skinName,
			String color,
			String classDungeon,
			String classColor,
			double maxHealth,
			float health,
			float absorption,
			List<ItemStack> items,
			int selectedSlot,
			List<MobEffectInstance> effects
		) {
			this.ownerUUID = ownerUUID;
			this.name = name;
			this.skinUUID = skinUUID;
			this.skinName = skinName;
			this.color = color;
			this.classDungeon = classDungeon;
			this.classColor = classColor;
			this.maxHealth = maxHealth;
			this.health = health;
			this.absorption = absorption;
			this.items = items;
			this.selectedSlot = selectedSlot;
			this.effects = effects;
		}
	}

	private static final Map<UUID, SwapRecord> ACTIVE_SWAPS = new ConcurrentHashMap<>();
	private static final Map<UUID, String> CLIENT_SWAPPED_NAMES = new ConcurrentHashMap<>();
	private static final Map<UUID, String> CLIENT_SWAPPED_COLORS = new ConcurrentHashMap<>();

	public static void handleClientSync(UUID playerUUID, String swappedName, String swappedColor, UUID skinOwnerUUID, String skinOwnerName, boolean isReset) {
		if (isReset) {
			CLIENT_SWAPPED_NAMES.remove(playerUUID);
			CLIENT_SWAPPED_COLORS.remove(playerUUID);
		} else {
			if (swappedName != null && !swappedName.isEmpty()) {
				CLIENT_SWAPPED_NAMES.put(playerUUID, swappedName);
			} else {
				CLIENT_SWAPPED_NAMES.remove(playerUUID);
			}
			if (swappedColor != null && !swappedColor.isEmpty()) {
				CLIENT_SWAPPED_COLORS.put(playerUUID, swappedColor);
			} else {
				CLIENT_SWAPPED_COLORS.remove(playerUUID);
			}
		}
		if (FMLEnvironment.getDist().isClient()) {
			net.mcreator.minigames.client.AccountSwapClient.handleSync(playerUUID, swappedName, swappedColor, skinOwnerUUID, skinOwnerName, isReset);
		}
	}

	public static String getSwappedName(UUID playerUUID) {
		if (playerUUID == null) {
			return null;
		}
		SwapRecord record = ACTIVE_SWAPS.get(playerUUID);
		if (record != null) {
			return record.swappedName;
		}
		return CLIENT_SWAPPED_NAMES.get(playerUUID);
	}

	public static String getSwappedColor(UUID playerUUID) {
		if (playerUUID == null) {
			return null;
		}
		SwapRecord record = ACTIVE_SWAPS.get(playerUUID);
		if (record != null) {
			return record.swappedColor;
		}
		return CLIENT_SWAPPED_COLORS.get(playerUUID);
	}

	public static SwapRecord getRecord(UUID playerUUID) {
		if (playerUUID == null) {
			return null;
		}
		return ACTIVE_SWAPS.get(playerUUID);
	}

	public static void clearClientSwaps() {
		CLIENT_SWAPPED_NAMES.clear();
		CLIENT_SWAPPED_COLORS.clear();
	}

	public static String resolvePlayerColor(ServerPlayer player) {
		if (player == null) {
			return "#FFFFFF";
		}
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		if (vars.color != null && vars.color.matches("^#?[0-9a-fA-F]{6}$")) {
			return vars.color.startsWith("#") ? vars.color : "#" + vars.color;
		}
		File file = new File(FMLPaths.GAMEDIR.get().toString() + "/config/minigames", "color.json");
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				JsonObject json = new Gson().fromJson(reader, JsonObject.class);
				if (json != null) {
					String key = player.getUUID().toString();
					if (json.has(key)) {
						String val = json.get(key).getAsString();
						if (val != null && val.matches("^#?[0-9a-fA-F]{6}$")) {
							return val.startsWith("#") ? val : "#" + val;
						}
					}
				}
			} catch (Exception ignored) {
			}
		}
		return "#FFFFFF";
	}

	private static SoulIdentity getCurrentSoul(ServerPlayer player) {
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		SwapRecord record = ACTIVE_SWAPS.get(player.getUUID());
		UUID ownerUUID;
		String name;
		UUID skinUUID;
		String skinName;
		String color;

		if (record != null && record.partnerUUID != null) {
			ownerUUID = record.partnerUUID;
			name = (record.swappedName != null) ? record.swappedName : player.getGameProfile().name();
			skinUUID = (record.swappedSkinUUID != null) ? record.swappedSkinUUID : player.getUUID();
			skinName = (record.swappedSkinName != null) ? record.swappedSkinName : player.getGameProfile().name();
			color = (record.swappedColor != null && !record.swappedColor.isEmpty()) ? record.swappedColor : resolvePlayerColor(player);
		} else {
			ownerUUID = player.getUUID();
			name = player.getGameProfile().name();
			skinUUID = player.getUUID();
			skinName = player.getGameProfile().name();
			color = resolvePlayerColor(player);
		}

		return new SoulIdentity(
			ownerUUID,
			name,
			skinUUID,
			skinName,
			color,
			vars.classDungeon,
			vars.classColor,
			player.getAttribute(Attributes.MAX_HEALTH).getBaseValue(),
			player.getHealth(),
			player.getAbsorptionAmount(),
			copyInventory(player.getInventory()),
			player.getInventory().getSelectedSlot(),
			copyEffects(player.getActiveEffects())
		);
	}

	public static void swapAccounts(
		LevelAccessor world,
		ServerPlayer player1,
		ServerPlayer player2,
		boolean swapClass,
		boolean swapEffects,
		boolean swapHearts,
		boolean swapInventory,
		boolean swapName,
		boolean swapSkin
	) {
		if (world.isClientSide()) {
			return;
		}
		if (player1 == null || player2 == null) {
			return;
		}
		if (player1 == player2 || player1.getUUID().equals(player2.getUUID())) {
			return;
		}

		MinigamesModVariables.PlayerVariables vars1 = player1.getData(MinigamesModVariables.PLAYER_VARIABLES);
		MinigamesModVariables.PlayerVariables vars2 = player2.getData(MinigamesModVariables.PLAYER_VARIABLES);

		SwapRecord record1 = ACTIVE_SWAPS.get(player1.getUUID());
		if (record1 == null) {
			record1 = new SwapRecord(
				player1.getUUID(),
				player1.getGameProfile().name(),
				player1.getUUID(),
				player1.getGameProfile().name(),
				vars1.classDungeon,
				vars1.classColor,
				resolvePlayerColor(player1),
				vars1.showCustomNameColor,
				player1.getAttribute(Attributes.MAX_HEALTH).getBaseValue(),
				player1.getHealth(),
				player1.getAbsorptionAmount(),
				copyInventory(player1.getInventory()),
				player1.getInventory().getSelectedSlot(),
				copyEffects(player1.getActiveEffects())
			);
		}

		SwapRecord record2 = ACTIVE_SWAPS.get(player2.getUUID());
		if (record2 == null) {
			record2 = new SwapRecord(
				player2.getUUID(),
				player2.getGameProfile().name(),
				player2.getUUID(),
				player2.getGameProfile().name(),
				vars2.classDungeon,
				vars2.classColor,
				resolvePlayerColor(player2),
				vars2.showCustomNameColor,
				player2.getAttribute(Attributes.MAX_HEALTH).getBaseValue(),
				player2.getHealth(),
				player2.getAbsorptionAmount(),
				copyInventory(player2.getInventory()),
				player2.getInventory().getSelectedSlot(),
				copyEffects(player2.getActiveEffects())
			);
		}

		SoulIdentity soul1 = getCurrentSoul(player1);
		SoulIdentity soul2 = getCurrentSoul(player2);

		if (soul2.ownerUUID.equals(player1.getUUID())) {
			restoreToOriginalSoul(player1, record1, soul2, swapInventory, swapHearts, swapEffects, swapClass);
		} else {
			applySoul(player1, record1, soul2, swapClass, swapEffects, swapHearts, swapInventory, swapName, swapSkin);
		}

		if (soul1.ownerUUID.equals(player2.getUUID())) {
			restoreToOriginalSoul(player2, record2, soul1, swapInventory, swapHearts, swapEffects, swapClass);
		} else {
			applySoul(player2, record2, soul1, swapClass, swapEffects, swapHearts, swapInventory, swapName, swapSkin);
		}
	}

	private static void applySoul(
		ServerPlayer player,
		SwapRecord record,
		SoulIdentity incomingSoul,
		boolean swapClass,
		boolean swapEffects,
		boolean swapHearts,
		boolean swapInventory,
		boolean swapName,
		boolean swapSkin
	) {
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);

		record.partnerUUID = incomingSoul.ownerUUID;
		record.swapClass = swapClass;
		record.swapEffects = swapEffects;
		record.swapHearts = swapHearts;
		record.swapInventory = swapInventory;
		record.swapName = swapName;
		record.swapSkin = swapSkin;

		if (swapClass) {
			vars.classDungeon = incomingSoul.classDungeon;
			vars.classColor = incomingSoul.classColor;
			vars.markSyncDirty();
		}

		if (swapHearts) {
			record.partnerMaxHealth = incomingSoul.maxHealth;
			player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(incomingSoul.maxHealth);
			player.setHealth(Math.min(incomingSoul.health, (float) incomingSoul.maxHealth));
			player.setAbsorptionAmount(incomingSoul.absorption);
		}

		if (swapEffects) {
			player.removeAllEffects();
			for (MobEffectInstance inst : incomingSoul.effects) {
				player.addEffect(new MobEffectInstance(inst));
			}
		}

		if (swapInventory) {
			applySoulInventory(player, incomingSoul);
		}

		if (swapName) {
			record.swappedName = incomingSoul.name;
			record.swappedColor = incomingSoul.color;
			vars.color = incomingSoul.color;
			vars.showCustomNameColor = true;
			vars.markSyncDirty();
		} else {
			record.swappedName = null;
			record.swappedColor = null;
		}

		if (swapSkin) {
			record.swappedSkinUUID = incomingSoul.skinUUID;
			record.swappedSkinName = incomingSoul.skinName;
		} else {
			record.swappedSkinUUID = null;
			record.swappedSkinName = null;
		}

		ACTIVE_SWAPS.put(player.getUUID(), record);

		if (swapName) {
			player.refreshDisplayName();
			player.refreshTabListName();
			TextColor parsed = TextColor.parseColor(incomingSoul.color).result().orElse(null);
			Style st = (parsed != null) ? Style.EMPTY.withColor(parsed) : Style.EMPTY;
			player.setCustomName(Component.literal(incomingSoul.name).setStyle(st));
			player.setCustomNameVisible(true);
			net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player));
		}

		PacketDistributor.sendToAllPlayers(new AccountSwapSyncMessage(
			player.getUUID(),
			record.swappedName,
			record.swappedColor,
			record.swappedSkinUUID,
			record.swappedSkinName,
			false
		));
	}

	private static void restoreToOriginalSoul(
		ServerPlayer player,
		SwapRecord record,
		SoulIdentity incomingSoul,
		boolean swapInventory,
		boolean swapHearts,
		boolean swapEffects,
		boolean swapClass
	) {
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);

		vars.color = record.originalColor;
		vars.showCustomNameColor = record.originalShowCustomNameColor;
		if (swapClass) {
			vars.classDungeon = record.originalClassDungeon;
			vars.classColor = record.originalClassColor;
		}
		vars.markSyncDirty();

		if (swapHearts) {
			player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(record.originalMaxHealth);
			player.setHealth(Math.min(incomingSoul.health, (float) record.originalMaxHealth));
			player.setAbsorptionAmount(incomingSoul.absorption);
		}

		if (swapEffects) {
			player.removeAllEffects();
			for (MobEffectInstance inst : incomingSoul.effects) {
				player.addEffect(new MobEffectInstance(inst));
			}
		}

		if (swapInventory) {
			applySoulInventory(player, incomingSoul);
		}

		record.swappedName = null;
		record.swappedColor = null;
		record.swappedSkinUUID = null;
		record.swappedSkinName = null;

		ACTIVE_SWAPS.remove(player.getUUID());

		player.refreshDisplayName();
		player.refreshTabListName();
		TextColor parsed = TextColor.parseColor(record.originalColor).result().orElse(null);
		Style st = (parsed != null) ? Style.EMPTY.withColor(parsed) : Style.EMPTY;
		player.setCustomName(Component.literal(record.originalName).setStyle(st));
		net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player));

		PacketDistributor.sendToAllPlayers(new AccountSwapSyncMessage(
			player.getUUID(),
			"",
			"",
			null,
			"",
			true
		));
	}

	private static void applySoulInventory(ServerPlayer player, SoulIdentity soul) {
		Inventory inv = player.getInventory();
		int size = Math.min(soul.items.size(), inv.getContainerSize());
		for (int i = 0; i < size; i++) {
			inv.setItem(i, soul.items.get(i).copy());
		}
		if (soul.selectedSlot >= 0 && soul.selectedSlot < 9) {
			inv.setSelectedSlot(soul.selectedSlot);
		}
		inv.setChanged();
		player.containerMenu.broadcastChanges();
		player.inventoryMenu.broadcastChanges();
	}

	public static void swapWithDummy(
		LevelAccessor world,
		ServerPlayer player,
		String dummyName,
		boolean swapClass,
		boolean swapEffects,
		boolean swapHearts,
		boolean swapInventory,
		boolean swapName,
		boolean swapSkin
	) {
		if (world.isClientSide()) {
			return;
		}
		if (player == null || dummyName == null || dummyName.trim().isEmpty()) {
			return;
		}
		String name = dummyName.trim();
		UUID dummyUUID;
		if ("alex".equalsIgnoreCase(name)) {
			dummyUUID = UUID.fromString("c06f8906-4c8a-4911-9c29-ea1dbd1aab82");
		} else if ("steve".equalsIgnoreCase(name)) {
			dummyUUID = UUID.fromString("853c80ef-3c37-49fd-aa49-938b674adae6");
		} else {
			dummyUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(java.nio.charset.StandardCharsets.UTF_8));
		}

		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		SwapRecord record = ACTIVE_SWAPS.get(player.getUUID());
		if (record == null) {
			record = new SwapRecord(
				player.getUUID(),
				player.getGameProfile().name(),
				player.getUUID(),
				player.getGameProfile().name(),
				vars.classDungeon,
				vars.classColor,
				resolvePlayerColor(player),
				vars.showCustomNameColor,
				player.getAttribute(Attributes.MAX_HEALTH).getBaseValue(),
				player.getHealth(),
				player.getAbsorptionAmount(),
				copyInventory(player.getInventory()),
				player.getInventory().getSelectedSlot(),
				copyEffects(player.getActiveEffects())
			);
		}

		record.partnerUUID = dummyUUID;
		record.swapClass = swapClass;
		record.swapEffects = swapEffects;
		record.swapHearts = swapHearts;
		record.swapInventory = swapInventory;
		record.swapName = swapName;
		record.swapSkin = swapSkin;

		if (swapClass) {
			String currentClass = vars.classDungeon == null ? "" : vars.classDungeon.toLowerCase();
			if ("mage".equals(currentClass)) {
				vars.classDungeon = "warrior";
				vars.classColor = "0xFFFF001F";
			} else {
				vars.classDungeon = "mage";
				vars.classColor = "0xFFFF7BFE";
			}
			vars.markSyncDirty();
		}

		if (swapHearts) {
			double testMaxHp = 30.0;
			record.partnerMaxHealth = testMaxHp;
			player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(testMaxHp);
			player.setHealth((float) testMaxHp);
			player.setAbsorptionAmount(4.0f);
		}

		if (swapEffects) {
			player.removeAllEffects();
			player.addEffect(new MobEffectInstance(net.minecraft.world.effect.MobEffects.SPEED, 2400, 1));
			player.addEffect(new MobEffectInstance(net.minecraft.world.effect.MobEffects.GLOWING, 2400, 0));
		}

		if (swapInventory) {
			player.getInventory().clearContent();
			player.getInventory().setItem(0, new ItemStack(net.minecraft.world.item.Items.DIAMOND_SWORD));
			player.getInventory().setItem(1, new ItemStack(net.minecraft.world.item.Items.BOW));
			player.getInventory().setItem(2, new ItemStack(net.minecraft.world.item.Items.GOLDEN_APPLE, 16));
			player.getInventory().setItem(8, new ItemStack(net.minecraft.world.item.Items.ARROW, 64));
			player.getInventory().setItem(38, new ItemStack(net.minecraft.world.item.Items.DIAMOND_CHESTPLATE));
			player.getInventory().setSelectedSlot(0);
			player.getInventory().setChanged();
			player.containerMenu.broadcastChanges();
			player.inventoryMenu.broadcastChanges();
		}

		if (swapName) {
			record.swappedName = name;
			record.swappedColor = "#FFAA00";
			vars.color = "#FFAA00";
			vars.showCustomNameColor = true;
			vars.markSyncDirty();
		} else {
			record.swappedName = null;
			record.swappedColor = null;
		}

		if (swapSkin) {
			record.swappedSkinUUID = dummyUUID;
			record.swappedSkinName = name;
		} else {
			record.swappedSkinUUID = null;
			record.swappedSkinName = null;
		}

		ACTIVE_SWAPS.put(player.getUUID(), record);

		if (swapName) {
			player.refreshDisplayName();
			player.refreshTabListName();
			net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player));
		}

		PacketDistributor.sendToAllPlayers(new AccountSwapSyncMessage(
			player.getUUID(),
			record.swappedName,
			record.swappedColor,
			record.swappedSkinUUID,
			record.swappedSkinName,
			false
		));
	}

	public static void resetIdentity(LevelAccessor world, Entity entity, boolean resetInventory) {
		if (world.isClientSide()) {
			return;
		}
		if (entity instanceof ServerPlayer player) {
			resetPlayerAndPartner(player, resetInventory);
		} else if (entity == null) {
			resetAll(resetInventory);
		}
	}

	private static void resetPlayerAndPartner(ServerPlayer player, boolean resetInventory) {
		SwapRecord record = ACTIVE_SWAPS.get(player.getUUID());
		if (record == null) {
			return;
		}
		ServerPlayer partner = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(record.partnerUUID);
		resetSinglePlayer(player, record, resetInventory);
		if (partner != null) {
			SwapRecord partnerRecord = ACTIVE_SWAPS.get(partner.getUUID());
			if (partnerRecord != null) {
				resetSinglePlayer(partner, partnerRecord, resetInventory);
			}
		}
	}

	private static void resetAll(boolean resetInventory) {
		for (SwapRecord record : new ArrayList<>(ACTIVE_SWAPS.values())) {
			ServerPlayer player = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(record.originalUUID);
			if (player != null) {
				resetSinglePlayer(player, record, resetInventory);
			} else {
				ACTIVE_SWAPS.remove(record.originalUUID);
				PacketDistributor.sendToAllPlayers(new AccountSwapSyncMessage(
					record.originalUUID,
					"",
					"",
					null,
					"",
					true
				));
			}
		}
	}

	private static void resetSinglePlayer(ServerPlayer player, SwapRecord record, boolean resetInventory) {
		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);

		if (record.swapName) {
			vars.color = record.originalColor;
			vars.showCustomNameColor = record.originalShowCustomNameColor;
			vars.markSyncDirty();
			record.swappedName = null;
			record.swappedColor = null;
			player.refreshDisplayName();
			player.refreshTabListName();
			TextColor parsed = TextColor.parseColor(record.originalColor).result().orElse(null);
			Style st = (parsed != null) ? Style.EMPTY.withColor(parsed) : Style.EMPTY;
			player.setCustomName(Component.literal(record.originalName).setStyle(st));
			net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player));
		}

		if (record.swapClass) {
			vars.classDungeon = record.originalClassDungeon;
			vars.classColor = record.originalClassColor;
			vars.markSyncDirty();
		}

		if (record.swapHearts) {
			player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(record.originalMaxHealth);
			player.setHealth(Math.min(record.originalHealth, (float) record.originalMaxHealth));
			player.setAbsorptionAmount(record.originalAbsorption);
		}

		if (record.swapEffects) {
			player.removeAllEffects();
			for (MobEffectInstance inst : record.originalEffects) {
				player.addEffect(new MobEffectInstance(inst));
			}
		}

		if (resetInventory && record.swapInventory) {
			restoreInventory(player, record);
		}

		ACTIVE_SWAPS.remove(player.getUUID());

		PacketDistributor.sendToAllPlayers(new AccountSwapSyncMessage(
			player.getUUID(),
			"",
			"",
			null,
			"",
			true
		));
	}

	private static void swapInventories(ServerPlayer p1, ServerPlayer p2) {
		Inventory inv1 = p1.getInventory();
		Inventory inv2 = p2.getInventory();
		int size = Math.min(inv1.getContainerSize(), inv2.getContainerSize());

		for (int i = 0; i < size; i++) {
			ItemStack temp = inv1.getItem(i).copy();
			inv1.setItem(i, inv2.getItem(i).copy());
			inv2.setItem(i, temp);
		}
		int tempSel = inv1.getSelectedSlot();
		inv1.setSelectedSlot(inv2.getSelectedSlot());
		inv2.setSelectedSlot(tempSel);

		inv1.setChanged();
		inv2.setChanged();
		p1.containerMenu.broadcastChanges();
		p2.containerMenu.broadcastChanges();
		p1.inventoryMenu.broadcastChanges();
		p2.inventoryMenu.broadcastChanges();
	}

	private static void restoreInventory(ServerPlayer player, SwapRecord record) {
		Inventory inv = player.getInventory();
		int size = Math.min(record.originalItems.size(), inv.getContainerSize());
		for (int i = 0; i < size; i++) {
			inv.setItem(i, record.originalItems.get(i).copy());
		}
		if (record.originalSelectedSlot >= 0 && record.originalSelectedSlot < 9) {
			inv.setSelectedSlot(record.originalSelectedSlot);
		}
		inv.setChanged();
		player.containerMenu.broadcastChanges();
		player.inventoryMenu.broadcastChanges();
	}

	private static List<ItemStack> copyInventory(Inventory inv) {
		List<ItemStack> copy = new ArrayList<>(inv.getContainerSize());
		for (int i = 0; i < inv.getContainerSize(); i++) {
			copy.add(inv.getItem(i).copy());
		}
		return copy;
	}

	private static List<MobEffectInstance> copyEffects(Collection<MobEffectInstance> effects) {
		List<MobEffectInstance> list = new ArrayList<>(effects.size());
		for (MobEffectInstance inst : effects) {
			list.add(new MobEffectInstance(inst));
		}
		return list;
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (SwapRecord record : ACTIVE_SWAPS.values()) {
				PacketDistributor.sendToPlayer(player, new AccountSwapSyncMessage(
					record.originalUUID,
					record.swappedName,
					record.swappedColor,
					record.swappedSkinUUID,
					record.swappedSkinName,
					false
				));
			}
			SwapRecord ownRecord = ACTIVE_SWAPS.get(player.getUUID());
			if (ownRecord != null) {
				player.refreshDisplayName();
				player.refreshTabListName();
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			SwapRecord record = ACTIVE_SWAPS.get(player.getUUID());
			if (record != null) {
				if (record.swapHearts) {
					player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(record.partnerMaxHealth);
				}
				player.refreshDisplayName();
				player.refreshTabListName();
				PacketDistributor.sendToAllPlayers(new AccountSwapSyncMessage(
					record.originalUUID,
					record.swappedName,
					record.swappedColor,
					record.swappedSkinUUID,
					record.swappedSkinName,
					false
				));
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (SwapRecord record : ACTIVE_SWAPS.values()) {
				PacketDistributor.sendToPlayer(player, new AccountSwapSyncMessage(
					record.originalUUID,
					record.swappedName,
					record.swappedColor,
					record.swappedSkinUUID,
					record.swappedSkinName,
					false
				));
			}
		}
	}
}
