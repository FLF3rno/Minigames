package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class CountMonstersInRoomProcedure {
	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel(), null);
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (!(world instanceof ServerLevel serverLevel))
			return;
		if (!serverLevel.dimension().location().equals(ResourceLocation.parse("minigames:dungeon_dimension")))
			return;
		double aliveEnemies = 0;
		double currentRoomID = MinigamesModVariables.MapVariables.get(world).currentRoomID;
		int currentRoomIDInt = (int) currentRoomID;
		TagKey<net.minecraft.world.entity.EntityType<?>> dungeonTag = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("minigames:dungeon"));
		for (Entity roomEntity : serverLevel.getAllEntities()) {
			int entityDataId = roomEntity.getPersistentData().getIntOr("DataID", 0);
			if (roomEntity.getType().is(dungeonTag) && roomEntity.isAlive() && entityDataId == currentRoomIDInt) {
				aliveEnemies = aliveEnemies + 1;
			}
		}
		MinigamesModVariables.MapVariables.get(world).aliveEnemies = aliveEnemies;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		
		if (MinigamesModVariables.MapVariables.get(world).inCombat) {
			if (MinigamesModVariables.MapVariables.get(world).aliveEnemies <= 0) {
					CompleteRoomProcedure.execute(world);
				}
		}

	}
}
