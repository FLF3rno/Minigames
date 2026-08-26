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

import net.mcreator.minigames.entity.SpikeTrapEntity;
import net.mcreator.minigames.client.model.Modelspike_trap;

import com.mojang.blaze3d.vertex.PoseStack;

public class SpikeTrapRenderer extends MobRenderer<SpikeTrapEntity, LivingEntityRenderState, Modelspike_trap> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/spike_trap.png");

	public SpikeTrapRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelspike_trap(context.bakeLayer(Modelspike_trap.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(SpikeTrapEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		SpikeTrapEntity entity = (SpikeTrapEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "spike_trap")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(0.25f, 0.25f, 0.25f);
	}

	public static final ContextKey<SpikeTrapEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:spike_trap_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(SpikeTrapRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}