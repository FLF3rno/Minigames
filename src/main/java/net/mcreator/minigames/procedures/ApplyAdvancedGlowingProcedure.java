package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ApplyAdvancedGlowingProcedure {
	public static void execute(Entity target, boolean hideParticles, double amplifier, double length, String color) {
		if (target == null || color == null)
			return;
		{
			MinigamesModVariables.PlayerVariables _vars = target.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.advancedGlowingColor = color;
			_vars.markSyncDirty();
		}
		{
			Entity _ent = target;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @s minigames:advanced_glowing " + length + " " + amplifier + " " + hideParticles));
			}
		}
	}
}