package net.mcreator.minigames.procedures;

import net.mcreator.minigames.client.LivingEntityTransparencyDataAccessor;
import net.mcreator.minigames.init.MinigamesModRenderStateModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.ARGB;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(value = Dist.CLIENT)
public class PlayerTransparencyRenderProcedure {
	@SubscribeEvent
	public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
		LivingEntity entity = (LivingEntity) event.getRenderState().getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
		if (!(entity instanceof LivingEntityTransparencyDataAccessor transparencyData)) {
			return;
		}

		float transparency = Math.max(0.0f, Math.min(100.0f, transparencyData.minigames$getTransparency()));
		if (transparency <= 0.0f) {
			return;
		}

		event.setCanceled(true);
		renderTransparentPlayer(event, transparency);
	}

	private static void renderTransparentPlayer(RenderPlayerEvent event, float transparency) {
		PlayerRenderState state = event.getRenderState();
		PlayerModel model = event.getRenderer().getModel();
		PoseStack poseStack = event.getPoseStack();
		float opacityMultiplier = 1.0f - (transparency / 100.0f);
		int alpha = Math.round(255.0f * opacityMultiplier);

		poseStack.pushPose();
		event.getRenderer().setupRotations(state, poseStack, state.bodyRot, 0.0f);
		poseStack.scale(-0.9375f, -0.9375f, 0.9375f);
		poseStack.translate(0.0D, -1.5010000467300415D, 0.0D);

		model.setupAnim(state);
		VertexConsumer buffer = event.getMultiBufferSource().getBuffer(RenderType.entityTranslucent(event.getRenderer().getTextureLocation(state)));
		model.renderToBuffer(poseStack, buffer, event.getPackedLight(), LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState) state, 0.0F), ARGB.color(alpha, 255, 255, 255));
		poseStack.popPose();
	}
}
