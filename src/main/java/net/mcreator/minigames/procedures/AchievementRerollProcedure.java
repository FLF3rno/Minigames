package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.server.ServerLifecycleHooks;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class AchievementRerollProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double rerollCount = 0;
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.AchievementLobbyState = "Rerolling";
			_vars.markSyncDirty();
		}
		rerollCount = 0;
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute as @a at @s run playsound minecraft:block.beacon.deactivate ui @s ~ ~ ~ 1 1");
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if ((entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).AchievementLobbyState).equals("Rerolling")) {
				rerollCount = rerollCount + 1;
			}
		}
		if ((world.isClientSide() ? Minecraft.getInstance().getConnection().getOnlinePlayers().size() : ServerLifecycleHooks.getCurrentServer().getPlayerCount()) == rerollCount) {
			AchievementInitiateRerollProcedure.execute(world, x, y, z);
		}
	}
}