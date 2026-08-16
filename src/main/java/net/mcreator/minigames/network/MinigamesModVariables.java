package net.mcreator.minigames.network;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.ProblemReporter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;

import net.mcreator.minigames.MinigamesMod;

import java.util.function.Supplier;
import java.util.ArrayList;

@EventBusSubscriber
public class MinigamesModVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MinigamesMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("player_variables", () -> AttachmentType.serializable(() -> new PlayerVariables()).build());
	public static double health = 20.0;
	public static Identifier crown = null;
	public static Entity firstSpleef = null;
	public static Entity secondSpleef = null;
	public static Entity thirdSpleef = null;
	public static Entity VotingEntity = null;

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(SavedDataSyncMessage.TYPE, SavedDataSyncMessage.STREAM_CODEC, SavedDataSyncMessage::handleData);
		MinigamesMod.addNetworkMessage(PlayerVariablesSyncMessage.TYPE, PlayerVariablesSyncMessage.STREAM_CODEC, PlayerVariablesSyncMessage::handleData);
	}

	@SubscribeEvent
	public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (Entity entityiterator : player.level().players())
				if (entityiterator != player && entityiterator instanceof ServerPlayer playeriterator)
					PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(playeriterator.getData(PLAYER_VARIABLES), playeriterator.getId()));
			PacketDistributor.sendToPlayersInDimension(player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (Entity entityiterator : player.level().players())
				if (entityiterator != player && entityiterator instanceof ServerPlayer playeriterator)
					PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(playeriterator.getData(PLAYER_VARIABLES), playeriterator.getId()));
			PacketDistributor.sendToPlayersInDimension(player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (Entity entityiterator : player.level().players())
				if (entityiterator != player && entityiterator instanceof ServerPlayer playeriterator)
					PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(playeriterator.getData(PLAYER_VARIABLES), playeriterator.getId()));
			PacketDistributor.sendToPlayersInDimension(player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
		}
	}

	@SubscribeEvent
	public static void onPlayerTickUpdateSyncPlayerVariables(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player && player.getData(PLAYER_VARIABLES)._syncDirty) {
			PacketDistributor.sendToPlayersInDimension(player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
			player.getData(PLAYER_VARIABLES)._syncDirty = false;
		}
	}

	@SubscribeEvent
	public static void clonePlayer(PlayerEvent.Clone event) {
		PlayerVariables original = event.getOriginal().getData(PLAYER_VARIABLES);
		PlayerVariables clone = new PlayerVariables();
		clone.team = original.team;
		clone.joinFirstTime = original.joinFirstTime;
		clone.isCrowned = original.isCrowned;
		clone.color = original.color;
		clone.helmet = original.helmet;
		clone.snowballCountSpleef = original.snowballCountSpleef;
		clone.thrusterDirection = original.thrusterDirection;
		clone.thrusterTicks = original.thrusterTicks;
		clone.classDungeon = original.classDungeon;
		clone.playerSlots = original.playerSlots;
		clone.playerInInventory = original.playerInInventory;
		clone.showOnlyHearts = original.showOnlyHearts;
		clone.backpackSlots = original.backpackSlots;
		clone.jumps = original.jumps;
		clone.canDash = original.canDash;
		clone.dashCooldown = original.dashCooldown;
		clone.maxDashCooldown = original.maxDashCooldown;
		clone.dashLength = original.dashLength;
		clone.showCustomNameColor = original.showCustomNameColor;
		clone.votedYes = original.votedYes;
		clone.voted = original.voted;
		clone.voteCooldown = original.voteCooldown;
		clone.healCD = original.healCD;
		clone.PassiveHealCooldown = original.PassiveHealCooldown;
		clone.PassiveHealAmount = original.PassiveHealAmount;
		clone.gravity = original.gravity;
		clone.ascendingTimer = original.ascendingTimer;
		clone.removeEffectsSingleTarget = original.removeEffectsSingleTarget;
		clone.ascendingActive = original.ascendingActive;
		clone.advancedGlowingColor = original.advancedGlowingColor;
		clone.tooltipSize = original.tooltipSize;
		clone.performKnockback = original.performKnockback;
		clone.openBattleBox = original.openBattleBox;
		clone.selectedButtonBattleBox = original.selectedButtonBattleBox;
		clone.AchievementLobbyState = original.AchievementLobbyState;
		clone.TimerColor = original.TimerColor;
		clone.timerSeconds = original.timerSeconds;
		clone.timerMinutes = original.timerMinutes;
		clone.timerHours = original.timerHours;
		clone.timerScale = original.timerScale;
		clone.timerTick = original.timerTick;
		clone.timerSpeed = original.timerSpeed;
		if (!event.isWasDeath()) {
		}
		event.getEntity().setData(PLAYER_VARIABLES, clone);
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			SavedData mapdata = MapVariables.get(event.getEntity().level());
			SavedData worlddata = WorldVariables.get(event.getEntity().level());
			if (mapdata != null)
				PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(0, mapdata));
			if (worlddata != null)
				PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(1, worlddata));
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			SavedData worlddata = WorldVariables.get(event.getEntity().level());
			if (worlddata != null)
				PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(1, worlddata));
		}
	}

	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		if (event.getLevel() instanceof ServerLevel level) {
			WorldVariables worldVariables = WorldVariables.get(level);
			if (worldVariables._syncDirty) {
				PacketDistributor.sendToPlayersInDimension(level, new SavedDataSyncMessage(1, worldVariables));
				worldVariables._syncDirty = false;
			}
			MapVariables mapVariables = MapVariables.get(level);
			if (mapVariables._syncDirty) {
				PacketDistributor.sendToAllPlayers(new SavedDataSyncMessage(0, mapVariables));
				mapVariables._syncDirty = false;
			}
		}
	}

	public static class WorldVariables extends SavedData {
		public static final SavedDataType<WorldVariables> TYPE = new SavedDataType<>(Identifier.parse("minigames:worldvars"), level -> new WorldVariables(), level -> CompoundTag.CODEC.xmap(tag -> {
			WorldVariables instance = new WorldVariables();
			instance.read(tag, level.registryAccess());
			return instance;
		}, instance -> instance.save(new CompoundTag(), level.registryAccess())));
		boolean _syncDirty = false;

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
		}

		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			this._syncDirty = true;
		}

		static WorldVariables clientSide = new WorldVariables();

		public static WorldVariables get(LevelAccessor world) {
			if (world instanceof ServerLevel level) {
				return level.getDataStorage().computeIfAbsent(WorldVariables.TYPE);
			} else {
				return clientSide;
			}
		}
	}

	public static class MapVariables extends SavedData {
		public static final SavedDataType<MapVariables> TYPE = new SavedDataType<>(Identifier.parse("minigames:mapvars"), level -> new MapVariables(), level -> CompoundTag.CODEC.xmap(tag -> {
			MapVariables instance = new MapVariables();
			instance.read(tag, level.registryAccess());
			return instance;
		}, instance -> instance.save(new CompoundTag(), level.registryAccess())));
		boolean _syncDirty = false;
		public boolean nightVision = true;
		public double gameTick = 0;
		public double gameSeconds = 0;
		public double gameMinutes = 0;
		public double gameHours = 0;
		public double pvpstate = -1.0;
		public boolean pvpAnimationStart = false;
		public double pvpAnimationTick = 0;
		public double respawningPlayers = 0;
		public boolean achievementHunterMode = false;
		public boolean randomHunterAchievement = false;
		public boolean animateHunter = false;
		public double overwoldHuntedX = 0;
		public double overworldHuntedZ = 0;
		public double netherHuntedX = 0;
		public double netherHuntedZ = 0;
		public boolean minimap = true;
		public boolean waypoints = true;
		public boolean CrownHuntInGame = false;
		public boolean crownHuntWinDisplay = false;
		public boolean canGrabCrown = false;
		public boolean returnToCastle = false;
		public double crownMinutes = 3.0;
		public double graceMinutes = 6.0;
		public boolean applyCustomNameColor = false;
		public boolean inGracePeriod = false;
		public double winAnimationState = 0;
		public boolean playingSpleef = false;
		public double layersRemainingSpleef = 0;
		public double layerCountdownSpleef = 0;
		public double spleefAlivePlayers = 0;
		public double gapBetweenLayersSpleef = 0;
		public boolean spleefPowerups = true;
		public double mapsSpleef = 15.0;
		public String sky = "normal";
		public String currentMapSpleef = "\"\"";
		public Vec3 spleefMapMiddleX = Vec3.ZERO;
		public double layerConquestCooldownSpleef = 0;
		public double passiveSnowballsSpleef = 0.05;
		public Vec3 dungeonSize = Vec3.ZERO;
		public double lootRoomsDungeon = 0;
		public double lootRoomPlacedDungeon = 0;
		public double minibossRoomsDungeon = 0;
		public double minibossRoomPlacedDungeon = 0;
		public double dungeonSpawn = 0;
		public double dungeonBoss = 0;
		public Vec3 roomLimitDungeon = Vec3.ZERO;
		public double secretRoomDungeon = 0.0;
		public double secretRoomPlacedDungeon = 0.0;
		public double dungeonCoins = 0;
		public double dungeonFloor = 0;
		public Vec3 dungeonRoomSize = Vec3.ZERO;
		public Vec3 dungeonStartLocation = Vec3.ZERO;
		public boolean playingDungeons = false;
		public boolean showCoins = false;
		public boolean playingAchievement = false;
		public String VotingMessage = "\"\"";
		public double voteType = 0;
		public boolean ActiveVote = false;
		public String winnerUUID = "\"\"";
		public boolean inCombat = false;
		public double currentRoomID = 0;
		public Vec3 DoorPosition = Vec3.ZERO;
		public Vec3 DoorOffset = Vec3.ZERO;
		public double aliveEnemies = 0;
		public double startingEnemies = 0;
		public double roomCheckDelayTicks = 0.0;
		public boolean removeEffects = false;
		public boolean ascendingGlobalActive = false;
		public String floorTypeDungeon = "\"\"";
		public String VotingPlayerUUID = "\"\"";
		public String VotingPlayerName = "";
		public String battleBoxStatus = "\"\"";
		public double Achievement = 0;
		public double AchievementCategory = 0;
		public double AchievementModifier = 0;
		public ItemStack AchievementIcon = ItemStack.EMPTY;
		public String AchievementTitle = "\"\"";
		public String AchievementDescription = "";
		public boolean showWinscreen = false;
		public ArrayList<Object> WinnerList = new ArrayList<>();
		public boolean ShowTimer = false;
		public double WhenPVPActive = 300.0;
		public String hunterAchievementUUID = "\"\"";
		public double hunterAnimation = 0;
		public ArrayList<Object> glueY = new ArrayList<>();
		public Vec3 coordinateOffset = Vec3.ZERO;
		public double bossNumber = 0;
		public String bossName = "\"\"";

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nightVision = nbt.getBooleanOr("nightVision", false);
			gameTick = nbt.getDoubleOr("gameTick", 0);
			gameSeconds = nbt.getDoubleOr("gameSeconds", 0);
			gameMinutes = nbt.getDoubleOr("gameMinutes", 0);
			gameHours = nbt.getDoubleOr("gameHours", 0);
			pvpstate = nbt.getDoubleOr("pvpstate", 0);
			pvpAnimationStart = nbt.getBooleanOr("pvpAnimationStart", false);
			pvpAnimationTick = nbt.getDoubleOr("pvpAnimationTick", 0);
			respawningPlayers = nbt.getDoubleOr("respawningPlayers", 0);
			achievementHunterMode = nbt.getBooleanOr("achievementHunterMode", false);
			randomHunterAchievement = nbt.getBooleanOr("randomHunterAchievement", false);
			animateHunter = nbt.getBooleanOr("animateHunter", false);
			overwoldHuntedX = nbt.getDoubleOr("overwoldHuntedX", 0);
			overworldHuntedZ = nbt.getDoubleOr("overworldHuntedZ", 0);
			netherHuntedX = nbt.getDoubleOr("netherHuntedX", 0);
			netherHuntedZ = nbt.getDoubleOr("netherHuntedZ", 0);
			minimap = nbt.getBooleanOr("minimap", false);
			waypoints = nbt.getBooleanOr("waypoints", false);
			CrownHuntInGame = nbt.getBooleanOr("CrownHuntInGame", false);
			crownHuntWinDisplay = nbt.getBooleanOr("crownHuntWinDisplay", false);
			canGrabCrown = nbt.getBooleanOr("canGrabCrown", false);
			returnToCastle = nbt.getBooleanOr("returnToCastle", false);
			crownMinutes = nbt.getDoubleOr("crownMinutes", 0);
			graceMinutes = nbt.getDoubleOr("graceMinutes", 0);
			applyCustomNameColor = nbt.getBooleanOr("applyCustomNameColor", false);
			inGracePeriod = nbt.getBooleanOr("inGracePeriod", false);
			winAnimationState = nbt.getDoubleOr("winAnimationState", 0);
			playingSpleef = nbt.getBooleanOr("playingSpleef", false);
			layersRemainingSpleef = nbt.getDoubleOr("layersRemainingSpleef", 0);
			layerCountdownSpleef = nbt.getDoubleOr("layerCountdownSpleef", 0);
			spleefAlivePlayers = nbt.getDoubleOr("spleefAlivePlayers", 0);
			gapBetweenLayersSpleef = nbt.getDoubleOr("gapBetweenLayersSpleef", 0);
			spleefPowerups = nbt.getBooleanOr("spleefPowerups", false);
			mapsSpleef = nbt.getDoubleOr("mapsSpleef", 0);
			sky = nbt.getStringOr("sky", "");
			currentMapSpleef = nbt.getStringOr("currentMapSpleef", "");
			spleefMapMiddleX = Vec3.CODEC.parse(NbtOps.INSTANCE, nbt.get("spleefMapMiddleX")).result().orElse(Vec3.ZERO);
			layerConquestCooldownSpleef = nbt.getDoubleOr("layerConquestCooldownSpleef", 0);
			passiveSnowballsSpleef = nbt.getDoubleOr("passiveSnowballsSpleef", 0);
			dungeonSize = Vec3.CODEC.parse(NbtOps.INSTANCE, nbt.get("dungeonSize")).result().orElse(Vec3.ZERO);
			lootRoomsDungeon = nbt.getDoubleOr("lootRoomsDungeon", 0);
			lootRoomPlacedDungeon = nbt.getDoubleOr("lootRoomPlacedDungeon", 0);
			minibossRoomsDungeon = nbt.getDoubleOr("minibossRoomsDungeon", 0);
			minibossRoomPlacedDungeon = nbt.getDoubleOr("minibossRoomPlacedDungeon", 0);
			dungeonSpawn = nbt.getDoubleOr("dungeonSpawn", 0);
			dungeonBoss = nbt.getDoubleOr("dungeonBoss", 0);
			roomLimitDungeon = Vec3.CODEC.parse(NbtOps.INSTANCE, nbt.get("roomLimitDungeon")).result().orElse(Vec3.ZERO);
			secretRoomDungeon = nbt.getDoubleOr("secretRoomDungeon", 0);
			secretRoomPlacedDungeon = nbt.getDoubleOr("secretRoomPlacedDungeon", 0);
			dungeonCoins = nbt.getDoubleOr("dungeonCoins", 0);
			dungeonFloor = nbt.getDoubleOr("dungeonFloor", 0);
			dungeonRoomSize = Vec3.CODEC.parse(NbtOps.INSTANCE, nbt.get("dungeonRoomSize")).result().orElse(Vec3.ZERO);
			dungeonStartLocation = Vec3.CODEC.parse(NbtOps.INSTANCE, nbt.get("dungeonStartLocation")).result().orElse(Vec3.ZERO);
			playingDungeons = nbt.getBooleanOr("playingDungeons", false);
			showCoins = nbt.getBooleanOr("showCoins", false);
			playingAchievement = nbt.getBooleanOr("playingAchievement", false);
			VotingMessage = nbt.getStringOr("VotingMessage", "");
			voteType = nbt.getDoubleOr("voteType", 0);
			ActiveVote = nbt.getBooleanOr("ActiveVote", false);
			winnerUUID = nbt.getStringOr("winnerUUID", "");
			inCombat = nbt.getBooleanOr("inCombat", false);
			currentRoomID = nbt.getDoubleOr("currentRoomID", 0);
			DoorPosition = Vec3.CODEC.parse(NbtOps.INSTANCE, nbt.get("DoorPosition")).result().orElse(Vec3.ZERO);
			DoorOffset = Vec3.CODEC.parse(NbtOps.INSTANCE, nbt.get("DoorOffset")).result().orElse(Vec3.ZERO);
			aliveEnemies = nbt.getDoubleOr("aliveEnemies", 0);
			startingEnemies = nbt.getDoubleOr("startingEnemies", 0);
			roomCheckDelayTicks = nbt.getDoubleOr("roomCheckDelayTicks", 0);
			removeEffects = nbt.getBooleanOr("removeEffects", false);
			ascendingGlobalActive = nbt.getBooleanOr("ascendingGlobalActive", false);
			floorTypeDungeon = nbt.getStringOr("floorTypeDungeon", "");
			VotingPlayerUUID = nbt.getStringOr("VotingPlayerUUID", "");
			VotingPlayerName = nbt.getStringOr("VotingPlayerName", "");
			battleBoxStatus = nbt.getStringOr("battleBoxStatus", "");
			Achievement = nbt.getDoubleOr("Achievement", 0);
			AchievementCategory = nbt.getDoubleOr("AchievementCategory", 0);
			AchievementModifier = nbt.getDoubleOr("AchievementModifier", 0);
			AchievementIcon = ItemStack.OPTIONAL_CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), nbt.getCompoundOrEmpty("AchievementIcon")).result().orElse(ItemStack.EMPTY);
			AchievementTitle = nbt.getStringOr("AchievementTitle", "");
			AchievementDescription = nbt.getStringOr("AchievementDescription", "");
			showWinscreen = nbt.getBooleanOr("showWinscreen", false);
			WinnerList = NbtArrayLists.loadGlobalMap(nbt.getListOrEmpty("WinnerList"), lookupProvider);
			ShowTimer = nbt.getBooleanOr("ShowTimer", false);
			WhenPVPActive = nbt.getDoubleOr("WhenPVPActive", 0);
			hunterAchievementUUID = nbt.getStringOr("hunterAchievementUUID", "");
			hunterAnimation = nbt.getDoubleOr("hunterAnimation", 0);
			glueY = NbtArrayLists.loadGlobalMap(nbt.getListOrEmpty("glueY"), lookupProvider);
			coordinateOffset = Vec3.CODEC.parse(NbtOps.INSTANCE, nbt.get("coordinateOffset")).result().orElse(Vec3.ZERO);
			bossNumber = nbt.getDoubleOr("bossNumber", 0);
			bossName = nbt.getStringOr("bossName", "");
		}

		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nbt.putBoolean("nightVision", nightVision);
			nbt.putDouble("gameTick", gameTick);
			nbt.putDouble("gameSeconds", gameSeconds);
			nbt.putDouble("gameMinutes", gameMinutes);
			nbt.putDouble("gameHours", gameHours);
			nbt.putDouble("pvpstate", pvpstate);
			nbt.putBoolean("pvpAnimationStart", pvpAnimationStart);
			nbt.putDouble("pvpAnimationTick", pvpAnimationTick);
			nbt.putDouble("respawningPlayers", respawningPlayers);
			nbt.putBoolean("achievementHunterMode", achievementHunterMode);
			nbt.putBoolean("randomHunterAchievement", randomHunterAchievement);
			nbt.putBoolean("animateHunter", animateHunter);
			nbt.putDouble("overwoldHuntedX", overwoldHuntedX);
			nbt.putDouble("overworldHuntedZ", overworldHuntedZ);
			nbt.putDouble("netherHuntedX", netherHuntedX);
			nbt.putDouble("netherHuntedZ", netherHuntedZ);
			nbt.putBoolean("minimap", minimap);
			nbt.putBoolean("waypoints", waypoints);
			nbt.putBoolean("CrownHuntInGame", CrownHuntInGame);
			nbt.putBoolean("crownHuntWinDisplay", crownHuntWinDisplay);
			nbt.putBoolean("canGrabCrown", canGrabCrown);
			nbt.putBoolean("returnToCastle", returnToCastle);
			nbt.putDouble("crownMinutes", crownMinutes);
			nbt.putDouble("graceMinutes", graceMinutes);
			nbt.putBoolean("applyCustomNameColor", applyCustomNameColor);
			nbt.putBoolean("inGracePeriod", inGracePeriod);
			nbt.putDouble("winAnimationState", winAnimationState);
			nbt.putBoolean("playingSpleef", playingSpleef);
			nbt.putDouble("layersRemainingSpleef", layersRemainingSpleef);
			nbt.putDouble("layerCountdownSpleef", layerCountdownSpleef);
			nbt.putDouble("spleefAlivePlayers", spleefAlivePlayers);
			nbt.putDouble("gapBetweenLayersSpleef", gapBetweenLayersSpleef);
			nbt.putBoolean("spleefPowerups", spleefPowerups);
			nbt.putDouble("mapsSpleef", mapsSpleef);
			nbt.putString("sky", sky);
			nbt.putString("currentMapSpleef", currentMapSpleef);
			nbt.put("spleefMapMiddleX", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, spleefMapMiddleX).result().orElseGet(CompoundTag::new));
			nbt.putDouble("layerConquestCooldownSpleef", layerConquestCooldownSpleef);
			nbt.putDouble("passiveSnowballsSpleef", passiveSnowballsSpleef);
			nbt.put("dungeonSize", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, dungeonSize).result().orElseGet(CompoundTag::new));
			nbt.putDouble("lootRoomsDungeon", lootRoomsDungeon);
			nbt.putDouble("lootRoomPlacedDungeon", lootRoomPlacedDungeon);
			nbt.putDouble("minibossRoomsDungeon", minibossRoomsDungeon);
			nbt.putDouble("minibossRoomPlacedDungeon", minibossRoomPlacedDungeon);
			nbt.putDouble("dungeonSpawn", dungeonSpawn);
			nbt.putDouble("dungeonBoss", dungeonBoss);
			nbt.put("roomLimitDungeon", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, roomLimitDungeon).result().orElseGet(CompoundTag::new));
			nbt.putDouble("secretRoomDungeon", secretRoomDungeon);
			nbt.putDouble("secretRoomPlacedDungeon", secretRoomPlacedDungeon);
			nbt.putDouble("dungeonCoins", dungeonCoins);
			nbt.putDouble("dungeonFloor", dungeonFloor);
			nbt.put("dungeonRoomSize", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, dungeonRoomSize).result().orElseGet(CompoundTag::new));
			nbt.put("dungeonStartLocation", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, dungeonStartLocation).result().orElseGet(CompoundTag::new));
			nbt.putBoolean("playingDungeons", playingDungeons);
			nbt.putBoolean("showCoins", showCoins);
			nbt.putBoolean("playingAchievement", playingAchievement);
			nbt.putString("VotingMessage", VotingMessage);
			nbt.putDouble("voteType", voteType);
			nbt.putBoolean("ActiveVote", ActiveVote);
			nbt.putString("winnerUUID", winnerUUID);
			nbt.putBoolean("inCombat", inCombat);
			nbt.putDouble("currentRoomID", currentRoomID);
			nbt.put("DoorPosition", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, DoorPosition).result().orElseGet(CompoundTag::new));
			nbt.put("DoorOffset", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, DoorOffset).result().orElseGet(CompoundTag::new));
			nbt.putDouble("aliveEnemies", aliveEnemies);
			nbt.putDouble("startingEnemies", startingEnemies);
			nbt.putDouble("roomCheckDelayTicks", roomCheckDelayTicks);
			nbt.putBoolean("removeEffects", removeEffects);
			nbt.putBoolean("ascendingGlobalActive", ascendingGlobalActive);
			nbt.putString("floorTypeDungeon", floorTypeDungeon);
			nbt.putString("VotingPlayerUUID", VotingPlayerUUID);
			nbt.putString("VotingPlayerName", VotingPlayerName);
			nbt.putString("battleBoxStatus", battleBoxStatus);
			nbt.putDouble("Achievement", Achievement);
			nbt.putDouble("AchievementCategory", AchievementCategory);
			nbt.putDouble("AchievementModifier", AchievementModifier);
			nbt.put("AchievementIcon", (CompoundTag) ItemStack.OPTIONAL_CODEC.encode(AchievementIcon, lookupProvider.createSerializationContext(NbtOps.INSTANCE), new CompoundTag()).result().orElseGet(CompoundTag::new));
			nbt.putString("AchievementTitle", AchievementTitle);
			nbt.putString("AchievementDescription", AchievementDescription);
			nbt.putBoolean("showWinscreen", showWinscreen);
			nbt.put("WinnerList", NbtArrayLists.saveGlobalMap(WinnerList));
			nbt.putBoolean("ShowTimer", ShowTimer);
			nbt.putDouble("WhenPVPActive", WhenPVPActive);
			nbt.putString("hunterAchievementUUID", hunterAchievementUUID);
			nbt.putDouble("hunterAnimation", hunterAnimation);
			nbt.put("glueY", NbtArrayLists.saveGlobalMap(glueY));
			nbt.put("coordinateOffset", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, coordinateOffset).result().orElseGet(CompoundTag::new));
			nbt.putDouble("bossNumber", bossNumber);
			nbt.putString("bossName", bossName);
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			this._syncDirty = true;
		}

		static MapVariables clientSide = new MapVariables();

		public static MapVariables get(LevelAccessor world) {
			if (world instanceof ServerLevelAccessor serverLevelAccessor) {
				return serverLevelAccessor.getLevel().getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(MapVariables.TYPE);
			} else {
				return clientSide;
			}
		}
	}

	public record SavedDataSyncMessage(int dataType, SavedData data) implements CustomPacketPayload {
		public static final Type<SavedDataSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "saved_data_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, SavedDataSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SavedDataSyncMessage message) -> {
			buffer.writeInt(message.dataType);
			if (message.data instanceof MapVariables mapVariables)
				buffer.writeNbt(mapVariables.save(new CompoundTag(), buffer.registryAccess()));
			else if (message.data instanceof WorldVariables worldVariables)
				buffer.writeNbt(worldVariables.save(new CompoundTag(), buffer.registryAccess()));
		}, (RegistryFriendlyByteBuf buffer) -> {
			int dataType = buffer.readInt();
			CompoundTag nbt = buffer.readNbt();
			SavedData data = null;
			if (nbt != null) {
				data = dataType == 0 ? new MapVariables() : new WorldVariables();
				if (data instanceof MapVariables mapVariables)
					mapVariables.read(nbt, buffer.registryAccess());
				else if (data instanceof WorldVariables worldVariables)
					worldVariables.read(nbt, buffer.registryAccess());
			}
			return new SavedDataSyncMessage(dataType, data);
		});

		@Override
		public Type<SavedDataSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final SavedDataSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> {
					if (message.dataType == 0)
						MapVariables.clientSide.read(((MapVariables) message.data).save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
					else
						WorldVariables.clientSide.read(((WorldVariables) message.data).save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}

	public static class PlayerVariables implements ValueIOSerializable {
		boolean _syncDirty = false;
		public double team = 0;
		public boolean joinFirstTime = false;
		public boolean isCrowned = false;
		public String color = "white";
		public ItemStack helmet = ItemStack.EMPTY;
		public double snowballCountSpleef = 0;
		public Vec3 thrusterDirection = Vec3.ZERO;
		public double thrusterTicks = 0;
		public String classDungeon = "";
		public double playerSlots = 9.0;
		public boolean playerInInventory = false;
		public boolean showOnlyHearts = false;
		public double backpackSlots = 3.0;
		public boolean jumps = false;
		public boolean canDash = false;
		public double dashCooldown = 0;
		public double maxDashCooldown = 0;
		public double dashLength = 0;
		public boolean showCustomNameColor = true;
		public boolean votedYes = false;
		public boolean voted = false;
		public double voteCooldown = 0;
		public double healCD = 0;
		public double PassiveHealCooldown = 80.0;
		public double PassiveHealAmount = 1.0;
		public Direction gravity = Direction.DOWN;
		public double ascendingTimer = 0.0;
		public boolean removeEffectsSingleTarget = false;
		public boolean ascendingActive = false;
		public String advancedGlowingColor = "";
		public double tooltipSize = 0.8;
		public Vec3 performKnockback = Vec3.ZERO;
		public boolean openBattleBox = false;
		public double selectedButtonBattleBox = 0;
		public String AchievementLobbyState = "\"\"";
		public String TimerColor = "FFFFFF";
		public double timerSeconds = 0;
		public double timerMinutes = 0;
		public double timerHours = 0;
		public double timerScale = 2.5;
		public double timerTick = 0;
		public double timerSpeed = 1.0;

		@Override
		public void serialize(ValueOutput output) {
			output.putDouble("team", team);
			output.putBoolean("joinFirstTime", joinFirstTime);
			output.putBoolean("isCrowned", isCrowned);
			output.putString("color", color);
			output.store("helmet", ItemStack.OPTIONAL_CODEC, helmet);
			output.putDouble("snowballCountSpleef", snowballCountSpleef);
			output.store("thrusterDirection", Vec3.CODEC, thrusterDirection);
			output.putDouble("thrusterTicks", thrusterTicks);
			output.putString("classDungeon", classDungeon);
			output.putDouble("playerSlots", playerSlots);
			output.putBoolean("playerInInventory", playerInInventory);
			output.putBoolean("showOnlyHearts", showOnlyHearts);
			output.putDouble("backpackSlots", backpackSlots);
			output.putBoolean("jumps", jumps);
			output.putBoolean("canDash", canDash);
			output.putDouble("dashCooldown", dashCooldown);
			output.putDouble("maxDashCooldown", maxDashCooldown);
			output.putDouble("dashLength", dashLength);
			output.putBoolean("showCustomNameColor", showCustomNameColor);
			output.putBoolean("votedYes", votedYes);
			output.putBoolean("voted", voted);
			output.putDouble("voteCooldown", voteCooldown);
			output.putDouble("healCD", healCD);
			output.putDouble("PassiveHealCooldown", PassiveHealCooldown);
			output.putDouble("PassiveHealAmount", PassiveHealAmount);
			output.putInt("gravity", gravity.get3DDataValue());
			output.putDouble("ascendingTimer", ascendingTimer);
			output.putBoolean("removeEffectsSingleTarget", removeEffectsSingleTarget);
			output.putBoolean("ascendingActive", ascendingActive);
			output.putString("advancedGlowingColor", advancedGlowingColor);
			output.putDouble("tooltipSize", tooltipSize);
			output.store("performKnockback", Vec3.CODEC, performKnockback);
			output.putBoolean("openBattleBox", openBattleBox);
			output.putDouble("selectedButtonBattleBox", selectedButtonBattleBox);
			output.putString("AchievementLobbyState", AchievementLobbyState);
			output.putString("TimerColor", TimerColor);
			output.putDouble("timerSeconds", timerSeconds);
			output.putDouble("timerMinutes", timerMinutes);
			output.putDouble("timerHours", timerHours);
			output.putDouble("timerScale", timerScale);
			output.putDouble("timerTick", timerTick);
			output.putDouble("timerSpeed", timerSpeed);
		}

		@Override
		public void deserialize(ValueInput input) {
			team = input.getDoubleOr("team", 0);
			joinFirstTime = input.getBooleanOr("joinFirstTime", false);
			isCrowned = input.getBooleanOr("isCrowned", false);
			color = input.getStringOr("color", "");
			helmet = input.read("helmet", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
			snowballCountSpleef = input.getDoubleOr("snowballCountSpleef", 0);
			thrusterDirection = input.read("thrusterDirection", Vec3.CODEC).orElse(Vec3.ZERO);
			thrusterTicks = input.getDoubleOr("thrusterTicks", 0);
			classDungeon = input.getStringOr("classDungeon", "");
			playerSlots = input.getDoubleOr("playerSlots", 0);
			playerInInventory = input.getBooleanOr("playerInInventory", false);
			showOnlyHearts = input.getBooleanOr("showOnlyHearts", false);
			backpackSlots = input.getDoubleOr("backpackSlots", 0);
			jumps = input.getBooleanOr("jumps", false);
			canDash = input.getBooleanOr("canDash", false);
			dashCooldown = input.getDoubleOr("dashCooldown", 0);
			maxDashCooldown = input.getDoubleOr("maxDashCooldown", 0);
			dashLength = input.getDoubleOr("dashLength", 0);
			showCustomNameColor = input.getBooleanOr("showCustomNameColor", false);
			votedYes = input.getBooleanOr("votedYes", false);
			voted = input.getBooleanOr("voted", false);
			voteCooldown = input.getDoubleOr("voteCooldown", 0);
			healCD = input.getDoubleOr("healCD", 0);
			PassiveHealCooldown = input.getDoubleOr("PassiveHealCooldown", 0);
			PassiveHealAmount = input.getDoubleOr("PassiveHealAmount", 0);
			gravity = Direction.from3DDataValue(input.getIntOr("gravity", 0));
			ascendingTimer = input.getDoubleOr("ascendingTimer", 0);
			removeEffectsSingleTarget = input.getBooleanOr("removeEffectsSingleTarget", false);
			ascendingActive = input.getBooleanOr("ascendingActive", false);
			advancedGlowingColor = input.getStringOr("advancedGlowingColor", "");
			tooltipSize = input.getDoubleOr("tooltipSize", 0);
			performKnockback = input.read("performKnockback", Vec3.CODEC).orElse(Vec3.ZERO);
			openBattleBox = input.getBooleanOr("openBattleBox", false);
			selectedButtonBattleBox = input.getDoubleOr("selectedButtonBattleBox", 0);
			AchievementLobbyState = input.getStringOr("AchievementLobbyState", "");
			TimerColor = input.getStringOr("TimerColor", "");
			timerSeconds = input.getDoubleOr("timerSeconds", 0);
			timerMinutes = input.getDoubleOr("timerMinutes", 0);
			timerHours = input.getDoubleOr("timerHours", 0);
			timerScale = input.getDoubleOr("timerScale", 0);
			timerTick = input.getDoubleOr("timerTick", 0);
			timerSpeed = input.getDoubleOr("timerSpeed", 0);
		}

		public void markSyncDirty() {
			_syncDirty = true;
		}
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data, int player) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "player_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, PlayerVariablesSyncMessage message) -> {
			TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, buffer.registryAccess());
			message.data.serialize(output);
			buffer.writeInt(message.player());
			buffer.writeNbt(output.buildResult());
		}, (RegistryFriendlyByteBuf buffer) -> {
			PlayerVariablesSyncMessage message = new PlayerVariablesSyncMessage(new PlayerVariables(), buffer.readInt());
			message.data.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, buffer.registryAccess(), buffer.readNbt()));
			return message;
		});

		@Override
		public Type<PlayerVariablesSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final PlayerVariablesSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> {
					Entity player = context.player().level().getEntity(message.player);
					if (player == null)
						return;
					TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, context.player().registryAccess());
					message.data.serialize(output);
					player.getData(PLAYER_VARIABLES).deserialize(TagValueInput.create(ProblemReporter.DISCARDING, context.player().registryAccess(), output.buildResult()));
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}
}