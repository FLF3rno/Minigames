package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

public class SickSickleHitProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		ApplyEffectProcedure.execute(world, entity, false, GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"), GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), "minigames:decay");
	}
}