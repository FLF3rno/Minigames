package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

public class StartRoomProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).aliveEnemies = 1;
		MinigamesModVariables.MapVariables.get(world).roomCheckDelayTicks = 10;
		MinigamesModVariables.MapVariables.get(world).inCombat = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands()
					.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL,
							new Vec3((MinigamesModVariables.MapVariables.get(world).DoorOffset.x()), (MinigamesModVariables.MapVariables.get(world).DoorOffset.y()), (MinigamesModVariables.MapVariables.get(world).DoorOffset.z())), Vec2.ZERO, _level,
							LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "execute in minigames:dungeon_dimension run tp @a ~ ~ ~");
		MinigamesMod.queueServerWork(3, () -> {
			MinigamesModVariables.MapVariables.get(world).startingEnemies = MinigamesModVariables.MapVariables.get(world).aliveEnemies;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		});
	}
}