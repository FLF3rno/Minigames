package net.mcreator.minigames.mixin;

import net.mcreator.minigames.client.LivingEntityTransparencyDataAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityTransparencyDataMixin implements LivingEntityTransparencyDataAccessor {
	@Unique
	private static final EntityDataAccessor<Float> minigames$TRANSPARENCY = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void minigames$defineTransparencyData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(minigames$TRANSPARENCY, 0.0f);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void minigames$syncTransparency(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		float transparency = (float) entity.getPersistentData().getDoubleOr("transparency", 0);
		if (entity.getEntityData().get(minigames$TRANSPARENCY) != transparency) {
			entity.getEntityData().set(minigames$TRANSPARENCY, transparency);
		}
	}

	@Override
	public float minigames$getTransparency() {
		return ((LivingEntity) (Object) this).getEntityData().get(minigames$TRANSPARENCY);
	}

	@Override
	public void minigames$setTransparency(float transparency) {
		((LivingEntity) (Object) this).getEntityData().set(minigames$TRANSPARENCY, transparency);
	}
}

