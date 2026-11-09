package net.mcreator.minigames.photon_plugin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public class ModifyBlockEffectPacket implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("minigames", "modify_block_effect");
    public static final Type<ModifyBlockEffectPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ModifyBlockEffectPacket> CODEC =
            StreamCodec.ofMember(ModifyBlockEffectPacket::encode, ModifyBlockEffectPacket::decode);

    private List<BlockPos> positions = new ArrayList<>();
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

    public ModifyBlockEffectPacket() {
    }

    public void setPositions(List<BlockPos> positions) {
        this.positions = positions;
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
        buf.writeVarInt(positions.size());
        for (BlockPos pos : positions) {
            buf.writeBlockPos(pos);
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

    public static ModifyBlockEffectPacket decode(RegistryFriendlyByteBuf buf) {
        var packet = new ModifyBlockEffectPacket();

        int posCount = buf.readVarInt();
        List<BlockPos> positions = new ArrayList<>(posCount);
        for (int i = 0; i < posCount; i++) {
            positions.add(buf.readBlockPos());
        }
        packet.positions = positions;

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

    public static void execute(ModifyBlockEffectPacket packet, IPayloadContext context) {
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            Client.execute(packet);
        }
    }

    private static class Client {
        static void execute(ModifyBlockEffectPacket packet) {
            if (packet.positions.isEmpty()) {
                return;
            }

            var builder = ModifyFXBlockClient.modify(packet.positions);
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
