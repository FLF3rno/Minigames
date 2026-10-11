package net.mcreator.minigames.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.Sheets;

public class ScalingSubmitNodeCollector implements SubmitNodeCollector {

    private final SubmitNodeCollector delegate;
    private final float scale;
    private final float opacity;

    public ScalingSubmitNodeCollector(SubmitNodeCollector delegate, float scale) {
        this(delegate, scale, 1.0f);
    }

    public ScalingSubmitNodeCollector(SubmitNodeCollector delegate, float scale, float opacity) {
        this.delegate = delegate;
        this.scale = scale;
        this.opacity = Math.max(0.0f, Math.min(1.0f, opacity));
    }

    @Override
    public OrderedSubmitNodeCollector order(int order) {
        return new ScalingOrderedCollector(delegate.order(order), scale, opacity);
    }

    @Override
    public void submitItem(
        PoseStack poseStack,
        ItemDisplayContext displayContext,
        int lightCoords,
        int overlayCoords,
        int outlineColor,
        int[] tintLayers,
        List<BakedQuad> quads,
        ItemStackRenderState.FoilType foilType
    ) {
        doSubmitItem(delegate, scale, opacity, poseStack, displayContext, lightCoords, overlayCoords, outlineColor, tintLayers, quads, foilType);
    }

    @Override
    public void submitShadow(PoseStack poseStack, float radius, List<EntityRenderState.ShadowPiece> pieces) {
        delegate.submitShadow(poseStack, radius, pieces);
    }

    @Override
    public void submitNameTag(PoseStack poseStack, @Nullable Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, double distanceToCameraSq, CameraRenderState camera) {
        delegate.submitNameTag(poseStack, nameTagAttachment, offset, name, seeThrough, lightCoords, distanceToCameraSq, camera);
    }

    @Override
    public void submitText(PoseStack poseStack, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor) {
        delegate.submitText(poseStack, x, y, string, dropShadow, displayMode, lightCoords, color, backgroundColor, outlineColor);
    }

    @Override
    public void submitFlame(PoseStack poseStack, EntityRenderState renderState, Quaternionf rotation) {
        delegate.submitFlame(poseStack, renderState, rotation);
    }

    @Override
    public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {
        delegate.submitLeash(poseStack, leashState);
    }

