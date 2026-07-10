package net.mcreator.minigames.client.renderer;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.mcreator.minigames.entity.PlagueMiddleEntity;

import com.mojang.blaze3d.vertex.PoseStack;

public class PlagueMiddleRenderer extends HumanoidMobRenderer<PlagueMiddleEntity, HumanoidRenderState, HumanoidModel<HumanoidRenderState>> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/empty.png");

	public PlagueMiddleRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<HumanoidRenderState>(context.bakeLayer(ModelLayers.PLAYER)), 0f);
		this.addLayer(new HumanoidArmorLayer(this, ArmorModelSet.bake(ModelLayers.PLAYER_ARMOR, context.getModelSet(), HumanoidModel::new), context.getEquipmentRenderer()));
	}

	@Override
	public HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	@Override
	public void extractRenderState(PlagueMiddleEntity entity, HumanoidRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(HumanoidRenderState state) {
		PlagueMiddleEntity entity = (PlagueMiddleEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "empty")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	@Override
	protected void scale(HumanoidRenderState state, PoseStack poseStack) {
		poseStack.scale(0.01f, 0.01f, 0.01f);
	}

	public static final ContextKey<PlagueMiddleEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:plague_middle_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(PlagueMiddleRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}