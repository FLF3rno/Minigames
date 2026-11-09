package net.mcreator.minigames.photon_plugin;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public class ModifyEntityEffectPacket implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("minigames", "modify_entity_effect");
    public static final Type<ModifyEntityEffectPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ModifyEntityEffectPacket> CODEC =
            StreamCodec.ofMember(ModifyEntityEffectPacket::encode, ModifyEntityEffectPacket::decode);

    private List<Integer> entityIds = new ArrayList<>();
    private boolean hasName;
    private String effectName = "";
    private boolean hasOffset;
    private double offsetX, offsetY, offsetZ;
    private boolean hasRotation;
    private double rotationX, rotationY, rotationZ;
    private boolean hasScale;
    private double scaleX, scaleY, scaleZ;
    private boolean hasColor;
    private int colorR, colorG, colorB;
    private boolean hasDuration;
    private int duration;

    public ModifyEntityEffectPacket() {
    }

    public void setEntityIds(List<Integer> entityIds) {
        this.entityIds = entityIds;
    }

    public void setName(String name) {
        this.hasName = name != null && !name.isEmpty();
        this.effectName = this.hasName ? name : "";
    }

    public void setOffset(double x, double y, double z) {
        this.hasOffset = true;
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
    }

    public void setRotation(double x, double y, double z) {
        this.hasRotation = true;
        this.rotationX = x;
        this.rotationY = y;
        this.rotationZ = z;
    }

    public void setScale(double x, double y, double z) {
        this.hasScale = true;
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    public void setColor(int r, int g, int b) {
        this.hasColor = true;
        this.colorR = Math.clamp(r, 0, 255);
        this.colorG = Math.clamp(g, 0, 255);
        this.colorB = Math.clamp(b, 0, 255);
    }

    public void setDuration(int duration) {
        this.hasDuration = true;
        this.duration = Math.max(1, duration);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(entityIds.size());
        for (int id : entityIds) {
            buf.writeVarInt(id);
        }

        buf.writeBoolean(hasName);
        if (hasName) {
            buf.writeUtf(effectName);
        }

        buf.writeBoolean(hasOffset);
        if (hasOffset) {
            buf.writeDouble(offsetX);
            buf.writeDouble(offsetY);
            buf.writeDouble(offsetZ);
        }

        buf.writeBoolean(hasRotation);
        if (hasRotation) {
            buf.writeDouble(rotationX);
            buf.writeDouble(rotationY);
            buf.writeDouble(rotationZ);
        }

        buf.writeBoolean(hasScale);
        if (hasScale) {
            buf.writeDouble(scaleX);
            buf.writeDouble(scaleY);
            buf.writeDouble(scaleZ);
        }

        buf.writeBoolean(hasColor);
        if (hasColor) {
            buf.writeVarInt(colorR);
            buf.writeVarInt(colorG);
            buf.writeVarInt(colorB);
        }

        buf.writeBoolean(hasDuration);
        if (hasDuration) {
            buf.writeVarInt(duration);
        }
    }

    public static ModifyEntityEffectPacket decode(RegistryFriendlyByteBuf buf) {
        var packet = new ModifyEntityEffectPacket();

        int idCount = buf.readVarInt();
        List<Integer> ids = new ArrayList<>(idCount);
        for (int i = 0; i < idCount; i++) {
            ids.add(buf.readVarInt());
        }
        packet.entityIds = ids;

        packet.hasName = buf.readBoolean();
        if (packet.hasName) {
            packet.effectName = buf.readUtf();
        }

        packet.hasOffset = buf.readBoolean();
        if (packet.hasOffset) {
            packet.offsetX = buf.readDouble();
            packet.offsetY = buf.readDouble();
            packet.offsetZ = buf.readDouble();
        }

        packet.hasRotation = buf.readBoolean();
        if (packet.hasRotation) {
            packet.rotationX = buf.readDouble();
            packet.rotationY = buf.readDouble();
            packet.rotationZ = buf.readDouble();
        }

        packet.hasScale = buf.readBoolean();
        if (packet.hasScale) {
            packet.scaleX = buf.readDouble();
            packet.scaleY = buf.readDouble();
            packet.scaleZ = buf.readDouble();
        }

        packet.hasColor = buf.readBoolean();
        if (packet.hasColor) {
            packet.colorR = buf.readVarInt();
            packet.colorG = buf.readVarInt();
            packet.colorB = buf.readVarInt();
        }

        packet.hasDuration = buf.readBoolean();
        if (packet.hasDuration) {
            packet.duration = buf.readVarInt();
        }

        return packet;
    }

    public static void execute(ModifyEntityEffectPacket packet, IPayloadContext context) {
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            Client.execute(packet);
        }
    }

    private static class Client {
        static void execute(ModifyEntityEffectPacket packet) {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            List<Entity> entities = new ArrayList<>();
            for (int id : packet.entityIds) {
                var entity = level.getEntity(id);
                if (entity != null) {
                    entities.add(entity);
                }
            }

            if (entities.isEmpty()) {
                return;
            }

            var builder = ModifyFXEntityClient.modify(entities);
            if (packet.hasName) {
                builder.name(packet.effectName);
            }
            if (packet.hasOffset) {
                builder.offset(packet.offsetX, packet.offsetY, packet.offsetZ);
            }
            if (packet.hasRotation) {
                builder.rotation(packet.rotationX, packet.rotationY, packet.rotationZ);
            }
            if (packet.hasScale) {
                builder.scale(packet.scaleX, packet.scaleY, packet.scaleZ);
            }
            if (packet.hasColor) {
                builder.color(packet.colorR, packet.colorG, packet.colorB);
            }
            if (packet.hasDuration) {
                builder.duration(packet.duration);
            }
            builder.send();
        }
    }
}
