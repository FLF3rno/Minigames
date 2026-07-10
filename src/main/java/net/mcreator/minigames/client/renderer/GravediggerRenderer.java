package net.mcreator.minigames.client.renderer;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.client.model.Modelgravedigger;
import net.mcreator.minigames.entity.GravediggerEntity;

public class GravediggerRenderer extends MobRenderer<GravediggerEntity, LivingEntityRenderState, Modelgravedigger> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/gravedigger.png");

	public GravediggerRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelgravedigger(context.bakeLayer(Modelgravedigger.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(GravediggerEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}
}
