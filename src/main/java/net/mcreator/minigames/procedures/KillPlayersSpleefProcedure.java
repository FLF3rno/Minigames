package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;

import java.util.ArrayList;

@EventBusSubscriber
public class KillPlayersSpleefProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (MinigamesModVariables.MapVariables.get(world).playingSpleef) {
			for (Entity entityiterator : world.getEntities(null, new AABB((-100 + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x()), 0, (-100), (100 + MinigamesModVariables.MapVariables.get(world).spleefMapMiddleX.x()), 85, 100))) {
				if (entityiterator instanceof Player _plr2 && _plr2.gameMode() == GameType.SURVIVAL) {
					EnableSpectativeProcedure.execute(entityiterator);
                    if (!entityiterator.level().isClientSide() && entityiterator.level().getServer() != null) {
							entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
									entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "/title @p times 10 60 10");
						entityiterator.level().getServer().getPlayerList().broadcastSystemMessage(entityiterator.getDisplayName().copy().append(Component.literal(" died!").withColor(0xff5555)), false);
						entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
								entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "execute as @a at @s run playsound minigames:teameliminated master @s ~ ~ ~ 2 1");

					}
					if (MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers == 2) {
						{
                            if (!entityiterator.level().isClientSide() && entityiterator.level().getServer() != null) {
								entityiterator.level().getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, entityiterator.getName().getString(),
												entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator),
										("/title @p subtitle [{\"color\":\"gold\",\"text\":\"You were \"},{\"color\":\"red\",\"text\":\"" + ""
												+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers) + "nd\"}]"));
							}
						}
						MinigamesModVariables.secondSpleef = entityiterator;
					} else if (MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers == 3) {
						{
                            if (!entityiterator.level().isClientSide() && entityiterator.level().getServer() != null) {
								entityiterator.level().getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, entityiterator.getName().getString(),
												entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator),
										("/title @p subtitle [{\"color\":\"gold\",\"text\":\"You were \"},{\"color\":\"red\",\"text\":\"" + ""
												+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers) + "rd\"}]"));}
						}
						MinigamesModVariables.thirdSpleef = entityiterator;
					} else {
						{
                            if (!entityiterator.level().isClientSide() && entityiterator.level().getServer() != null) {
								entityiterator.level().getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, entityiterator.getName().getString(),
												entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator),
										("/title @p subtitle [{\"color\":\"gold\",\"text\":\"You were \"},{\"color\":\"red\",\"text\":\"" + ""
												+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers) + "th\"}]"));
								entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
										entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "/title @p title {\"bold\":true,\"color\":\"red\",\"text\":\"You died!\"}");

							}
						}
					}
					MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers = MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers - 1;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
			}
			if (MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers == 1) {
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					if (entityiterator instanceof Player _plr14 && _plr14.gameMode() == GameType.SURVIVAL) {
						MinigamesModVariables.firstSpleef = entityiterator;
						{
                            if (!entityiterator.level().isClientSide() && entityiterator.level().getServer() != null) {
								entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
										entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "effect clear @a");
								entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
										entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "/effect give @p minigames:crowned infinite 1 true");
								entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
										entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "/title @a times 10 60 10");
								entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
										entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "/title @p title {\"bold\":true,\"color\":\"gold\",\"text\":\"You won!\"}");
								entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
										entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "/playsound minecraft:ui.toast.challenge_complete player @s ~ ~ ~ 0.6 1.2");
							}
						}
					}
					if (entityiterator instanceof Player _plr20 && _plr20.gameMode() == GameType.ADVENTURE) {
						{
                            if (!entityiterator.level().isClientSide() && entityiterator.level().getServer() != null) {
								entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
										entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "/title @a times 10 60 10");
								entityiterator.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entityiterator.position(), entityiterator.getRotationVector(), entityiterator.level() instanceof ServerLevel ? (ServerLevel) entityiterator.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
										entityiterator.getName().getString(), entityiterator.getDisplayName(), entityiterator.level().getServer(), entityiterator), "/title @p title {\"bold\":true,\"color\":\"red\",\"text\":\"You lost!\"}");
							}
						}
					}
				}
				StopSpleefProcedure.execute(world);
			}
		}
	}
}




