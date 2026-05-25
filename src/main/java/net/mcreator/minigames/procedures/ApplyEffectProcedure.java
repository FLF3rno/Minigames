package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class ApplyEffectProcedure {
	public static void execute(Entity target, boolean hide, double level, double ticks, String effect) {
		if (target == null || effect == null)
			return;
		{
			Entity _ent = target;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands()
						.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(),
								_ent.getDisplayName(), _ent.level().getServer(), _ent),
								("effect give @s " + effect + " " + new java.text.DecimalFormat("##.##").format(ticks) + " " + new java.text.DecimalFormat("##.##").format(level - 1) + " " + hide));
			}
		}
	}
}