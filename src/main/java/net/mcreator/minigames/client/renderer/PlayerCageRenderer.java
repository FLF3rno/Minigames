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

import net.mcreator.minigames.entity.PlayerCageEntity;
import net.mcreator.minigames.client.model.Modelplayer_cage;

import com.mojang.blaze3d.vertex.PoseStack;

public class PlayerCageRenderer extends MobRenderer<PlayerCageEntity, LivingEntityRenderState, Modelplayer_cage> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/cage.png");

	public PlayerCageRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelplayer_cage(context.bakeLayer(Modelplayer_cage.LAYER_LOCATION)), 0f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(PlayerCageEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		PlayerCageEntity entity = (PlayerCageEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "cage")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(0.94f, 0.94f, 0.94f);
	}

	public static final ContextKey<PlayerCageEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:player_cage_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(PlayerCageRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}