package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;

import net.mcreator.minigames.entity.GoldenSkeletonEntity;

public class GoldenSkeletonRenderer extends SkeletonRenderer {
	private static final Identifier SKELETON_LOCATION = Identifier.fromNamespaceAndPath("minigames", "textures/entities/skeleton.png");

	public GoldenSkeletonRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public SkeletonRenderState createRenderState() {
		return new SkeletonRenderState();
	}

	@Override
	public Identifier getTextureLocation(SkeletonRenderState state) {
		return SKELETON_LOCATION;
	}
}
