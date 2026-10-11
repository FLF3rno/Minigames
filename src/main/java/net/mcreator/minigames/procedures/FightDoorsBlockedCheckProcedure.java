package net.mcreator.minigames.procedures;

import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.util.ProblemReporter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.MinigamesMod;

public class FightDoorsBlockedCheckProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		MinigamesMod.queueServerWork(20, () -> {
			ResourceKey<Level> dim = (world instanceof Level _lvl ? _lvl.dimension() : (world instanceof WorldGenLevel _wgl ? _wgl.getLevel().dimension() : Level.OVERWORLD));
			if (!dim.equals(ResourceKey.create(Registries.DIMENSION, Identifier.parse("minigames:dungeon_dimension")))) {
				return;
			}

			BlockPos bp = BlockPos.containing(x, y, z);
			BlockState current = world.getBlockState(bp);
			if (current.getBlock() != MinigamesModBlocks.FIGHT_DOORS.get()) {
				return;
			}

			Direction facing = getDirectionFromBlockState(current);
			BlockPos neighborPos = bp.relative(facing);
			BlockState neighborState = world.getBlockState(neighborPos);

			boolean isBlocked = neighborState.isAir()
					|| neighborState.is(net.minecraft.tags.BlockTags.create(Identifier.parse("minigames:replace")));

			if (isBlocked) {
				if ("church".equals(MinigamesModVariables.MapVariables.get(world).floorTypeDungeon)) {
					{
						BlockPos _bp = bp;
						BlockState _bs = Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState();
						BlockState _bso = current;
						for (Property<?> _propertyOld : _bso.getProperties()) {
							Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
							if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
								try {
									_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
								} catch (Exception e) {
								}
						}
						BlockEntity _be = world.getBlockEntity(_bp);
						CompoundTag _bnbt = null;
						if (_be != null) {
							_bnbt = _be.saveWithFullMetadata(world.registryAccess());
							_be.setRemoved();
						}
						world.setBlock(_bp, _bs, 3);
						if (_bnbt != null) {
							_be = world.getBlockEntity(_bp);
							if (_be != null) {
								try {
									_be.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), _bnbt));
								} catch (Exception ignored) {
								}
							}
						}
					}
				} else {
					{
						BlockPos _bp = bp;
						BlockState _bs = MinigamesModBlocks.FIGHT_DOORS_BLOCKED.get().defaultBlockState();
						BlockState _bso = current;
						for (Property<?> _propertyOld : _bso.getProperties()) {
							Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
							if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
								try {
									_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
								} catch (Exception e) {
								}
						}
						BlockEntity _be = world.getBlockEntity(_bp);
						CompoundTag _bnbt = null;
						if (_be != null) {
							_bnbt = _be.saveWithFullMetadata(world.registryAccess());
							_be.setRemoved();
						}
						world.setBlock(_bp, _bs, 3);
						if (_bnbt != null) {
							_be = world.getBlockEntity(_bp);
							if (_be != null) {
								try {
									_be.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), _bnbt));
								} catch (Exception ignored) {
								}
							}
						}
					}
				}
			}
		});
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