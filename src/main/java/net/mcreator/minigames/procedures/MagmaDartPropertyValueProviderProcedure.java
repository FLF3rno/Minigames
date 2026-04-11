package net.mcreator.minigames.procedures;

import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;

public class MagmaDartPropertyValueProviderProcedure {
	private static final int MIN_ARENA_COORD = -20;
	private static final int MAX_ARENA_COORD = 20;
	private static final double LOOK_CHECK_DISTANCE = 10;

	public static double execute(Entity entity) {
		if (shouldTriggerInstantly(entity)) {
			return 1;
		}
		return 0;
	}

	public static boolean shouldTriggerInstantly(Entity entity) {
		if (entity == null)
			return false;
		Vec3 eyePosition = entity.getEyePosition(1);
		Vec3 lookEnd = eyePosition.add(entity.getLookAngle().scale(LOOK_CHECK_DISTANCE));
		BlockHitResult hitResult = entity.level().clip(new ClipContext(eyePosition, lookEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
		if (hitResult.getType() == HitResult.Type.MISS)
			return true;
		BlockPos hitPos = hitResult.getBlockPos();
		if (!isInsideArenaBounds(entity, hitPos))
			return true;
		BlockState hitState = entity.level().getBlockState(hitPos);
		return hitState.isAir();
	}

	private static boolean isInsideArenaBounds(Entity entity, BlockPos blockPos) {
		Vec3 arenaCenter = MinigamesModVariables.MapVariables.get(entity.level()).spleefMapMiddleX;
		return blockPos.getX() >= arenaCenter.x() + MIN_ARENA_COORD && blockPos.getX() <= arenaCenter.x() + MAX_ARENA_COORD && blockPos.getZ() >= arenaCenter.z() + MIN_ARENA_COORD
				&& blockPos.getZ() <= arenaCenter.z() + MAX_ARENA_COORD;
	}
}
