package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class TablistCrownIconProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (!((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get())) {
			if ((entity instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().name() : _teamEnt.getStringUUID()) != null
					? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().name() : _teamEnt.getStringUUID()).getName()
					: "").equals("crowned")) {
				if (entity.isAlive()) {
					{
						Entity _entityTeam = entity;
						PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam("crowned");
						if (_pt != null)
							_entityTeam.level().getScoreboard().removePlayerFromTeam(_entityTeam.getStringUUID(), _pt);
					}
				}
			}
		} else if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
			if (!((entity instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().name() : _teamEnt.getStringUUID()) != null
					? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().name() : _teamEnt.getStringUUID()).getName()
					: "").equals("crowned"))) {
				if (entity.isAlive()) {
					{
						Entity _entityTeam = entity;
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
	}
}