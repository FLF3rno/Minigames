package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;

public class DebugGravityProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		if ((StringArgumentType.getString(arguments, "rotation")).equals("down")) {
			ChangeGravityExecuteProcedure.execute(Direction.DOWN, entity);
		} else if ((StringArgumentType.getString(arguments, "rotation")).equals("up")) {
			ChangeGravityExecuteProcedure.execute(Direction.UP, entity);
		} else if ((StringArgumentType.getString(arguments, "rotation")).equals("north")) {
			ChangeGravityExecuteProcedure.execute(Direction.NORTH, entity);
		} else if ((StringArgumentType.getString(arguments, "rotation")).equals("south")) {
			ChangeGravityExecuteProcedure.execute(Direction.SOUTH, entity);
		} else if ((StringArgumentType.getString(arguments, "rotation")).equals("west")) {
			ChangeGravityExecuteProcedure.execute(Direction.WEST, entity);
		} else {
			ChangeGravityExecuteProcedure.execute(Direction.EAST, entity);
		}
	}
}