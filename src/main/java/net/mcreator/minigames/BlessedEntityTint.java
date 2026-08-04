package net.mcreator.minigames;

import net.mcreator.minigames.init.MinigamesModRenderStateModifiers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(
		modid = MinigamesMod.MODID,
		value = Dist.CLIENT
)
public class BlessedEntityTint {

	private static final int BLESSED_TINT_COLOR = 0xE070FFFF;

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.AddLayers event) {

		for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {

			EntityRenderer<?, ?> renderer = event.getRenderer(type);

			if (renderer instanceof LivingEntityRenderer livingRenderer) {
				livingRenderer.addLayer(
						new BlessedTintLayer(livingRenderer)
				);
			}
		}
	}


	public static class BlessedTintLayer
			extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {


		private final LivingEntityRenderer<?, LivingEntityRenderState, ?> parentRenderer;


		@SuppressWarnings({"rawtypes", "unchecked"})
		public BlessedTintLayer(LivingEntityRenderer<?, ?, ?> parent) {
			super((RenderLayerParent) parent);

			this.parentRenderer =
					(LivingEntityRenderer<?, LivingEntityRenderState, ?>) parent;
		}


		@Override
		public void submit(
				PoseStack poseStack,
				SubmitNodeCollector nodeCollector,
				int packedLight,
				LivingEntityRenderState state,
				float yRot,
				float xRot
		) {

			LivingEntity entity =
					state.getRenderData(
							MinigamesModRenderStateModifiers.LIVING_ENTITY
					);


			if (entity == null ||
					entity.isInvisible() ||
					!hasBlessedMarker(entity)) {
				return;
			}


			RenderLayer.renderColoredCutoutModel(
					this.getParentModel(),
					this.parentRenderer.getTextureLocation(state),
					poseStack,
					nodeCollector,
					state.lightCoords,
					state,
					BLESSED_TINT_COLOR,
					state.outlineColor
			);
		}


		private static boolean hasBlessedMarker(LivingEntity entity) {

			if (!entity.hasCustomName())
				return false;

			String name =
					entity.getCustomName().getString();

			return name != null &&
					name.contains("[blessed]");
		}
	}
}