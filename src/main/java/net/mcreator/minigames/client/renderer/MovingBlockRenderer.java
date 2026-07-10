package net.mcreator.minigames.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.entity.MovingBlockEntity;

public class MovingBlockRenderer extends EntityRenderer<MovingBlockEntity, LivingEntityRenderState> {
	public MovingBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	public void extractRenderState(MovingBlockEntity entity, LivingEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return Identifier.parse("minigames:textures/entities/crown.png");
	}
}
