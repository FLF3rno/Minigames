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

public class OnWinCrownHuntProcedure {
	public static void execute(LevelAccessor world) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/worldborder set 50000000");
		MinigamesModVariables.MapVariables.get(world).ShowCrownTimer = false;
		MinigamesModVariables.MapVariables.get(world).MoveCrownTimer = false;
		MinigamesModVariables.MapVariables.get(world).crownHuntWinDisplay = true;
		MinigamesModVariables.MapVariables.get(world).CrownHuntInGame = false;
		MinigamesModVariables.MapVariables.get(world).canGrabCrown = false;
		MinigamesModVariables.MapVariables.get(world).returnToCastle = false;
		MinigamesModVariables.MapVariables.get(world).winAnimationTick = 0;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}