package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.MinigamesMod;

public class InflatableWallSoundProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		// Sound is handled only for the initial placement in InflatableWallAddedProcedure.
		MinigamesMod.queueServerWork(1, () -> {
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateY") > 0) {
				world.setBlock(BlockPos.containing(x, y + 1, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y + 1, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateY", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateY") - 1));
						_blockEntity.getPersistentData().putDouble("inflateX", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateX")));
						_blockEntity.getPersistentData().putDouble("inflateZ", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZ")));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateY") < 0) {
				world.setBlock(BlockPos.containing(x, y - 1, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y - 1, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateY", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateY") + 1));
						_blockEntity.getPersistentData().putDouble("inflateX", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateX")));
						_blockEntity.getPersistentData().putDouble("inflateZ", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZ")));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateXonce") > 0) {
				world.setBlock(BlockPos.containing(x + 1, y, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x + 1, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateXonce", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateXonce") - 1));
						_blockEntity.getPersistentData().putDouble("inflateZ", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZ")));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateXonce") < 0) {
				world.setBlock(BlockPos.containing(x - 1, y, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x - 1, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateXonce", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateXonce") + 1));
						_blockEntity.getPersistentData().putDouble("inflateZ", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZ")));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZonce") > 0) {
				world.setBlock(BlockPos.containing(x, y, z + 1), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z + 1);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateZonce", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZonce") - 1));
						_blockEntity.getPersistentData().putDouble("inflateX", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateX")));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZonce") < 0) {
				world.setBlock(BlockPos.containing(x, y, z - 1), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z - 1);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateZonce", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZonce") + 1));
						_blockEntity.getPersistentData().putDouble("inflateX", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateX")));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateX") > 0) {
				world.setBlock(BlockPos.containing(x + 1, y, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				world.setBlock(BlockPos.containing(x - 1, y, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x + 1, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateX", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateX") - 1));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x - 1, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateX", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateX") - 1));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
			if (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZ") > 0) {
				world.setBlock(BlockPos.containing(x, y, z + 1), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				world.setBlock(BlockPos.containing(x, y, z - 1), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z + 1);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateZ", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZ") - 1));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z - 1);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateZ", (getBlockNBTNumber(world, BlockPos.containing(x, y, z), "inflateZ") - 1));
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		});
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}
