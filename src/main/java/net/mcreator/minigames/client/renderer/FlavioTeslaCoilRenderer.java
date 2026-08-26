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

import net.mcreator.minigames.entity.FlavioTeslaCoilEntity;
import net.mcreator.minigames.client.model.Modeltesla_coil_flavio;

public class FlavioTeslaCoilRenderer extends MobRenderer<FlavioTeslaCoilEntity, LivingEntityRenderState, Modeltesla_coil_flavio> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/tesla_coil_flavio.png");

	public FlavioTeslaCoilRenderer(EntityRendererProvider.Context context) {
		super(context, new Modeltesla_coil_flavio(context.bakeLayer(Modeltesla_coil_flavio.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(FlavioTeslaCoilEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		FlavioTeslaCoilEntity entity = (FlavioTeslaCoilEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "tesla_coil_flavio")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	public static final ContextKey<FlavioTeslaCoilEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:flavio_tesla_coil_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(FlavioTeslaCoilRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}