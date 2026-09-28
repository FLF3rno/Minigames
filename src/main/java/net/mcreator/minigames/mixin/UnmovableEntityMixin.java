package net.mcreator.minigames.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.mcreator.minigames.UnmovableEntityHandler;

@Mixin(Entity.class)
public abstract class UnmovableEntityMixin {
	@Inject(method = "setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
	private void minigames$onSetDeltaMovement(Vec3 motion, CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		if (self.level() == null || self.level().isClientSide()) {
			return;
		}
		if (UnmovableEntityHandler.isUnmovable(self)) {
			if (!UnmovableEntityHandler.isSelfTicking(self)) {
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
