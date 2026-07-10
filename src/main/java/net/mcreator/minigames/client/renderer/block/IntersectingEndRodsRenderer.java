package net.mcreator.minigames.client.renderer.block;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.AnimationDefinition;

import net.mcreator.minigames.init.MinigamesModBlockEntities;
import net.mcreator.minigames.client.model.Modelintersecting_end_rod;
import net.mcreator.minigames.block.entity.IntersectingEndRodsBlockEntity;
import net.mcreator.minigames.block.IntersectingEndRodsBlock;

import java.util.Map;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(Dist.CLIENT)
public class IntersectingEndRodsRenderer implements BlockEntityRenderer<IntersectingEndRodsBlockEntity, IntersectingEndRodsRenderer.CustomRenderState> {
	private final CustomHierarchicalModel model;
	private final Identifier texture;

	IntersectingEndRodsRenderer(BlockEntityRendererProvider.Context context) {
		this.model = new CustomHierarchicalModel(context.bakeLayer(Modelintersecting_end_rod.LAYER_LOCATION));
		this.texture = Identifier.parse("minigames:textures/block/intersecting_end_rod.png");
	}

	@Override
	public CustomRenderState createRenderState() {
		return new CustomRenderState();
	}

	@Override
	public void extractRenderState(IntersectingEndRodsBlockEntity blockEntity, CustomRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);
		state.blockEntity = blockEntity;
		state.blockState = blockEntity.getBlockState();
		int tickCount = (int) blockEntity.getLevel().getGameTime();
		state.entityRenderState.ageInTicks = tickCount + partialTicks;
	}

	@Override
	public void submit(CustomRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		poseStack.pushPose();
		poseStack.scale(-1, -1, 1);
		poseStack.translate(-0.5, -0.5, 0.5);
		BlockState state = renderState.blockState;
		Direction facing = state.getValue(IntersectingEndRodsBlock.FACING);
		switch (facing) {
			case NORTH -> {
			}
			case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
			case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(-90));
			case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
			case UP -> poseStack.mulPose(Axis.XN.rotationDegrees(90));
			case DOWN -> poseStack.mulPose(Axis.XN.rotationDegrees(-90));
		}
		poseStack.translate(0, -1, 0);
		model.setupBlockEntityAnim(renderState.blockEntity, renderState.entityRenderState);
		submitNodeCollector.submitModel(this.model, renderState.entityRenderState, poseStack, RenderTypes.entityCutout(texture), renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(MinigamesModBlockEntities.INTERSECTING_END_RODS.get(), IntersectingEndRodsRenderer::new);
	}

	public static class CustomRenderState extends BlockEntityRenderState {
		protected final LivingEntityRenderState entityRenderState = new LivingEntityRenderState();
		protected IntersectingEndRodsBlockEntity blockEntity;
		protected BlockState blockState;
	}

	private static final class CustomHierarchicalModel extends Modelintersecting_end_rod {
		public CustomHierarchicalModel(ModelPart root) {
			super(root);
		}

		private KeyframeAnimation safeBake(AnimationDefinition source) {
			try {
				return source.bake(root);
			} catch (IllegalArgumentException e) {
				return new AnimationDefinition(0, false, Map.of()).bake(root);
			}
		}

		public void setupBlockEntityAnim(IntersectingEndRodsBlockEntity blockEntity, LivingEntityRenderState state) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			super.setupAnim(state);
		}
	}
}