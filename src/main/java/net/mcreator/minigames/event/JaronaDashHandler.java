package net.mcreator.minigames.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.procedures.CheckRelicProcedure;
import net.mcreator.minigames.procedures.GetItemAttributeProcedure;
import net.mcreator.minigames.procedures.PlayerTrailProcedure;
import net.mcreator.minigames.procedures.ReturnRelicItemstackProcedure;

import java.util.List;

@EventBusSubscriber
public class JaronaDashHandler {

	private static final TagKey<EntityType<?>> DUNGEON_TAG = TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:dungeon"));

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player == null)
			return;

		Level world = player.level();
		if (world.isClientSide())
			return;

		handleCollision(player, world);
		detectDash(player, world);
	}

	public static void triggerJaronaDash(Level world, Player player, double extraImpulse) {
		if (player == null || world == null)
			return;
		double dashLength = player.getData(MinigamesModVariables.PLAYER_VARIABLES).dashLength;
		double dashTicks = Math.max(6, extraImpulse * 5 * dashLength);
		PlayerTrailProcedure.execute(world, player, true, 8, 0.7, dashTicks, "rainbow");
		player.getPersistentData().putInt("jaronaDashTicks", (int) Math.round(dashTicks));
	}

	private static void detectDash(Player player, Level world) {
		int currentCooldown = (int) player.getData(MinigamesModVariables.PLAYER_VARIABLES).dashCooldown;
		int lastCooldown = player.getPersistentData().getIntOr("jaronaLastDashCooldown", 0);
		player.getPersistentData().putInt("jaronaLastDashCooldown", currentCooldown);

		if (lastCooldown <= 0 && currentCooldown > 0) {
			if (!player.getPersistentData().contains("jaronaDashTicks")) {
				if (CheckRelicProcedure.execute(player, new ItemStack(MinigamesModItems.JARONA.get()))) {
					double extraImpulse = 1.0;
					for (Entity p : world.players()) {
						if (CheckRelicProcedure.execute(p, new ItemStack(MinigamesModItems.THRUSTERS_ROGUELIKE.get()))) {
							extraImpulse += GetItemAttributeProcedure.execute(
									ReturnRelicItemstackProcedure.execute(p, new ItemStack(MinigamesModItems.THRUSTERS_ROGUELIKE.get())),
									"minigames:effect_potency"
							) / 100.0;
						}
					}
					triggerJaronaDash(world, player, extraImpulse);
				}
			}
		}
	}

	private static void handleCollision(Player player, Level world) {
		if (!player.getPersistentData().contains("jaronaDashTicks"))
			return;

		int jTicks = player.getPersistentData().getIntOr("jaronaDashTicks", 0);
		if (jTicks <= 0) {
			player.getPersistentData().remove("jaronaDashTicks");
			return;
		}

		player.getPersistentData().putInt("jaronaDashTicks", jTicks - 1);

		if (world.isClientSide())
			return;

		AABB hitBox = player.getBoundingBox().inflate(0.6, 0.3, 0.6);
		List<LivingEntity> enemies = world.getEntitiesOfClass(LivingEntity.class, hitBox, target ->
				target != player && target.isAlive() && !(target instanceof Player) &&
						(target instanceof Enemy || target.is(DUNGEON_TAG))
		);

		if (!enemies.isEmpty()) {
			LivingEntity hitMonster = enemies.get(0);
			player.setDeltaMovement(Vec3.ZERO);
			player.hurtMarked = true;
			if (player instanceof ServerPlayer serverPlayer) {
				serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
			}
			player.getPersistentData().remove("jaronaDashTicks");

			double attackDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
			float totalDamage = (float) (attackDamage + 1.0);

			if (world instanceof ServerLevel serverLevel) {
				hitMonster.hurtServer(serverLevel, serverLevel.damageSources().playerAttack(player), totalDamage);
				serverLevel.sendParticles(ParticleTypes.CRIT, hitMonster.getX(), hitMonster.getY(0.5), hitMonster.getZ(), 15, 0.2, 0.2, 0.2, 0.1);
			}

			world.playSound(null, hitMonster.getX(), hitMonster.getY(), hitMonster.getZ(),
					BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.player.attack.crit")),
					SoundSource.PLAYERS, 1.0F, 1.0F);
		}
	}
}
