package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class SlamFallOnGroundProcedure {
	@SubscribeEvent
	public static void onEntityFall(LivingFallEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.getPersistentData().getBooleanOr("slam", false)) {
			entity.getPersistentData().putBoolean("slam", false);
			ExplodeProcedure.execute(world, x, y, z, entity, true, true, 0.1, 0, GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.SLAM.get()), "minigames:ability_range"), "groundbreaking");
			{
				final Vec3 _center = new Vec3(x, y, z);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.SLAM.get()), "minigames:ability_range") / 2d), e -> true)
						.stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (!(entityiterator == entity)) {
						entityiterator.setDeltaMovement(new Vec3(0, GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.SLAM.get()), "minigames:effect_potency"), 0));
					}
				}
			}
		}
	}
}