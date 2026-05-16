package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class StartDebugVoteProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		StartVoteProcedure.execute(world, entity, entity, "achievement run");
	}
}