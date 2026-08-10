package net.mcreator.minigames.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class ConfettiParticle extends SingleQuadParticle {

    public static ConfettiParticleProvider provider(SpriteSet spriteSet) {
        return new ConfettiParticleProvider(spriteSet);
    }

    public static class ConfettiParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public ConfettiParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(
                SimpleParticleType typeIn,
                ClientLevel worldIn,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed,
                RandomSource random) {

            return new ConfettiParticle(
                    worldIn,
                    x, y, z,
                    xSpeed, ySpeed, zSpeed,
                    this.spriteSet,
                    random
            );
        }
    }

    private final SpriteSet spriteSet;

    protected ConfettiParticle(
            ClientLevel world,
            double x,
            double y,
            double z,
            double vx,
            double vy,
            double vz,
            SpriteSet spriteSet,
            RandomSource random) {

        super(world, x, y, z, spriteSet.get(random.nextInt(7), 6));

        this.spriteSet = spriteSet;

        this.setSize(0.4f, 0.4f);
        this.quadSize *= 0.75f;

        this.lifetime = 120;
        this.gravity = 0.5f;
        this.hasPhysics = true;

        this.xd = vx * 0.7;
        this.yd = vy * 0.7;
        this.zd = vz * 0.7;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
    }
}