package net.mcreator.minigames.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;

import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record SpawnCustomParticleMessage(
    String particleId,
    double x,
    double y,
    double z,
    double vx,
    double vy,
    double vz,
    int lifetime
) implements CustomPacketPayload {
    public static final Type<SpawnCustomParticleMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "spawn_custom_particle"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpawnCustomParticleMessage> STREAM_CODEC = StreamCodec.of(
        SpawnCustomParticleMessage::write,
        SpawnCustomParticleMessage::read
    );

    public static void write(FriendlyByteBuf buffer, SpawnCustomParticleMessage message) {
        buffer.writeUtf(message.particleId);
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
        buffer.writeDouble(message.vx);
        buffer.writeDouble(message.vy);
        buffer.writeDouble(message.vz);
        buffer.writeInt(message.lifetime);
    }

    public static SpawnCustomParticleMessage read(FriendlyByteBuf buffer) {
        return new SpawnCustomParticleMessage(
            buffer.readUtf(),
            buffer.readDouble(),
            buffer.readDouble(),
            buffer.readDouble(),
            buffer.readDouble(),
            buffer.readDouble(),
            buffer.readDouble(),
            buffer.readInt()
        );
    }

    @Override
    public Type<SpawnCustomParticleMessage> type() {
        return TYPE;
    }

    public static void handleData(final SpawnCustomParticleMessage message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.CLIENTBOUND) {
            context.enqueueWork(() -> {
                spawnClientParticle(message);
            });
        }
    }

    private static void spawnClientParticle(SpawnCustomParticleMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Identifier id = Identifier.parse(message.particleId);
        if (!BuiltInRegistries.PARTICLE_TYPE.containsKey(id)) return;

        ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.getValue(id);
        if (type instanceof ParticleOptions particleOptions) {
            Particle particle = mc.particleEngine.createParticle(
                particleOptions,
                message.x,
                message.y,
                message.z,
                message.vx,
                message.vy,
                message.vz
            );
            if (particle != null) {
                if (message.lifetime > 0) {
                    particle.setLifetime(message.lifetime);
                }
                particle.setParticleSpeed(message.vx, message.vy, message.vz);
                mc.particleEngine.add(particle);
            }
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MinigamesMod.addNetworkMessage(SpawnCustomParticleMessage.TYPE, SpawnCustomParticleMessage.STREAM_CODEC, SpawnCustomParticleMessage::handleData);
    }
}
