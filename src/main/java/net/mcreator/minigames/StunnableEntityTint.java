package net.mcreator.minigames;

import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.init.MinigamesModRenderStateModifiers;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.EntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class StunnableEntityTint {
	private static final int DEFAULT_TINT_COLOR = 0xD0F3FF2A;

    public StunnableEntityTint(IEventBus modBus) {
        modBus.addListener(this::registerLayers);
    }

    public void registerLayers(EntityRenderersEvent.AddLayers event) {
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            EntityRenderer<?, ?> renderer = event.getRenderer((EntityType) type);
            if (renderer instanceof LivingEntityRenderer livingRenderer) {
                livingRenderer.addLayer((RenderLayer) new StunTintLayer(livingRenderer));
            }
        }
    }

    public static class StunTintLayer extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {
        private final LivingEntityRenderer<?, LivingEntityRenderState, ?> parentRenderer;

        @SuppressWarnings({"rawtypes", "unchecked"})
        public StunTintLayer(LivingEntityRenderer<?, ?, ?> parent) {
            super((RenderLayerParent) parent);
            this.parentRenderer = (LivingEntityRenderer<?, LivingEntityRenderState, ?>) parent;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntityRenderState state, float limbSwing, float limbSwingAmount) {
            var localPlayer = net.minecraft.client.Minecraft.getInstance().player;
            if (localPlayer == null || localPlayer.getAttributeValue(MinigamesModAttributes.SHOW_FULL_HEALTH_MOBS) <= 0) {
                return;
            }

            LivingEntity entity = state.getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
            if (entity == null || entity instanceof Player || entity.isInvisible() || entity.getHealth() < entity.getMaxHealth()) {
                return;
            }

            Identifier texture = this.parentRenderer.getTextureLocation(state);
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
            this.getParentModel().renderToBuffer(
                poseStack,
                buffer,
                packedLight,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F),
                getTintColorFromItem(localPlayer.getMainHandItem())
            );
        }

		private static int getTintColorFromItem(ItemStack stack) {
			if (stack == null) return DEFAULT_TINT_COLOR;

			CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
			if (customData == null) return DEFAULT_TINT_COLOR;
			CompoundTag tag = customData.copyTag();
			if (!tag.contains("tintColor")) return DEFAULT_TINT_COLOR;

			String raw = tag.getStringOr("tintColor", "").trim();
			if (raw.isEmpty()) return DEFAULT_TINT_COLOR;

			try {
				return Integer.decode(raw);
			} catch (Exception ignored) {
				return DEFAULT_TINT_COLOR;
			}
		}
    }
}

