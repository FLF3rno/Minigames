package net.mcreator.minigames;

import net.mcreator.minigames.init.MinigamesModRenderStateModifiers;
import net.mcreator.minigames.network.MinigamesModVariables;

import net.minecraft.client.model.EntityModel;
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
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class GlowingEffectTint {
	private static final int DEFAULT_GLOW_TINT = 0xA0FFFFFF;

	public GlowingEffectTint(IEventBus modBus) {
		modBus.addListener(this::registerLayers);
	}

	public void registerLayers(EntityRenderersEvent.AddLayers event) {
		for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
			EntityRenderer<?, ?> renderer = event.getRenderer((EntityType) type);
			if (renderer instanceof LivingEntityRenderer livingRenderer) {
				livingRenderer.addLayer((RenderLayer) new GlowTintLayer(livingRenderer));
			}
		}
	}

	public static class GlowTintLayer extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {
		private final LivingEntityRenderer<?, LivingEntityRenderState, ?> parentRenderer;

		@SuppressWarnings({"rawtypes", "unchecked"})
		public GlowTintLayer(LivingEntityRenderer<?, ?, ?> parent) {
			super((RenderLayerParent) parent);
			this.parentRenderer = (LivingEntityRenderer<?, LivingEntityRenderState, ?>) parent;
		}

		@Override
		public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntityRenderState state, float limbSwing, float limbSwingAmount) {
			LivingEntity entity = state.getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
			if (entity == null || entity.isInvisible() || !entity.hasEffect(net.minecraft.world.effect.MobEffects.GLOWING)) {
				return;
			}
			int tint = resolveTint(entity);
			ResourceLocation texture = this.parentRenderer.getTextureLocation(state);
			VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
			this.getParentModel().renderToBuffer(poseStack, buffer, packedLight, LivingEntityRenderer.getOverlayCoords(state, 0.0F), tint);
		}

		private static int resolveTint(LivingEntity entity) {
			if (entity instanceof Player player) {
				String color = player.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
				int rgb = parseHex(color, -1);
				if (rgb != -1) {
					return 0xA0000000 | rgb;
				}
			}
			if (entity.hasCustomName()) {
				String name = entity.getCustomName().getString();
				int idx = name.indexOf("[glow:#");
				if (idx >= 0) {
					int end = name.indexOf(']', idx);
					if (end > idx + 7) {
						int rgb = parseHex(name.substring(idx + 7, end), -1);
						if (rgb != -1) {
							return 0xA0000000 | rgb;
						}
					}
				}
			}
			return DEFAULT_GLOW_TINT;
		}

		private static int parseHex(String value, int fallback) {
			if (value == null) return fallback;
			String h = value.startsWith("#") ? value.substring(1) : value;
			if (!h.matches("^[0-9a-fA-F]{6}$")) return fallback;
			return Integer.parseInt(h, 16);
		}
	}
}
