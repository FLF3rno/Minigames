package net.mcreator.minigames.block;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.SpawnMinibossRoomProcedureProcedure;
import net.mcreator.minigames.block.entity.SpawnMinibossRoomBlockEntity;

public class SpawnMinibossRoomBlock extends Block implements EntityBlock {
	public static final BooleanProperty CLOCKWISE = BooleanProperty.create("clockwise");
	public static final BooleanProperty ANTICLOCKWISE = BooleanProperty.create("anticlockwise");
	public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");
	public static final BooleanProperty STRUCTURE = BooleanProperty.create("structure");

	public SpawnMinibossRoomBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.EMPTY).strength(-1, 3600000));
		this.registerDefaultState(this.stateDefinition.any().setValue(CLOCKWISE, false).setValue(ANTICLOCKWISE, false).setValue(FLIPPED, false).setValue(STRUCTURE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(CLOCKWISE, ANTICLOCKWISE, FLIPPED, STRUCTURE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(CLOCKWISE, false).setValue(ANTICLOCKWISE, false).setValue(FLIPPED, false).setValue(STRUCTURE, false);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Player entity) {
		return ItemStack.EMPTY;
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(blockstate, world, pos, random);
		SpawnMinibossRoomProcedureProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), blockstate);
		world.scheduleTick(pos, this, 1);
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpawnMinibossRoomBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}
}