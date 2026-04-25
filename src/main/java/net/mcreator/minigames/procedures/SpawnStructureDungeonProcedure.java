package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class SpawnStructureDungeonProcedure {
	public static void execute(LevelAccessor world, double structureX, double structureY, double structureZ, String structure) {
		if (structure == null)
			return;
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("/execute in minigames:dungeon_dimension run place template minigames:" + structure + " " + new java.text.DecimalFormat("##").format(structureX) + " " + new java.text.DecimalFormat("##").format(structureY) + " "
							+ new java.text.DecimalFormat("##").format(structureZ)));
	}
}