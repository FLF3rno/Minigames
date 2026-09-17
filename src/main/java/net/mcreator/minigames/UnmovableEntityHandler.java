package net.mcreator.minigames;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@EventBusSubscriber
public class UnmovableEntityHandler {
	public static final TagKey<EntityType<?>> UNMOVABLE_TAG = TagKey.create(
		Registries.ENTITY_TYPE,
		Identifier.fromNamespaceAndPath("minigames", "unmovable")
	);

	private static final Set<Entity> SELF_LOCOMOTING = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

	public static boolean isUnmovable(Entity entity) {
		if (entity == null || entity.level() == null) {
			return false;
		}
		if (!MinigamesModVariables.MapVariables.get(entity.level()).playingDungeons) {
			return false;
		}
		return entity.is(UNMOVABLE_TAG);
	}

	public static void setSelfLocomotion(Entity entity, boolean active) {
		if (entity == null) {
			return;
		}
		if (active) {
			SELF_LOCOMOTING.add(entity);
		} else {
			SELF_LOCOMOTING.remove(entity);
		}
	}

	public static boolean isSelfLocomoting(Entity entity) {
		return entity != null && SELF_LOCOMOTING.contains(entity);
	}

	@SubscribeEvent
	public static void onLivingKnockBack(LivingKnockBackEvent event) {
		if (isUnmovable(event.getEntity())) {
			event.setCanceled(true);
		}
	}
}
