package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.CustomFireHelper;

public class BlazingBladeHitProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, ItemStack itemstack) {
		if (entity == null || sourceentity == null)
			return;
		if (net.mcreator.minigames.CustomFireHelper.isSoulFire(entity)) {
			entity.clearFire();
			ExplodeProcedure.execute(world, x, y, z, sourceentity, false, true, GetItemAttributeProcedure.execute(itemstack, "minigames:explosion_damage"), 0.01, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_range"), "red");
		} else if (net.mcreator.minigames.CustomFireHelper.isOnFire(entity)) {
			SetOnFireProcedure.execute(world, entity, sourceentity, GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"), Math.round(CustomFireHelper.getRemainingFireTicks(entity) * 1.3), "soul_fire");
		}
	}
}