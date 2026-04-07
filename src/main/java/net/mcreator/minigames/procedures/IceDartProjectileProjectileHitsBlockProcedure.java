package net.mcreator.minigames.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModBlocks;
import net.minecraft.world.entity.Entity;

public class IceDartProjectileProjectileHitsBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity sourceEntity) {
		world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.SPREADING_ICE.get().defaultBlockState(), 3);
	}

	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.setBlock(BlockPos.containing(x, y, z), MinigamesModBlocks.SPREADING_ICE.get().defaultBlockState(), 3);
	}
}
