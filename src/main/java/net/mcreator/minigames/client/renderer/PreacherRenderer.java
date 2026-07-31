package net.mcreator.minigames.client.renderer;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.client.model.Modelpreacher;
import net.mcreator.minigames.entity.PreacherEntity;

public class PreacherRenderer extends MobRenderer<PreacherEntity, PreacherRenderer.PreacherRenderState, Modelpreacher> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/preacher.png");

	public PreacherRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelpreacher(context.bakeLayer(Modelpreacher.LAYER_LOCATION)), 0.5f);
	}

	// 1. Define custom state to hold animations
	public static class PreacherRenderState extends LivingEntityRenderState {
		public final AnimationState animationState0 = new AnimationState();
		public final AnimationState animationState1 = new AnimationState();
	}

	@Override
	public PreacherRenderState createRenderState() {
		return new PreacherRenderState();
	}

	@Override
	public void extractRenderState(PreacherEntity entity, PreacherRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.animationState0.copyFrom(entity.animationState0);
		state.animationState1.copyFrom(entity.animationState1);
	}

	@Override
	public Identifier getTextureLocation(PreacherRenderState state) {
		return entityTexture;
	}
}