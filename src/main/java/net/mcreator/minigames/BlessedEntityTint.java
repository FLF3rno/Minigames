package net.mcreator.minigames;

import net.mcreator.minigames.init.MinigamesModRenderStateModifiers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class BlessedEntityTint {
	private static final int BLESSED_TINT_COLOR = 0xE070FFFF; // ARGB, brighter holy-cyan

	public BlessedEntityTint(IEventBus modBus) {
		modBus.addListener(this::registerLayers);
	}

	public void registerLayers(EntityRenderersEvent.AddLayers event) {
		for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
			EntityRenderer<?, ?> renderer = event.getRenderer((EntityType) type);
			if (renderer instanceof LivingEntityRenderer livingRenderer) {
				livingRenderer.addLayer((RenderLayer) new BlessedTintLayer(livingRenderer));
			}
		}
	}

	public static class BlessedTintLayer extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {
		private final LivingEntityRenderer<?, LivingEntityRenderState, ?> parentRenderer;

		@SuppressWarnings({"rawtypes", "unchecked"})
		public BlessedTintLayer(LivingEntityRenderer<?, ?, ?> parent) {
			super((RenderLayerParent) parent);
			this.parentRenderer = (LivingEntityRenderer<?, LivingEntityRenderState, ?>) parent;
		}

		@Override
		public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntityRenderState state, float limbSwing, float limbSwingAmount) {
			LivingEntity entity = state.getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
			if (entity == null || entity.isInvisible() || !hasBlessedMarker(entity)) {
				return;
			}

			ResourceLocation texture = this.parentRenderer.getTextureLocation(state);
			VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
			this.getParentModel().renderToBuffer(
				poseStack,
				buffer,
				LightTexture.FULL_BRIGHT,
				LivingEntityRenderer.getOverlayCoords(state, 0.0F),
				BLESSED_TINT_COLOR
			);
		}

		private static boolean hasBlessedMarker(LivingEntity entity) {
			if (!entity.hasCustomName()) return false;
			String name = entity.getCustomName().getString();
			return name != null && name.contains("[blessed]");
		}

	}
}
