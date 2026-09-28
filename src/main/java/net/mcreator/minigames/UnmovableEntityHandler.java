package net.mcreator.minigames;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayDeque;

@EventBusSubscriber
public class UnmovableEntityHandler {
	public static final TagKey<EntityType<?>> UNMOVABLE_TAG = TagKey.create(
		Registries.ENTITY_TYPE,
		Identifier.fromNamespaceAndPath("minigames", "unmovable")
	);

	private static final ThreadLocal<ArrayDeque<Entity>> TICKING_ENTITIES = ThreadLocal.withInitial(ArrayDeque::new);

	public static boolean isUnmovable(Entity entity) {
		if (entity == null || entity.level() == null) {
			return false;
		}
		if (entity instanceof net.mcreator.minigames.entity.ChurchSharpenerEntity sharpener && sharpener.getState() == net.mcreator.minigames.entity.ChurchSharpenerEntity.STATE_CHARGING) {
			return true;
		}
		if (!MinigamesModVariables.MapVariables.get(entity.level()).playingDungeons) {
			return false;
		}
		return entity.is(UNMOVABLE_TAG);
	}

	public static void pushTickingEntity(Entity entity) {
		if (entity != null) {
			TICKING_ENTITIES.get().push(entity);
		}
	}

	public static void popTickingEntity(Entity entity) {
		ArrayDeque<Entity> stack = TICKING_ENTITIES.get();
		if (!stack.isEmpty()) {
			if (stack.peek() == entity) {
				stack.pop();
			} else {
				stack.remove(entity);
			}
		}
	}

	public static boolean isSelfTicking(Entity entity) {
		if (entity == null) {
			return false;
		}
		return TICKING_ENTITIES.get().peek() == entity;
	}

	public static void setSelfLocomotion(Entity entity, boolean active) {
		if (active) {
			pushTickingEntity(entity);
		} else {
			popTickingEntity(entity);
		}
	}

	public static boolean isSelfLocomoting(Entity entity) {
		return isSelfTicking(entity);
	}

	@SubscribeEvent
	public static void onEntityTickPre(EntityTickEvent.Pre event) {
		pushTickingEntity(event.getEntity());
	}

	@SubscribeEvent
	public static void onEntityTickPost(EntityTickEvent.Post event) {
		popTickingEntity(event.getEntity());
	}

	@SubscribeEvent
	public static void onLivingKnockBack(LivingKnockBackEvent event) {
		if (isUnmovable(event.getEntity())) {
			event.setCanceled(true);
			return;
		}
		if (net.mcreator.minigames.entity.ChurchSharpenerEntity.isVictimPinned(event.getEntity())) {
			event.setCanceled(true);
		}
	}
}
