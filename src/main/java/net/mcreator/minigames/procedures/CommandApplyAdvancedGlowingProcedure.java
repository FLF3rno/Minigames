package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class CommandApplyAdvancedGlowingProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments) {
		ApplyAdvancedGlowingProcedure.execute(commandParameterEntity(arguments, "target"), BoolArgumentType.getBool(arguments, "hideParticles"), DoubleArgumentType.getDouble(arguments, "transparency"),
				DoubleArgumentType.getDouble(arguments, "seconds"), StringArgumentType.getString(arguments, "color"));
	}

	private static Entity commandParameterEntity(CommandContext<CommandSourceStack> arguments, String parameter) {
		try {
			return EntityArgument.getEntity(arguments, parameter);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}