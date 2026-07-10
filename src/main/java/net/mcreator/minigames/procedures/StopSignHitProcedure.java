package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

public class StopSignHitProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Vec3 value = Vec3.ZERO;
		double dmg = 0;
		if (entity.getDeltaMovement().x() < 0) {
			value = new Vec3((value.x() + entity.getDeltaMovement().x() * (-1)), (value.y()), (value.z()));
		} else {
			value = new Vec3((value.x() + entity.getDeltaMovement().x() * 1), (value.y()), (value.z()));
		}
		if (entity.getDeltaMovement().y() < 0) {
			value = new Vec3((value.x()), (value.y() + entity.getDeltaMovement().y() * (-1)), (value.z()));
		} else {
			value = new Vec3((value.x()), (value.y() + entity.getDeltaMovement().y() * 1), (value.z()));
		}
		if (entity.getDeltaMovement().z() < 0) {
			value = new Vec3((value.x()), (value.y()), (value.z() + entity.getDeltaMovement().z() * (-1)));
		} else {
			value = new Vec3((value.x()), (value.y()), (value.z() + entity.getDeltaMovement().z() * 1));
		}
		dmg = value.x() + value.y() + value.z();
		entity.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("minigames:self_damage")))), (float) (dmg * 1.2));
		entity.setDeltaMovement(new Vec3(0, 0, 0));
	}
}