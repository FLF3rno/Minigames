package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.init.MinigamesModBlocks;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class InflatableWallAddedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Direction clickedFace, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		itemstack.shrink(1);
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:inflate")), SoundSource.BLOCKS, (float) 0.1, 1);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:inflate")), SoundSource.BLOCKS, (float) 0.1, 1, false);
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
		queueDeflate(world, getPlacedPos(x, y, z, clickedFace));
	}

	private static BlockPos getPlacedPos(double x, double y, double z, Direction clickedFace) {
		return switch (clickedFace) {
			case UP -> BlockPos.containing(x, y + 1, z);
			case DOWN -> BlockPos.containing(x, y - 1, z);
			case NORTH -> BlockPos.containing(x, y, z - 1);
			case SOUTH -> BlockPos.containing(x, y, z + 1);
			case WEST -> BlockPos.containing(x - 1, y, z);
			case EAST -> BlockPos.containing(x + 1, y, z);
		};
	}

	private static void queueDeflate(LevelAccessor world, BlockPos origin) {
		MinigamesMod.queueServerWork(300, () -> {
			if (!(world instanceof Level level) || level.isClientSide()) {
				return;
			}
			if (!level.getBlockState(origin).is(MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get())) {
				return;
			}
			playDeflateSound(level, origin);
			clearWall(level, origin);
		});
	}

	private static void playDeflateSound(Level level, BlockPos pos) {
		level.playSound(null, pos, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:pop")), SoundSource.BLOCKS, 0.3f, 1f);
	}

	private static void clearWall(LevelAccessor world, BlockPos origin) {
		ArrayDeque<BlockPos> queue = new ArrayDeque<>();
		Set<BlockPos> visited = new HashSet<>();
		queue.add(origin);

		while (!queue.isEmpty() && visited.size() < 128) {
			BlockPos current = queue.removeFirst();
			if (!visited.add(current)) {
				continue;
			}
			if (!world.getBlockState(current).is(MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get())) {
				continue;
			}

			world.setBlock(current, Blocks.AIR.defaultBlockState(), 3);
			for (Direction direction : Direction.values()) {
				BlockPos neighbor = current.relative(direction);
				if (!visited.contains(neighbor) && world.getBlockState(neighbor).is(MinigamesModBlocks.INFLATABLE_WALL_BLOCK.get())) {
					queue.add(neighbor);
				}
			}
		}
	}
}
