package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.core.Holder;
import net.mcreator.minigames.init.MinigamesModParticleTypes;
import net.mcreator.minigames.entity.PlagueMiddleEntity;

import java.util.List;
import java.util.ArrayList;

public class PlagueMiddleTickUpdateProcedure {
	public static final TagKey<EntityType<?>> IS_MARKER = TagKey.create(
			Registries.ENTITY_TYPE,
			Identifier.fromNamespaceAndPath("minigames", "marker"));

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		int range = entity instanceof PlagueMiddleEntity _e ? _e.getEntityData().get(PlagueMiddleEntity.DATA_range) : 0;
		double xRadius = range + 0.5;

		for (double loop = 0; loop < 65; loop++) {
			double pX = x + 0.5 + Math.cos(((Math.PI * 2) / 65) * loop) * xRadius;
			double pZ = z + 0.5 + Math.sin(((Math.PI * 2) / 65) * loop) * xRadius;
			world.addParticle(ParticleTypes.HAPPY_VILLAGER, pX, y, pZ, 0, 0.05, 0);

			if (Math.random() < 0.04) {
				world.addParticle((SimpleParticleType) MinigamesModParticleTypes.POISON.get(), pX, y, pZ, 0, 0.05, 0);
			}
		}

		int duration = entity instanceof PlagueMiddleEntity _e ? _e.getEntityData().get(PlagueMiddleEntity.DATA_duration) : 0;

		if (duration <= 0) {
			if (!entity.level().isClientSide())
				entity.discard();
		} else {
			if (entity instanceof PlagueMiddleEntity _e)
				_e.getEntityData().set(PlagueMiddleEntity.DATA_duration, duration - 1);
		}

		if (duration % 7 == 0) {
			Vec3 center = new Vec3(x, y, z);

			List<Entity> nearby = world.getEntitiesOfClass(
					Entity.class,
					new AABB(center, center).inflate(range + 0.5),
					e -> true);

			for (Entity source : nearby) {
				if (source instanceof LivingEntity livingSource
						&& source instanceof PathfinderMob
						&& livingSource.isAlive()
						&& !(source instanceof PlagueMiddleEntity)
						&& !source.is(IS_MARKER)) {

					List<MobEffectInstance> effects =
							new ArrayList<>(livingSource.getActiveEffects());

					for (MobEffectInstance effectInstance : effects) {
						Holder<MobEffect> effectHolder = effectInstance.getEffect();
						String effectKey = BuiltInRegistries.MOB_EFFECT.getKey(effectHolder.value()).toString();

						for (Entity target : nearby) {
							if (target instanceof LivingEntity livingTarget
									&& target instanceof PathfinderMob
									&& livingTarget.isAlive()
									&& !(target instanceof PlagueMiddleEntity)
									&& !target.is(IS_MARKER)) {

								ApplyEffectProcedure.execute(
										world,
										livingTarget,
										false,
										effectInstance.getAmplifier(),
										effectInstance.getDuration(),
										effectKey);
							}
						}
					}
				}
			}
		}
	}
}
