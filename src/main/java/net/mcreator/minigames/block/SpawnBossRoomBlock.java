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
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.block.entity.SpawnBossRoomBlockEntity;

public class SpawnBossRoomBlock extends Block implements EntityBlock {
	public static final BooleanProperty CLOCKWISE = BooleanProperty.create("clockwise");
	public static final BooleanProperty ANTICLOCKWISE = BooleanProperty.create("anticlockwise");
	public static final BooleanProperty STRUCTURE = BooleanProperty.create("structure");

	public SpawnBossRoomBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.EMPTY).strength(-1, 3600000));
		this.registerDefaultState(this.stateDefinition.any().setValue(CLOCKWISE, false).setValue(ANTICLOCKWISE, false).setValue(STRUCTURE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(CLOCKWISE, ANTICLOCKWISE, STRUCTURE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null)
			return null;
		return state.setValue(CLOCKWISE, false).setValue(ANTICLOCKWISE, false).setValue(STRUCTURE, false);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Player entity) {
		return ItemStack.EMPTY;
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpawnBossRoomBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}
}