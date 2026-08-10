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
import net.mcreator.minigames.procedures.StunnedEffectExpiresProcedure;
import net.mcreator.minigames.procedures.PhantomEffectEndsProcedure;
import net.mcreator.minigames.procedures.BlessedEffectExpiresProcedure;
import net.mcreator.minigames.procedures.AscendingEffectExpiresProcedure;
import net.mcreator.minigames.potion.*;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public class MinigamesModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, MinigamesMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> IMMOBILIZED = REGISTRY.register("immobilized", ImmobilizedMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> CROWNED = REGISTRY.register("crowned", CrownedMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> BLOCK_LEFT_CLICK = REGISTRY.register("block_left_click", BlockLeftClickMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> HYPNOTIZED = REGISTRY.register("hypnotized", HypnotizedMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> STUNNED = REGISTRY.register("stunned", StunnedMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> BLESSED = REGISTRY.register("blessed", BlessedMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> ASCENDING = REGISTRY.register("ascending", AscendingMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> ADVANCED_GLOWING = REGISTRY.register("advanced_glowing", AdvancedGlowingMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> PHANTOM = REGISTRY.register("phantom", PhantomMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> DAMAGE_BOOST = REGISTRY.register("damage_boost", DamageBoostMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> BLEED = REGISTRY.register("bleed", BleedMobEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> DECAY = REGISTRY.register("decay", DecayMobEffect::new);

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
		if (effectInstance.is(CROWNED)) {
			SyncCrownedRemoveProcedure.execute(entity.level(), entity);
		} else if (effectInstance.is(STUNNED)) {
			StunnedEffectExpiresProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
		} else if (effectInstance.is(BLESSED)) {
			BlessedEffectExpiresProcedure.execute(entity.level(), entity, effectInstance.getAmplifier());
		} else if (effectInstance.is(ASCENDING)) {
			AscendingEffectExpiresProcedure.execute(entity);
		} else if (effectInstance.is(PHANTOM)) {
			PhantomEffectEndsProcedure.execute(entity);
		}
	}
}