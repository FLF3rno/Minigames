package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;

public class RouletteGridDestroyedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.ROULETTE_GRID.get().defaultBlockState(), 3);
		if (blockstate.getBlock() == (blockStateWithEnum(MinigamesModBlocks.ROULETTE_GRID.get().defaultBlockState(), "grindstate", "none")).getBlock()) {
			{
				String _value = "mined_none";
				BlockPos _pos = BlockPos.containing(x, y, z);
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("grindstate") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
					world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
			}
		} else if (blockstate.getBlock() == (blockStateWithEnum(MinigamesModBlocks.ROULETTE_GRID.get().defaultBlockState(), "grindstate", "tnt")).getBlock()) {
			{
				String _value = "mined_tnt";
				BlockPos _pos = BlockPos.containing(x, y, z);
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("grindstate") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
					world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
			}
		} else if (blockstate.getBlock() == (blockStateWithEnum(MinigamesModBlocks.ROULETTE_GRID.get().defaultBlockState(), "grindstate", "powerup")).getBlock()) {
			{
				String _value = "mined_powerup";
				BlockPos _pos = BlockPos.containing(x, y, z);
				BlockState _bs = world.getBlockState(_pos);
				if (_bs.getBlock().getStateDefinition().getProperty("grindstate") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
					world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
			}
		}
	}

	private static BlockState blockStateWithEnum(BlockState blockState, String property, String newValue) {
		Property<?> prop = blockState.getBlock().getStateDefinition().getProperty(property);
		return prop instanceof EnumProperty ep && ep.getValue(newValue).isPresent() ? blockState.setValue(ep, (Enum) ep.getValue(newValue).get()) : blockState;
	}
}