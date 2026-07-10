package net.mcreator.minigames.client.renderer;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.client.model.Modeldemon;
import net.mcreator.minigames.entity.DemonEntity;

public class DemonRenderer extends MobRenderer<DemonEntity, LivingEntityRenderState, Modeldemon> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/demon.png");

	public DemonRenderer(EntityRendererProvider.Context context) {
		super(context, new Modeldemon(context.bakeLayer(Modeldemon.LAYER_LOCATION)), 1f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(DemonEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}
}
