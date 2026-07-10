package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;

import net.mcreator.minigames.entity.MovingBlockEntity;

@EventBusSubscriber
public class MovingOnMovingBlockProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		boolean touchingTopOfMovingBlock = false;
		MovingBlockEntity supportBlock = null;
		{
			final Vec3 center = new Vec3(x, y - 0.25, z);
			for (Entity e : world.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(1.0), it -> it instanceof MovingBlockEntity)) {
				if (entity.getBoundingBox().minY >= e.getY() + e.getBbHeight() - 0.5 && entity.getBoundingBox().intersects(e.getBoundingBox().inflate(0.0, 0.5, 0.0))) {
					touchingTopOfMovingBlock = true;
					supportBlock = (MovingBlockEntity) e;
					break;
				}
			}
		}
		if (!touchingTopOfMovingBlock)
			return;

		Minecraft mc = Minecraft.getInstance();
		double friction = 0.91;
		if (supportBlock != null) {
			String blockId = supportBlock.getEntityData().get(MovingBlockEntity.DATA_block_id);
			var block = BuiltInRegistries.BLOCK.getOptional(Identifier.tryParse(blockId)).orElse(Blocks.STONE);
			// Vanilla block friction uses 0.6 as normal, up to ~0.98 for ice.
			// Convert it to this procedure's damping scale (0.91 normal baseline).
			double vanillaFriction = block.defaultBlockState().getFriction(world, supportBlock.blockPosition(), supportBlock);
			friction = Math.max(0.0, Math.min(1.0, vanillaFriction / 0.6 * 0.91));
		}
		double baseSpeed = 0.08;
		double speed = (baseSpeed * (0.5 + friction * 0.5)) * 0.45;
		double inputX = 0;
		double inputZ = 0;

		if (mc.options.keyUp.isDown())
			inputZ += speed;
		if (mc.options.keyDown.isDown())
			inputZ -= speed;
		if (mc.options.keyLeft.isDown())
			inputX += speed;
		if (mc.options.keyRight.isDown())
			inputX -= speed;

		if (inputX != 0 || inputZ != 0) {
			entity.setPos(entity.getX(), entity.getY() + 0.01, entity.getZ());
			float yaw = entity.getYRot();
			double rad = Math.toRadians(yaw);
			double worldX = inputX * Math.cos(rad) - inputZ * Math.sin(rad);
			double worldZ = inputZ * Math.cos(rad) + inputX * Math.sin(rad);
			Vec3 current = entity.getDeltaMovement();
			double dampedX = current.x * friction;
			double dampedZ = current.z * friction;
			entity.setDeltaMovement(dampedX + worldX, current.y, dampedZ + worldZ);
		}
	}
}
