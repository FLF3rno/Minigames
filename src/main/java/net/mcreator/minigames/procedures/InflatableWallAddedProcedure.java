package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.init.MinigamesModBlocks;

public class InflatableWallAddedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Direction clickedFace, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		itemstack.shrink(1);
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:inflate")), SoundSource.BLOCKS, (float) 0.1, 1);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:inflate")), SoundSource.BLOCKS, (float) 0.1, 1, false);
			}
		}
		if (clickedFace == Direction.UP) {
			world.setBlock(BlockPos.containing(x, y + 1, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y + 1, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("inflateY", 2);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if ((entity.getDirection()) == Direction.WEST || (entity.getDirection()) == Direction.EAST) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y + 1, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateZ", 2);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y + 1, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateX", 2);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		} else if (clickedFace == Direction.DOWN) {
			world.setBlock(BlockPos.containing(x, y - 1, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y - 1, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("inflateY", (-2));
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if ((entity.getDirection()) == Direction.WEST || (entity.getDirection()) == Direction.EAST) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y - 1, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateZ", 2);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			} else {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y - 1, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putDouble("inflateX", 2);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		} else if (clickedFace == Direction.NORTH) {
			world.setBlock(BlockPos.containing(x, y, z - 1), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z - 1);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("inflateZonce", (-2));
					_blockEntity.getPersistentData().putDouble("inflateX", 2);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else if (clickedFace == Direction.SOUTH) {
			world.setBlock(BlockPos.containing(x, y, z + 1), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z + 1);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("inflateZonce", 2);
					_blockEntity.getPersistentData().putDouble("inflateX", 2);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else if (clickedFace == Direction.WEST) {
			world.setBlock(BlockPos.containing(x - 1, y, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x - 1, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("inflateXonce", (-2));
					_blockEntity.getPersistentData().putDouble("inflateZ", 2);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else if (clickedFace == Direction.EAST) {
			world.setBlock(BlockPos.containing(x + 1, y, z), MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get().defaultBlockState(), 3);
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x + 1, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null) {
					_blockEntity.getPersistentData().putDouble("inflateXonce", 2);
					_blockEntity.getPersistentData().putDouble("inflateZ", 2);
				}
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		}
	}
}
