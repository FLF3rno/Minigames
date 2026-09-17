package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.mcreator.minigames.UnmovableEntityHandler;

@Mixin(Entity.class)
public abstract class UnmovableEntityMixin {
	@Shadow
	private Vec3 deltaMovement;

	@Inject(method = "setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
	private void minigames$onSetDeltaMovement(Vec3 motion, CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		if (UnmovableEntityHandler.isUnmovable(self)) {
			if (motion.lengthSqr() <= 1.0E-7D) {
				return;
			}
			if (!UnmovableEntityHandler.isSelfLocomoting(self)) {
				this.deltaMovement = Vec3.ZERO;
				ci.cancel();
			}
		}
	}

	@Inject(method = "push(DDD)V", at = @At("HEAD"), cancellable = true)
	private void minigames$onPush(double x, double y, double z, CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		if (UnmovableEntityHandler.isUnmovable(self)) {
			ci.cancel();
		}
	}

	@Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
	private void minigames$onPushEntity(Entity entity, CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		if (UnmovableEntityHandler.isUnmovable(self)) {
			ci.cancel();
		}
	}

	@Inject(method = "move", at = @At("HEAD"), cancellable = true)
	private void minigames$onMove(MoverType moverType, Vec3 delta, CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		if (moverType == MoverType.PISTON && UnmovableEntityHandler.isUnmovable(self)) {
			ci.cancel();
		}
	}
}
