package net.mcreator.minigames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mcreator.minigames.block.entity.CopycatBlockEntity;

public class CopycatLayerBlock extends Block implements EntityBlock {
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

	private static final VoxelShape[] SHAPES = new VoxelShape[] {
		Shapes.empty(),
		Block.box(0, 0, 0, 16, 2, 16),
		Block.box(0, 0, 0, 16, 4, 16),
		Block.box(0, 0, 0, 16, 6, 16),
		Block.box(0, 0, 0, 16, 8, 16),
		Block.box(0, 0, 0, 16, 10, 16),
		Block.box(0, 0, 0, 16, 12, 16),
		Block.box(0, 0, 0, 16, 14, 16),
		Block.box(0, 0, 0, 16, 16, 16)
	};

	public CopycatLayerBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f).noOcclusion());
		this.registerDefaultState(this.defaultBlockState().setValue(LAYERS, 1));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAYERS);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		if (context.getPlayer() != null && (context.getPlayer().isSpectator() || !context.getPlayer().mayBuild())) {
			return false;
		}
		int layers = state.getValue(LAYERS);
		if (context.getItemInHand().is(this.asItem()) && layers < 8) {
			if (context.replacingClickedOnBlock()) {
				return context.getClickedFace() == Direction.UP;
			}
			return true;
		}
		return false;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState existingState = context.getLevel().getBlockState(context.getClickedPos());
		if (existingState.is(this)) {
			int layers = existingState.getValue(LAYERS);
			return existingState.setValue(LAYERS, Math.min(8, layers + 1));
		}
		return this.defaultBlockState();
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CopycatBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (player.isSpectator() || !player.mayBuild()) {
			return InteractionResult.PASS;
		}
		if (stack.is(this.asItem()) && state.getValue(LAYERS) < 8) {
			return InteractionResult.PASS;
		}
		return CopycatHelper.handleUseItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean isMoving) {
		super.affectNeighborsAfterRemoval(state, level, pos, isMoving);
		CopycatHelper.handleOnRemove(state, level, pos, state, isMoving);
	}

	@Override
	public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
		return CopycatHelper.getSoundType(state, level, pos, entity);
	}

	@Override
	public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
		return CopycatHelper.getFriction(state, level, pos, entity);
	}

	@Override
	public boolean hasDynamicLightEmission(BlockState state) {
		return true;
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		return CopycatHelper.getLightEmission(state, level, pos);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		CopycatHelper.fallOn(level, state, pos, entity, fallDistance);
	}

	@Override
	public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
		CopycatHelper.updateEntityMovementAfterFallOn(level, entity);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		CopycatHelper.stepOn(level, pos, state, entity);
	}
}