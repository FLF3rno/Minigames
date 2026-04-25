package net.mcreator.minigames.network;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

import net.mcreator.minigames.MinigamesMod;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber
public class TeammateHealthSync {
	private static final Map<UUID, HealthSnapshot> LAST_SENT = new ConcurrentHashMap<>();
	private static final Map<Integer, HealthSnapshot> CLIENT_HEALTH = new ConcurrentHashMap<>();

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(TeammateHealthSyncMessage.TYPE, TeammateHealthSyncMessage.STREAM_CODEC, TeammateHealthSyncMessage::handleData);
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}

		HealthSnapshot current = new HealthSnapshot(player.getHealth(), player.getMaxHealth(), player.getAbsorptionAmount());
		current = new HealthSnapshot(current.health(), current.maxHealth(), current.absorption(), player.hasEffect(MobEffects.POISON), player.hasEffect(MobEffects.WITHER), hasHarmfulEffect(player), player.hurtTime);
		HealthSnapshot previous = LAST_SENT.get(player.getUUID());
		if (previous == null || hasMeaningfulChange(previous, current)) {
			LAST_SENT.put(player.getUUID(), current);
			PacketDistributor.sendToPlayersInDimension(player.level(),
					new TeammateHealthSyncMessage(player.getId(), current.health(), current.maxHealth(), current.absorption(), current.poisoned(), current.withered(), current.hasHarmfulEffect(), current.hurtFlashTicks()));
		}
	}

	private static boolean hasMeaningfulChange(HealthSnapshot a, HealthSnapshot b) {
		return Math.abs(a.health() - b.health()) > 0.01F || Math.abs(a.maxHealth() - b.maxHealth()) > 0.01F || Math.abs(a.absorption() - b.absorption()) > 0.01F || a.poisoned() != b.poisoned()
				|| a.withered() != b.withered() || a.hasHarmfulEffect() != b.hasHarmfulEffect() || a.hurtFlashTicks() != b.hurtFlashTicks();
	}

	private static boolean hasHarmfulEffect(Player player) {
		for (var effectInstance : player.getActiveEffects()) {
			if (effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
				return true;
			}
		}
		return false;
	}

	public static Optional<HealthSnapshot> get(Player player) {
		if (player == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(CLIENT_HEALTH.get(player.getId()));
	}

	public record HealthSnapshot(float health, float maxHealth, float absorption, boolean poisoned, boolean withered, boolean hasHarmfulEffect, int hurtFlashTicks) {
		public HealthSnapshot(float health, float maxHealth, float absorption) {
			this(health, maxHealth, absorption, false, false, false, 0);
		}
	}

	public record TeammateHealthSyncMessage(int entityId, float health, float maxHealth, float absorption, boolean poisoned, boolean withered, boolean hasHarmfulEffect, int hurtFlashTicks) implements CustomPacketPayload {
		public static final Type<TeammateHealthSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "teammate_health_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, TeammateHealthSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, TeammateHealthSyncMessage message) -> {
			buffer.writeInt(message.entityId());
			buffer.writeFloat(message.health());
			buffer.writeFloat(message.maxHealth());
			buffer.writeFloat(message.absorption());
			buffer.writeBoolean(message.poisoned());
			buffer.writeBoolean(message.withered());
			buffer.writeBoolean(message.hasHarmfulEffect());
			buffer.writeInt(message.hurtFlashTicks());
		}, (RegistryFriendlyByteBuf buffer) -> new TeammateHealthSyncMessage(buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readInt()));

		@Override
		public Type<TeammateHealthSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final TeammateHealthSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND) {
				context.enqueueWork(() -> CLIENT_HEALTH.put(message.entityId(),
						new HealthSnapshot(message.health(), message.maxHealth(), message.absorption(), message.poisoned(), message.withered(), message.hasHarmfulEffect(), message.hurtFlashTicks()))).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}
}
