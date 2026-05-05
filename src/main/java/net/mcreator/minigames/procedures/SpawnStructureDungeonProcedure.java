package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

public class SpawnStructureDungeonProcedure {
	public static void execute(LevelAccessor world, double ID, double structureX, double structureY, double structureZ, String rotation, String structure) {
		if (rotation == null || structure == null)
			return;
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("/execute in minigames:dungeon_dimension run place template minigames:" + structure + " " + new java.text.DecimalFormat("##").format(structureX) + " " + new java.text.DecimalFormat("##").format(structureY) + " "
							+ new java.text.DecimalFormat("##").format(structureZ) + " " + rotation));
		MinigamesMod.queueServerWork(20, () -> {
			if (world instanceof ServerLevel _level) {
				double dx = MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x();
				double dy = MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.y();
				double dz = MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z();
				if (rotation.equals("clockwise_90")) {
					dx = -dx;
				} else if (rotation.equals("counterclockwise_90")) {
					dz = -dz;
				} else if (rotation.equals("clockwise_180")) {
					dx = -dx;
					dz = -dz;
				}
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("/execute in minigames:dungeon_dimension as @e[type=!player,x=" + new java.text.DecimalFormat("##").format(structureX) + ",y=0,z=" + new java.text.DecimalFormat("##").format(structureZ) + ",dx="
								+ new java.text.DecimalFormat("##").format(dx) + ",dy=200,dz=" + new java.text.DecimalFormat("##").format(dz) + "] run data modify entity @s DataID set value " + new java.text.DecimalFormat("##").format(ID)));
			}
		});
	}
}