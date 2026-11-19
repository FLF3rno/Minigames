package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.network.MinigamesModVariables;

public class DisableSpectativeProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.spectative = false;
				_vars.markSyncDirty();
			}
			if (entity instanceof LivingEntity _entity) {
				_entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(Identifier.parse("minigames:spectative"));
			}
			if (entity instanceof LivingEntity _entity) {
				_entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).removeModifier(Identifier.parse("minigames:spectative"));
			}
			if (entity instanceof Player player && player.level() instanceof ServerLevel level) {
				player.getAbilities().setFlyingSpeed(0.05F);
			}
			if (entity instanceof Player _player) {
				_player.getAbilities().mayfly = false;
				_player.getAbilities().flying = false;
				_player.onUpdateAbilities();
			}
			entity.setInvulnerableTime(0);
		}
	}
}