package net.mcreator.minigames.procedures;

import net.mcreator.minigames.DungeonItemAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

@Mod("minigames")
public class DungeonWeaponBlockWeakAttack {

	@SubscribeEvent
	private static void onClientSwing(final InputEvent.InteractionKeyMappingTriggered event)
	{
		if (event.isAttack())
		{
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null && player.getAttackStrengthScale(0f) < 1f)
			{
				ItemStack mainHand = player.getMainHandItem();
				if (player.getAttackStrengthScale(0f) < 0.1f && DungeonItemAccess.isDungeonItem(mainHand))
				{
					event.setCanceled(true);
					event.setSwingHand(false);
				}
			}
		}
	}
	@EventBusSubscriber(modid = "minigames")
	public static class ServerDamageEvents {

		@SubscribeEvent
		public static void onLivingDamage(LivingIncomingDamageEvent event) {
			if (event.getSource().getDirectEntity() instanceof Player player) {
				ItemStack mainHand = player.getMainHandItem();

				if (DungeonItemAccess.isDungeonItem(mainHand)) {
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
}
