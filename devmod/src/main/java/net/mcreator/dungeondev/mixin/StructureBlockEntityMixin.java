package net.mcreator.dungeondev.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.ArrayList;
import java.util.List;

@Mixin(StructureBlockEntity.class)
public abstract class StructureBlockEntityMixin {

    @ModifyConstant(method = "loadAdditional", constant = @Constant(intValue = 48))
    private int dungeonDev$modifyStructureBlockMaxLimit(int original) {
        return 512;
    }

    @ModifyConstant(method = "loadAdditional", constant = @Constant(intValue = -48))
    private int dungeonDev$modifyStructureBlockMinLimit(int original) {
        return -512;
    }

    @ModifyArg(
        method = "saveStructure(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/Identifier;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Vec3i;ZLjava/lang/String;ZLjava/util/List;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;fillFromWorld(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Vec3i;ZLjava/util/List;)V"
        ),
        index = 4
    )
    private static List<Block> dungeonDev$addAirToIgnoredBlocks(List<Block> original) {
        List<Block> list = new ArrayList<>(original);
        list.add(Blocks.AIR);
        list.add(Blocks.CAVE_AIR);
        list.add(Blocks.VOID_AIR);
        return list;
    }
}