    @Override
    public <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, @Nullable TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        delegate.submitModel(model, state, poseStack, renderType, lightCoords, overlayCoords, tintedColor, sprite, outlineColor, crumblingOverlay);
    }

    @Override
    public void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, @Nullable TextureAtlasSprite sprite, boolean sheeted, boolean hasFoil, int tintedColor, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay, int outlineColor) {
        delegate.submitModelPart(modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, sheeted, hasFoil, tintedColor, crumblingOverlay, outlineColor);
    }

    @Override
    public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
        delegate.submitMovingBlock(poseStack, movingBlockRenderState);
    }

    @Override
    public void submitBlockModel(PoseStack poseStack, RenderType renderType, List<BlockStateModelPart> parts, int[] tintLayers, int lightCoords, int overlayCoords, int outlineColor) {
        delegate.submitBlockModel(poseStack, renderType, parts, tintLayers, lightCoords, overlayCoords, outlineColor);
    }

    @Override
    public void submitBreakingBlockModel(PoseStack poseStack, BlockStateModel model, long seed, int progress) {
        delegate.submitBreakingBlockModel(poseStack, model, seed, progress);
    }

    @Override
    public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, CustomGeometryRenderer customGeometryRenderer) {
        delegate.submitCustomGeometry(poseStack, renderType, customGeometryRenderer);
    }

    @Override
    public void submitParticleGroup(ParticleGroupRenderer particleGroupRenderer) {
        delegate.submitParticleGroup(particleGroupRenderer);
    }

    @Override
    public void submitMultiLayerBlockModel(PoseStack poseStack, List<BlockStateModelPart> modelParts, boolean translucent, int[] tintLayers, int lightCoords, int overlayCoords, int outlineColor) {
        delegate.submitMultiLayerBlockModel(poseStack, modelParts, translucent, tintLayers, lightCoords, overlayCoords, outlineColor);
    }

    private static class ScalingOrderedCollector implements OrderedSubmitNodeCollector {
        private final OrderedSubmitNodeCollector delegate;
        private final float scale;
        private final float opacity;

        public ScalingOrderedCollector(OrderedSubmitNodeCollector delegate, float scale, float opacity) {
            this.delegate = delegate;
            this.scale = scale;
            this.opacity = opacity;
        }

        @Override
        public void submitItem(
            PoseStack poseStack,
            ItemDisplayContext displayContext,
            int lightCoords,
            int overlayCoords,
            int outlineColor,
            int[] tintLayers,
            List<BakedQuad> quads,
            ItemStackRenderState.FoilType foilType
        ) {
            doSubmitItem(delegate, scale, opacity, poseStack, displayContext, lightCoords, overlayCoords, outlineColor, tintLayers, quads, foilType);
        }

        @Override
        public void submitShadow(PoseStack poseStack, float radius, List<EntityRenderState.ShadowPiece> pieces) {
            delegate.submitShadow(poseStack, radius, pieces);
        }

        @Override
        public void submitNameTag(PoseStack poseStack, @Nullable Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, double distanceToCameraSq, CameraRenderState camera) {
            delegate.submitNameTag(poseStack, nameTagAttachment, offset, name, seeThrough, lightCoords, distanceToCameraSq, camera);
        }

        @Override
        public void submitText(PoseStack poseStack, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor) {
            delegate.submitText(poseStack, x, y, string, dropShadow, displayMode, lightCoords, color, backgroundColor, outlineColor);
        }

        @Override
        public void submitFlame(PoseStack poseStack, EntityRenderState renderState, Quaternionf rotation) {
            delegate.submitFlame(poseStack, renderState, rotation);
        }

        @Override
        public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {
            delegate.submitLeash(poseStack, leashState);
        }

        @Override
        public <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, @Nullable TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
            delegate.submitModel(model, state, poseStack, renderType, lightCoords, overlayCoords, tintedColor, sprite, outlineColor, crumblingOverlay);
        }

        @Override
        public void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, @Nullable TextureAtlasSprite sprite, boolean sheeted, boolean hasFoil, int tintedColor, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay, int outlineColor) {
            delegate.submitModelPart(modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, sheeted, hasFoil, tintedColor, crumblingOverlay, outlineColor);
        }

        @Override
        public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
            delegate.submitMovingBlock(poseStack, movingBlockRenderState);
        }

        @Override
        public void submitBlockModel(PoseStack poseStack, RenderType renderType, List<BlockStateModelPart> parts, int[] tintLayers, int lightCoords, int overlayCoords, int outlineColor) {
            delegate.submitBlockModel(poseStack, renderType, parts, tintLayers, lightCoords, overlayCoords, outlineColor);
        }

        @Override
        public void submitBreakingBlockModel(PoseStack poseStack, BlockStateModel model, long seed, int progress) {
            delegate.submitBreakingBlockModel(poseStack, model, seed, progress);
        }

        @Override
        public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, CustomGeometryRenderer customGeometryRenderer) {
            delegate.submitCustomGeometry(poseStack, renderType, customGeometryRenderer);
        }

        @Override
        public void submitParticleGroup(ParticleGroupRenderer particleGroupRenderer) {
            delegate.submitParticleGroup(particleGroupRenderer);
        }

        @Override
        public void submitMultiLayerBlockModel(PoseStack poseStack, List<BlockStateModelPart> modelParts, boolean translucent, int[] tintLayers, int lightCoords, int overlayCoords, int outlineColor) {
            delegate.submitMultiLayerBlockModel(poseStack, modelParts, translucent, tintLayers, lightCoords, overlayCoords, outlineColor);
        }
    }

    private static void doSubmitItem(
        OrderedSubmitNodeCollector delegate,
        float scale,
        float opacity,
        PoseStack poseStack,
        ItemDisplayContext displayContext,
        int lightCoords,
        int overlayCoords,
        int outlineColor,
        int[] tintLayers,
        List<BakedQuad> quads,
        ItemStackRenderState.FoilType foilType
    ) {
        poseStack.pushPose();
        if (scale != 1.0f) {
            // Anchor scaling around the grip of handheld items (x: 2.5/16, y: 2.5/16, z: 0.5)
            float gx = 2.5f / 16.0f;
            float gy = 2.5f / 16.0f;
            float gz = 0.5f;
            poseStack.translate(gx, gy, gz);
            poseStack.scale(scale, scale, scale);
            poseStack.translate(-gx, -gy, -gz);
        }

        if (opacity < 0.999f && quads != null && !quads.isEmpty()) {
            if (opacity <= 0.001f) {
                poseStack.popPose();
                return;
            }
            int alpha = Math.max(0, Math.min(255, Math.round(opacity * 255.0f)));
            int[] newTintLayers = new int[quads.size()];
            List<BakedQuad> newQuads = new ArrayList<>(quads.size());

            for (int i = 0; i < quads.size(); i++) {
                BakedQuad quad = quads.get(i);
                BakedQuad.MaterialInfo mat = quad.materialInfo();

                int origColor = -1;
                if (mat.isTinted() && tintLayers != null && mat.tintIndex() >= 0 && mat.tintIndex() < tintLayers.length) {
                    origColor = tintLayers[mat.tintIndex()];
                }

                int origAlpha = (origColor >>> 24) & 0xFF;
                int finalAlpha = Math.round(origAlpha * (alpha / 255.0f));
                int finalColor = (finalAlpha << 24) | (origColor & 0x00FFFFFF);
                newTintLayers[i] = finalColor;

                RenderType origType = mat.itemRenderType();
                RenderType newType;
                if (origType == Sheets.cutoutBlockItemSheet() || (origType != null && origType.toString().contains("block"))) {
                    newType = Sheets.translucentBlockItemSheet();
                } else if (origType != null && origType.hasBlending()) {
                    newType = origType;
                } else {
                    newType = Sheets.translucentItemSheet();
                }

                BakedQuad.MaterialInfo newMat = new BakedQuad.MaterialInfo(
                    mat.sprite(),
                    mat.layer(),
                    newType,
                    i,
                    mat.shade(),
                    mat.lightEmission()
                );

                newQuads.add(new BakedQuad(
                    quad.position0(),
                    quad.position1(),
                    quad.position2(),
                    quad.position3(),
                    quad.packedUV0(),
                    quad.packedUV1(),
                    quad.packedUV2(),
                    quad.packedUV3(),
                    quad.direction(),
                    newMat
                ));
            }

            delegate.submitItem(poseStack, displayContext, lightCoords, overlayCoords, outlineColor, newTintLayers, newQuads, foilType);
        } else {
            delegate.submitItem(poseStack, displayContext, lightCoords, overlayCoords, outlineColor, tintLayers, quads, foilType);
        }

        poseStack.popPose();
    }
}
