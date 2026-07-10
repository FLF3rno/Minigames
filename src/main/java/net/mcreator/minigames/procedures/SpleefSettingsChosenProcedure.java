package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SpleefSettingsChosenProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		SaveSpleefSettingsProcedure.execute(world, entity);
		if (entity instanceof Player _player)
			_player.closeContainer();
		if (MinigamesModVariables.MapVariables.get(world).connectedPlayers >= 2) {
			StartVoteProcedure.execute(world, entity, entity, "spleef");
		} else {
			if (entity instanceof ServerPlayer _player)
				_player.sendSystemMessage(Component.literal("\u00A7cNot enough players to start Spleef (minimum of 2)"), true);
		}
	}
}