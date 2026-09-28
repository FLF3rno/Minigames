package net.mcreator.minigames.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.init.MinigamesModBlockEntities;
import net.mcreator.minigames.block.entity.AscenscionBlockEntity;

public class AscenscionBlock extends Block implements EntityBlock {
	public AscenscionBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f).lightLevel(state -> 7));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AscenscionBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return type == MinigamesModBlockEntities.ASCENSCION.get() ? (lvl, p, st, be) -> AscenscionBlockEntity.tick(lvl, p, st, (AscenscionBlockEntity) be) : null;
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		super.stepOn(level, pos, state, entity);
		if (!level.isClientSide() && entity instanceof Player player) {
			AscenscionBlockEntity.applyBeamEffects(player);
		}
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, level, pos, oldState, isMoving);
		if (!level.isClientSide()) {
			double topY = AscenscionBlockEntity.getTopY(level, pos);
			AscenscionBlockEntity.updateLights(level, pos, topY);
		}
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean isMoving) {
		super.affectNeighborsAfterRemoval(state, level, pos, isMoving);
		AscenscionBlockEntity.cleanUpLights(level, pos);
	}
}