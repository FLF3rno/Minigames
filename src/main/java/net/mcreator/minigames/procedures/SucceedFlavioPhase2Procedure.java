package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.FlavioFightManager;

public class SucceedFlavioPhase2Procedure {
	public static void execute(LevelAccessor world) {
		if (world instanceof ServerLevel _level) {
			BlockPos _bp = new BlockPos(0, -10, 0);
			if (BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), _level, _bp, null) || BoneMealItem.growWaterPlant(new ItemStack(Items.BONE_MEAL), _level, _bp, null)) {
				_level.levelEvent(2005, _bp, 0);
			}
		}
		net.mcreator.minigames.FlavioFightManager.completePhase2(world);
	}
}