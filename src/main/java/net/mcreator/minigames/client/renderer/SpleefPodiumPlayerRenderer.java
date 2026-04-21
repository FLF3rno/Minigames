package net.mcreator.minigames.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.mcreator.minigames.entity.SpleefPodiumPlayerEntity;

import java.util.UUID;

public class SpleefPodiumPlayerRenderer extends HumanoidMobRenderer<SpleefPodiumPlayerEntity, HumanoidRenderState, HumanoidModel<HumanoidRenderState>> {
	private SpleefPodiumPlayerEntity entity = null;
	private final ResourceLocation entityTexture = ResourceLocation.parse("minigames:textures/entities/empty.png");

	public SpleefPodiumPlayerRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<HumanoidRenderState>(context.bakeLayer(ModelLayers.PLAYER)), 0f);
		this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getEquipmentRenderer()));
	}

	@Override
	public HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	@Override
	public void extractRenderState(SpleefPodiumPlayerEntity entity, HumanoidRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		this.entity = entity;
	}

	@Override
	public ResourceLocation getTextureLocation(HumanoidRenderState state) {
		if (entity != null) {
			String displayUuid = entity.getEntityData().get(SpleefPodiumPlayerEntity.DATA_display_uuid);
			if (!displayUuid.isBlank()) {
				try {
					UUID uuid = parseUuid(displayUuid);
					if (Minecraft.getInstance().level != null) {
						if (Minecraft.getInstance().level.getPlayerByUUID(uuid) instanceof AbstractClientPlayer player) {
							return player.getSkin().texture();
						}
					}
				} catch (IllegalArgumentException ignored) {
				}
			}
		}
		if (entity != null && !"empty".equals(entity.getTexture()))
			return ResourceLocation.parse("minigames:textures/entities/" + entity.getTexture() + ".png");
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
