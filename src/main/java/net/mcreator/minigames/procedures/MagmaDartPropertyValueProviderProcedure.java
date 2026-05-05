package net.mcreator.minigames.procedures;

import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MagmaDartPropertyValueProviderProcedure {
	public static double execute(Entity entity) {
		return execute(entity, null);
	}

	public static double execute(Entity entity, ItemStack itemstack) {
		if (shouldTriggerInstantly(entity)) {
			return 1;
		}
		return 0;
	}

	public static boolean shouldTriggerInstantly(Entity entity) {
		if (entity == null)
			return false;
		Vec3 eyePosition = entity.getEyePosition(1);
		Vec3 lookEnd = eyePosition.add(entity.getLookAngle().scale(30));
		BlockHitResult hitResult = entity.level().clip(new ClipContext(eyePosition, lookEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
		if (hitResult.getType() == HitResult.Type.MISS)
			return true;
		double yDiff = Math.abs(hitResult.getBlockPos().getY() - entity.getY());
		return yDiff <= 2;
	}
}
