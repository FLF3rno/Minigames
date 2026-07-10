package net.mcreator.minigames.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class PoisonParticle {
	public static ParticleProvider<SimpleParticleType> provider(SpriteSet spriteSet) {
		return new ParticleProvider<SimpleParticleType>() {
			@Override
			public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
				return null;
			}
		};
	}
}
