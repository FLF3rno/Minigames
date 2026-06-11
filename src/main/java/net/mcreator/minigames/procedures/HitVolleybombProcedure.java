package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.minigames.entity.VolleybombEntityEntity;

import javax.annotation.Nullable;

@EventBusSubscriber
public class HitVolleybombProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getDirectEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity, Entity sourceentity) {
		execute(null, world, x, y, z, entity, immediatesourceentity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity, Entity sourceentity) {
		if (entity == null || immediatesourceentity == null || sourceentity == null)
			return;
		double force = 0;
		if (entity instanceof VolleybombEntityEntity) {
			if (!(immediatesourceentity instanceof Player || immediatesourceentity instanceof ServerPlayer)) {
				entity.getPersistentData().putDouble("explosionDamage", (entity.getPersistentData().getDoubleOr("explosionDamage", 0) * 1.5));
				entity.getPersistentData().putDouble("explosionSize", (entity.getPersistentData().getDoubleOr("explosionSize", 0) * 1.5));
				if (!entity.level().isClientSide())
					entity.discard();
				ExplodeProcedure.execute(world, x, y, z, sourceentity, false, true, entity.getPersistentData().getDoubleOr("explosionDamage", 0), Math.sqrt(entity.getPersistentData().getDoubleOr("explosionDamage", 0)) * 0.5,
						entity.getPersistentData().getDoubleOr("explosionSize", 0), "red");
				entity.getPersistentData().putDouble("explosionDamage", (entity.getPersistentData().getDoubleOr("explosionDamage", 0) * 0));
			} else {
				entity.getPersistentData().putDouble("explosionDamage", (entity.getPersistentData().getDoubleOr("explosionDamage", 0) * 1.2));
				entity.getPersistentData().putDouble("explosionSize", (entity.getPersistentData().getDoubleOr("explosionSize", 0) * 0.8));
				force = 5;
				entity.setDeltaMovement(new Vec3((sourceentity.getLookAngle().x * force), (sourceentity.getLookAngle().y * force), (sourceentity.getLookAngle().z * force)));
				if (entity instanceof VolleybombEntityEntity _datEntSetL)
					_datEntSetL.getEntityData().set(VolleybombEntityEntity.DATA_exploding, false);
			}
		}
	}
}