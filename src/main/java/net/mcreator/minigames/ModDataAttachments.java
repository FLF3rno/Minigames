package net.mcreator.minigames;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ModDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = 
        DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "minigames");

    public static final class BeamData {
        public final boolean hasBeam;
        public final int targetId;
        public final int startTick;
        public final int durationTicks;
        public final double scale;
        public final String texture;

        public BeamData(boolean hasBeam, int targetId, int startTick, int durationTicks, double scale, String texture) {
            this.hasBeam = hasBeam;
            this.targetId = targetId;
            this.startTick = startTick;
            this.durationTicks = durationTicks;
            this.scale = scale;
            this.texture = texture;
        }

        public static final Codec<BeamData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("hasBeam").forGetter(d -> d.hasBeam),
            Codec.INT.fieldOf("targetId").forGetter(d -> d.targetId),
            Codec.INT.fieldOf("startTick").forGetter(d -> d.startTick),
            Codec.INT.fieldOf("durationTicks").forGetter(d -> d.durationTicks),
            Codec.DOUBLE.fieldOf("scale").forGetter(d -> d.scale),
            Codec.STRING.fieldOf("texture").forGetter(d -> d.texture)
        ).apply(instance, BeamData::new));

        public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, BeamData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, d -> d.hasBeam,
            ByteBufCodecs.INT, d -> d.targetId,
            ByteBufCodecs.INT, d -> d.startTick,
            ByteBufCodecs.INT, d -> d.durationTicks,
            ByteBufCodecs.DOUBLE, d -> d.scale,
            ByteBufCodecs.STRING_UTF8, d -> d.texture,
            BeamData::new
        );
    }

    public static final class BlessedData {
        public final int dataId;

        public BlessedData(int dataId) {
            this.dataId = dataId;
        }

        public static final Codec<BlessedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("dataId").forGetter(d -> d.dataId)
        ).apply(instance, BlessedData::new));

        public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, BlessedData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, d -> d.dataId,
            BlessedData::new
        );
    }

    public static final java.util.function.Supplier<AttachmentType<BeamData>> BEAM_DATA = 
        ATTACHMENTS.register("beam_data", () -> AttachmentType.builder(() -> new BeamData(false, -1, 0, 0, 0.0D, ""))
            .serialize(BeamData.CODEC.fieldOf("beam_data"))
            .sync(BeamData.STREAM_CODEC)
            .copyOnDeath()
            .build());

    public static final java.util.function.Supplier<AttachmentType<BlessedData>> BLESSED_DATA =
        ATTACHMENTS.register("blessed_data", () -> AttachmentType.builder(() -> new BlessedData(0))
            .serialize(BlessedData.CODEC.fieldOf("blessed_data"))
            .sync(BlessedData.STREAM_CODEC)
            .copyOnDeath()
            .build());
}
