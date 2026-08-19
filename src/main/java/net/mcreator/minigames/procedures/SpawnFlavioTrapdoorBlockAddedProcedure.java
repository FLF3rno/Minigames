package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModEntities;

import java.util.ArrayList;

public class SpawnFlavioTrapdoorBlockAddedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		boolean spawn = false;
		spawn = true;
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (entityiterator instanceof Player _plr0 && _plr0.gameMode() == GameType.CREATIVE) {
				spawn = false;
			}
		}
		if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.SOUTH) {
			if (spawn == true) {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
				if (world instanceof ServerLevel _level) {
					Entity entityToSpawn = MinigamesModEntities.FLAVIO_TRAPDOOR.get().spawn(_level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED);
					if (entityToSpawn != null) {
						entityToSpawn.setYRot(180);
						entityToSpawn.setYBodyRot(180);
						entityToSpawn.setYHeadRot(180);
						entityToSpawn.setDeltaMovement(0, 0, 0);
					}
				}
			}
		} else if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.WEST) {
			if (spawn == true) {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
				if (world instanceof ServerLevel _level) {
					Entity entityToSpawn = MinigamesModEntities.FLAVIO_TRAPDOOR.get().spawn(_level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED);
					if (entityToSpawn != null) {
						entityToSpawn.setYRot(270);
						entityToSpawn.setYBodyRot(270);
						entityToSpawn.setYHeadRot(270);
						entityToSpawn.setDeltaMovement(0, 0, 0);
					}
				}
			}
		} else if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.EAST) {
			if (spawn == true) {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
				if (world instanceof ServerLevel _level) {
					Entity entityToSpawn = MinigamesModEntities.FLAVIO_TRAPDOOR.get().spawn(_level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED);
					if (entityToSpawn != null) {
						entityToSpawn.setYRot(90);
						entityToSpawn.setYBodyRot(90);
						entityToSpawn.setYHeadRot(90);
						entityToSpawn.setDeltaMovement(0, 0, 0);
					}
				}
			}
		} else if ((getDirectionFromBlockState((world.getBlockState(BlockPos.containing(x, y, z))))) == Direction.NORTH) {
			if (spawn == true) {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
				if (world instanceof ServerLevel _level) {
					Entity entityToSpawn = MinigamesModEntities.FLAVIO_TRAPDOOR.get().spawn(_level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED);
					if (entityToSpawn != null) {
						entityToSpawn.setYRot((float) 0.1);
						entityToSpawn.setYBodyRot((float) 0.1);
						entityToSpawn.setYHeadRot((float) 0.1);
						entityToSpawn.setDeltaMovement(0, 0, 0);
					}
				}
			}
		}
	}

	private static Direction getDirectionFromBlockState(BlockState blockState) {
		if (getPropertyByName(blockState, "facing") instanceof EnumProperty ep && ep.getValueClass() == Direction.class)
			return (Direction) blockState.getValue(ep);
		if (getPropertyByName(blockState, "axis") instanceof EnumProperty ep && ep.getValueClass() == Direction.Axis.class)
			return Direction.fromAxisAndDirection((Direction.Axis) blockState.getValue(ep), Direction.AxisDirection.POSITIVE);
		return Direction.NORTH;
	}

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}
}