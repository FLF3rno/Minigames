package net.mcreator.minigames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mcreator.minigames.block.entity.CopycatBlockEntity;

import net.minecraft.world.level.block.state.StateDefinition;

public class CopycatBlockBlock extends Block implements EntityBlock {
	public CopycatBlockBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f).noOcclusion().lightLevel(state -> state.hasProperty(CopycatHelper.LIGHT_LEVEL) ? state.getValue(CopycatHelper.LIGHT_LEVEL) : 0));
		this.registerDefaultState(this.stateDefinition.any().setValue(CopycatHelper.LIGHT_LEVEL, 0).setValue(CopycatHelper.TRANSPARENT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(CopycatHelper.LIGHT_LEVEL, CopycatHelper.TRANSPARENT);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CopycatBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
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

	@Override
	protected boolean propagatesSkylightDown(BlockState state) {
		return state.hasProperty(CopycatHelper.TRANSPARENT) && state.getValue(CopycatHelper.TRANSPARENT);
	}

	@Override
	protected int getLightDampening(BlockState state) {
		return (state.hasProperty(CopycatHelper.TRANSPARENT) && state.getValue(CopycatHelper.TRANSPARENT)) ? 0 : 15;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof CopycatBlockEntity copycat && copycat.hasMaterial()) {
			return copycat.getMaterial().getShadeBrightness(level, pos);
		}
		return (state.hasProperty(CopycatHelper.TRANSPARENT) && state.getValue(CopycatHelper.TRANSPARENT)) ? 1.0F : 0.2F;
	}

	@Override
	protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.hasProperty(CopycatHelper.TRANSPARENT) && state.getValue(CopycatHelper.TRANSPARENT)) {
			return Shapes.empty();
		}
		return super.getVisualShape(state, level, pos, context);
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState state) {
		return false;
	}
}