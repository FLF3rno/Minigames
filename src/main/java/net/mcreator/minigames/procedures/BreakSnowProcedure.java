package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModBlocks;

public class BreakSnowProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((world.getBlockState(BlockPos.containing(x, y, z))).is(BlockTags.create(Identifier.parse("minigames:spleefables")))) {
			if (!world.isClientSide()) {
				BlockBreakSimulationProcedure.execute(world, x, y, z, world.getBlockState(BlockPos.containing(x, y, z)), false, true);
			}
			if (MinigamesModVariables.MapVariables.get(world).glueY.contains(Math.round(y))) {
				world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
			} else {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			}
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.snowballCountSpleef = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef + 0.75;
				_vars.markSyncDirty();
			}
			SpleefPowerupProcedure.execute(world, entity);
		}
	}
}