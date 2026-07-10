package net.mcreator.minigames.client.renderer;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.minigames.entity.SculklingEntity;
import net.mcreator.minigames.client.model.Modelsculklings;

import com.mojang.blaze3d.vertex.PoseStack;

public class SculklingRenderer extends MobRenderer<SculklingEntity, LivingEntityRenderState, Modelsculklings> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/sculkings.png");

	public SculklingRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelsculklings(context.bakeLayer(Modelsculklings.LAYER_LOCATION)), 0.4f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(SculklingEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		SculklingEntity entity = (SculklingEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "sculkings")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(0.8f, 0.8f, 0.8f);
	}

	public static final ContextKey<SculklingEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:sculkling_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(SculklingRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}