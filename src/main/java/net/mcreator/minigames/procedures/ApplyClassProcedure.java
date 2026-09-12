package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ApplyClassProcedure {
	public static void execute(Entity entity, String selectClass) {
		if (entity == null || selectClass == null)
			return;
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.classDungeon = selectClass;
			_vars.markSyncDirty();
		}
		if ((selectClass).equals("warrior")) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.classColor = "0xFFFF001F";
				_vars.markSyncDirty();
			}
		} else if ((selectClass).equals("support")) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.classColor = "0xFF09E2F6";
				_vars.markSyncDirty();
			}
		} else if ((selectClass).equals("thief")) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.classColor = "0xFFFFB700";
				_vars.markSyncDirty();
			}
		} else if ((selectClass).equals("mage")) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.classColor = "0xFFFF7BFE";
				_vars.markSyncDirty();
			}
		}

		if (entity instanceof net.minecraft.world.entity.player.Player _player) {
			java.util.List<net.minecraft.world.item.ItemStack> itemsToGive = new java.util.ArrayList<>();
			if ((selectClass).equals("warrior")) {
				itemsToGive.add(new net.minecraft.world.item.ItemStack(net.mcreator.minigames.init.MinigamesModItems.BLANK_SWORD.get()));
				itemsToGive.add(new net.minecraft.world.item.ItemStack(net.mcreator.minigames.init.MinigamesModItems.CHOICE_BAG_WARRIOR.get()));
			} else if ((selectClass).equals("support")) {
				itemsToGive.add(new net.minecraft.world.item.ItemStack(net.mcreator.minigames.init.MinigamesModItems.BLANK_LONG_SWORD.get()));
				itemsToGive.add(new net.minecraft.world.item.ItemStack(net.mcreator.minigames.init.MinigamesModItems.CHOICE_BAG_SUPPORT.get()));
			} else if ((selectClass).equals("thief")) {
				itemsToGive.add(new net.minecraft.world.item.ItemStack(net.mcreator.minigames.init.MinigamesModItems.BLANK_DAGGER.get()));
				itemsToGive.add(new net.minecraft.world.item.ItemStack(net.mcreator.minigames.init.MinigamesModItems.CHOICE_BAG_THIEF.get()));
			}

			for (net.minecraft.world.item.ItemStack stack : itemsToGive) {
				if (!_player.addItem(stack)) {
					_player.drop(stack, false);
				}
			}
		}
	}
}