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

import net.mcreator.minigames.entity.FlavioTrapdoorEntity;
import net.mcreator.minigames.client.model.Modelflavio_trapdoor;

public class FlavioTrapdoorRenderer extends MobRenderer<FlavioTrapdoorEntity, LivingEntityRenderState, Modelflavio_trapdoor> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/flavio_trapdoor.png");

	public FlavioTrapdoorRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelflavio_trapdoor(context.bakeLayer(Modelflavio_trapdoor.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(FlavioTrapdoorEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		FlavioTrapdoorEntity entity = (FlavioTrapdoorEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "flavio_trapdoor")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	public static final ContextKey<FlavioTrapdoorEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:flavio_trapdoor_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(FlavioTrapdoorRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}