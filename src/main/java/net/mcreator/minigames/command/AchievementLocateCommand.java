package net.mcreator.minigames.command;

import com.mojang.brigadier.Command;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.util.AchievementStrongholdLocator;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

@EventBusSubscriber
public class AchievementLocateCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("achievementlocate")
			.requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
			.executes(arguments -> {
				ServerPlayer player = arguments.getSource().getPlayerOrException();
				if (!(player.level() instanceof ServerLevel serverLevel) || !MinigamesModVariables.MapVariables.get(serverLevel).playingAchievement) {
					arguments.getSource().sendFailure(Component.literal("Achievement run is not active."));
					return 0;
				}

				BlockPos target = AchievementStrongholdLocator.getNearestTarget(serverLevel, player.blockPosition());
				if (target == null) {
					arguments.getSource().sendFailure(Component.literal("No achievement target has been set."));
					return 0;
				}

				arguments.getSource().sendSuccess(() -> Component.literal(
					"Achievement target found at " + target.getX() + " " + target.getY() + " " + target.getZ()
				), false);
				return Command.SINGLE_SUCCESS;
			}));
	}
}
