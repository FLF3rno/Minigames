/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.procedures.SyncCrownedRemoveProcedure;
import net.mcreator.minigames.procedures.StartPVPProcedure;
import net.mcreator.minigames.potion.NerfHuntersMobEffect;
import net.mcreator.minigames.potion.ImmobilizedMobEffect;
import net.mcreator.minigames.potion.HypnotizedMobEffect;
import net.mcreator.minigames.potion.CrownedMobEffect;
import net.mcreator.minigames.potion.BlockLeftClickMobEffect;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public class MinigamesModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, MinigamesMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> IMMOBILIZED = REGISTRY.register("immobilized", () -> new ImmobilizedMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> CROWNED = REGISTRY.register("crowned", () -> new CrownedMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> NERF_HUNTERS = REGISTRY.register("nerf_hunters", () -> new NerfHuntersMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> BLOCK_LEFT_CLICK = REGISTRY.register("block_left_click", () -> new BlockLeftClickMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> HYPNOTIZED = REGISTRY.register("hypnotized", () -> new HypnotizedMobEffect());

	@SubscribeEvent
	public static void onEffectRemoved(MobEffectEvent.Remove event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			expireEffects(event.getEntity(), effectInstance);
		}
	}

	@SubscribeEvent
	public static void onEffectExpired(MobEffectEvent.Expired event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			expireEffects(event.getEntity(), effectInstance);
		}
	}

	private static void expireEffects(Entity entity, MobEffectInstance effectInstance) {
		if (effectInstance.getEffect().is(CROWNED)) {
			SyncCrownedRemoveProcedure.execute(entity.level(), entity);
		} else if (effectInstance.getEffect().is(NERF_HUNTERS)) {
			StartPVPProcedure.execute(entity.level());
		}
	}
}