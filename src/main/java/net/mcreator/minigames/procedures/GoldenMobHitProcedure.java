package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class GoldenMobHitProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingDamageEvent.Pre event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		execute(null, world, x, y, z, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.GOLDEN)) {
			if (sourceentity instanceof Player && !world.isClientSide()) {
				MinigamesModVariables.MapVariables.get(world).dungeonCoins = MinigamesModVariables.MapVariables.get(world).dungeonCoins
						+ (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.GOLDEN) ? _livEnt.getEffect(MinigamesModMobEffects.GOLDEN).getAmplifier() : 0) + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:coin")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:coin")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
				MinigamesModVariables.MapVariables.get(world).showCoins = true;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				MinigamesMod.queueServerWork(100, () -> {
					MinigamesModVariables.MapVariables.get(world).showCoins = false;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				});
			}
		}
	}
}