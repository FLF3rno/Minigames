package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class ShowHoveredTeammateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		DisplayClientsideGlowProcedure.execute(world, x, y, z, entity, ReturnHoveredTeammateProcedure.execute(world, entity), 2, "red");
	}
}