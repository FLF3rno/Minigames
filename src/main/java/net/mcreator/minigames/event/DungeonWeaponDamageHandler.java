package net.mcreator.minigames.event;

import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.procedures.CheckRelicProcedure;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

@EventBusSubscriber(modid = "minigames")
public class DungeonWeaponDamageHandler {

	@SubscribeEvent
	public static void onLivingDamage(LivingIncomingDamageEvent event) {
		if (event.getSource().getDirectEntity() instanceof Player player) {
			ItemStack mainHand = player.getMainHandItem();
			boolean playingDungeons = player.level() != null
					&& MinigamesModVariables.MapVariables.get(player.level()).playingDungeons;

			boolean isBoxing = net.mcreator.minigames.procedures.BoxingMasteryHandler.isBoxing(player);

			if (playingDungeons && !DungeonItemAccess.isDungeonWeapon(mainHand) && !isBoxing) {
				event.setCanceled(true);
				return;
			}

			if (isBoxing) {
				double baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
				if (event.getAmount() < baseDamage) {
					event.setAmount((float) baseDamage);
				}
			} else if (DungeonItemAccess.isDungeonItem(mainHand)) {
				double baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
				double finalForcedDamage = baseDamage + 1.0;
				if (event.getAmount() < finalForcedDamage) {
					event.setAmount((float) finalForcedDamage);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onCriticalHit(CriticalHitEvent event) {
		Player player = event.getEntity();
		if (player != null) {
			ItemStack mainHand = player.getMainHandItem();
			if (DungeonItemAccess.isDungeonItem(mainHand)) {
				event.setCriticalHit(false);
			}
		}
	}
}
