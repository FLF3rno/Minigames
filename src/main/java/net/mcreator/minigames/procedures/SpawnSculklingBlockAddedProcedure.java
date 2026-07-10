package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModEntities;

import java.util.ArrayList;

public class SpawnSculklingBlockAddedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		boolean spawn = false;
		spawn = true;
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (entityiterator instanceof Player _plr0 && _plr0.gameMode() == GameType.CREATIVE) {
				spawn = false;
			}
		}
		if (spawn == true) {
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = MinigamesModEntities.SCULKLING.get().spawn(_level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setDeltaMovement(0, 0, 0);
				}
			}
		}
	}
}