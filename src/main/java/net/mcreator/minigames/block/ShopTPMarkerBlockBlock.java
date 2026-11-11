package net.mcreator.minigames.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.ShopTPMarkerExecuteProcedure;

public class ShopTPMarkerBlockBlock extends Block {
	public ShopTPMarkerBlockBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.GRAVEL).strength(1f, 10f));
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		ShopTPMarkerExecuteProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}
}