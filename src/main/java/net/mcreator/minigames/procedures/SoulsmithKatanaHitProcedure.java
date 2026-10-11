package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.minecraft.world.phys.Vec3;

public class SoulsmithKatanaHitProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (!entity.isAlive()) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = entity.getType().create(_level, EntitySpawnReason.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setPos(x, y, z);
					entityToSpawn.setDeltaMovement(0, 0, 0);
					entityToSpawn.getPersistentData().putBoolean("respawned", true);
					_level.addFreshEntity(entityToSpawn);
					if (entityToSpawn instanceof LivingEntity living)
					{
						living.setHealth((float) (living.getMaxHealth() * GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency")) / 100);
					}
				}
			}
		}
	}
}