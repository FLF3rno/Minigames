package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class BlessedEffectStartedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
				_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
						LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/data merge entity @s {CustomName:'[blessed]',CustomNameVisible:0b}");
			}
		}
	}
}