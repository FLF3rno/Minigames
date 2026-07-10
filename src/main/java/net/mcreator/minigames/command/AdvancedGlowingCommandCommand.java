package net.mcreator.minigames.command;

import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;

import net.mcreator.minigames.procedures.CommandApplyAdvancedGlowingProcedure;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

@EventBusSubscriber
public class AdvancedGlowingCommandCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("effects").requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
				.then(Commands.literal("give").then(Commands.argument("target", EntityArgument.entities()).then(Commands.literal("advanced_glowing").then(Commands.argument("seconds", DoubleArgumentType.doubleArg())
						.then(Commands.argument("transparency", DoubleArgumentType.doubleArg()).then(Commands.argument("hideParticles", BoolArgumentType.bool()).then(Commands.argument("color", StringArgumentType.word()).executes(arguments -> {
							Level world = arguments.getSource().getUnsidedLevel();
							double x = arguments.getSource().getPosition().x();
							double y = arguments.getSource().getPosition().y();
							double z = arguments.getSource().getPosition().z();
							Entity entity = arguments.getSource().getEntity();
							if (entity == null && world instanceof ServerLevel _servLevel)
								entity = FakePlayerFactory.getMinecraft(_servLevel);
							Direction direction = Direction.DOWN;
							if (entity != null)
								direction = entity.getDirection();

							CommandApplyAdvancedGlowingProcedure.execute(arguments);
							return 0;
						})))))))));
	}

}