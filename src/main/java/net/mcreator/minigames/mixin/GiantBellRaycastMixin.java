package net.mcreator.minigames.mixin;

import net.mcreator.minigames.block.entity.GiantBellBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class GiantBellRaycastMixin {
	@Inject(method = "pick", at = @At("RETURN"), cancellable = true)
	private void minigames$pickGiantBell(double maxDistance, float partialTicks, boolean hitFluids, CallbackInfoReturnable<HitResult> cir) {
		if (GiantBellBlockEntity.ACTIVE_BELLS.isEmpty()) {
			return;
		}

		Entity entity = (Entity) (Object) this;
		Level level = entity.level();
		if (level == null) {
			return;
		}

		Vec3 from = entity.getEyePosition(partialTicks);
		Vec3 view = entity.getViewVector(partialTicks);
		Vec3 to = from.add(view.scale(maxDistance));

		HitResult currentHit = cir.getReturnValue();
		double closestDistSqr = (currentHit != null && currentHit.getType() != HitResult.Type.MISS)
				? currentHit.getLocation().distanceToSqr(from)
				: maxDistance * maxDistance;

		BlockHitResult bestBellHit = null;

		for (GiantBellBlockEntity be : GiantBellBlockEntity.ACTIVE_BELLS) {
			if (be.getLevel() == level && !be.isRemoved()) {
				BlockPos bellPos = be.getBlockPos();
				AABB bounds = new AABB(bellPos.getX() - 1, bellPos.getY(), bellPos.getZ() - 1,
						bellPos.getX() + 2, bellPos.getY() + 4.125, bellPos.getZ() + 2);
				if (bounds.clip(from, to).isPresent()) {
					VoxelShape shape = be.getBlockState().getShape(level, bellPos);
					BlockHitResult hit = shape.clip(from, to, bellPos);
					if (hit != null && hit.getType() != HitResult.Type.MISS) {
						double distSqr = hit.getLocation().distanceToSqr(from);
						if (distSqr < closestDistSqr) {
							closestDistSqr = distSqr;
							bestBellHit = hit;
						}
					}
				}
			}
		}

		if (bestBellHit != null) {
			cir.setReturnValue(bestBellHit);
		}
	}
}
