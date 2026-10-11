package net.mcreator.minigames.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber(modid = MinigamesMod.MODID)
public class FoodHandler {

	public static boolean isEligible(Level level) {
		if (level == null) {
			return false;
		}
		try {
			if (MinigamesModVariables.MapVariables.get(level).playingDungeons) {
				return true;
			}
			if (MinigamesModVariables.MapVariables.get(level).playingSpleef) {
				return true;
			}
		} catch (Throwable ignored) {
		}
		return false;
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide()) {
			return;
		}

		if (isEligible(player.level())) {
			FoodData foodData = player.getFoodData();
			if (foodData.getFoodLevel() < 20) {
				foodData.setFoodLevel(20);
			}
			if (foodData.getSaturationLevel() < 20.0F) {
				foodData.setSaturation(20.0F);
			}
		}
	}
}
