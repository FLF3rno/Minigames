package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.minigames.entity.GoldenSpiderEntity;
import net.mcreator.minigames.client.model.Modelspider;

public class GoldenSpiderRenderer extends MobRenderer<GoldenSpiderEntity, LivingEntityRenderState, Modelspider> {
	private GoldenSpiderEntity entity = null;
	private final ResourceLocation entityTexture = ResourceLocation.parse("minigames:textures/entities/spider.png");

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
		this.entity = entity;
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
		if (entity != null && entity.getTexture() != "spider")
			return ResourceLocation.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return entityTexture;
	}
}