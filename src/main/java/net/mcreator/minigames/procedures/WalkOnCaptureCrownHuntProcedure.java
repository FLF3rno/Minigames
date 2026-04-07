package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

public class WalkOnCaptureCrownHuntProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
			if (MinigamesModVariables.MapVariables.get(world).returnToCastle) {
				if (MinigamesModVariables.MapVariables.get(world).canGrabCrown) {
					if (MinigamesModVariables.MapVariables.get(world).gameHours == 0 && MinigamesModVariables.MapVariables.get(world).gameMinutes == 0 && MinigamesModVariables.MapVariables.get(world).gameSeconds == 0) {
						OnWinCrownHuntProcedure.execute(world);
					}
				}
			}
		}
	}
}