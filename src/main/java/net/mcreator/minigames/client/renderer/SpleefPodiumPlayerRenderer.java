package net.mcreator.minigames.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.mcreator.minigames.entity.SpleefPodiumPlayerEntity;

import java.util.UUID;

public class SpleefPodiumPlayerRenderer extends HumanoidMobRenderer<SpleefPodiumPlayerEntity, SpleefPodiumPlayerRenderer.SpleefPodiumPlayerRenderState, HumanoidModel<SpleefPodiumPlayerRenderer.SpleefPodiumPlayerRenderState>> {
	private final Identifier entityTexture = Identifier.parse("minigames:textures/entities/empty.png");

	public SpleefPodiumPlayerRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0f);
	}

	public static class SpleefPodiumPlayerRenderState extends HumanoidRenderState {
		public String displayUuid = "";
		public String texture = "";
	}

	@Override
	public SpleefPodiumPlayerRenderState createRenderState() {
		return new SpleefPodiumPlayerRenderState();
	}

	@Override
	public void extractRenderState(SpleefPodiumPlayerEntity entity, SpleefPodiumPlayerRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.displayUuid = entity.getEntityData().get(SpleefPodiumPlayerEntity.DATA_display_uuid);
		state.texture = entity.getTexture();
	}

	@Override
	public Identifier getTextureLocation(SpleefPodiumPlayerRenderState state) {
		if (state.displayUuid != null && !state.displayUuid.isBlank()) {
			try {
				UUID uuid = parseUuid(state.displayUuid);
				if (Minecraft.getInstance().level != null) {
					if (Minecraft.getInstance().level.getPlayerByUUID(uuid) instanceof AbstractClientPlayer player) {
						return player.getSkin().body().texturePath();
					}
				}
			} catch (IllegalArgumentException ignored) {
			}
		}
		if (state.texture != null && !"empty".equals(state.texture))
			return Identifier.parse("minigames:textures/entities/" + state.texture + ".png");
		return entityTexture;
	}

	private static UUID parseUuid(String value) {
		String trimmed = value.trim();
		if (trimmed.startsWith("[I;") && trimmed.endsWith("]")) {
			String body = trimmed.substring(3, trimmed.length() - 1).trim();
			String[] parts = body.split(",");
			if (parts.length == 4) {
				int a = Integer.parseInt(parts[0].trim());
				int b = Integer.parseInt(parts[1].trim());
				int c = Integer.parseInt(parts[2].trim());
				int d = Integer.parseInt(parts[3].trim());
				long most = ((long) a << 32) | (b & 0xffffffffL);
				long least = ((long) c << 32) | (d & 0xffffffffL);
				return new UUID(most, least);
			}
		}
		return UUID.fromString(trimmed);
	}
}