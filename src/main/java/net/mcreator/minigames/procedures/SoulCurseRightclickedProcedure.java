package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

public class SoulCurseRightclickedProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (ReturnHoveredTeammateProcedure.execute(world, entity) != null && !(ReturnHoveredTeammateProcedure.execute(world, entity) == entity)) {
			ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
			if (world instanceof ServerLevel _level) {
				itemstack.hurtAndBreak(1, _level, null, _stkprov -> {
				});
			}
			SwapAccountProcedure.execute(world, entity, ReturnHoveredTeammateProcedure.execute(world, entity), true, true, true, true, true, true);
		}
	}
}