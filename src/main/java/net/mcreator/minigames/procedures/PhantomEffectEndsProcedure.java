package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

public class PhantomEffectEndsProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("transparency", 0);
	}
}