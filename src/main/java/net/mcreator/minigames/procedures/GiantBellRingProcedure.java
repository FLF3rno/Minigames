package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.block.entity.GiantBellBlockEntity;

public class GiantBellRingProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(world, x, y, z, entity, Direction.NORTH);
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Direction hitDirection) {
		if (entity == null)
			return;
		if (hitDirection == null)
			hitDirection = Direction.NORTH;
		BlockPos pos = BlockPos.containing(x, y, z);
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, pos, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.bell.use")), SoundSource.BLOCKS, 4, (float) 0.4);
				_level.blockEvent(pos, _level.getBlockState(pos).getBlock(), 1, hitDirection.get3DDataValue());

				int radius = 10;
				for (BlockPos targetPos : BlockPos.betweenClosed(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius))) {
					if (targetPos.distSqr(pos) <= radius * radius) {
						BlockState targetState = _level.getBlockState(targetPos);
						if (targetState.is(BlockTags.create(Identifier.parse("minigames:shatterable")))) {
							_level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
							BlockBreakSimulationProcedure.execute(_level, targetPos.getX(), targetPos.getY(), targetPos.getZ(), targetState, true, true);
						}
					}
				}

				ExplodeProcedure.execute(world, x, y, z, entity, false, true, 0, 0, 10, "shatter_glass");
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.bell.use")), SoundSource.BLOCKS, 4, (float) 0.4, false);
				if (world.getBlockEntity(pos) instanceof GiantBellBlockEntity bellEntity) {
					bellEntity.startRinging(hitDirection);
				}
			}
		}
	}
}