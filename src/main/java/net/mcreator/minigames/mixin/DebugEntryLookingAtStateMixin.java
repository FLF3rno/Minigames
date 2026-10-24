package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(targets = "net.minecraft.client.gui.components.debug.DebugEntryLookingAt$DebugEntryLookingAtState")
public class DebugEntryLookingAtStateMixin {

    @Shadow
    @Final
    private String prefix;

    @Inject(method = "extractInfo", at = @At("TAIL"))
    private void modifyTargetedBlockCoords(List<String> list, Level level, BlockPos pos, CallbackInfo ci) {
        if (list == null || list.isEmpty() || level == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        var mapVariables = MinigamesModVariables.MapVariables.get(minecraft.level);
        double offsetX = mapVariables.coordinateOffset.x;
        double offsetZ = mapVariables.coordinateOffset.z;

        if (offsetX == 0 && offsetZ == 0) {
            return;
        }

        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        if (level.dimension() == Level.OVERWORLD) {
            x -= offsetX;
            z -= offsetZ;
        } else if (level.dimension() == Level.NETHER) {
            x = ((x * 8.0) - offsetX) / 8.0;
            z = ((z * 8.0) - offsetZ) / 8.0;
        }

        BlockPos offsetPos = BlockPos.containing(x, y, z);
        String replacement = ChatFormatting.UNDERLINE + prefix + ": " + offsetPos.getX() + ", " + offsetPos.getY() + ", " + offsetPos.getZ();
        list.set(0, replacement);
    }
}
