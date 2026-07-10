package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.minigames.network.MinigamesModVariables;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class BBCommandProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.openBattleBox = BoolArgumentType.getBool(arguments, "open");
			_vars.markSyncDirty();
		}
		if (BoolArgumentType.getBool(arguments, "open")) {
			MinigamesModVariables.MapVariables.get(world).battleBoxStatus = "action";
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
	}
}