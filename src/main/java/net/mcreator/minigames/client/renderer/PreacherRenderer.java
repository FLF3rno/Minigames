package net.mcreator.minigames.client.renderer;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.client.model.Modelpreacher;
import net.mcreator.minigames.entity.PreacherEntity;

public class PreacherRenderer extends MobRenderer<PreacherEntity, LivingEntityRenderState, Modelpreacher> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/preacher.png");

	public PreacherRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelpreacher(context.bakeLayer(Modelpreacher.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public void extractRenderState(PreacherEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return entityTexture;
	}
}
