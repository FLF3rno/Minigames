package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModBlocks;

public class PlaceTNTRouletteGridProcedure {
	public static void execute(LevelAccessor world) {
		for (int index = 0; index < (int) ((MinigamesModVariables.MapVariables.get(world).TNTRouletteGridHeight * MinigamesModVariables.MapVariables.get(world).TNTRouletteGridLength)); index++) {
			world.setBlock(BlockPos.containing(Math.floor(index / MinigamesModVariables.MapVariables.get(world).TNTRouletteGridLength), 80, index % MinigamesModVariables.MapVariables.get(world).TNTRouletteGridLength),
					MinigamesModBlocks.ROULETTE_GRID.get().defaultBlockState(), 3);
			if ((MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) index) instanceof String _str4 ? _str4 : "").equals("tnt")) {
				{
					String _value = "tnt";
					BlockPos _pos = BlockPos.containing(Math.floor(index / MinigamesModVariables.MapVariables.get(world).TNTRouletteGridLength), 80, index % MinigamesModVariables.MapVariables.get(world).TNTRouletteGridLength);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("gridstate") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
						world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
				}
			} else if ((MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) index) instanceof String _str9 ? _str9 : "").equals("powerup")) {
				{
					String _value = "powerup";
					BlockPos _pos = BlockPos.containing(Math.floor(index / MinigamesModVariables.MapVariables.get(world).TNTRouletteGridLength), 80, index % MinigamesModVariables.MapVariables.get(world).TNTRouletteGridLength);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("gridstate") instanceof EnumProperty _enumProp && _enumProp.getValue(_value).isPresent())
						world.setBlock(_pos, _bs.setValue(_enumProp, (Enum) _enumProp.getValue(_value).get()), 3);
				}
			}
		}
	}
}