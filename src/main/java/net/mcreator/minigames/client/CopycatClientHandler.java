package net.mcreator.minigames.client;

import com.mojang.blaze3d.platform.Transparency;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;
import net.mcreator.minigames.MinigamesMod;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.mcreator.minigames.block.CopycatBlockBlock;
import net.mcreator.minigames.block.CopycatCornerSliceBlock;
import net.mcreator.minigames.block.CopycatHelper;
import net.mcreator.minigames.block.CopycatLayerBlock;
import net.mcreator.minigames.block.CopycatSlabBlock;
import net.mcreator.minigames.block.CopycatSlabType;
import net.mcreator.minigames.block.entity.CopycatBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.NyliumBlock;
import net.minecraft.world.level.block.SpreadingSnowyBlock;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.color.block.BlockTintSource;
import net.neoforged.neoforge.client.model.quad.BakedColors;

public class CopycatClientHandler {
	private static final Map<TextureAtlasSprite, Optional<TextureAtlasSprite>> CONNECTING_CACHE = new ConcurrentHashMap<>();

	@SubscribeEvent
	public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
		CONNECTING_CACHE.clear();
		Map<BlockState, BlockStateModel> models = event.getBakingResult().blockStateModels();
		int wrapped = 0;
		for (Map.Entry<BlockState, BlockStateModel> entry : models.entrySet()) {
			BlockState state = entry.getKey();
			if (CopycatHelper.isCopycatBlock(state.getBlock())) {
				if (!(entry.getValue() instanceof CopycatBlockStateModel)) {
					entry.setValue(new CopycatBlockStateModel(entry.getValue()));
					wrapped++;
				}
			}
		}
		MinigamesMod.LOGGER.info("[Copycat] onModifyBakingResult wrapped {} copycat blockstate models", wrapped);
	}

	public static TextureAtlasSprite getConnectingSprite(TextureAtlasSprite baseSprite) {
		if (baseSprite == null) return null;
		return CONNECTING_CACHE.computeIfAbsent(baseSprite, CopycatClientHandler::resolveConnectingSprite).orElse(null);
	}

	private static Optional<TextureAtlasSprite> resolveConnectingSprite(TextureAtlasSprite baseSprite) {
		if (baseSprite == null) return Optional.empty();
		Identifier id = baseSprite.contents().name();
		if (id == null) return Optional.empty();

		String path = id.getPath();
		if (path.contains("connecting/")) {
			return Optional.of(baseSprite);
		}

		String texName = path.startsWith("block/") ? path.substring("block/".length()) : path;
		String simpleName = texName.contains("/") ? texName.substring(texName.lastIndexOf('/') + 1) : texName;
		String namespace = id.getNamespace();

		AtlasManager am = Minecraft.getInstance().getAtlasManager();
		if (am == null) return Optional.empty();
		net.minecraft.client.renderer.texture.TextureAtlas blockAtlas = null;
		try {
			blockAtlas = am.getAtlasOrThrow(Identifier.fromNamespaceAndPath("minecraft", "blocks"));
		} catch (Throwable ignored) {
		}
		if (blockAtlas == null) {
			final net.minecraft.client.renderer.texture.TextureAtlas[] found = new net.minecraft.client.renderer.texture.TextureAtlas[1];
			am.forEach((atlasId, atlas) -> {
				if (atlasId.getPath().contains("blocks") || atlas.location().getPath().contains("blocks")) {
					found[0] = atlas;
				}
			});
			blockAtlas = found[0];
		}
		if (blockAtlas == null) return Optional.empty();

		Identifier missingId = MissingTextureAtlasSprite.getLocation();
		TextureAtlasSprite missingSprite = blockAtlas.getSprite(missingId);

		List<Identifier> candidates = new ArrayList<>();
		candidates.add(Identifier.fromNamespaceAndPath("minigames", "block/connecting/" + texName));
		candidates.add(Identifier.fromNamespaceAndPath("minigames", "block/connecting/connecting_" + texName));
		candidates.add(Identifier.fromNamespaceAndPath(namespace, "block/connecting/" + texName));
		candidates.add(Identifier.fromNamespaceAndPath(namespace, "block/connecting/connecting_" + texName));
		if (!simpleName.equals(texName)) {
			candidates.add(Identifier.fromNamespaceAndPath("minigames", "block/connecting/" + simpleName));
			candidates.add(Identifier.fromNamespaceAndPath("minigames", "block/connecting/connecting_" + simpleName));
			candidates.add(Identifier.fromNamespaceAndPath(namespace, "block/connecting/" + simpleName));
			candidates.add(Identifier.fromNamespaceAndPath(namespace, "block/connecting/connecting_" + simpleName));
		}

		for (Identifier candidate : candidates) {
			try {
				TextureAtlasSprite sprite = blockAtlas.getSprite(candidate);
				if (sprite != null && sprite != missingSprite && !missingId.equals(sprite.contents().name())) {
					MinigamesMod.LOGGER.info("[Copycat] Found connecting sprite for {} -> {}", id, candidate);
					return Optional.of(sprite);
				}
			} catch (Throwable ignored) {
			}
		}

		return Optional.empty();
	}

	private static BakedQuad.MaterialInfo createMaterialInfo(TextureAtlasSprite sprite, BakedQuad.MaterialInfo original) {
		ChunkSectionLayer layer = original != null ? original.layer() : ChunkSectionLayer.CUTOUT;
		return new BakedQuad.MaterialInfo(
			sprite,
			layer,
			original != null ? original.itemRenderType() : null,
			original != null ? original.tintIndex() : -1,
			original != null ? original.shade() : true,
			original != null ? original.lightEmission() : 0,
			original != null ? original.ambientOcclusion() : true
		);
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
					Map<Direction, BakedQuad.MaterialInfo> overlayMaterials = new HashMap<>();
					BakedQuad.MaterialInfo fallbackInfo = null;
					Material.Baked particle = matModel.particleMaterial(level, pos, material);
					boolean isRegularCopycat = state.getBlock() instanceof CopycatBlockBlock;

					for (BlockStateModelPart mp : matParts) {
						for (Direction dir : Direction.values()) {
							List<BakedQuad> mq = mp.getQuads(dir);
							if (mq != null && !mq.isEmpty()) {
								BakedQuad.MaterialInfo info0 = mq.get(0).materialInfo();
								BakedQuad.MaterialInfo info1 = mq.size() > 1 ? mq.get(1).materialInfo() : null;
								for (BakedQuad.MaterialInfo mi : new BakedQuad.MaterialInfo[] { info0, info1 }) {
									if (mi == null || mi.sprite() == null) continue;
									String p = mi.sprite().contents().name().getPath();
									if (p.contains("overlay")) {
										overlayMaterials.putIfAbsent(dir, mi);
									} else {
										dirMaterials.putIfAbsent(dir, mi);
									}
								}
								if (!dirMaterials.containsKey(dir)) {
									dirMaterials.put(dir, info0);
								}
								if (fallbackInfo == null) fallbackInfo = dirMaterials.get(dir);
							}
						}
						List<BakedQuad> unculled = mp.getQuads(null);
						if (unculled != null && !unculled.isEmpty()) {
							BakedQuad.MaterialInfo info0 = unculled.get(0).materialInfo();
							BakedQuad.MaterialInfo info1 = unculled.size() > 1 ? unculled.get(1).materialInfo() : null;
							for (BakedQuad.MaterialInfo mi : new BakedQuad.MaterialInfo[] { info0, info1 }) {
								if (mi == null || mi.sprite() == null) continue;
								String p = mi.sprite().contents().name().getPath();
								if (p.contains("overlay")) {
									overlayMaterials.putIfAbsent(null, mi);
								} else {
									dirMaterials.putIfAbsent(null, mi);
								}
							}
							if (fallbackInfo == null) fallbackInfo = info0;
						}
					}
					if (fallbackInfo == null && particle != null) {
						fallbackInfo = BakedQuad.MaterialInfo.of(particle, Transparency.NONE, -1, true, 0, true);
					}

					boolean isGroundSlab = false;
					if (state != null) {
						if (state.getBlock() instanceof CopycatSlabBlock) {
							isGroundSlab = state.getValue(CopycatSlabBlock.TYPE) == CopycatSlabType.BOTTOM;
						} else if (state.getBlock() instanceof CopycatLayerBlock) {
							isGroundSlab = !state.hasProperty(CopycatLayerBlock.FACING) || state.getValue(CopycatLayerBlock.FACING) == Direction.UP;
						} else if (state.getBlock() instanceof CopycatCornerSliceBlock) {
							boolean hasTop = state.getValue(CopycatCornerSliceBlock.C4)
								|| state.getValue(CopycatCornerSliceBlock.C5)
								|| state.getValue(CopycatCornerSliceBlock.C6)
								|| state.getValue(CopycatCornerSliceBlock.C7);
							isGroundSlab = !hasTop;
						}
					}

					for (BlockStateModelPart origPart : originalParts) {
						parts.add(new RetexturedPart(origPart, dirMaterials, overlayMaterials, fallbackInfo, particle, level, pos, material, isRegularCopycat, isGroundSlab));
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
					Material.Baked p = matModel.particleMaterial(level, pos, material);
					if (state.getBlock() instanceof CopycatBlockBlock && p != null && p.sprite() != null) {
						TextureAtlasSprite connecting = getConnectingSprite(p.sprite());
						if (connecting != null) {
							return new Material.Baked(connecting, false);
						}
					}
					return p;
				}
			}
			return super.particleMaterial(level, pos, state);
		}
	}

	public static class RetexturedPart implements BlockStateModelPart {
		private final BlockStateModelPart original;
		private final Map<Direction, BakedQuad.MaterialInfo> dirMaterials;
		private final Map<Direction, BakedQuad.MaterialInfo> overlayMaterials;
		private final BakedQuad.MaterialInfo fallbackInfo;
		private final Material.Baked particle;
		private final BlockAndTintGetter level;
		private final BlockPos pos;
		private final BlockState material;
		private final boolean isRegularCopycat;
		private final boolean isGroundSlab;
		private final boolean isTopSoil;

		public RetexturedPart(
			BlockStateModelPart original,
			Map<Direction, BakedQuad.MaterialInfo> dirMaterials,
			Map<Direction, BakedQuad.MaterialInfo> overlayMaterials,
			BakedQuad.MaterialInfo fallbackInfo,
			Material.Baked particle,
			BlockAndTintGetter level,
			BlockPos pos,
			BlockState material,
			boolean isRegularCopycat,
			boolean isGroundSlab
		) {
			this.original = original;
			this.dirMaterials = dirMaterials;
			this.overlayMaterials = overlayMaterials != null ? overlayMaterials : Collections.emptyMap();
			this.fallbackInfo = fallbackInfo;
			this.particle = particle;
			this.level = level;
			this.pos = pos;
			this.material = material;
			this.isRegularCopycat = isRegularCopycat;
			this.isGroundSlab = isGroundSlab;
			this.isTopSoil = checkTopSoil(material);
		}

		public RetexturedPart(
			BlockStateModelPart original,
			Map<Direction, BakedQuad.MaterialInfo> dirMaterials,
			BakedQuad.MaterialInfo fallbackInfo,
			Material.Baked particle,
			BlockAndTintGetter level,
			BlockPos pos,
			BlockState material,
			boolean isRegularCopycat
		) {
			this(original, dirMaterials, Collections.emptyMap(), fallbackInfo, particle, level, pos, material, isRegularCopycat, false);
		}

		private static boolean checkTopSoil(BlockState state) {
			return state != null && (
				state.is(Blocks.GRASS_BLOCK)
				|| state.is(Blocks.PODZOL)
				|| state.is(Blocks.MYCELIUM)
				|| state.is(Blocks.DIRT_PATH)
				|| state.is(Blocks.CRIMSON_NYLIUM)
				|| state.is(Blocks.WARPED_NYLIUM)
				|| state.getBlock() instanceof SpreadingSnowyBlock
				|| state.getBlock() instanceof DirtPathBlock
				|| state.getBlock() instanceof NyliumBlock
			);
		}

		private static long remapUV(long packedUV, TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite, float vOffset) {
			if (oldSprite == null || newSprite == null || (oldSprite == newSprite && vOffset == 0.0f)) {
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
			float relV = ((oldV - oldSprite.getV0()) / dv) - vOffset;
			return UVPair.pack(newSprite.getU(relU), newSprite.getV(relV));
		}

		private static Direction getQuadDirection(BakedQuad q, Direction side) {
			if (q.direction() != null) {
				return q.direction();
			}
			if (side != null) {
				return side;
			}
			var p0 = q.position0();
			var p1 = q.position1();
			var p2 = q.position2();
			float v1x = p1.x() - p0.x();
			float v1y = p1.y() - p0.y();
			float v1z = p1.z() - p0.z();
			float v2x = p2.x() - p0.x();
			float v2y = p2.y() - p0.y();
			float v2z = p2.z() - p0.z();
			float nx = v1y * v2z - v1z * v2y;
			float ny = v1z * v2x - v1x * v2z;
			float nz = v1x * v2y - v1y * v2x;
			if (Math.abs(nx) < 1e-4f && Math.abs(ny) < 1e-4f && Math.abs(nz) < 1e-4f) {
				var p3 = q.position3();
				v2x = p3.x() - p0.x();
				v2y = p3.y() - p0.y();
				v2z = p3.z() - p0.z();
				nx = v1y * v2z - v1z * v2y;
				ny = v1z * v2x - v1x * v2z;
				nz = v1x * v2y - v1y * v2x;
			}
			float max = Math.max(Math.abs(nx), Math.max(Math.abs(ny), Math.abs(nz)));
			if (max > 1e-4f) {
				nx /= max;
				ny /= max;
				nz /= max;
			}
			return Direction.getNearest(Math.round(nx), Math.round(ny), Math.round(nz), Direction.NORTH);
		}

		@Override
		public List<BakedQuad> getQuads(Direction side) {
			if (isRegularCopycat && side != null && shouldCullFace(level, pos, material, side)) {
				return Collections.emptyList();
			}

			List<BakedQuad> quads = original.getQuads(side);
			if (quads == null || quads.isEmpty()) return quads;
			List<BakedQuad> result = new ArrayList<>(quads.size());

			for (BakedQuad q : quads) {
				Direction effSide = getQuadDirection(q, side);
				BakedQuad.MaterialInfo info = effSide != null ? dirMaterials.get(effSide) : null;
				if (info == null) info = fallbackInfo;
				if (info != null && info.sprite() != null) {
					if (isRegularCopycat && effSide != null && level != null && pos != null) {
						TextureAtlasSprite connecting = getConnectingSprite(info.sprite());
						if (connecting != null) {
							int w = connecting.contents().width();
							int h = connecting.contents().height();
							if (w == 5 * h) {
								generateCTM5Quads(result, q, effSide, connecting, info, level, pos, material);
								continue;
							} else if (w == 4 * h) {
								generateCTM4HQuads(result, q, effSide, connecting, info, level, pos, material);
								continue;
							} else if (h == 4 * w) {
								generateCTM4VQuads(result, q, effSide, connecting, info, level, pos, material);
								continue;
							}
						}
					}

					TextureAtlasSprite oldSprite = q.materialInfo() != null ? q.materialInfo().sprite() : null;
					TextureAtlasSprite newSprite = info.sprite();

					float vOffset = 0.0f;
					if (isGroundSlab && isTopSoil && effSide != null && effSide.getAxis() != Direction.Axis.Y && oldSprite != null) {
						float dv = oldSprite.getV1() - oldSprite.getV0();
						if (dv != 0.0f) {
							float relV0 = (UVPair.unpackV(q.packedUV0()) - oldSprite.getV0()) / dv;
							float relV1 = (UVPair.unpackV(q.packedUV1()) - oldSprite.getV0()) / dv;
							float relV2 = (UVPair.unpackV(q.packedUV2()) - oldSprite.getV0()) / dv;
							float relV3 = (UVPair.unpackV(q.packedUV3()) - oldSprite.getV0()) / dv;
							float minRelV = Math.min(Math.min(relV0, relV1), Math.min(relV2, relV3));
							if (minRelV > 0.001f) {
								vOffset = minRelV;
							}
						}
					}

					BakedColors bakedColors = q.bakedColors();
					if (material != null && level != null && pos != null) {
						int tintIndex = (info != null && info.isTinted()) ? info.tintIndex() : -1;
						if (tintIndex == -1) {
							Block block = material.getBlock();
							List<BlockTintSource> sources = Minecraft.getInstance().getBlockColors().getTintSources(material);
							if (sources != null && !sources.isEmpty()) {
								if (block instanceof net.minecraft.world.level.block.LeavesBlock
										|| block instanceof net.minecraft.world.level.block.VineBlock
										|| block instanceof net.minecraft.world.level.block.SugarCaneBlock
										|| material.is(Blocks.LILY_PAD)
										|| block instanceof net.minecraft.world.level.block.TallGrassBlock
										|| block instanceof net.minecraft.world.level.block.DoublePlantBlock
										|| block instanceof net.minecraft.world.level.block.BushBlock) {
									tintIndex = 0;
								} else if (newSprite != null) {
									String path = newSprite.contents().name().getPath();
									if (path.contains("top") || path.contains("overlay")) {
										tintIndex = 0;
									}
								}
							}
						}
						if (tintIndex >= 0) {
							BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(material, tintIndex);
							if (tintSource != null) {
								int tint = tintSource.colorInWorld(material, level, pos);
								if (tint == 0 || tint == -1) {
									tint = tintSource.color(material);
								}
								bakedColors = BakedColors.of(tint | 0xFF000000);
							}
						}
					}

					BakedQuad.MaterialInfo ovInfo = effSide != null ? overlayMaterials.get(effSide) : overlayMaterials.get(null);
					BakedColors overlayColors = q.bakedColors();
					if (ovInfo != null && material != null && level != null && pos != null) {
						int ovTintIndex = ovInfo.isTinted() ? ovInfo.tintIndex() : -1;
						if (ovTintIndex == -1) {
							Block block = material.getBlock();
							List<BlockTintSource> sources = Minecraft.getInstance().getBlockColors().getTintSources(material);
							if (sources != null && !sources.isEmpty()) {
								if (block instanceof net.minecraft.world.level.block.LeavesBlock
										|| block instanceof net.minecraft.world.level.block.VineBlock
										|| block instanceof net.minecraft.world.level.block.SugarCaneBlock
										|| material.is(Blocks.LILY_PAD)
										|| block instanceof net.minecraft.world.level.block.TallGrassBlock
										|| block instanceof net.minecraft.world.level.block.DoublePlantBlock
										|| block instanceof net.minecraft.world.level.block.BushBlock) {
									ovTintIndex = 0;
								} else if (ovInfo.sprite() != null) {
									String path = ovInfo.sprite().contents().name().getPath();
									if (path.contains("top") || path.contains("overlay")) {
										ovTintIndex = 0;
									}
								}
							}
						}
						if (ovTintIndex >= 0) {
							BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(material, ovTintIndex);
							if (tintSource != null) {
								int tint = tintSource.colorInWorld(material, level, pos);
								if (tint == 0 || tint == -1) {
									tint = tintSource.color(material);
								}
								overlayColors = BakedColors.of(tint | 0xFF000000);
							}
						}
					} else {
						overlayColors = bakedColors;
					}

					long uv0 = remapUV(q.packedUV0(), oldSprite, newSprite, vOffset);
					long uv1 = remapUV(q.packedUV1(), oldSprite, newSprite, vOffset);
					long uv2 = remapUV(q.packedUV2(), oldSprite, newSprite, vOffset);
					long uv3 = remapUV(q.packedUV3(), oldSprite, newSprite, vOffset);
					result.add(new BakedQuad(
						q.position0(), q.position1(), q.position2(), q.position3(),
						uv0, uv1, uv2, uv3,
						q.direction(), info, q.bakedNormals(), bakedColors
					));

					if (ovInfo != null && ovInfo.sprite() != null) {
						TextureAtlasSprite ovSprite = ovInfo.sprite();
						long ouv0 = remapUV(q.packedUV0(), oldSprite, ovSprite, vOffset);
						long ouv1 = remapUV(q.packedUV1(), oldSprite, ovSprite, vOffset);
						long ouv2 = remapUV(q.packedUV2(), oldSprite, ovSprite, vOffset);
						long ouv3 = remapUV(q.packedUV3(), oldSprite, ovSprite, vOffset);
						result.add(new BakedQuad(
							q.position0(), q.position1(), q.position2(), q.position3(),
							ouv0, ouv1, ouv2, ouv3,
							q.direction(), ovInfo, q.bakedNormals(), overlayColors
						));
					}
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

	private static BlockState getNeighborMaterial(BlockGetter level, BlockPos nPos, BlockState nState) {
		if (nState.getBlock() instanceof CopycatBlockBlock) {
			if (level instanceof BlockAndTintGetter btg) {
				ModelData nData = btg.getModelData(nPos);
				if (nData != null && nData.has(CopycatBlockEntity.MATERIAL_PROPERTY)) {
					BlockState mat = nData.get(CopycatBlockEntity.MATERIAL_PROPERTY);
					if (mat != null && !mat.isAir()) return mat;
				}
			}
			BlockEntity nbe = level.getBlockEntity(nPos);
			if (nbe instanceof CopycatBlockEntity cn && cn.hasMaterial()) {
				return cn.getMaterial();
			}
			return null;
		}
		return nState;
	}

	private static boolean shouldCullFace(BlockGetter level, BlockPos pos, BlockState material, Direction side) {
		if (level == null || pos == null || material == null || side == null) return false;
		BlockPos nPos = pos.relative(side);
		BlockState nState = level.getBlockState(nPos);
		BlockState nMaterial = getNeighborMaterial(level, nPos, nState);
		if (nMaterial != null && nMaterial.getBlock() == material.getBlock()) {
			if (material.skipRendering(nMaterial, side)) {
				return true;
			}
			if (nMaterial.isSolidRender()) {
				return true;
			}
		}
		return false;
	}

	private static boolean connectsTo(BlockGetter level, BlockPos pos, BlockState material, Direction dir, Direction side) {
		if (level == null || pos == null || material == null || dir == null) return false;
		BlockPos nPos = pos.relative(dir);
		BlockState nState = level.getBlockState(nPos);
		Block targetBlock = material.getBlock();

		BlockState nMaterial = getNeighborMaterial(level, nPos, nState);
		if (nMaterial == null || nMaterial.getBlock() != targetBlock) {
			return false;
		}

		if (side != null) {
			BlockPos inFront = nPos.relative(side);
			BlockState frontState = level.getBlockState(inFront);
			if (frontState.isSolidRender()) {
				return false;
			}
		}
		return true;
	}

	private static boolean connectsToDiagonal(BlockGetter level, BlockPos pos, BlockState material, Direction dirH, Direction dirV, Direction side) {
		if (!connectsTo(level, pos, material, dirH, side) || !connectsTo(level, pos, material, dirV, side)) {
			return false;
		}
		BlockPos diagPos = pos.relative(dirH).relative(dirV);
		BlockState nState = level.getBlockState(diagPos);
		Block targetBlock = material.getBlock();

		BlockState nMaterial = getNeighborMaterial(level, diagPos, nState);
		if (nMaterial == null || nMaterial.getBlock() != targetBlock) {
			return false;
		}

		if (side != null) {
			BlockPos inFront = diagPos.relative(side);
			BlockState frontState = level.getBlockState(inFront);
			if (frontState.isSolidRender()) {
				return false;
			}
		}
		return true;
	}

	private static Direction getDir(float x, float y, float z) {
		return Direction.getNearest(Math.round(x), Math.round(y), Math.round(z), Direction.NORTH);
	}

	private static void generateCTM5Quads(
		List<BakedQuad> result, BakedQuad q, Direction side,
		TextureAtlasSprite sprite, BakedQuad.MaterialInfo info,
		BlockGetter level, BlockPos pos, BlockState material
	) {
		Vector3f origin;
		Vector3f uAxis;
		Vector3f vAxis;
		switch (side) {
			case UP -> {
				origin = new Vector3f(0f, 1f, 0f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, 0f, 1f);
			}
			case DOWN -> {
				origin = new Vector3f(0f, 0f, 1f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, 0f, -1f);
			}
			case NORTH -> {
				origin = new Vector3f(1f, 1f, 0f);
				uAxis = new Vector3f(-1f, 0f, 0f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			case SOUTH -> {
				origin = new Vector3f(0f, 1f, 1f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			case WEST -> {
				origin = new Vector3f(0f, 1f, 0f);
				uAxis = new Vector3f(0f, 0f, 1f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			case EAST -> {
				origin = new Vector3f(1f, 1f, 1f);
				uAxis = new Vector3f(0f, 0f, -1f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			default -> {
				origin = new Vector3f(0f, 1f, 0f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, 0f, 1f);
			}
		}

		BakedQuad.MaterialInfo newInfo = createMaterialInfo(sprite, info);

		for (int i = 0; i < 4; i++) {
			boolean isRight = (i % 2 == 1);
			boolean isBottom = (i >= 2);

			Direction dirH = isRight
				? getDir(uAxis.x, uAxis.y, uAxis.z)
				: getDir(-uAxis.x, -uAxis.y, -uAxis.z);
			Direction dirV = isBottom
				? getDir(vAxis.x, vAxis.y, vAxis.z)
				: getDir(-vAxis.x, -vAxis.y, -vAxis.z);

			boolean hConn = connectsTo(level, pos, material, dirH, side);
			boolean vConn = connectsTo(level, pos, material, dirV, side);
			boolean dConn = connectsToDiagonal(level, pos, material, dirH, dirV, side);

			int tile;
			if (!hConn && !vConn) {
				tile = 0;
			} else if (!hConn && vConn) {
				tile = 2;
			} else if (hConn && !vConn) {
				tile = 3;
			} else if (hConn && vConn && !dConn) {
				tile = 4;
			} else {
				tile = 1;
			}

			float uLocal0 = isRight ? 0.5f : 0.0f;
			float uLocal1 = isRight ? 1.0f : 0.5f;
			float vLocal0 = isBottom ? 0.5f : 0.0f;
			float vLocal1 = isBottom ? 1.0f : 0.5f;

			float spriteU0 = (tile * 0.2f) + (isRight ? 0.1f : 0.0f);
			float spriteU1 = spriteU0 + 0.1f;
			float spriteV0 = isBottom ? 0.5f : 0.0f;
			float spriteV1 = spriteV0 + 0.5f;

			float atlasU0 = sprite.getU(spriteU0);
			float atlasU1 = sprite.getU(spriteU1);
			float atlasV0 = sprite.getV(spriteV0);
			float atlasV1 = sprite.getV(spriteV1);

			Vector3f p0 = new Vector3f(origin).add(new Vector3f(uAxis).mul(uLocal0)).add(new Vector3f(vAxis).mul(vLocal0));
			Vector3f p1 = new Vector3f(origin).add(new Vector3f(uAxis).mul(uLocal0)).add(new Vector3f(vAxis).mul(vLocal1));
			Vector3f p2 = new Vector3f(origin).add(new Vector3f(uAxis).mul(uLocal1)).add(new Vector3f(vAxis).mul(vLocal1));
			Vector3f p3 = new Vector3f(origin).add(new Vector3f(uAxis).mul(uLocal1)).add(new Vector3f(vAxis).mul(vLocal0));

			long uv0 = UVPair.pack(atlasU0, atlasV0);
			long uv1 = UVPair.pack(atlasU0, atlasV1);
			long uv2 = UVPair.pack(atlasU1, atlasV1);
			long uv3 = UVPair.pack(atlasU1, atlasV0);

			result.add(new BakedQuad(p0, p1, p2, p3, uv0, uv1, uv2, uv3, side, newInfo, q.bakedNormals(), q.bakedColors()));
		}
	}

	private static void generateCTM4HQuads(
		List<BakedQuad> result, BakedQuad q, Direction side,
		TextureAtlasSprite sprite, BakedQuad.MaterialInfo info,
		BlockGetter level, BlockPos pos, BlockState material
	) {
		Vector3f origin;
		Vector3f uAxis;
		Vector3f vAxis;
		switch (side) {
			case UP -> {
				origin = new Vector3f(0f, 1f, 0f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, 0f, 1f);
			}
			case DOWN -> {
				origin = new Vector3f(0f, 0f, 1f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, 0f, -1f);
			}
			case NORTH -> {
				origin = new Vector3f(1f, 1f, 0f);
				uAxis = new Vector3f(-1f, 0f, 0f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			case SOUTH -> {
				origin = new Vector3f(0f, 1f, 1f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			case WEST -> {
				origin = new Vector3f(0f, 1f, 0f);
				uAxis = new Vector3f(0f, 0f, 1f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			case EAST -> {
				origin = new Vector3f(1f, 1f, 1f);
				uAxis = new Vector3f(0f, 0f, -1f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			default -> {
				origin = new Vector3f(0f, 1f, 0f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, 0f, 1f);
			}
		}

		BakedQuad.MaterialInfo newInfo = createMaterialInfo(sprite, info);
		Direction dirL = getDir(-uAxis.x, -uAxis.y, -uAxis.z);
		Direction dirR = getDir(uAxis.x, uAxis.y, uAxis.z);
		boolean connL = connectsTo(level, pos, material, dirL, side);
		boolean connR = connectsTo(level, pos, material, dirR, side);

		int tile = (!connL && !connR) ? 0 : (!connL && connR) ? 1 : (connL && connR) ? 2 : 3;

		float spriteU0 = tile * 0.25f;
		float spriteU1 = spriteU0 + 0.25f;
		float atlasU0 = sprite.getU(spriteU0);
		float atlasU1 = sprite.getU(spriteU1);
		float atlasV0 = sprite.getV(0.0f);
		float atlasV1 = sprite.getV(1.0f);

		Vector3f p0 = new Vector3f(origin);
		Vector3f p1 = new Vector3f(origin).add(new Vector3f(vAxis));
		Vector3f p2 = new Vector3f(origin).add(new Vector3f(uAxis)).add(new Vector3f(vAxis));
		Vector3f p3 = new Vector3f(origin).add(new Vector3f(uAxis));

		long uv0 = UVPair.pack(atlasU0, atlasV0);
		long uv1 = UVPair.pack(atlasU0, atlasV1);
		long uv2 = UVPair.pack(atlasU1, atlasV1);
		long uv3 = UVPair.pack(atlasU1, atlasV0);

		result.add(new BakedQuad(p0, p1, p2, p3, uv0, uv1, uv2, uv3, side, newInfo, q.bakedNormals(), q.bakedColors()));
	}

	private static void generateCTM4VQuads(
		List<BakedQuad> result, BakedQuad q, Direction side,
		TextureAtlasSprite sprite, BakedQuad.MaterialInfo info,
		BlockGetter level, BlockPos pos, BlockState material
	) {
		Vector3f origin;
		Vector3f uAxis;
		Vector3f vAxis;
		switch (side) {
			case UP -> {
				origin = new Vector3f(0f, 1f, 0f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, 0f, 1f);
			}
			case DOWN -> {
				origin = new Vector3f(0f, 0f, 1f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, 0f, -1f);
			}
			case NORTH -> {
				origin = new Vector3f(1f, 1f, 0f);
				uAxis = new Vector3f(-1f, 0f, 0f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			case SOUTH -> {
				origin = new Vector3f(0f, 1f, 1f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			case WEST -> {
				origin = new Vector3f(0f, 1f, 0f);
				uAxis = new Vector3f(0f, 0f, 1f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			case EAST -> {
				origin = new Vector3f(1f, 1f, 1f);
				uAxis = new Vector3f(0f, 0f, -1f);
				vAxis = new Vector3f(0f, -1f, 0f);
			}
			default -> {
				origin = new Vector3f(0f, 1f, 0f);
				uAxis = new Vector3f(1f, 0f, 0f);
				vAxis = new Vector3f(0f, 0f, 1f);
			}
		}

		BakedQuad.MaterialInfo newInfo = createMaterialInfo(sprite, info);
		Direction dirU = getDir(-vAxis.x, -vAxis.y, -vAxis.z);
		Direction dirD = getDir(vAxis.x, vAxis.y, vAxis.z);
		boolean connU = connectsTo(level, pos, material, dirU, side);
		boolean connD = connectsTo(level, pos, material, dirD, side);

		int tile = (!connU && !connD) ? 0 : (!connU && connD) ? 1 : (connU && connD) ? 2 : 3;

		float spriteV0 = tile * 0.25f;
		float spriteV1 = spriteV0 + 0.25f;
		float atlasU0 = sprite.getU(0.0f);
		float atlasU1 = sprite.getU(1.0f);
		float atlasV0 = sprite.getV(spriteV0);
		float atlasV1 = sprite.getV(spriteV1);

		Vector3f p0 = new Vector3f(origin);
		Vector3f p1 = new Vector3f(origin).add(new Vector3f(vAxis));
		Vector3f p2 = new Vector3f(origin).add(new Vector3f(uAxis)).add(new Vector3f(vAxis));
		Vector3f p3 = new Vector3f(origin).add(new Vector3f(uAxis));

		long uv0 = UVPair.pack(atlasU0, atlasV0);
		long uv1 = UVPair.pack(atlasU0, atlasV1);
		long uv2 = UVPair.pack(atlasU1, atlasV1);
		long uv3 = UVPair.pack(atlasU1, atlasV0);

		result.add(new BakedQuad(p0, p1, p2, p3, uv0, uv1, uv2, uv3, side, newInfo, q.bakedNormals(), q.bakedColors()));
	}
}
