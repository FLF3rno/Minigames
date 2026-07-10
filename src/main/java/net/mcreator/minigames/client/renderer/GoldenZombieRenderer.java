package net.mcreator.minigames.client.renderer;

import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.entity.GoldenZombieEntity;

public class GoldenZombieRenderer extends ZombieRenderer {
	private GoldenZombieEntity entity = null;

	public GoldenZombieRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void extractRenderState(net.minecraft.world.entity.monster.zombie.Zombie entity, ZombieRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		this.entity = (entity instanceof GoldenZombieEntity golden) ? golden : null;
	}

	@Override
	public Identifier getTextureLocation(ZombieRenderState state) {
		if (entity != null) {
			return Identifier.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
		}
		return Identifier.parse("minigames:textures/entities/zombie.png");
	}
}
