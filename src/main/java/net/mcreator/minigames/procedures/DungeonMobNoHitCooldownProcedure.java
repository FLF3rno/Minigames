package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DungeonMobNoHitCooldownProcedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (!(entity instanceof LivingEntity livingEntity))
			return;
		PlayerTeam team = livingEntity.level().getScoreboard().getPlayersTeam(livingEntity instanceof Player player ? player.getGameProfile().getName() : livingEntity.getStringUUID());
		if (team != null && "dungeon_mobs".equals(team.getName())) {
			livingEntity.invulnerableTime = 0;
		}
	}
}
