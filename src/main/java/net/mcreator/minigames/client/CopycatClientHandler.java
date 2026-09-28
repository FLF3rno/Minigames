package net.mcreator.minigames.client;

import com.mojang.blaze3d.platform.Transparency;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;
import net.mcreator.minigames.block.CopycatHelper;
import net.mcreator.minigames.block.entity.CopycatBlockEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(Dist.CLIENT)
public class CopycatClientHandler {
	@SubscribeEvent
	public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
		Map<BlockState, BlockStateModel> models = event.getBakingResult().blockStateModels();
		for (Map.Entry<BlockState, BlockStateModel> entry : models.entrySet()) {
			BlockState state = entry.getKey();
			if (CopycatHelper.isCopycatBlock(state.getBlock())) {
				entry.setValue(new CopycatBlockStateModel(entry.getValue()));
			}
		}
	}

	public static class CopycatBlockStateModel extends DelegateBlockStateModel {
		public CopycatBlockStateModel(BlockStateModel delegate) {
			super(delegate);
		}

		@Override
		public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
			BlockState material = null;
			ModelData modelData = level.getModelData(pos);
			if (modelData != null && modelData.has(CopycatBlockEntity.MATERIAL_PROPERTY)) {
				material = modelData.get(CopycatBlockEntity.MATERIAL_PROPERTY);
			}
			if (material == null || material.isAir()) {
				BlockEntity be = level.getBlockEntity(pos);
				if (be instanceof CopycatBlockEntity copycat && copycat.hasMaterial()) {
					material = copycat.getMaterial();
				}
			}

			if (material != null && !material.isAir()) {
				BlockStateModel matModel = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(material);
				if (matModel != null) {
					List<BlockStateModelPart> originalParts = new ArrayList<>();
					delegate.collectParts(level, pos, state, random, originalParts);

					List<BlockStateModelPart> matParts = new ArrayList<>();
					matModel.collectParts(level, pos, material, random, matParts);

					Map<Direction, BakedQuad.MaterialInfo> dirMaterials = new HashMap<>();
					BakedQuad.MaterialInfo fallbackInfo = null;
					Material.Baked particle = matModel.particleMaterial(level, pos, material);

					for (BlockStateModelPart mp : matParts) {
						for (Direction dir : Direction.values()) {
							List<BakedQuad> mq = mp.getQuads(dir);
							if (mq != null && !mq.isEmpty()) {
								BakedQuad.MaterialInfo info = mq.get(0).materialInfo();
								dirMaterials.putIfAbsent(dir, info);
								if (fallbackInfo == null) fallbackInfo = info;
							}
						}
						List<BakedQuad> unculled = mp.getQuads(null);
						if (unculled != null && !unculled.isEmpty()) {
							if (fallbackInfo == null) fallbackInfo = unculled.get(0).materialInfo();
						}
					}
					if (fallbackInfo == null && particle != null) {
						fallbackInfo = BakedQuad.MaterialInfo.of(particle, Transparency.NONE, -1, true, 0, true);
					}

					for (BlockStateModelPart origPart : originalParts) {
						parts.add(new RetexturedPart(origPart, dirMaterials, fallbackInfo, particle));
					}
					return;
				}
			}
			super.collectParts(level, pos, state, random, parts);
		}

		@Override
		public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
			BlockState material = null;
			ModelData modelData = level.getModelData(pos);
			if (modelData != null && modelData.has(CopycatBlockEntity.MATERIAL_PROPERTY)) {
				material = modelData.get(CopycatBlockEntity.MATERIAL_PROPERTY);
			}
			if (material == null || material.isAir()) {
				BlockEntity be = level.getBlockEntity(pos);
				if (be instanceof CopycatBlockEntity copycat && copycat.hasMaterial()) {
					material = copycat.getMaterial();
				}
			}
			if (material != null && !material.isAir()) {
				BlockStateModel matModel = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(material);
				if (matModel != null) {
					return matModel.particleMaterial(level, pos, material);
				}
			}
			return super.particleMaterial(level, pos, state);
		}
	}

	public static class RetexturedPart implements BlockStateModelPart {
		private final BlockStateModelPart original;
		private final Map<Direction, BakedQuad.MaterialInfo> dirMaterials;
		private final BakedQuad.MaterialInfo fallbackInfo;
		private final Material.Baked particle;

		public RetexturedPart(BlockStateModelPart original, Map<Direction, BakedQuad.MaterialInfo> dirMaterials, BakedQuad.MaterialInfo fallbackInfo, Material.Baked particle) {
			this.original = original;
			this.dirMaterials = dirMaterials;
			this.fallbackInfo = fallbackInfo;
			this.particle = particle;
		}

		private static long remapUV(long packedUV, TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite) {
			if (oldSprite == null || newSprite == null || oldSprite == newSprite) {
				return packedUV;
			}
			float du = oldSprite.getU1() - oldSprite.getU0();
			float dv = oldSprite.getV1() - oldSprite.getV0();
			if (du == 0.0f || dv == 0.0f) {
				return packedUV;
			}
			float oldU = UVPair.unpackU(packedUV);
			float oldV = UVPair.unpackV(packedUV);
			float relU = (oldU - oldSprite.getU0()) / du;
			float relV = (oldV - oldSprite.getV0()) / dv;
			return UVPair.pack(newSprite.getU(relU), newSprite.getV(relV));
		}

		@Override
		public List<BakedQuad> getQuads(Direction side) {
			List<BakedQuad> quads = original.getQuads(side);
			if (quads == null || quads.isEmpty()) return quads;
			List<BakedQuad> result = new ArrayList<>(quads.size());
			for (BakedQuad q : quads) {
				BakedQuad.MaterialInfo info = q.direction() != null ? dirMaterials.get(q.direction()) : null;
				if (info == null && side != null) info = dirMaterials.get(side);
				if (info == null) info = fallbackInfo;
				if (info != null) {
					TextureAtlasSprite oldSprite = q.materialInfo() != null ? q.materialInfo().sprite() : null;
					TextureAtlasSprite newSprite = info.sprite();
					long uv0 = remapUV(q.packedUV0(), oldSprite, newSprite);
					long uv1 = remapUV(q.packedUV1(), oldSprite, newSprite);
					long uv2 = remapUV(q.packedUV2(), oldSprite, newSprite);
					long uv3 = remapUV(q.packedUV3(), oldSprite, newSprite);
					result.add(new BakedQuad(
						q.position0(), q.position1(), q.position2(), q.position3(),
						uv0, uv1, uv2, uv3,
						q.direction(), info, q.bakedNormals(), q.bakedColors()
					));
				} else {
					result.add(q);
				}
			}
			return result;
		}

		@Override
		public boolean useAmbientOcclusion() {
			return original.useAmbientOcclusion();
		}

		@Override
		public Material.Baked particleMaterial() {
			return particle != null ? particle : original.particleMaterial();
		}

		@Override
		public int materialFlags() {
			return original.materialFlags();
		}
	}
}
