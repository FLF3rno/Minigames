package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class ResetStatsProcedure {
	public static void execute(LevelAccessor world) {
		if (!world.isClientSide() && world.getServer() != null) {
			for (Entity entityiterator : new ArrayList<>(world.getServer().getPlayerList().getPlayers())) {
				if (entityiterator instanceof LivingEntity _livingEntity0 && _livingEntity0.getAttributes().hasAttribute(Attributes.SCALE))
					_livingEntity0.getAttribute(Attributes.SCALE).setBaseValue(1);
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.showOnlyHearts = false;
					_vars.markSyncDirty();
				}
				if (entityiterator instanceof LivingEntity _entity) {
					_entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(Identifier.parse("minigames:extra_damage_shop"));
				}
				if (entityiterator instanceof LivingEntity _entity) {
					_entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(Identifier.parse("minigames:health_upgrade_shop"));
				}
				if (entityiterator instanceof LivingEntity _entity) {
					_entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(Identifier.parse("minigames:speed_upgrade_shop"));
				}
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.showOnlyHearts = false;
					_vars.playerInInventory = false;
					_vars.playerSlots = 9;
					_vars.backpackSlots = 0;
					_vars.maxDashCooldown = 60;
					_vars.dashLength = 1;
					_vars.PassiveHealCooldown = 80;
					_vars.PassiveHealAmount = 1;
					_vars.minimumLightLevel = 0;
					_vars.maximumLightLevel = 15;
					_vars.classDungeon = "none";
					_vars.markSyncDirty();
				}
				if (entityiterator instanceof Player _player)
					_player.getInventory().clearContent();
				ClearEnderchestProcedure.execute(entityiterator);
			}
		}
		MinigamesModVariables.MapVariables.get(world).removeEffects = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}