package net.mcreator.minigames.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.mcreator.minigames.entity.MovingBlockEntity;

public class MovingBlockRenderer extends EntityRenderer<MovingBlockEntity, EntityRenderState> {
	private final BlockRenderDispatcher blockRenderer;
	private MovingBlockEntity entity;

	public MovingBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.blockRenderer = context.getBlockRenderDispatcher();
		this.shadowRadius = 0.0f;
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}

	@Override
	public void extractRenderState(MovingBlockEntity entity, EntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		this.entity = entity;
	}

	@Override
	public void render(EntityRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		if (this.entity == null)
			return;

		String blockId = this.entity.getEntityData().get(MovingBlockEntity.DATA_block_id);
		Block block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(blockId)).orElse(Blocks.STONE);
		BlockState blockState = block.defaultBlockState();

		poseStack.pushPose();
		poseStack.translate(-0.5, 0.0, -0.5);
		this.blockRenderer.renderSingleBlock(blockState, poseStack, buffer, packedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
	}

	public ResourceLocation getTextureLocation(MovingBlockEntity entity) {
		return net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS;
	}
}
