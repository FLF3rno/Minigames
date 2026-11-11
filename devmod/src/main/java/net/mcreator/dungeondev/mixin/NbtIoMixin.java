package net.mcreator.dungeondev.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

@Mixin(NbtIo.class)
public abstract class NbtIoMixin {

    @Redirect(
        method = "createCompressorStream",
        at = @At(
            value = "NEW",
            target = "(Ljava/io/OutputStream;)Ljava/util/zip/GZIPOutputStream;"
        )
    )
    private static GZIPOutputStream dungeonDev$createLevel9Gzip(OutputStream out) throws IOException {
        return new GZIPOutputStream(out) {
            {
                this.def.setLevel(Deflater.BEST_COMPRESSION);
            }
        };
    }
}
