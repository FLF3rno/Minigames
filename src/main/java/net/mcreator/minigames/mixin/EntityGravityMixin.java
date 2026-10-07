package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravityOrientation;

@Mixin(Entity.class)
public abstract class EntityGravityMixin {
	@Inject(
		method = "makeBoundingBox(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/AABB;",
		at = @At("RETURN"),
		cancellable = true
	)
	private void minigames$gravityBoundingBox(Vec3 pos, CallbackInfoReturnable<AABB> cir) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		AABB box = cir.getReturnValue().move(pos.scale(-1.0D));
		cir.setReturnValue(GravityOrientation.boxPlayerToWorld(box, gravity).move(pos));
	}

	@Inject(
		method = "getEyePosition(F)Lnet/minecraft/world/phys/Vec3;",
		at = @At("RETURN"),
		cancellable = true
	)
	private void minigames$gravityEyePosition(float partialTick, CallbackInfoReturnable<Vec3> cir) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		double lerpedX = Mth.lerp(partialTick, player.xo, player.getX());
		double lerpedY = Mth.lerp(partialTick, player.yo, player.getY());
		double lerpedZ = Mth.lerp(partialTick, player.zo, player.getZ());

		Vec3 eyeOffset = GravityOrientation.vecPlayerToWorld(0, player.getEyeHeight(), 0, gravity);
		cir.setReturnValue(new Vec3(lerpedX + eyeOffset.x, lerpedY + eyeOffset.y, lerpedZ + eyeOffset.z));
	}

	@Inject(
		method = "getEyePosition()Lnet/minecraft/world/phys/Vec3;",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames$gravityEyePosition0(CallbackInfoReturnable<Vec3> cir) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		Vec3 eyeOffset = GravityOrientation.vecPlayerToWorld(0, player.getEyeHeight(), 0, gravity);
		cir.setReturnValue(new Vec3(player.getX() + eyeOffset.x, player.getY() + eyeOffset.y, player.getZ() + eyeOffset.z));
	}

	@Inject(
		method = "getEyeY",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames$gravityEyeY(CallbackInfoReturnable<Double> cir) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		Vec3 eyeOffset = GravityOrientation.vecPlayerToWorld(0, player.getEyeHeight(), 0, gravity);
		cir.setReturnValue(player.getY() + eyeOffset.y);
	}

	@Inject(
		method = "isInWall",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames$gravityIsInWall(CallbackInfoReturnable<Boolean> cir) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		if (player.noPhysics) {
			cir.setReturnValue(false);
			return;
		}

		float checkWidth = player.getDimensions(player.getPose()).width() * 0.8F;
		Vec3 eyePos = player.getEyePosition();
		AABB localEyeBb = new AABB(-checkWidth / 2.0, -1.0E-6 / 2.0, -checkWidth / 2.0, checkWidth / 2.0, 1.0E-6 / 2.0, checkWidth / 2.0);
		AABB eyeBb = GravityOrientation.boxPlayerToWorld(localEyeBb, gravity).move(eyePos);

		boolean suffocating = net.minecraft.core.BlockPos.betweenClosedStream(eyeBb).anyMatch(pos -> {
			net.minecraft.world.level.block.state.BlockState state = player.level().getBlockState(pos);
			return !state.isAir()
				&& state.isSuffocating(player.level(), pos)
				&& net.minecraft.world.phys.shapes.Shapes.joinIsNotEmpty(
					state.getCollisionShape(player.level(), pos).move(pos.getX(), pos.getY(), pos.getZ()),
					net.minecraft.world.phys.shapes.Shapes.create(eyeBb),
					net.minecraft.world.phys.shapes.BooleanOp.AND
				);
		});
		cir.setReturnValue(suffocating);
	}

	@Inject(
		method = "getViewVector(F)Lnet/minecraft/world/phys/Vec3;",
		at = @At("RETURN"),
		cancellable = true
	)
	private void minigames$gravityViewVector(float partialTick, CallbackInfoReturnable<Vec3> cir) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		cir.setReturnValue(GravityOrientation.vecPlayerToWorld(cir.getReturnValue(), gravity));
	}

	@Inject(
		method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
		at = @At("HEAD"),
		cancellable = true
	)
	private void minigames$gravityCollide(Vec3 movement, CallbackInfoReturnable<Vec3> cir) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		AABB aabb = player.getBoundingBox();
		java.util.List<net.minecraft.world.phys.shapes.VoxelShape> entityColliders = player.level().getEntityCollisions(player, aabb.expandTowards(movement));
		Vec3 movementStep = movement.lengthSqr() == 0.0 ? movement : Entity.collideBoundingBox(player, movement, aabb, player.level(), entityColliders);
		cir.setReturnValue(movementStep);
	}

	private static final ThreadLocal<Vec3> MINIGAMES$START_POS = new ThreadLocal<>();

	@Inject(
		method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
		at = @At("HEAD")
	)
	private void minigames$gravityMoveHead(net.minecraft.world.entity.MoverType moverType, Vec3 delta, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		MINIGAMES$START_POS.set(player.position());
	}

	@Inject(
		method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
		at = @At("TAIL")
	)
	private void minigames$gravityCollisionCorrection(net.minecraft.world.entity.MoverType moverType, Vec3 delta, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
		if (!((Object) this instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		Vec3 startPos = MINIGAMES$START_POS.get();
		Vec3 actualMovement = startPos != null ? player.position().subtract(startPos) : player.getDeltaMovement();
		Vec3 localDelta = GravityOrientation.vecWorldToPlayer(delta, gravity);
		Vec3 localMovement = GravityOrientation.vecWorldToPlayer(actualMovement, gravity);

		boolean localXCol = Math.abs(localDelta.x - localMovement.x) > 1.0E-5;
		boolean localZCol = Math.abs(localDelta.z - localMovement.z) > 1.0E-5;
		boolean localHCol = localXCol || localZCol;

		boolean localVCol = Math.abs(localDelta.y - localMovement.y) > 1.0E-5;
		boolean localVColBelow = localVCol && localDelta.y < -1.0E-5;

		player.horizontalCollision = localHCol;
		player.minorHorizontalCollision = false;
		player.verticalCollision = localVCol;
		player.verticalCollisionBelow = localVColBelow;

		boolean onGround = localVColBelow || (localDelta.y <= 1.0E-5 && net.mcreator.minigames.gravity.GravitySupport.isSupportedInGravity(player, gravity));
		player.setOnGroundWithMovement(onGround, localHCol, actualMovement);
	}
}
