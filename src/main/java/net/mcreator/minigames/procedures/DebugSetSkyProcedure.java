package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.minigames.network.MinigamesModVariables;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class DebugSetSkyProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments) {
		MinigamesModVariables.MapVariables.get(world).sky = StringArgumentType.getString(arguments, "sky");
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof ServerLevel _level) {
			_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((StringArgumentType.getString(arguments, "sky"))), false);
		}
	}
}