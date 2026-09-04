package net.mcreator.minigames.entity.I;

import net.minecraft.core.BlockPos;

public interface IDiggerMob {
    boolean isDigging();
    void beginDigging(BlockPos pos);
}