package net.mcreator.minigames.procedures;

import org.joml.Vector3f;

import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.EntityModel;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModRenderStateModifiers;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.init.MinigamesModHumanoidModels;

import javax.annotation.Nullable;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Collection;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(Dist.CLIENT)
public class RenderCrownOnPlayerProcedure {
	@SubscribeEvent
	public static void onPlayerRendered(RenderPlayerEvent.Pre event) {
		Entity entity = (Entity) event.getRenderState().getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
		execute(event, entity, event);
	}

	public static Collection<Runnable> capes = new ConcurrentLinkedQueue<>();

	public static void offsetScale(PlayerModel model, Vector3f offset) {
		model.head.offsetScale(offset);
		model.head.y += offset.x() > 0 ? 0.05 : -0.05;
		model.body.offsetScale(offset);
		model.leftArm.offsetScale(offset);
		model.rightArm.offsetScale(offset);
		model.leftLeg.offsetScale(offset);
		model.rightLeg.offsetScale(offset);
		model.hat.offsetScale(offset);
		model.hat.y += offset.x() > 0 ? 0.05 : -0.05;
		model.jacket.offsetScale(offset);
		model.leftSleeve.offsetScale(offset);
		model.rightSleeve.offsetScale(offset);
		model.leftPants.offsetScale(offset);
		model.rightPants.offsetScale(offset);
	}

	public static void renderHumanoid(RenderPlayerEvent playerRenderEvent, PlayerModel model, VertexConsumer vertexConsumer, PlayerRenderState state) {
		LivingEntity eventEntity_ = (LivingEntity) playerRenderEvent.getRenderState().getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
		PoseStack poseStack = playerRenderEvent.getPoseStack();
		poseStack.pushPose();
		CompoundTag playerData = state.getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY).getPersistentData();
		float oldAnimationProgress = 0;
		float oldAgeInTicks = 0;
		if (playerData.contains("PlayerAnimationProgress")) {
			oldAnimationProgress = playerData.getFloatOr("PlayerAnimationProgress", 0);
			oldAgeInTicks = playerData.getFloatOr("LastTickTime", 0);
		}
		model.setupAnim(state);
		if (playerData.contains("PlayerAnimationProgress") && playerData.getFloatOr("PlayerAnimationProgress", 0) > 0) {
			playerData.putFloat("PlayerAnimationProgress", oldAnimationProgress);
			playerData.putFloat("LastTickTime", oldAgeInTicks);
		} else if (oldAnimationProgress > 0) {
			model.setupAnim(state);
		}
		if (eventEntity_.hasPose(Pose.SLEEPING)) {
			Direction direction = eventEntity_.getBedOrientation();
			if (direction != null) {
				float eyeHeightOffset = eventEntity_.getEyeHeight(Pose.STANDING) - 0.1F;
				poseStack.translate((float) (-direction.getStepX()) * eyeHeightOffset, 0.0F, (float) (-direction.getStepZ()) * eyeHeightOffset);
			}
		}
		playerRenderEvent.getRenderer().setupRotations(state, poseStack, state.bodyRot, 0);
		poseStack.scale(-0.938f, -0.938f, 0.938f);
		poseStack.translate(0.0D, -1.501, 0.0D);
		Vector3f offset = new Vector3f(0.015f);
		offsetScale(model, offset);
		if (!capes.isEmpty()) {
			capes.forEach(cape -> cape.run());
			capes.clear();
		}
		model.renderToBuffer(poseStack, vertexConsumer, playerRenderEvent.getPackedLight(), LivingEntityRenderer.getOverlayCoords(state, 0));
		offset.negate();
		offsetScale(model, offset);
		poseStack.popPose();
	}

	public static void renderEntity(RenderPlayerEvent playerRenderEvent, EntityModel model, VertexConsumer vertexConsumer, LivingEntityRenderState state) {
		LivingEntity eventEntity_ = (LivingEntity) playerRenderEvent.getRenderState().getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
		PoseStack poseStack = playerRenderEvent.getPoseStack();
		poseStack.pushPose();
		if (eventEntity_.hasPose(Pose.SLEEPING)) {
			Direction direction = eventEntity_.getBedOrientation();
			if (direction != null) {
				float eyeHeightOffset = eventEntity_.getEyeHeight(Pose.STANDING) - 0.1F;
				poseStack.translate((float) (-direction.getStepX()) * eyeHeightOffset, 0.0F, (float) (-direction.getStepZ()) * eyeHeightOffset);
			}
		}
		playerRenderEvent.getRenderer().setupRotations((PlayerRenderState) state, poseStack, state.bodyRot, 69);
		poseStack.scale(-0.938f, -0.938f, 0.938f);
		poseStack.translate(0.0D, -1.501, 0.0D);
		model.setupAnim(state);
		model.renderToBuffer(poseStack, vertexConsumer, playerRenderEvent.getPackedLight(), LivingEntityRenderer.getOverlayCoords(state, 0));
		poseStack.popPose();
	}

	public static void execute(Entity entity, RenderPlayerEvent playerRenderEvent) {
		execute(null, entity, playerRenderEvent);
	}

	private static void execute(@Nullable Event event, Entity entity, RenderPlayerEvent playerRenderEvent) {
		if (entity == null || playerRenderEvent == null)
			return;
		boolean hasCrownHelmet = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get();
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).isCrowned && !hasCrownHelmet) {
			{
				ResourceLocation texture = MinigamesModVariables.crown;
				renderHumanoid(playerRenderEvent, MinigamesModHumanoidModels.CROWN, playerRenderEvent.getMultiBufferSource().getBuffer(RenderType.armorCutoutNoCull(texture)), playerRenderEvent.getRenderState());
			}
		}
	}
}
