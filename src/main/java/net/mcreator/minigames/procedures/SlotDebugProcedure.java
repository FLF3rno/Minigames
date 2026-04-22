package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.minigames.network.MinigamesModVariables;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class SlotDebugProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.playerSlots = DoubleArgumentType.getDouble(arguments, "name");
			_vars.backpackSlots = DoubleArgumentType.getDouble(arguments, "backpack");
			_vars.showOnlyHearts = true;
			_vars.markSyncDirty();
		}
		MinigamesModVariables.MapVariables.get(world).playingDungeons = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}