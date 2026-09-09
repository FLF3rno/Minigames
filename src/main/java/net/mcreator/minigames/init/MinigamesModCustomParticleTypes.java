package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.mcreator.minigames.MinigamesMod;

public class MinigamesModCustomParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, MinigamesMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CUSTOM_ENCHANT = REGISTRY.register("custom_enchant", () -> new SimpleParticleType(false));
}
