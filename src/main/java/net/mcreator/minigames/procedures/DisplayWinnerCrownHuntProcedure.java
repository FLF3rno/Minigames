package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

import java.util.ArrayList;

public class DisplayWinnerCrownHuntProcedure {
	public static Entity execute(LevelAccessor world) {
		Entity display = null;
		if (MinigamesModVariables.MapVariables.get(world).winAnimationState == 7) {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).winner == true) {
					display = entityiterator;
				}
			}
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if ((entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
					display = entityiterator;
				}
			}
		}
		return display;
	}
}
