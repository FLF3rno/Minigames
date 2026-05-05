package net.mcreator.minigames.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.SpawnShieldAngelBlockAddedProcedure;

public class SpawnShieldAngelBlock extends Block {
	public SpawnShieldAngelBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.EMPTY).strength(1f, 10f));
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		SpawnShieldAngelBlockAddedProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}
}