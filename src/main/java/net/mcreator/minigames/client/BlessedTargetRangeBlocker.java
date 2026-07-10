package net.mcreator.minigames.client;

import net.mcreator.minigames.init.MinigamesModMobEffects;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.ProjectileUtil;

@EventBusSubscriber(value = Dist.CLIENT)
public class BlessedTargetRangeBlocker {
	private static final Identifier MODIFIER_ID = Identifier.fromNamespaceAndPath("minigames", "blessed_target_no_hit");
	private static final AttributeModifier BLOCK_HIT_RANGE = new AttributeModifier(MODIFIER_ID, -1000.0, AttributeModifier.Operation.ADD_VALUE);
	private static final double TARGET_CHECK_RANGE = 6.0;
	private static boolean applied = false;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null) {
			applied = false;
			return;
		}

		boolean shouldBlock = false;
		EntityHitResult entityHitResult = getLookedAtEntity(minecraft, TARGET_CHECK_RANGE);
		if (entityHitResult != null && entityHitResult.getEntity() instanceof LivingEntity living) {
			shouldBlock = hasBlessedMarker(living) || living.hasEffect(MinigamesModMobEffects.BLESSED);
		}

		var rangeAttr = minecraft.player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
		if (rangeAttr == null) {
			return;
		}

		if (shouldBlock && !applied) {
			rangeAttr.addOrUpdateTransientModifier(BLOCK_HIT_RANGE);
			applied = true;
		} else if (!shouldBlock && applied) {
			rangeAttr.removeModifier(MODIFIER_ID);
			applied = false;
		}
	}

	private static EntityHitResult getLookedAtEntity(Minecraft minecraft, double range) {
		Entity camera = minecraft.getCameraEntity();
		if (camera == null || minecraft.level == null) return null;

		Vec3 start = camera.getEyePosition(1.0F);
		Vec3 end = start.add(camera.getViewVector(1.0F).scale(range));
		AABB searchBox = camera.getBoundingBox().expandTowards(camera.getViewVector(1.0F).scale(range)).inflate(1.0D);

		return ProjectileUtil.getEntityHitResult(camera, start, end, searchBox, e -> e instanceof LivingEntity && e.isPickable(), range * range);
	}

	private static boolean hasBlessedMarker(LivingEntity entity) {
		if (!entity.hasCustomName()) return false;
		String name = entity.getCustomName().getString();
		return name != null && name.contains("[blessed]");
	}
}
