package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

public class SlamRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		entity.getPersistentData().putBoolean("slam", true);
		if (entity.onGround()) {
			SlamFallOnGroundProcedure.execute(world, x, y, z, entity);
		} else {
			entity.setDeltaMovement(new Vec3(0, (-6), 0));
		}
		ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
	}
}