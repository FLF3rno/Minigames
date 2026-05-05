package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class SpawnStartingRoomProcedureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		double structureX = 0;
		double structureZ = 0;
		String spawnRoomName = "";
		String rotation = "";
		if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _withbp2 ? blockstate.setValue(_withbp2, true) : blockstate)) {
			spawnRoomName = "dungeon_start_generic";
			rotation = "none";
			structureX = x;
			structureZ = z;
			if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _withbp6 ? blockstate.setValue(_withbp6, true) : blockstate)) {
				structureX = x + 28;
				rotation = "clockwise_90";
			} else if (blockstate == (blockstate.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _withbp10 ? blockstate.setValue(_withbp10, true) : blockstate)) {
				structureZ = z + 28;
				rotation = "counterclockwise_90";
			}
			SpawnStructureDungeonProcedure.execute(world, Mth.nextInt(RandomSource.create(), 1, 999999999), structureX, y, structureZ, rotation, spawnRoomName);
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"execute in minigames:dungeon_dimension run forceload remove 10 10 -10 -10");
		}
	}
}