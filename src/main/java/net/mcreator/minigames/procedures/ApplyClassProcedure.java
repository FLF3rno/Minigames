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
	}
}