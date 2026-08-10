package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class OnEntityTickDungeonProcedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).playingDungeons) {
			if (entity.tickCount % 10 == 0) {
				if (entity.is(TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:blesser")))) {
					{
						final Vec3 _center = new Vec3(x, y, z);
						for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(50 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
							if (entityiterator instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(MinigamesModMobEffects.BLESSED) && entityiterator.is(TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:dungeon")))
									&& entity.getPersistentData().getDoubleOr("DataID", 0) == entityiterator.getPersistentData().getDoubleOr("DataID", 0)) {
								RenderBeamProcedure.execute(entityiterator, entity, 2, 10, Identifier.fromNamespaceAndPath("minigames", "textures/entities/blessedbeamcolor.png"));
							}
						}
					}
				}
			}
		}
	}
}