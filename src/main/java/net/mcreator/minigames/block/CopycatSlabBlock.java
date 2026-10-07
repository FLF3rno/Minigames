package net.mcreator.minigames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mcreator.minigames.block.entity.CopycatBlockEntity;

public class CopycatSlabBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
	public static final EnumProperty<CopycatSlabType> TYPE = EnumProperty.create("type", CopycatSlabType.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape BOTTOM_SHAPE = Block.box(0, 0, 0, 16, 8, 16);
	private static final VoxelShape TOP_SHAPE = Block.box(0, 8, 0, 16, 16, 16);
	private static final VoxelShape DOUBLE_SHAPE = Shapes.block();
	private static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 0, 16, 16, 8);
	private static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 8, 16, 16, 16);
	private static final VoxelShape WEST_SHAPE = Block.box(0, 0, 0, 8, 16, 16);
	private static final VoxelShape EAST_SHAPE = Block.box(8, 0, 0, 16, 16, 16);

	public CopycatSlabBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f).noOcclusion().lightLevel(state -> state.hasProperty(CopycatHelper.LIGHT_LEVEL) ? state.getValue(CopycatHelper.LIGHT_LEVEL) : 0));
		this.registerDefaultState(this.defaultBlockState().setValue(TYPE, CopycatSlabType.BOTTOM).setValue(WATERLOGGED, false).setValue(CopycatHelper.LIGHT_LEVEL, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED, CopycatHelper.LIGHT_LEVEL);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(TYPE)) {
			case TOP -> TOP_SHAPE;
			case DOUBLE -> DOUBLE_SHAPE;
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
			case EAST -> EAST_SHAPE;
			default -> BOTTOM_SHAPE;
		};
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		if (context.getPlayer() != null && (context.getPlayer().isSpectator() || !context.getPlayer().mayBuild())) {
			return false;
		}
		ItemStack stack = context.getItemInHand();
		CopycatSlabType slabType = state.getValue(TYPE);
		if (slabType == CopycatSlabType.DOUBLE || !stack.is(this.asItem())) {
			return false;
		}
		if (context.replacingClickedOnBlock()) {
			Direction face = context.getClickedFace();
			return switch (slabType) {
				case BOTTOM -> face == Direction.UP;
				case TOP -> face == Direction.DOWN;
				case NORTH -> face == Direction.SOUTH;
				case SOUTH -> face == Direction.NORTH;
				case WEST -> face == Direction.EAST;
				case EAST -> face == Direction.WEST;
				default -> false;
			};
		}
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		BlockState existingState = context.getLevel().getBlockState(pos);
		if (existingState.is(this)) {
			return existingState.setValue(TYPE, CopycatSlabType.DOUBLE).setValue(WATERLOGGED, false);
		}
		FluidState fluid = context.getLevel().getFluidState(pos);
		Direction face = context.getClickedFace();
		CopycatSlabType slabType;
		if (face == Direction.UP) {
			slabType = CopycatSlabType.BOTTOM;
		} else if (face == Direction.DOWN) {
			slabType = CopycatSlabType.TOP;
		} else {
			slabType = switch (face) {
				case NORTH -> CopycatSlabType.SOUTH;
				case SOUTH -> CopycatSlabType.NORTH;
				case WEST -> CopycatSlabType.EAST;
				case EAST -> CopycatSlabType.WEST;
				default -> CopycatSlabType.BOTTOM;
			};
		}
		return this.defaultBlockState().setValue(TYPE, slabType).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
		return state.getValue(TYPE) != CopycatSlabType.DOUBLE && SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
	}

	@Override
	public boolean canPlaceLiquid(LivingEntity entity, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
		return state.getValue(TYPE) != CopycatSlabType.DOUBLE && SimpleWaterloggedBlock.super.canPlaceLiquid(entity, level, pos, state, fluid);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
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
		if (stack.is(this.asItem()) && state.getValue(TYPE) != CopycatSlabType.DOUBLE) {
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
	public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
		return CopycatHelper.getSoundType(state, level, pos, entity);
	}

	@Override
	public float getFriction(BlockState state, LevelReader level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
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
	public void fallOn(Level level, BlockState state, BlockPos pos, net.minecraft.world.entity.Entity entity, double fallDistance) {
		CopycatHelper.fallOn(level, state, pos, entity, fallDistance);
	}

	@Override
	public void updateEntityMovementAfterFallOn(BlockGetter level, net.minecraft.world.entity.Entity entity) {
		CopycatHelper.updateEntityMovementAfterFallOn(level, entity);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.Entity entity) {
		CopycatHelper.stepOn(level, pos, state, entity);
	}
}