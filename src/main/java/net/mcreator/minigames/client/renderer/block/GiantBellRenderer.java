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
import net.mcreator.minigames.client.model.Modelgiant_bell;
import net.mcreator.minigames.block.entity.GiantBellBlockEntity;

import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

@EventBusSubscriber(Dist.CLIENT)
public class GiantBellRenderer implements BlockEntityRenderer<GiantBellBlockEntity, GiantBellRenderer.CustomRenderState> {
	private final CustomHierarchicalModel model;
	private final Identifier texture;

	GiantBellRenderer(BlockEntityRendererProvider.Context context) {
		this.model = new CustomHierarchicalModel(context.bakeLayer(Modelgiant_bell.LAYER_LOCATION));
		this.texture = Identifier.parse("minigames:textures/block/giant_bell.png");
	}

	@Override
	public CustomRenderState createRenderState() {
		return new CustomRenderState();
	}

	@Override
	public void extractRenderState(GiantBellBlockEntity blockEntity, CustomRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);
		state.blockEntity = blockEntity;
		state.blockState = blockEntity.getBlockState();
		int tickCount = (int) blockEntity.getLevel().getGameTime();
		state.entityRenderState.ageInTicks = tickCount + partialTicks;
		state.ticks = blockEntity.ticks + partialTicks;
		state.shaking = blockEntity.shaking;
		state.clickDirection = blockEntity.clickDirection;
	}

	@Override
	public void submit(CustomRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		poseStack.pushPose();
		if (renderState.shaking && renderState.clickDirection != null) {
			float shake = renderState.ticks;
			float angle = (float) (Math.sin(shake / Math.PI) / (4.0 + shake / 3.0) * 2.2);
			poseStack.translate(0.5, 2.7, 0.5);
			switch (renderState.clickDirection) {
				case NORTH -> poseStack.mulPose(Axis.XP.rotation(-angle));
				case SOUTH -> poseStack.mulPose(Axis.XP.rotation(angle));
				case EAST -> poseStack.mulPose(Axis.ZP.rotation(-angle));
				case WEST -> poseStack.mulPose(Axis.ZP.rotation(angle));
				default -> poseStack.mulPose(Axis.XP.rotation(-angle));
			}
			poseStack.translate(-0.5, -2.7, -0.5);
		}
		poseStack.scale(-1, -1, 1);
		poseStack.translate(-0.5, -0.5, 0.5);
		poseStack.translate(0, -1, 0);
		model.setupBlockEntityAnim(renderState.blockEntity, renderState.entityRenderState);
		submitNodeCollector.submitModel(this.model, renderState.entityRenderState, poseStack, RenderTypes.entityCutout(texture), renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(MinigamesModBlockEntities.GIANT_BELL.get(), GiantBellRenderer::new);
	}

	public static class CustomRenderState extends BlockEntityRenderState {
		protected final LivingEntityRenderState entityRenderState = new LivingEntityRenderState();
		protected GiantBellBlockEntity blockEntity;
		protected BlockState blockState;
		public float ticks;
		public boolean shaking;
		public Direction clickDirection;
	}

	private static final class CustomHierarchicalModel extends Modelgiant_bell {
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

		public void setupBlockEntityAnim(GiantBellBlockEntity blockEntity, LivingEntityRenderState state) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			super.setupAnim(state);
		}
	}
}