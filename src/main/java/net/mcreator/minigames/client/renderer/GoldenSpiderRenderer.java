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

import net.mcreator.minigames.entity.GoldenSpiderEntity;
import net.mcreator.minigames.client.model.Modelspider;

public class GoldenSpiderRenderer extends MobRenderer<GoldenSpiderEntity, LivingEntityRenderState, Modelspider> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/spider.png");

	public GoldenSpiderRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelspider(context.bakeLayer(Modelspider.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(GoldenSpiderEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		GoldenSpiderEntity entity = (GoldenSpiderEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "spider")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	public static final ContextKey<GoldenSpiderEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:golden_spider_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(GoldenSpiderRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}