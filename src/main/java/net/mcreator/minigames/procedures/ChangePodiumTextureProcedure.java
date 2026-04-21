package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.entity.SpleefPodiumPlayerEntity;

import java.util.ArrayList;

public class ChangePodiumTextureProcedure {
	public static void execute(LevelAccessor world, double position, String uuid) {
		if (uuid == null)
			return;
		for (Entity entityiterator : new ArrayList<>(world.getEntities(null, new AABB(-30000000, -64, -30000000, 30000000, 320, 30000000)))) {
			if (entityiterator instanceof SpleefPodiumPlayerEntity) {
				int podiumPosition = entityiterator instanceof SpleefPodiumPlayerEntity _datEntI ? _datEntI.getEntityData().get(SpleefPodiumPlayerEntity.DATA_position) : 0;
				if (podiumPosition == 0) {
					BlockPos below = BlockPos.containing(entityiterator.getX(), entityiterator.getY() - 1, entityiterator.getZ());
					if (world.getBlockState(below).getBlock() == Blocks.GOLD_BLOCK) {
						podiumPosition = 1;
					} else if (world.getBlockState(below).getBlock() == Blocks.IRON_BLOCK) {
						podiumPosition = 2;
					} else if (world.getBlockState(below).getBlock() == Blocks.WAXED_COPPER_BLOCK) {
						podiumPosition = 3;
					}
					if (podiumPosition != 0 && entityiterator instanceof SpleefPodiumPlayerEntity _datEntSetI)
						_datEntSetI.getEntityData().set(SpleefPodiumPlayerEntity.DATA_position, podiumPosition);
				}
				if (podiumPosition == position) {
					if (entityiterator instanceof SpleefPodiumPlayerEntity _datEntSetS)
						_datEntSetS.getEntityData().set(SpleefPodiumPlayerEntity.DATA_display_uuid, uuid);
				}
			}
		}
	}
}
