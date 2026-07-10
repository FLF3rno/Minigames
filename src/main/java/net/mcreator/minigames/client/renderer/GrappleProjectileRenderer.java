package net.mcreator.minigames.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.entity.GrappleEntity;

public class GrappleProjectileRenderer extends EntityRenderer<GrappleEntity, LivingEntityRenderState> {
	public GrappleProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	public void extractRenderState(GrappleEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return Identifier.parse("minigames:textures/entities/grapple.png");
	}
}
