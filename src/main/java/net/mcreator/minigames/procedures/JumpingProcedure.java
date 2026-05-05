package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.entity.MovingBlockEntity;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class JumpingProcedure {
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
		double X = 0;
		double Z = 0;
		boolean touchingTopOfMovingBlock = false;
		{
			final Vec3 _center = new Vec3(x, y - 0.25, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> e instanceof MovingBlockEntity)) {
				if (entity.getBoundingBox().minY >= entityiterator.getY() + entityiterator.getBbHeight() - 0.5 && entity.getBoundingBox().intersects(entityiterator.getBoundingBox().inflate(0.0, 0.5, 0.0))) {
					touchingTopOfMovingBlock = true;
					break;
				}
			}
		}
		if (touchingTopOfMovingBlock && Minecraft.getInstance().options.keyJump.isDown()) {
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.jumps = true;
				_vars.markSyncDirty();
			}
			MinigamesMod.queueServerWork(1, () -> {
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.jumps = false;
					_vars.markSyncDirty();
				}
			});
		}
		if (touchingTopOfMovingBlock) {
			final Vec3 _center = new Vec3(x, (y - 0.4), z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1.0), e -> e instanceof MovingBlockEntity).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).jumps) {
					entity.setPos(entity.getX(), entity.getY() + 0.01, entity.getZ());
					entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x()), 0.4, (entity.getDeltaMovement().z())));
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.jumps = false;
					_vars.markSyncDirty();
				}
			}
		}
	}
}
