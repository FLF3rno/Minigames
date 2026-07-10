package net.mcreator.minigames.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class TuffPedestalBlock extends Block {
	private static final VoxelShape SHAPE = Shapes.or(box(1, 0, 1, 15, 1, 15), box(2, 1, 2, 14, 3, 14), box(4, 3, 4, 12, 11, 12), box(3, 11, 3, 13, 12, 13), box(2, 12, 2, 14, 13, 14));

	public TuffPedestalBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.TUFF).strength(1f, 10f).requiresCorrectToolForDrops().noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state) {
		return true;
	}

	@Override
	public int getLightDampening(BlockState state) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}
}