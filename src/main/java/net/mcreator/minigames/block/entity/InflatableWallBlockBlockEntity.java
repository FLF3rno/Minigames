package net.mcreator.minigames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.mcreator.minigames.init.MinigamesModBlockEntities;

public class InflatableWallBlockBlockEntity extends BlockEntity {
	public InflatableWallBlockBlockEntity(BlockPos pos, BlockState state) {
		super(MinigamesModBlockEntities.INFLATABLE_WALL_BLOCK.get(), pos, state);
	}
}
