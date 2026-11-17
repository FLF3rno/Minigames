package net.mcreator.minigames.procedures;

import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.network.MinigamesModVariables;

public class EnableSpectativeProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player) {
			if (entity instanceof ServerPlayer _player)
				_player.setGameMode(GameType.ADVENTURE);
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.spectative = true;
				_vars.markSyncDirty();
			}
			if (entity instanceof LivingEntity _entity) {
				AttributeModifier modifier = new AttributeModifier(Identifier.parse("minigames:spectative"), (-1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
				if (!_entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).hasModifier(modifier.id())) {
					_entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).addPermanentModifier(modifier);
				}
			}
			if (entity instanceof LivingEntity _entity) {
				AttributeModifier modifier = new AttributeModifier(Identifier.parse("minigames:spectative"), (-1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
				if (!_entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).hasModifier(modifier.id())) {
					_entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).addPermanentModifier(modifier);
				}
			}
			if (entity instanceof Player player && player.level() instanceof ServerLevel level) {
				level.getChunkSource().removeEntity(player);
				level.getChunkSource().addEntity(player);
				player.getAbilities().setFlyingSpeed(0.10F);
			}
			if (entity instanceof Player _player) {
				_player.getAbilities().mayfly = true;
				_player.getAbilities().flying = true;
				_player.onUpdateAbilities();
			}
			entity.invulnerableTime = 10000000;
		}
	}
}