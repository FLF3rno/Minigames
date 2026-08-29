package net.mcreator.minigames;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ModDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "minigames");

    public static final class BeamData {
        public final boolean hasBeam;
        public final int targetId;
        public final int startTick;
        public final int durationTicks;
        public final double scale;
        public final String texture;
        public final int x, y, z;

        public BeamData(boolean hasBeam, int targetId, int startTick, int durationTicks, double scale, String texture, int x, int y, int z) { this.hasBeam = hasBeam; this.targetId = targetId; this.startTick = startTick; this.durationTicks = durationTicks; this.scale = scale; this.texture = texture; this.x = x; this.y = y; this.z = z; }

        public static final Codec<BeamData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.fieldOf("hasBeam").forGetter(d -> d.hasBeam), Codec.INT.fieldOf("targetId").forGetter(d -> d.targetId), Codec.INT.fieldOf("startTick").forGetter(d -> d.startTick), Codec.INT.fieldOf("durationTicks").forGetter(d -> d.durationTicks), Codec.DOUBLE.fieldOf("scale").forGetter(d -> d.scale), Codec.STRING.fieldOf("texture").forGetter(d -> d.texture), Codec.INT.fieldOf("x").forGetter(d -> d.x), Codec.INT.fieldOf("y").forGetter(d -> d.y), Codec.INT.fieldOf("z").forGetter(d -> d.z)).apply(instance, BeamData::new));

        public static final StreamCodec<FriendlyByteBuf, BeamData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, d -> d.hasBeam, ByteBufCodecs.INT, d -> d.targetId, ByteBufCodecs.INT, d -> d.startTick, ByteBufCodecs.INT, d -> d.durationTicks, ByteBufCodecs.DOUBLE, d -> d.scale, ByteBufCodecs.STRING_UTF8, d -> d.texture, ByteBufCodecs.INT, d -> d.x, ByteBufCodecs.INT, d -> d.y, ByteBufCodecs.INT, d -> d.z, BeamData::new);
    }

    public static final class BeamXYZData {
        public final boolean active;
        public final double fromX, fromY, fromZ, toX, toY, toZ;
        public final int startTick, durationTicks;
        public final double scale;
        public final String texture, type;
        public final boolean emissive;

        public BeamXYZData(boolean active, double fromX, double fromY, double fromZ, double toX, double toY, double toZ, int startTick, int durationTicks, double scale, String texture, String type, boolean emissive) { this.active = active; this.fromX = fromX; this.fromY = fromY; this.fromZ = fromZ; this.toX = toX; this.toY = toY; this.toZ = toZ; this.startTick = startTick; this.durationTicks = durationTicks; this.scale = scale; this.texture = texture; this.type = type; this.emissive = emissive; }

        public static final Codec<BeamXYZData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.fieldOf("active").forGetter(d -> d.active), Codec.DOUBLE.fieldOf("fromX").forGetter(d -> d.fromX), Codec.DOUBLE.fieldOf("fromY").forGetter(d -> d.fromY), Codec.DOUBLE.fieldOf("fromZ").forGetter(d -> d.fromZ), Codec.DOUBLE.fieldOf("toX").forGetter(d -> d.toX), Codec.DOUBLE.fieldOf("toY").forGetter(d -> d.toY), Codec.DOUBLE.fieldOf("toZ").forGetter(d -> d.toZ), Codec.INT.fieldOf("startTick").forGetter(d -> d.startTick), Codec.INT.fieldOf("durationTicks").forGetter(d -> d.durationTicks), Codec.DOUBLE.fieldOf("scale").forGetter(d -> d.scale), Codec.STRING.fieldOf("texture").forGetter(d -> d.texture), Codec.STRING.fieldOf("type").forGetter(d -> d.type), Codec.BOOL.fieldOf("emissive").forGetter(d -> d.emissive)).apply(instance, BeamXYZData::new));

        public static final StreamCodec<FriendlyByteBuf, BeamXYZData> STREAM_CODEC = StreamCodec.of((buf, d) -> { buf.writeBoolean(d.active); buf.writeDouble(d.fromX); buf.writeDouble(d.fromY); buf.writeDouble(d.fromZ); buf.writeDouble(d.toX); buf.writeDouble(d.toY); buf.writeDouble(d.toZ); buf.writeInt(d.startTick); buf.writeInt(d.durationTicks); buf.writeDouble(d.scale); buf.writeUtf(d.texture); buf.writeUtf(d.type); buf.writeBoolean(d.emissive); }, buf -> new BeamXYZData(buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readInt(), buf.readDouble(), buf.readUtf(), buf.readUtf(), buf.readBoolean()));
    }

    public static final class BlessedData {
        public final int dataId;

        public BlessedData(int dataId) { this.dataId = dataId; }

        public static final Codec<BlessedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.INT.fieldOf("dataId").forGetter(d -> d.dataId)).apply(instance, BlessedData::new));

        public static final StreamCodec<FriendlyByteBuf, BlessedData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, d -> d.dataId, BlessedData::new);
    }

    public static final java.util.function.Supplier<AttachmentType<BeamData>> BEAM_DATA = ATTACHMENTS.register("beam_data", () -> AttachmentType.builder(() -> new BeamData(false, -1, 0, 0, 0.0D, "", 0, 0, 0)).serialize(BeamData.CODEC.fieldOf("beam_data")).sync(BeamData.STREAM_CODEC).copyOnDeath().build());

    public static final java.util.function.Supplier<AttachmentType<BeamXYZData>> BEAM_XYZ_DATA = ATTACHMENTS.register("beam_xyz_data", () -> AttachmentType.builder(() -> new BeamXYZData(false, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0, 0, 0.0D, "", "beam", false)).serialize(BeamXYZData.CODEC.fieldOf("beam_xyz_data")).sync(BeamXYZData.STREAM_CODEC).copyOnDeath().build());

    public static final java.util.function.Supplier<AttachmentType<BlessedData>> BLESSED_DATA = ATTACHMENTS.register("blessed_data", () -> AttachmentType.builder(() -> new BlessedData(0)).serialize(BlessedData.CODEC.fieldOf("blessed_data")).sync(BlessedData.STREAM_CODEC).copyOnDeath().build());
}