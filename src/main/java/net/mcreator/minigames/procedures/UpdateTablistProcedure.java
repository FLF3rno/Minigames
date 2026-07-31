package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

public class UpdateTablistProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof ServerPlayer serverPlayer) {
    		serverPlayer.refreshTabListName();
		}
	}
}