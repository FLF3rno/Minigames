package net.mcreator.minigames.procedures;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import net.mcreator.minigames.item.BeaterItem;

public class BeaterHitProcedure {

	public static void execute() {
	}

	public static void processBeaterMiss(Player player) {
		if (player == null)
			return;
		player.getPersistentData().putInt("beaterCombo", 0);
	}

	public static void processBeaterHit(Player player, int targetId, boolean onBeat, String rating) {
		if (player == null)
			return;

		boolean holding = player.getMainHandItem().getItem() instanceof BeaterItem
				|| player.getOffhandItem().getItem() instanceof BeaterItem;
		if (!holding)
			return;

		Entity target = player.level().getEntity(targetId);
		if (!(target instanceof LivingEntity livingTarget) || !livingTarget.isAlive())
			return;

		if (player.distanceToSqr(target) > 36.0)
			return;

		Level world = player.level();

		if (onBeat) {
			int combo = player.getPersistentData().getIntOr("beaterCombo", 0) + 1;
			if (combo > 5) {
				combo = 1;
			}
			player.getPersistentData().putInt("beaterCombo", combo);

			// First Attack - Apply weakness 1 (3 seconds = 60 ticks)
			ApplyEffectProcedure.execute(world, livingTarget, false, 1, 60, "minecraft:weakness");

			// Second Attack - Apply slowness 1 (3 seconds = 60 ticks)
			if (combo >= 2) {
				ApplyEffectProcedure.execute(world, livingTarget, false, 1, 60, "minigames:slowdown");
			}

			// Third Attack - Apply stun (2 seconds = 40 ticks)
			if (combo >= 3) {
				ApplyEffectProcedure.execute(world, livingTarget, false, 1, 40, "minigames:stunned");
			}

			// Fourth Attack - +2 damage
			if (combo >= 4) {
				if (world instanceof ServerLevel serverLevel) {
					livingTarget.hurtServer(serverLevel, player.damageSources().playerAttack(player), 2.0f);
					serverLevel.sendParticles(ParticleTypes.CRIT, livingTarget.getX(), livingTarget.getY() + livingTarget.getBbHeight() / 2.0, livingTarget.getZ(), 8, 0.2, 0.2, 0.2, 0.1);
				}
			}

			// Fifth Attack - +2 damage and Explode
			if (combo >= 5) {
				if (world instanceof ServerLevel serverLevel) {
					livingTarget.hurtServer(serverLevel, player.damageSources().playerAttack(player), 2.0f);
				}
				ExplodeProcedure.execute(world, livingTarget.getX(), livingTarget.getY(), livingTarget.getZ(), player, false, true, 8.0, 1.0, 3.0, "normal");
				player.getPersistentData().putInt("beaterCombo", 0);
			}

			// On-beat audio cue
			if (world instanceof ServerLevel serverLevel) {
				float pitch = 1.0f + (combo * 0.15f);
				serverLevel.playSound(null, livingTarget.getX(), livingTarget.getY(), livingTarget.getZ(),
						SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.PLAYERS, 0.9f, pitch);
			}
		} else {
			// Miss: reset combo!
			player.getPersistentData().putInt("beaterCombo", 0);
			if (world instanceof ServerLevel serverLevel) {
				serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS, 0.7f, 0.5f);
			}
		}
	}
}