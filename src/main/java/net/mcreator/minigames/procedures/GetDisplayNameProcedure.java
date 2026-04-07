package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

public class GetDisplayNameProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		return entity.getDisplayName().getString();
	}
}