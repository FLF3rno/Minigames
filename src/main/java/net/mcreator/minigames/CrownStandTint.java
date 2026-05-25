package net.mcreator.minigames;

import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.init.MinigamesModRenderStateModifiers;

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
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class CrownStandTint {
	public CrownStandTint(IEventBus modBus) {
		modBus.addListener(this::registerLayers);
	}

	public void registerLayers(EntityRenderersEvent.AddLayers event) {
		for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
			EntityRenderer<?, ?> renderer = event.getRenderer((EntityType) type);
			if (renderer instanceof LivingEntityRenderer livingRenderer) {
				livingRenderer.addLayer((RenderLayer) new CrownTintLayer(livingRenderer));
			}
		}
	}

	public static class CrownTintLayer extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {
		private final LivingEntityRenderer<?, LivingEntityRenderState, ?> parentRenderer;

		@SuppressWarnings({"rawtypes", "unchecked"})
		public CrownTintLayer(LivingEntityRenderer<?, ?, ?> parent) {
			super((RenderLayerParent) parent);
			this.parentRenderer = (LivingEntityRenderer<?, LivingEntityRenderState, ?>) parent;
		}

		@Override
		public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntityRenderState state, float limbSwing, float limbSwingAmount) {
			LivingEntity entity = state.getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
			if (!(entity instanceof ArmorStand stand) || stand.isInvisible()) {
				return;
			}
			if (stand.getItemBySlot(EquipmentSlot.HEAD).getItem() != MinigamesModItems.CROWN_HELMET_HELMET.get()) {
				return;
			}
			int color = parseColorFromName(stand);
			if (color == 0) {
				return;
			}
			ResourceLocation texture = this.parentRenderer.getTextureLocation(state);
			VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
			this.getParentModel().renderToBuffer(poseStack, buffer, packedLight, LivingEntityRenderer.getOverlayCoords(state, 0.0F), color);
		}

		private static int parseColorFromName(ArmorStand stand) {
			if (!stand.hasCustomName()) return 0;
			String name = stand.getCustomName().getString();
			if (name == null || !name.startsWith("[crown:") || !name.endsWith("]")) return 0;
			String hex = name.substring(7, name.length() - 1).trim();
			if (hex.startsWith("#")) hex = hex.substring(1);
			if (!hex.matches("^[0-9a-fA-F]{6}$")) return 0;
			return 0xA0000000 | Integer.parseInt(hex, 16);
		}
	}
}
