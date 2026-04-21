package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class ChangePodiumTextureProcedure {
	public static void execute(LevelAccessor world, double position, String uuid) {
		if (uuid == null)
			return;
		if (world instanceof ServerLevel _level) {
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute in minigames:spleef_dimension run data merge entity @e[type=minigames:spleef_podium_player,limit=1,nbt={Dataposition:" + ((int) position) + "}] {Datadisplay_uuid:\""
							+ uuid + "\"}");
		}
	}
}
