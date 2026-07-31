package net.mcreator.minigames.procedures;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.MinigamesMod;

import java.util.ArrayList;

public class AnimateCrownPickupProcedure {
	public static void execute(LevelAccessor world) {
		MinigamesModVariables.MapVariables.get(world).ShowTimer = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				Entity _ent = entityiterator;
				if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
					_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
							LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound minecraft:item.goat_horn.sound.1 ui @s ~ ~ ~ 10000000 1");
				}
			}
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.timerSpeed = -1;
				_vars.timerTick = 0;
				_vars.timerSeconds = 1;
				_vars.timerMinutes = MinigamesModVariables.MapVariables.get(world).crownMinutes;
				_vars.timerHours = 0;
				_vars.markSyncDirty();
			}
			if ((entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
				if (!((entityiterator instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().name() : _teamEnt.getStringUUID()) != null
						? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().name() : _teamEnt.getStringUUID()).getName()
						: "").equals("crowned"))) {
					if (entityiterator.isAlive()) {
						{
							Entity _entityTeam = entityiterator;
							PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam("crowned");
							if (_pt != null) {
								if (_entityTeam instanceof Player _player)
									_entityTeam.level().getScoreboard().addPlayerToTeam(_player.getGameProfile().name(), _pt);
								else
									_entityTeam.level().getScoreboard().addPlayerToTeam(_entityTeam.getStringUUID(), _pt);
							}
						}
					}
				}
			}
			UpdateTablistProcedure.execute(entityiterator);
		}
		net.mcreator.minigames.AnimationScreenTrigger.startAnimation(100, "crown", 1.0f);
		MinigamesMod.queueServerWork(50, () -> {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				{
					Entity _ent = entityiterator;
					if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
						_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
								LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound minigames:pvp_swordshit ui @s ~ ~ ~ 10000000 2");
					}
				}
			}
		});
		MinigamesMod.queueServerWork(60, () -> {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				{
					Entity _ent = entityiterator;
					if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
						_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
								LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound minecraft:entity.item.break ui @s ~ ~ ~ 0.5 1");
					}
				}
			}
		});
	}
}