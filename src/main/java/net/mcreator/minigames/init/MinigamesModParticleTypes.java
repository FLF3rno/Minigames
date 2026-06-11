/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.mcreator.minigames.MinigamesMod;

public class MinigamesModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, MinigamesMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RED_EXPLOSION = REGISTRY.register("red_explosion", () -> new SimpleParticleType(false));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POISON = REGISTRY.register("poison", () -> new SimpleParticleType(false));
}