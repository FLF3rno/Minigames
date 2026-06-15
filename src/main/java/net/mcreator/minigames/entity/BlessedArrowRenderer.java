package net.mcreator.minigames.entity;

import net.mcreator.minigames.entity.BlessedArrowEntity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.ResourceLocation;

public class BlessedArrowRenderer extends ArrowRenderer<BlessedArrowEntity, ArrowRenderState> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "minigames",
                    "textures/entity/projectiles/blessed_arrow.png"
            );

    public BlessedArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected ResourceLocation getTextureLocation(ArrowRenderState state) {
        return TEXTURE;
    }
}