package net.mcreator.minigames.mixin;

import net.mcreator.minigames.util.StrongholdPositionGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Mixin(ChunkGeneratorStructureState.class)
public class ChunkGeneratorStructureStateMixin {

    @Inject(
            method = "generateRingPositions",
            at = @At("HEAD"),
            cancellable = true
    )
    private void generateRingPositions(
            Holder<StructureSet> structureSet,
            ConcentricRingsStructurePlacement placement,
            CallbackInfoReturnable<CompletableFuture<List<ChunkPos>>> cir
    ) {

        boolean isStronghold = false;

        for (StructureSet.StructureSelectionEntry entry : structureSet.value().structures()) {
            if (entry.structure().unwrapKey().isPresent()
                    && entry.structure().unwrapKey().get().identifier().getPath().equals("stronghold")) {
                isStronghold = true;
                break;
            }
        }

        if (!isStronghold) {
            return;
        }

        List<ChunkPos> positions = new ArrayList<>();

        for (BlockPos pos : StrongholdPositionGenerator.POSITIONS) {
            positions.add(new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4));
        }

        cir.setReturnValue(
                CompletableFuture.completedFuture(positions)
        );
    }
}