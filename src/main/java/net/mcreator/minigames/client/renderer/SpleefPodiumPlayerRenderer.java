package net.mcreator.minigames.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.entity.SpleefPodiumPlayerEntity;

public class SpleefPodiumPlayerRenderer extends EntityRenderer<SpleefPodiumPlayerEntity, HumanoidRenderState> {
	public SpleefPodiumPlayerRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	public HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	public void extractRenderState(SpleefPodiumPlayerEntity entity, HumanoidRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
	}

	public Identifier getTextureLocation(HumanoidRenderState state) {
		return Identifier.parse("minigames:textures/entities/empty.png");
	}
}
