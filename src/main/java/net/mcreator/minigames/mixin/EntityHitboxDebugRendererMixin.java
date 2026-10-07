package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.core.Direction;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.gravity.GravityAccess;
import net.mcreator.minigames.gravity.GravityOrientation;

@Mixin(EntityHitboxDebugRenderer.class)
public abstract class EntityHitboxDebugRendererMixin {
	@Inject(method = "showHitboxes", at = @At("HEAD"), cancellable = true)
	private void minigames$renderGravityHitbox(Entity entity, float partialTicks, boolean isServerEntity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) return;
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		Vec3 latestPosition = entity.position();
		Vec3 currentPosition = entity.getPosition(partialTicks);
		Vec3 offset = currentPosition.subtract(latestPosition);
		int mainColor = isServerEntity ? -16711936 : -1;

		Gizmos.cuboid(entity.getBoundingBox().move(offset), GizmoStyle.stroke(mainColor));
		Gizmos.point(currentPosition, mainColor, 2.0F);

		Entity vehicle = entity.getVehicle();
		if (vehicle != null) {
			float width = Math.min(vehicle.getBbWidth(), entity.getBbWidth()) / 2.0F;
			Vec3 position = vehicle.getPassengerRidingPosition(entity).add(offset);
			Gizmos.cuboid(
				new AABB(position.x - width, position.y, position.z - width, position.x + width, position.y + 0.0625, position.z + width),
				GizmoStyle.stroke(-256)
			);
		}

		float halfWidth = entity.getBbWidth() / 2.0F;
		float eyeHeight = entity.getEyeHeight();
		AABB localEyeBox = new AABB(-halfWidth, eyeHeight - 0.01F, -halfWidth, halfWidth, eyeHeight + 0.01F, halfWidth);
		AABB worldEyeBox = GravityOrientation.boxPlayerToWorld(localEyeBox, gravity).move(currentPosition);
		Gizmos.cuboid(worldEyeBox, GizmoStyle.stroke(-65536));

		Vec3 eyeOffset = GravityOrientation.vecPlayerToWorld(0, eyeHeight, 0, gravity);
		Vec3 eyePosition = currentPosition.add(eyeOffset);
		Vec3 viewVector = entity.getViewVector(partialTicks);
		Gizmos.arrow(eyePosition, eyePosition.add(viewVector.scale(2.0)), -16776961);

		if (isServerEntity) {
			Vec3 deltaMovement = entity.getDeltaMovement();
			Gizmos.arrow(currentPosition, currentPosition.add(deltaMovement), -256);
		}

		ci.cancel();
	}
}
