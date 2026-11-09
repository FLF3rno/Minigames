package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.init.MinigamesModRenderStateModifiers;
import net.mcreator.minigames.procedures.GetItemAttributeProcedure;
import net.mcreator.minigames.procedures.ReturnDamageRoguelikeProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(
		modid = MinigamesMod.MODID,
		value = Dist.CLIENT
)
public class LethalEntityTint {
	private static final int DEFAULT_TINT_COLOR = 0xD0FF0000;

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.AddLayers event) {
		for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
			EntityRenderer<?, ?> renderer = event.getRenderer(type);

			if (renderer instanceof LivingEntityRenderer livingRenderer) {
				livingRenderer.addLayer(new StunTintLayer(livingRenderer));
			}
		}
	}

	public static class StunTintLayer
			extends RenderLayer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {

		private final LivingEntityRenderer<?, LivingEntityRenderState, ?> parentRenderer;

		@SuppressWarnings({"rawtypes", "unchecked"})
		public StunTintLayer(LivingEntityRenderer<?, ?, ?> parent) {
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
			var player = Minecraft.getInstance().player;

			if (player == null ||
					player.getAttributeValue(MinigamesModAttributes.SHOW_ONE_SHOTTABLE_MOBS) <= 0) {
				return;
			}

			LivingEntity entity = state.getRenderData(MinigamesModRenderStateModifiers.LIVING_ENTITY);
			if (entity == null) return;
			CustomData customData =
					player.getMainHandItem().getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
			if (player.distanceToSqr(entity) > 550 || entity instanceof Player ||
				entity.isInvisible() || entity.getHealth() > ReturnDamageRoguelikeProcedure.execute(Minecraft.getInstance().level, player.damageSources().playerAttack(player),
					entity, player, GetItemAttributeProcedure.getBaseAttribute(player.getMainHandItem(), "minecraft:attack_damage") + 1)
			) {
				return;
			}

			RenderLayer.renderColoredCutoutModel(
					getParentModel(),
					parentRenderer.getTextureLocation(state),
					poseStack,
					nodeCollector,
					packedLight,
					state,
					getTintColorFromItem(player.getMainHandItem()),
					state.outlineColor
			);
		}

		private static int getTintColorFromItem(ItemStack stack) {
			if (stack == null)
				return DEFAULT_TINT_COLOR;

			CustomData data = stack.get(DataComponents.CUSTOM_DATA);

			if (data == null)
				return DEFAULT_TINT_COLOR;

			CompoundTag tag = data.copyTag();

			String color = tag.getStringOr("tintColor", "");

			if (color.isEmpty())
				return DEFAULT_TINT_COLOR;

			try {
				return Integer.decode(color);
			} catch (Exception ignored) {
				return DEFAULT_TINT_COLOR;
			}
		}
	}
}