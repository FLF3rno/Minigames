package net.mcreator.minigames.block;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Containers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.GiantBellRingProcedure;
import net.mcreator.minigames.init.MinigamesModBlockEntities;
import net.mcreator.minigames.block.entity.GiantBellBlockEntity;

public class GiantBellBlock extends Block implements EntityBlock {
	public static final BooleanProperty ABILITYUSED = BooleanProperty.create("abilityused");

	private static final VoxelShape BODY = box(-12, 16, -12, 28, 48, 28);
	private static final VoxelShape CROWN = box(-10, 48, -10, 26, 58, 26);
	private static final VoxelShape MOUNT = box(3, 58, 3, 13, 66, 13);

	private static final VoxelShape SHAPE = Shapes.or(
		box(-16, 0, -16, 32, 16, 32),
		BODY,
		CROWN,
		MOUNT
	);

	public GiantBellBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.METAL).strength(1f, 10f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(ABILITYUSED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
		if (!player.mayBuild() && !player.isCreative()) {
			return 0.0f;
		}
		return super.getDestroyProgress(state, player, level, pos);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
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

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ABILITYUSED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null)
			return null;
		return state.setValue(ABILITYUSED, false);
	}

	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player entity) {
		super.attack(state, world, pos, entity);
		Direction direction = entity.getDirection().getOpposite();
		GiantBellRingProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity, direction);
	}

	@Override
	public void onProjectileHit(Level world, BlockState blockstate, BlockHitResult hit, Projectile entity) {
		Direction dir = hit.getDirection();
		if (dir.getAxis() == Direction.Axis.Y && entity != null) {
			dir = entity.getDirection().getOpposite();
		}
		GiantBellRingProcedure.execute(world, hit.getBlockPos().getX(), hit.getBlockPos().getY(), hit.getBlockPos().getZ(), entity, dir);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		Direction direction = hit.getDirection();
		if (direction.getAxis() == Direction.Axis.Y && entity != null) {
			direction = entity.getDirection().getOpposite();
		}
		GiantBellRingProcedure.execute(world, x, y, z, entity, direction);
		return InteractionResult.SUCCESS;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (!level.isClientSide()) {
			return null;
		}
		return type == MinigamesModBlockEntities.GIANT_BELL.get() ? (lvl, pos, st, be) -> ((GiantBellBlockEntity) be).clientTick(lvl, pos, st) : null;
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GiantBellBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState blockstate, ServerLevel world, BlockPos blockpos, boolean flag) {
		Containers.updateNeighboursAfterDestroy(blockstate, world, blockpos);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos, Direction direction) {
		BlockEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof GiantBellBlockEntity be)
			return AbstractContainerMenu.getRedstoneSignalFromContainer(be);
		else
			return 0;
	}
}