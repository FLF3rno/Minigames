package net.mcreator.minigames.client.renderer;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.entity.VolleybombEntityEntity;
import net.mcreator.minigames.client.model.Modelvolleybomb;
import net.mcreator.minigames.client.model.Modelpewseat;

import com.mojang.blaze3d.vertex.PoseStack;

public class VolleybombEntityRenderer extends MobRenderer<VolleybombEntityEntity, LivingEntityRenderState, Modelpewseat> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/empty.png");

	public VolleybombEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelpewseat(context.bakeLayer(Modelpewseat.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<>(this) {
			final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/volleybomb.png");
			final RenderType RENDER_TYPE = RenderTypes.entityCutout(LAYER_TEXTURE);
			final EntityModel LAYER_MODEL = new Modelvolleybomb(Minecraft.getInstance().getEntityModels().bakeLayer(Modelvolleybomb.LAYER_LOCATION));

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				LAYER_MODEL.setupAnim(state);
				submitNodeCollector.submitModel(LAYER_MODEL, state, poseStack, RENDER_TYPE, light, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
			}
		});
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(VolleybombEntityEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		VolleybombEntityEntity entity = (VolleybombEntityEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "empty")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(0.9f, 0.9f, 0.9f);
	}

	public static final ContextKey<VolleybombEntityEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:volleybomb_entity_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(VolleybombEntityRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}