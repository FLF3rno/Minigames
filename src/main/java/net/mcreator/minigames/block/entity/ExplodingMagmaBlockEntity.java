package net.mcreator.minigames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.mcreator.minigames.init.MinigamesModBlockEntities;

public class ExplodingMagmaBlockEntity extends BlockEntity {
	public ExplodingMagmaBlockEntity(BlockPos pos, BlockState state) {
		super(MinigamesModBlockEntities.EXPLODING_MAGMA.get(), pos, state);
	}
}
