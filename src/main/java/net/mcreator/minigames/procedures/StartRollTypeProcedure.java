package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.server.ServerLifecycleHooks;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

public class StartRollTypeProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).respawningPlayers == 0) {
			MinigamesModVariables.MapVariables.get(world).achievementHunterMode = false;
			MinigamesModVariables.MapVariables.get(world).randomHunterAchievement = false;
			MinigamesModVariables.MapVariables.get(world).hunterAchievement = "";
			MinigamesModVariables.MapVariables.get(world).headStart = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (entity instanceof Player _player)
				_player.closeContainer();
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/effect give @a minigames:immobilized 3000 1 true");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/team modify teamold nametagVisibility never");
			MinigamesModVariables.MapVariables.get(world).players = world.isClientSide() ? Minecraft.getInstance().getConnection().getOnlinePlayers().size() : ServerLifecycleHooks.getCurrentServer().getPlayerCount();
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			ResetAchievementRunProcedure.execute(world);
			MinigamesMod.queueServerWork(45, () -> {
				MinigamesModVariables.MapVariables.get(world).achivementTypeTimer = 0;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			});
			MinigamesModVariables.MapVariables.get(world).winAnimationTick = -1;
			MinigamesModVariables.MapVariables.get(world).winAnimationStart = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Not all players are alive!").withColor(0xff0033), false);
			}
		}
	}
}