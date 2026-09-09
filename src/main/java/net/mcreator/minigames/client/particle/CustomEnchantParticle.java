package net.mcreator.minigames.client.particle;

import net.minecraft.util.RandomSource;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;

public class CustomEnchantParticle extends SingleQuadParticle {
	public static CustomEnchantParticleProvider provider(SpriteSet spriteSet) {
		return new CustomEnchantParticleProvider(spriteSet);
	}

	public static class CustomEnchantParticleProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public CustomEnchantParticleProvider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			return new CustomEnchantParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}

	private final SpriteSet spriteSet;

	protected CustomEnchantParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
		super(world, x, y, z, spriteSet.first());
		this.spriteSet = spriteSet;
		this.setSize(0.1f, 0.1f);
		this.quadSize *= 0.75f;
		this.lifetime = 100;
		this.gravity = 0f;
		this.hasPhysics = false;
		this.xd = vx;
		this.yd = vy;
		this.zd = vz;
		this.rCol = 0.9F;
		this.gCol = 0.9F;
		this.bCol = 1.0F;
		this.setSpriteFromAge(spriteSet);
	}

	@Override
	public int getLightCoords(float partialTick) {
		return 15728880;
	}

	@Override
	public SingleQuadParticle.Layer getLayer() {
		return SingleQuadParticle.Layer.TRANSLUCENT;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.removed) {
			this.setSprite(this.spriteSet.get((this.age / 4) % 26, 26));
		}
	}
}
