package net.mcreator.minigames.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

import net.mcreator.minigames.entity.GoldenZombieEntity;

public class GoldenZombieRenderer extends ZombieRenderer {
	private GoldenZombieEntity entity = null;

	public GoldenZombieRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void extractRenderState(net.minecraft.world.entity.monster.Zombie entity, ZombieRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		this.entity = (entity instanceof GoldenZombieEntity _golden) ? _golden : null;
	}

	@Override
	public ResourceLocation getTextureLocation(ZombieRenderState state) {
		if (entity != null)
			return ResourceLocation.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		return ResourceLocation.parse("minigames:textures/entities/zombie.png");
	}
}
