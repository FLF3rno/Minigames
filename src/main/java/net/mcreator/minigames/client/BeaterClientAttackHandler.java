package net.mcreator.minigames.client;

import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.entity.LivingEntity;

import net.mcreator.minigames.item.BeaterItem;
import net.mcreator.minigames.network.BeaterAttackMessage;
import net.mcreator.minigames.network.BeaterMissMessage;

@EventBusSubscriber(value = Dist.CLIENT)
public class BeaterClientAttackHandler {

	private static long lastAttackMillis = 0;

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
		if (!event.isAttack() || event.isCanceled()) {
			return;
		}

		if (BeaterMusicManager.isRecording()) {
			event.setCanceled(true);
			BeaterMusicManager.recordCurrentBeat();
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null || !(player.getMainHandItem().getItem() instanceof BeaterItem)) {
			return;
		}

		if (player.getAttackStrengthScale(0f) < 0.9f) {
			return;
		}

		handleSwingOrAttack(mc, player);
	}

	@SubscribeEvent
	public static void onAttackEntity(AttackEntityEvent event) {
		if (event.getEntity() == null || !event.getEntity().level().isClientSide()) {
			return;
		}

		if (BeaterMusicManager.isRecording()) {
			event.setCanceled(true);
			return;
		}

		Player player = event.getEntity();
		if (!(player.getMainHandItem().getItem() instanceof BeaterItem)) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.player != player) {
			return;
		}

		if (System.currentTimeMillis() - lastAttackMillis < 50) {
			return;
		}

		handleSwingOrAttack(mc, player);
	}

	private static void handleSwingOrAttack(Minecraft mc, Player player) {
		lastAttackMillis = System.currentTimeMillis();

		BeaterMusicManager.TimingInfo timing = BeaterMusicManager.getTimingInfo();
		if (timing == null) {
			return;
		}

		// Record that player clicked for this beat to prevent WAKE UP!
		BeaterMusicManager.recordBeatClicked(timing.nearestBeatIndex);

		// Trigger rating HUD text immediately
		BeaterMusicManager.triggerRating(timing.rating);

		if (timing.onBeat) {
			BeaterMusicManager.triggerOnBeatHit();
		}

		if (mc.hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget) {
			ClientPacketDistributor.sendToServer(new BeaterAttackMessage(livingTarget.getId(), timing.onBeat, timing.rating));
		} else {
			if (!timing.onBeat) {
				ClientPacketDistributor.sendToServer(new BeaterMissMessage());
			}
		}
	}
}
