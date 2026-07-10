package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.minigames.entity.SculklingEntity;
import net.mcreator.minigames.client.model.Modelsculklings;

import com.mojang.blaze3d.vertex.PoseStack;

public class SculklingRenderer extends MobRenderer<SculklingEntity, LivingEntityRenderState, Modelsculklings> {
	private SculklingEntity entity = null;
	private final ResourceLocation entityTexture = ResourceLocation.parse("minigames:textures/entities/sculkings.png");

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
		this.entity = entity;
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
		if (entity != null && entity.getTexture() != "sculkings")
			return ResourceLocation.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
		poseStack.scale(0.8f, 0.8f, 0.8f);
	}
}