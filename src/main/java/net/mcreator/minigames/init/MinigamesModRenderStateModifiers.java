package net.mcreator.minigames.init;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.geom.ModelPart;

import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;

import com.google.common.reflect.TypeToken;

@EventBusSubscriber(Dist.CLIENT)
public class MinigamesModRenderStateModifiers {
	public static final ContextKey<LivingEntity> LIVING_ENTITY = new ContextKey<>(Identifier.parse("c:living_entity_attachment"));

	@SubscribeEvent
	public static void registerModifiers(RegisterRenderStateModifiersEvent event) {
		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
		}, (entity, state) -> state.setRenderData(LIVING_ENTITY, (LivingEntity) entity));
	}

	public static void submitModelPart(SubmitNodeCollector collector, ModelPart part, PoseStack poseStack, RenderType renderType, int light, int overlay) {
		// Freeze procedure changes before shared models are animated again by later renders.
		Map<String, List<ModelPart.Cube>> cubes = new HashMap<>();
		part.visit(new PoseStack(), (pose, path, index, cube) -> cubes.computeIfAbsent(path, key -> new ArrayList<>()).add(cube));
		collector.submitModelPart(snapshotModelPart(part, "", cubes), poseStack, renderType, light, overlay, null);
	}

	private static ModelPart snapshotModelPart(ModelPart part, String path, Map<String, List<ModelPart.Cube>> cubes) {
		Map<String, ModelPart> children = new LinkedHashMap<>();
		part.children.forEach((name, child) -> children.put(name, snapshotModelPart(child, path + "/" + name, cubes)));
		ModelPart snapshot = new ModelPart(cubes.getOrDefault(path, List.of()), children);
		snapshot.loadPose(part.storePose());
		snapshot.xScale = part.xScale;
		snapshot.yScale = part.yScale;
		snapshot.zScale = part.zScale;
		snapshot.visible = part.visible;
		snapshot.skipDraw = part.skipDraw;
		return snapshot;
	}

	public static LivingEntityRenderState cloneRenderState(LivingEntityRenderState newState, LivingEntityRenderState originalState) {
		newState.x = originalState.x;
		newState.y = originalState.y;
		newState.z = originalState.z;
		newState.lightCoords = originalState.lightCoords;
		newState.ageInTicks = originalState.ageInTicks;
		newState.distanceToCameraSq = originalState.distanceToCameraSq;
		newState.displayFireAnimation = originalState.displayFireAnimation;
		newState.wornHeadAnimationPos = originalState.wornHeadAnimationPos;
		newState.partialTick = originalState.partialTick;
		newState.passengerOffset = originalState.passengerOffset;
		newState.eyeHeight = originalState.eyeHeight;
		newState.bodyRot = originalState.bodyRot;
		newState.yRot = originalState.yRot;
		newState.xRot = originalState.xRot;
		newState.deathTime = originalState.deathTime;
		newState.walkAnimationPos = originalState.walkAnimationPos;
		newState.walkAnimationSpeed = originalState.walkAnimationSpeed;
		newState.scale = originalState.scale;
		newState.ageScale = originalState.ageScale;
		newState.isBaby = originalState.isBaby;
		newState.hasRedOverlay = originalState.hasRedOverlay;
		newState.isInvisibleToPlayer = originalState.isInvisibleToPlayer;
		return newState;
	}
}