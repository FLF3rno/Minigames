package net.mcreator.dungeondev.mixin;

import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerboundSetStructureBlockPacket.class)
public abstract class ServerboundSetStructureBlockPacketMixin {

    @ModifyConstant(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", constant = @Constant(intValue = 48))
    private int dungeonDev$modifyPacketMaxLimit(int original) {
        return 127;
    }

    @ModifyConstant(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", constant = @Constant(intValue = -48))
    private int dungeonDev$modifyPacketMinLimit(int original) {
        return -128;
    }
}
