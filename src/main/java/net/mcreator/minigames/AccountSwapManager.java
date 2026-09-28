package net.mcreator.minigames;

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
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import net.mcreator.minigames.network.AccountSwapSyncMessage;
import net.mcreator.minigames.network.MinigamesModVariables;

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

	private static final Map<UUID, SwapRecord> ACTIVE_SWAPS = new ConcurrentHashMap<>();
	private static final Map<UUID, String> CLIENT_SWAPPED_NAMES = new ConcurrentHashMap<>();

	public static void handleClientSync(UUID playerUUID, String swappedName, UUID skinOwnerUUID, String skinOwnerName, boolean isReset) {
		if (isReset) {
			CLIENT_SWAPPED_NAMES.remove(playerUUID);
		} else {
			if (swappedName != null && !swappedName.isEmpty()) {
				CLIENT_SWAPPED_NAMES.put(playerUUID, swappedName);
			} else {
				CLIENT_SWAPPED_NAMES.remove(playerUUID);
			}
		}
		if (FMLEnvironment.getDist().isClient()) {
			net.mcreator.minigames.client.AccountSwapClient.handleSync(playerUUID, swappedName, skinOwnerUUID, skinOwnerName, isReset);
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

	public static SwapRecord getRecord(UUID playerUUID) {
		if (playerUUID == null) {
			return null;
		}
		return ACTIVE_SWAPS.get(playerUUID);
	}

	public static void clearClientSwaps() {
		CLIENT_SWAPPED_NAMES.clear();
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
				vars1.color,
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
				vars2.color,
				vars2.showCustomNameColor,
				player2.getAttribute(Attributes.MAX_HEALTH).getBaseValue(),
				player2.getHealth(),
				player2.getAbsorptionAmount(),
				copyInventory(player2.getInventory()),
				player2.getInventory().getSelectedSlot(),
				copyEffects(player2.getActiveEffects())
			);
		}

		record1.partnerUUID = player2.getUUID();
		record2.partnerUUID = player1.getUUID();

		record1.swapClass = swapClass;
		record2.swapClass = swapClass;
		record1.swapEffects = swapEffects;
		record2.swapEffects = swapEffects;
		record1.swapHearts = swapHearts;
		record2.swapHearts = swapHearts;
		record1.swapInventory = swapInventory;
		record2.swapInventory = swapInventory;
		record1.swapName = swapName;
		record2.swapName = swapName;
		record1.swapSkin = swapSkin;
		record2.swapSkin = swapSkin;

		if (swapClass) {
			String tempClass = vars1.classDungeon;
			String tempClassColor = vars1.classColor;
			vars1.classDungeon = vars2.classDungeon;
			vars1.classColor = vars2.classColor;
			vars2.classDungeon = tempClass;
			vars2.classColor = tempClassColor;
			vars1.markSyncDirty();
			vars2.markSyncDirty();
		}

		if (swapHearts) {
			double maxHp1 = player1.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
			double maxHp2 = player2.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
			float curHp1 = player1.getHealth();
			float curHp2 = player2.getHealth();
			float abs1 = player1.getAbsorptionAmount();
			float abs2 = player2.getAbsorptionAmount();

			record1.partnerMaxHealth = maxHp2;
			record2.partnerMaxHealth = maxHp1;

			player1.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHp2);
			player2.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHp1);

			player1.setHealth(Math.min(curHp2, (float) maxHp2));
			player2.setHealth(Math.min(curHp1, (float) maxHp1));

			player1.setAbsorptionAmount(abs2);
			player2.setAbsorptionAmount(abs1);
		}

		if (swapEffects) {
			List<MobEffectInstance> eff1 = new ArrayList<>(player1.getActiveEffects());
			List<MobEffectInstance> eff2 = new ArrayList<>(player2.getActiveEffects());
			player1.removeAllEffects();
			player2.removeAllEffects();
			for (MobEffectInstance inst : eff2) {
				player1.addEffect(new MobEffectInstance(inst));
			}
			for (MobEffectInstance inst : eff1) {
				player2.addEffect(new MobEffectInstance(inst));
			}
		}

		if (swapInventory) {
			swapInventories(player1, player2);
		}

		if (swapName) {
			record1.swappedName = player2.getGameProfile().name();
			record2.swappedName = player1.getGameProfile().name();

			String tempColor = vars1.color;
			boolean tempShowColor = vars1.showCustomNameColor;
			vars1.color = vars2.color;
			vars1.showCustomNameColor = vars2.showCustomNameColor;
			vars2.color = tempColor;
			vars2.showCustomNameColor = tempShowColor;
			vars1.markSyncDirty();
			vars2.markSyncDirty();
		} else {
			record1.swappedName = null;
			record2.swappedName = null;
		}

		if (swapSkin) {
			record1.swappedSkinUUID = player2.getUUID();
			record1.swappedSkinName = player2.getGameProfile().name();
			record2.swappedSkinUUID = player1.getUUID();
			record2.swappedSkinName = player1.getGameProfile().name();
		} else {
			record1.swappedSkinUUID = null;
			record1.swappedSkinName = null;
			record2.swappedSkinUUID = null;
			record2.swappedSkinName = null;
		}

		ACTIVE_SWAPS.put(player1.getUUID(), record1);
		ACTIVE_SWAPS.put(player2.getUUID(), record2);

		if (swapName) {
			player1.refreshDisplayName();
			player2.refreshDisplayName();
			player1.refreshTabListName();
			player2.refreshTabListName();
			net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player1));
			net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player2));
		}

		PacketDistributor.sendToAllPlayers(new AccountSwapSyncMessage(
			player1.getUUID(),
			record1.swappedName,
			record1.swappedSkinUUID,
			record1.swappedSkinName,
			false
		));
		PacketDistributor.sendToAllPlayers(new AccountSwapSyncMessage(
			player2.getUUID(),
			record2.swappedName,
			record2.swappedSkinUUID,
			record2.swappedSkinName,
			false
		));
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
				vars.color,
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
			vars.color = "#FFAA00";
			vars.showCustomNameColor = true;
			vars.markSyncDirty();
		} else {
			record.swappedName = null;
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
			player.refreshDisplayName();
			player.refreshTabListName();
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
					record.swappedSkinUUID,
					record.swappedSkinName,
					false
				));
			}
		}
	}
}
