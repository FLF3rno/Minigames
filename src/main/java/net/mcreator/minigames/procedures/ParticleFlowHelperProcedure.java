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

        Vec3 delta = to.subtract(from);
        double distance = delta.length();
        if (distance < 0.001D)
            return;

        if (particleCount > 1) {
            Vec3 dir = delta.normalize();
            double step = distance / (double) particleCount;
            for (int i = 0; i < particleCount; i++) {
                double offset = (i + Math.random() * 0.5D) * step;
                if (offset > distance)
                    offset = distance;
                Vec3 p = from.add(dir.scale(offset));

                serverLevel.sendParticles(
                        particle,
                        true,
                        false,
                        p.x,
                        p.y,
                        p.z,
                        1,
                        0.0D,
                        0.0D,
                        0.0D,
                        0.0D
                );
            }
        } else {
            double friction = 0.98D;
            double fx = (1.0D - Math.pow(friction, ticks)) / (1.0D - friction);

            Vec3 velocity;
            if (easing != null && easing.equalsIgnoreCase("linear")) {
                velocity = delta.scale(1.0D / fx);
            } else {
                double totalDistanceFactor = getEasingDistanceFactor(easing);
                velocity = delta.scale(1.0D / (fx * totalDistanceFactor));
            }

            serverLevel.sendParticles(
                    particle,
                    true,
                    false,
                    from.x,
                    from.y,
                    from.z,
                    0,
                    velocity.x,
                    velocity.y,
                    velocity.z,
                    1.0D
            );
        }
    }

    private static double getEasingDistanceFactor(String easing) {
        if (easing == null) return 1.0D;
        return switch (easing.toLowerCase()) {
            case "easein" -> 0.5D;
            case "easeout" -> 0.5D;
            case "easeinout" -> 0.5D;
            default -> 1.0D;
        };
    }
}