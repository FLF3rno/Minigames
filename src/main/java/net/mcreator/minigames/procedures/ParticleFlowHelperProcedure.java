package net.mcreator.minigames.procedures;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class ParticleFlowHelperProcedure {

    public static void execute(
            LevelAccessor world,
            int particleCount,
            int ticks,
            String easing,
            String particleId,
            Vec3 from,
            Vec3 to) {

        if (!(world instanceof ServerLevel serverLevel))
            return;

        if (ticks <= 0 || particleCount <= 0
                || particleId == null
                || from == null
                || to == null)
            return;

        Identifier id = Identifier.parse(particleId);

        if (!BuiltInRegistries.PARTICLE_TYPE.containsKey(id))
            return;

        if (!(BuiltInRegistries.PARTICLE_TYPE.getValue(id)
                instanceof SimpleParticleType particle))
            return;

        double friction = 0.98;

        double fx = (1.0 - Math.pow(friction, ticks))
                / (1.0 - friction);

        Vec3 delta = to.subtract(from);

        Vec3 velocity;

        if (easing.equalsIgnoreCase("linear")) {

            velocity = delta.scale(1.0 / fx);

        } else {

            double totalDistanceFactor =
                    getEasingDistanceFactor(easing);

            velocity = delta.scale(
                    1.0 / (fx * totalDistanceFactor)
            );
        }

        for (int i = 0; i < particleCount; i++) {
            serverLevel.sendParticles(
                    particle,
                    from.x,
                    from.y,
                    from.z,
                    0,
                    velocity.x,
                    velocity.y,
                    velocity.z,
                    1.0
            );
        }
    }

    private static double getEasingDistanceFactor(String easing) {
        return switch (easing.toLowerCase()) {
            case "easein" -> 0.5;
            case "easeout" -> 0.5;
            case "easeinout" -> 0.5;
            default -> 1.0;
        };
    }
}