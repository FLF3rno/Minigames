package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

import java.util.ArrayList;

@EventBusSubscriber
public class PassiveHealingProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double bonus = 0;
		if (MinigamesModVariables.MapVariables.get(world).playingDungeons && !world.isClientSide()) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1)) {
				if (entity.tickCount % entity.getData(MinigamesModVariables.PLAYER_VARIABLES).PassiveHealCooldown == 0) {
					bonus = 0;
					for (Entity entityiterator : new ArrayList<>(world.players())) {
						if (CheckRelicProcedure.execute(entityiterator, new ItemStack(MinigamesModItems.EMPTY_HEART_AMULET.get()))) {
							bonus = GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(entityiterator, new ItemStack(MinigamesModItems.EMPTY_HEART_AMULET.get())), "minigames:effect_potency") + bonus;
						}
					}
					DungeonHealProcedure.execute(entity, entity, entity.getData(MinigamesModVariables.PLAYER_VARIABLES).PassiveHealAmount + entity.getData(MinigamesModVariables.PLAYER_VARIABLES).PassiveHealAmount * (bonus / 100), "passive");
				}
			}
		}
	}
}