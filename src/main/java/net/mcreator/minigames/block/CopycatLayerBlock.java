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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mcreator.minigames.block.entity.CopycatBlockEntity;

public class CopycatLayerBlock extends Block implements EntityBlock {
	public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

	private static final VoxelShape[][] SHAPES = new VoxelShape[6][9];

	static {
		for (Direction dir : Direction.values()) {
			int d = dir.get3DDataValue();
			SHAPES[d][0] = Shapes.empty();
			for (int i = 1; i <= 8; i++) {
				int size = i * 2;
				SHAPES[d][i] = switch (dir) {
					case DOWN -> Block.box(0, 16 - size, 0, 16, 16, 16);
					case UP -> Block.box(0, 0, 0, 16, size, 16);
					case NORTH -> Block.box(0, 0, 16 - size, 16, 16, 16);
					case SOUTH -> Block.box(0, 0, 0, 16, 16, size);
					case WEST -> Block.box(16 - size, 0, 0, 16, 16, 16);
					case EAST -> Block.box(0, 0, 0, size, 16, 16);
				};
			}
		}
	}

	public CopycatLayerBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f).noOcclusion().lightLevel(state -> state.hasProperty(CopycatHelper.LIGHT_LEVEL) ? state.getValue(CopycatHelper.LIGHT_LEVEL) : 0));
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP).setValue(LAYERS, 1).setValue(CopycatHelper.LIGHT_LEVEL, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LAYERS, CopycatHelper.LIGHT_LEVEL);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		Direction facing = state.hasProperty(FACING) ? state.getValue(FACING) : Direction.UP;
		int layers = state.hasProperty(LAYERS) ? state.getValue(LAYERS) : 1;
		return SHAPES[facing.get3DDataValue()][layers];
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		if (context.getPlayer() != null && (context.getPlayer().isSpectator() || !context.getPlayer().mayBuild())) {
			return false;
		}
		int layers = state.getValue(LAYERS);
		if (context.getItemInHand().is(this.asItem()) && layers < 8) {
			if (context.replacingClickedOnBlock()) {
				return context.getClickedFace() == state.getValue(FACING);
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
		return this.defaultBlockState().setValue(FACING, context.getClickedFace());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
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