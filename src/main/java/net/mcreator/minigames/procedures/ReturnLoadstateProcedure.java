package net.mcreator.minigames.procedures;

import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.component.DataComponents;

public class ReturnLoadstateProcedure {
	public static double execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return 0;
		double progress = 0;
		if (CrossbowItem.isCharged(itemstack)) {
				return 4;
			}
		else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()) {
			progress = (entity instanceof LivingEntity _entUseTicks3 ? _entUseTicks3.getTicksUsingItem() : 0) / GetItemAttributeProcedure.execute(itemstack, "minigames:load_time");
			 if (progress <= 0) {
				return 0;
			} else if (progress < 0.35) {
				return 1;
			} else if (progress < 0.6) {
				return 2;
			} else if (progress < 0.95) {
				return 3;
			}
			return 0;
		}
		return 0;
	}
}