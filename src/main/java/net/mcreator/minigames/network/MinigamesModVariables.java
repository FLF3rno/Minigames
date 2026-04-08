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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;

import net.mcreator.minigames.MinigamesMod;

import java.util.function.Supplier;

@EventBusSubscriber
public class MinigamesModVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MinigamesMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("player_variables", () -> AttachmentType.serializable(() -> new PlayerVariables()).build());
	public static double health = 20.0;
	public static double winAnimation = -1.0;
	public static ResourceLocation crown = null;

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
		clone.ready = original.ready;
		clone.winner = original.winner;
		clone.joinFirstTime = original.joinFirstTime;
		clone.wantsToReroll = original.wantsToReroll;
		clone.isCrowned = original.isCrowned;
		clone.color = original.color;
		clone.helmet = original.helmet;
		clone.snowballCountSpleef = original.snowballCountSpleef;
		clone.thrusterDirection = original.thrusterDirection;
		clone.thrusterTicks = original.thrusterTicks;
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
		public static final SavedDataType<WorldVariables> TYPE = new SavedDataType<>("minigames_worldvars", ctx -> new WorldVariables(), ctx -> CompoundTag.CODEC.xmap(tag -> {
			WorldVariables instance = new WorldVariables();
			instance.read(tag, ctx.levelOrThrow().registryAccess());
			return instance;
		}, instance -> instance.save(new CompoundTag(), ctx.levelOrThrow().registryAccess())));
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
		public static final SavedDataType<MapVariables> TYPE = new SavedDataType<>("minigames_mapvars", ctx -> new MapVariables(), ctx -> CompoundTag.CODEC.xmap(tag -> {
			MapVariables instance = new MapVariables();
			instance.read(tag, ctx.levelOrThrow().registryAccess());
			return instance;
		}, instance -> instance.save(new CompoundTag(), ctx.levelOrThrow().registryAccess())));
		boolean _syncDirty = false;
		public double achievmentType = 0.0;
		public double achivementTypeTimer = 295.0;
		public double overlayAnimation1 = 0.0;
		public boolean openGameGUI = false;
		public double playersReady = 0.0;
		public double achievement = -1.0;
		public boolean nightVision = true;
		public double players = 0;
		public double gameTick = 0;
		public double gameSeconds = 0;
		public double gameMinutes = 0;
		public double gameHours = 0;
		public boolean displayTimer = false;
		public double timertype = 0;
		public double pvpstate = -1.0;
		public double winningTeam = 0;
		public boolean winAnimationStart = false;
		public double winAnimationTick = 0.0;
		public boolean pvpAnimationStart = false;
		public double pvpAnimationTick = 0;
		public double p1state = 0;
		public double p2state = 0;
		public double p3state = 0;
		public double p4state = 0;
		public double p5state = 0;
		public double p6state = 0;
		public double rerollingPlayers = 0;
		public double respawningPlayers = 0;
		public boolean nerfWinner = false;
		public boolean randomizeSpawn = false;
		public boolean achievementHunterMode = false;
		public boolean randomHunterAchievement = false;
		public String hunterAchievement = "\"\"";
		public boolean animateHunter = false;
		public double animateHunterState = 0;
		public double displayHunterPlayerAnimation = 0;
		public double overwoldHuntedX = 0;
		public double overworldHuntedZ = 0;
		public double netherHuntedX = 0;
		public double netherHuntedZ = 0;
		public boolean hunteraWinAnimation = false;
		public double debuffLength = 0;
		public boolean headStart = false;
		public boolean minimap = true;
		public boolean waypoints = true;
		public boolean MoveCrownTimer = false;
		public boolean ShowCrownTimer = false;
		public boolean CrownHuntInGame = false;
		public boolean crownHuntWinDisplay = false;
		public boolean canGrabCrown = false;
		public boolean returnToCastle = false;
		public double crownMinutes = 0;
		public double graceMinutes = 0;
		public boolean applyCustomNameColor = false;
		public boolean inGracePeriod = false;
		public boolean showRedTimer = false;
		public double winAnimationState = 0;
		public boolean playingSpleef = false;
		public double layersRemainingSpleef = 0;
		public double layerCountdownSpleef = 0;
		public double spleefAlivePlayers = 0;
		public double gapBetweenLayersSpleef = 0;
		public boolean spleefPowerups = false;
		public double mapsSpleef = 1.0;

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			achievmentType = nbt.getDoubleOr("achievmentType", 0);
			achivementTypeTimer = nbt.getDoubleOr("achivementTypeTimer", 0);
			overlayAnimation1 = nbt.getDoubleOr("overlayAnimation1", 0);
			openGameGUI = nbt.getBooleanOr("openGameGUI", false);
			playersReady = nbt.getDoubleOr("playersReady", 0);
			achievement = nbt.getDoubleOr("achievement", 0);
			nightVision = nbt.getBooleanOr("nightVision", false);
			players = nbt.getDoubleOr("players", 0);
			gameTick = nbt.getDoubleOr("gameTick", 0);
			gameSeconds = nbt.getDoubleOr("gameSeconds", 0);
			gameMinutes = nbt.getDoubleOr("gameMinutes", 0);
			gameHours = nbt.getDoubleOr("gameHours", 0);
			displayTimer = nbt.getBooleanOr("displayTimer", false);
			timertype = nbt.getDoubleOr("timertype", 0);
			pvpstate = nbt.getDoubleOr("pvpstate", 0);
			winningTeam = nbt.getDoubleOr("winningTeam", 0);
			winAnimationStart = nbt.getBooleanOr("winAnimationStart", false);
			winAnimationTick = nbt.getDoubleOr("winAnimationTick", 0);
			pvpAnimationStart = nbt.getBooleanOr("pvpAnimationStart", false);
			pvpAnimationTick = nbt.getDoubleOr("pvpAnimationTick", 0);
			p1state = nbt.getDoubleOr("p1state", 0);
			p2state = nbt.getDoubleOr("p2state", 0);
			p3state = nbt.getDoubleOr("p3state", 0);
			p4state = nbt.getDoubleOr("p4state", 0);
			p5state = nbt.getDoubleOr("p5state", 0);
			p6state = nbt.getDoubleOr("p6state", 0);
			rerollingPlayers = nbt.getDoubleOr("rerollingPlayers", 0);
			respawningPlayers = nbt.getDoubleOr("respawningPlayers", 0);
			nerfWinner = nbt.getBooleanOr("nerfWinner", false);
			randomizeSpawn = nbt.getBooleanOr("randomizeSpawn", false);
			achievementHunterMode = nbt.getBooleanOr("achievementHunterMode", false);
			randomHunterAchievement = nbt.getBooleanOr("randomHunterAchievement", false);
			hunterAchievement = nbt.getStringOr("hunterAchievement", "");
			animateHunter = nbt.getBooleanOr("animateHunter", false);
			animateHunterState = nbt.getDoubleOr("animateHunterState", 0);
			displayHunterPlayerAnimation = nbt.getDoubleOr("displayHunterPlayerAnimation", 0);
			overwoldHuntedX = nbt.getDoubleOr("overwoldHuntedX", 0);
			overworldHuntedZ = nbt.getDoubleOr("overworldHuntedZ", 0);
			netherHuntedX = nbt.getDoubleOr("netherHuntedX", 0);
			netherHuntedZ = nbt.getDoubleOr("netherHuntedZ", 0);
			hunteraWinAnimation = nbt.getBooleanOr("hunteraWinAnimation", false);
			debuffLength = nbt.getDoubleOr("debuffLength", 0);
			headStart = nbt.getBooleanOr("headStart", false);
			minimap = nbt.getBooleanOr("minimap", false);
			waypoints = nbt.getBooleanOr("waypoints", false);
			MoveCrownTimer = nbt.getBooleanOr("MoveCrownTimer", false);
			ShowCrownTimer = nbt.getBooleanOr("ShowCrownTimer", false);
			CrownHuntInGame = nbt.getBooleanOr("CrownHuntInGame", false);
			crownHuntWinDisplay = nbt.getBooleanOr("crownHuntWinDisplay", false);
			canGrabCrown = nbt.getBooleanOr("canGrabCrown", false);
			returnToCastle = nbt.getBooleanOr("returnToCastle", false);
			crownMinutes = nbt.getDoubleOr("crownMinutes", 0);
			graceMinutes = nbt.getDoubleOr("graceMinutes", 0);
			applyCustomNameColor = nbt.getBooleanOr("applyCustomNameColor", false);
			inGracePeriod = nbt.getBooleanOr("inGracePeriod", false);
			showRedTimer = nbt.getBooleanOr("showRedTimer", false);
			winAnimationState = nbt.getDoubleOr("winAnimationState", 0);
			playingSpleef = nbt.getBooleanOr("playingSpleef", false);
			layersRemainingSpleef = nbt.getDoubleOr("layersRemainingSpleef", 0);
			layerCountdownSpleef = nbt.getDoubleOr("layerCountdownSpleef", 0);
			spleefAlivePlayers = nbt.getDoubleOr("spleefAlivePlayers", 0);
			gapBetweenLayersSpleef = nbt.getDoubleOr("gapBetweenLayersSpleef", 0);
			spleefPowerups = nbt.getBooleanOr("spleefPowerups", false);
			mapsSpleef = nbt.getDoubleOr("mapsSpleef", 0);
		}

		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nbt.putDouble("achievmentType", achievmentType);
			nbt.putDouble("achivementTypeTimer", achivementTypeTimer);
			nbt.putDouble("overlayAnimation1", overlayAnimation1);
			nbt.putBoolean("openGameGUI", openGameGUI);
			nbt.putDouble("playersReady", playersReady);
			nbt.putDouble("achievement", achievement);
			nbt.putBoolean("nightVision", nightVision);
			nbt.putDouble("players", players);
			nbt.putDouble("gameTick", gameTick);
			nbt.putDouble("gameSeconds", gameSeconds);
			nbt.putDouble("gameMinutes", gameMinutes);
			nbt.putDouble("gameHours", gameHours);
			nbt.putBoolean("displayTimer", displayTimer);
			nbt.putDouble("timertype", timertype);
			nbt.putDouble("pvpstate", pvpstate);
			nbt.putDouble("winningTeam", winningTeam);
			nbt.putBoolean("winAnimationStart", winAnimationStart);
			nbt.putDouble("winAnimationTick", winAnimationTick);
			nbt.putBoolean("pvpAnimationStart", pvpAnimationStart);
			nbt.putDouble("pvpAnimationTick", pvpAnimationTick);
			nbt.putDouble("p1state", p1state);
			nbt.putDouble("p2state", p2state);
			nbt.putDouble("p3state", p3state);
			nbt.putDouble("p4state", p4state);
			nbt.putDouble("p5state", p5state);
			nbt.putDouble("p6state", p6state);
			nbt.putDouble("rerollingPlayers", rerollingPlayers);
			nbt.putDouble("respawningPlayers", respawningPlayers);
			nbt.putBoolean("nerfWinner", nerfWinner);
			nbt.putBoolean("randomizeSpawn", randomizeSpawn);
			nbt.putBoolean("achievementHunterMode", achievementHunterMode);
			nbt.putBoolean("randomHunterAchievement", randomHunterAchievement);
			nbt.putString("hunterAchievement", hunterAchievement);
			nbt.putBoolean("animateHunter", animateHunter);
			nbt.putDouble("animateHunterState", animateHunterState);
			nbt.putDouble("displayHunterPlayerAnimation", displayHunterPlayerAnimation);
			nbt.putDouble("overwoldHuntedX", overwoldHuntedX);
			nbt.putDouble("overworldHuntedZ", overworldHuntedZ);
			nbt.putDouble("netherHuntedX", netherHuntedX);
			nbt.putDouble("netherHuntedZ", netherHuntedZ);
			nbt.putBoolean("hunteraWinAnimation", hunteraWinAnimation);
			nbt.putDouble("debuffLength", debuffLength);
			nbt.putBoolean("headStart", headStart);
			nbt.putBoolean("minimap", minimap);
			nbt.putBoolean("waypoints", waypoints);
			nbt.putBoolean("MoveCrownTimer", MoveCrownTimer);
			nbt.putBoolean("ShowCrownTimer", ShowCrownTimer);
			nbt.putBoolean("CrownHuntInGame", CrownHuntInGame);
			nbt.putBoolean("crownHuntWinDisplay", crownHuntWinDisplay);
			nbt.putBoolean("canGrabCrown", canGrabCrown);
			nbt.putBoolean("returnToCastle", returnToCastle);
			nbt.putDouble("crownMinutes", crownMinutes);
			nbt.putDouble("graceMinutes", graceMinutes);
			nbt.putBoolean("applyCustomNameColor", applyCustomNameColor);
			nbt.putBoolean("inGracePeriod", inGracePeriod);
			nbt.putBoolean("showRedTimer", showRedTimer);
			nbt.putDouble("winAnimationState", winAnimationState);
			nbt.putBoolean("playingSpleef", playingSpleef);
			nbt.putDouble("layersRemainingSpleef", layersRemainingSpleef);
			nbt.putDouble("layerCountdownSpleef", layerCountdownSpleef);
			nbt.putDouble("spleefAlivePlayers", spleefAlivePlayers);
			nbt.putDouble("gapBetweenLayersSpleef", gapBetweenLayersSpleef);
			nbt.putBoolean("spleefPowerups", spleefPowerups);
			nbt.putDouble("mapsSpleef", mapsSpleef);
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
		public static final Type<SavedDataSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "saved_data_sync"));
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
		public boolean ready = false;
		public boolean winner = false;
		public boolean joinFirstTime = false;
		public boolean wantsToReroll = false;
		public boolean isCrowned = false;
		public String color = "white";
		public ItemStack helmet = ItemStack.EMPTY;
		public double snowballCountSpleef = 0;
		public Vec3 thrusterDirection = Vec3.ZERO;
		public double thrusterTicks = 0;

		@Override
		public void serialize(ValueOutput output) {
			output.putDouble("team", team);
			output.putBoolean("ready", ready);
			output.putBoolean("winner", winner);
			output.putBoolean("joinFirstTime", joinFirstTime);
			output.putBoolean("wantsToReroll", wantsToReroll);
			output.putBoolean("isCrowned", isCrowned);
			output.putString("color", color);
			output.store("helmet", ItemStack.OPTIONAL_CODEC, helmet);
			output.putDouble("snowballCountSpleef", snowballCountSpleef);
			output.store("thrusterDirection", Vec3.CODEC, thrusterDirection);
			output.putDouble("thrusterTicks", thrusterTicks);
		}

		@Override
		public void deserialize(ValueInput input) {
			team = input.getDoubleOr("team", 0);
			ready = input.getBooleanOr("ready", false);
			winner = input.getBooleanOr("winner", false);
			joinFirstTime = input.getBooleanOr("joinFirstTime", false);
			wantsToReroll = input.getBooleanOr("wantsToReroll", false);
			isCrowned = input.getBooleanOr("isCrowned", false);
			color = input.getStringOr("color", "");
			helmet = input.read("helmet", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
			snowballCountSpleef = input.getDoubleOr("snowballCountSpleef", 0);
			thrusterDirection = input.read("thrusterDirection", Vec3.CODEC).orElse(Vec3.ZERO);
			thrusterTicks = input.getDoubleOr("thrusterTicks", 0);
		}

		public void markSyncDirty() {
			_syncDirty = true;
		}
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data, int player) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "player_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, PlayerVariablesSyncMessage message) -> {
			TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
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