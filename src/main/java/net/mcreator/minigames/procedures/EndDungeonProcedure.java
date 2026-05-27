package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class EndDungeonProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).playingDungeons = false;
		MinigamesModVariables.MapVariables.get(world).minimap = false;
		MinigamesModVariables.MapVariables.get(world).waypoints = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (entityiterator instanceof Player) {
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.showOnlyHearts = false;
					_vars.playerInInventory = false;
					_vars.canDash = false;
					_vars.playerSlots = 9;
					_vars.backpackSlots = 0;
					_vars.maxDashCooldown = 60;
					_vars.dashLength = 1;
					_vars.PassiveHealCooldown = 80;
					_vars.PassiveHealAmount = 1;
					_vars.markSyncDirty();
				}
			}
		}
		MinigamesModVariables.MapVariables.get(world).removeEffects = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}