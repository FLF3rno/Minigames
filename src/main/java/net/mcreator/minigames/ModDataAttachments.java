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
        public final double x;
        public final double y;
        public final double z;

        public BeamData(boolean hasBeam, double x, double y, double z) {
            this.hasBeam = hasBeam;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static final Codec<BeamData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("hasBeam").forGetter(d -> d.hasBeam),
            Codec.DOUBLE.fieldOf("x").forGetter(d -> d.x),
            Codec.DOUBLE.fieldOf("y").forGetter(d -> d.y),
            Codec.DOUBLE.fieldOf("z").forGetter(d -> d.z)
        ).apply(instance, BeamData::new));

        public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, BeamData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, d -> d.hasBeam,
            ByteBufCodecs.DOUBLE, d -> d.x,
            ByteBufCodecs.DOUBLE, d -> d.y,
            ByteBufCodecs.DOUBLE, d -> d.z,
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
        ATTACHMENTS.register("beam_data", () -> AttachmentType.builder(() -> new BeamData(false, 0, 0, 0))
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
