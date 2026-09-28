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
import net.mcreator.minigames.block.entity.CopycatBlockEntity;

public class CopycatBlockBlock extends Block implements EntityBlock {
	public CopycatBlockBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f).noOcclusion());
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
}