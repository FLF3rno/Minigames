package net.mcreator.minigames.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.MovingBlockSpawnBlockAddedProcedure;

public class MovingBlockSpawnBlock extends Block {
	public MovingBlockSpawnBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(-1, 3600000));
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		MovingBlockSpawnBlockAddedProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}
}