package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.arguments.EntityAnchorArgument;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.entity.FlavioEntity;
import net.mcreator.minigames.entity.BlessingDispenserEntity;

import java.util.Comparator;

public class BlessingDispenserTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.tickCount % 1 == 0) {
			{
				final Vec3 _center = new Vec3(x, y, z);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(40 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (entityiterator instanceof FlavioEntity) {
						ParticleFlowHelperProcedure.execute(world, 1, 50, "linear", "minigames:blessed_particle", new Vec3(x, (y + 3.7), z), new Vec3((entityiterator.getX()), (entityiterator.getY() + 1), (entityiterator.getZ())));
						if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.BLESSED, 100000, 1, false, false));
						entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ())));
					}
				}
			}
			{
				final Vec3 _center = new Vec3(x, y, z);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (entityiterator instanceof Player) {
						if (!(entityiterator instanceof LivingEntity _livEnt14 && _livEnt14.hasEffect(MinigamesModMobEffects.IMMOBILIZED))
								&& !(entityiterator instanceof LivingEntity _livEnt15 && _livEnt15.hasEffect(MinigamesModMobEffects.BLESSED))) {
							ParticleFlowHelperProcedure.execute(world, 1, 10, "linear", "minigames:cursed_particle", new Vec3(x, (y + 3.7), z), new Vec3((entityiterator.getX()), (entityiterator.getY() + 1), (entityiterator.getZ())));
							{
								Entity _ent = entityiterator;
								if (_ent.level() instanceof ServerLevel _serverLevel) {
									_ent.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(DamageTypes.GENERIC), entity), 1);
								}
							}
						}
					}
				}
			}
		}
		if (entity.tickCount % 54 == 0) {
			if (entity instanceof BlessingDispenserEntity _ent25) {
				_ent25.getEntityData().set(BlessingDispenserEntity.ANIM, 1000);
				_ent25.getEntityData().set(BlessingDispenserEntity.ANIM, 0);
			}
		}
	}
}