package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ThrusterMoveProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double mult = 0;
		mult = 0.9;
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).thrusterTicks > 0) {
			entity.setDeltaMovement(new Vec3((entity.getData(MinigamesModVariables.PLAYER_VARIABLES).thrusterDirection.x() * mult), (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).thrusterDirection.y() * mult),
					(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).thrusterDirection.z() * mult)));
			if (world instanceof ServerLevel _level) {
				_level.sendParticles(ParticleTypes.SMOKE, x, (y + 1), z, 20, 0.2, 0.2, 0.2, 0.01);
				_level.sendParticles(ParticleTypes.FLAME, x, (y + 1), z, 3, 0.1, 0.1, 0.1, 0.01);
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.thrusterTicks = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).thrusterTicks - 1;
				_vars.markSyncDirty();
			}
		}
	}
}
