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

import net.mcreator.minigames.entity.FlavioAntennaEntity;
import net.mcreator.minigames.client.model.Modelflavio_antenna;

public class FlavioAntennaRenderer extends MobRenderer<FlavioAntennaEntity, LivingEntityRenderState, Modelflavio_antenna> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/flavio_antenna.png");

	public FlavioAntennaRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelflavio_antenna(context.bakeLayer(Modelflavio_antenna.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(FlavioAntennaEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		FlavioAntennaEntity entity = (FlavioAntennaEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "flavio_antenna")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	public static final ContextKey<FlavioAntennaEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:flavio_antenna_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(FlavioAntennaRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}