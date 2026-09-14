package net.mcreator.minigames.client.renderer;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.entity.FlavioOmegaLaserScreenEntity;
import net.mcreator.minigames.client.model.Modelomega_laser_screen;

import com.mojang.blaze3d.vertex.PoseStack;

public class FlavioOmegaLaserScreenRenderer extends MobRenderer<FlavioOmegaLaserScreenEntity, LivingEntityRenderState, Modelomega_laser_screen> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/schermo.png");

	public FlavioOmegaLaserScreenRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelomega_laser_screen(context.bakeLayer(Modelomega_laser_screen.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<>(this) {
			final Identifier LAYER_TEXTURE = Identifier.parse("minigames:textures/entities/schermo_emissive.png");
			final RenderType RENDER_TYPE = RenderTypes.eyes(LAYER_TEXTURE);
			final EntityModel LAYER_MODEL = new Modelomega_laser_screen(Minecraft.getInstance().getEntityModels().bakeLayer(Modelomega_laser_screen.LAYER_LOCATION));

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, LivingEntityRenderState state, float headYaw, float headPitch) {
				LAYER_MODEL.setupAnim(state);
				submitNodeCollector.submitModel(LAYER_MODEL, state, poseStack, RENDER_TYPE, light, LivingEntityRenderer.getOverlayCoords(state, 0), state.outlineColor, null);
			}
		});
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(FlavioOmegaLaserScreenEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		FlavioOmegaLaserScreenEntity entity = (FlavioOmegaLaserScreenEntity) state.getRenderData(ENTITY_KEY);
		if (entity != null && entity.getTexture() != "schermo")
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	public static final ContextKey<FlavioOmegaLaserScreenEntity> ENTITY_KEY = new ContextKey<>(Identifier.parse("minigames:flavio_omega_laser_screen_entity"));

	@EventBusSubscriber(Dist.CLIENT)
	public static class EntityStateAdder {
		@SubscribeEvent
		private static void registerRenderStateModifiersEvent(RegisterRenderStateModifiersEvent event) {
			event.registerEntityModifier(FlavioOmegaLaserScreenRenderer.class, (entity, state) -> state.setRenderData(ENTITY_KEY, entity));
		}
	}
}