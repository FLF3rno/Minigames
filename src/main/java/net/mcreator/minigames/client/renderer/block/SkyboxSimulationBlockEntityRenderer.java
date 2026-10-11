package net.mcreator.minigames.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.mcreator.minigames.block.SkyboxSimulationBlock;
import net.mcreator.minigames.block.entity.SkyboxSimulationBlockEntity;
import net.mcreator.minigames.init.MinigamesModBlockEntities;
import net.mcreator.minigames.network.MinigamesModVariables;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

@EventBusSubscriber(Dist.CLIENT)
public class SkyboxSimulationBlockEntityRenderer implements BlockEntityRenderer<SkyboxSimulationBlockEntity, SkyboxSimulationRenderState> {
	private static final Identifier SUN_TEXTURE = Identifier.fromNamespaceAndPath("minigames", "textures/environment/sun.png");
	private static final Identifier MOON_TEXTURE = Identifier.fromNamespaceAndPath("minigames", "textures/environment/moon.png");
	private static final Identifier WHITE_TEXTURE = Identifier.fromNamespaceAndPath("minigames", "textures/block/white.png");

	public static class FaceData {
		public final Direction direction;
		public final float c0x, c0y, c0z;
		public final float c1x, c1y, c1z;
		public final float c2x, c2y, c2z;
		public final float c3x, c3y, c3z;
		public final float uAxisX, uAxisY, uAxisZ;
		public final float vAxisX, vAxisY, vAxisZ;
		public final float normalX, normalY, normalZ;

		public FaceData(Direction direction, Vector3f c0, Vector3f c1, Vector3f c2, Vector3f c3) {
			this.direction = direction;
			this.c0x = c0.x; this.c0y = c0.y; this.c0z = c0.z;
			this.c1x = c1.x; this.c1y = c1.y; this.c1z = c1.z;
			this.c2x = c2.x; this.c2y = c2.y; this.c2z = c2.z;
			this.c3x = c3.x; this.c3y = c3.y; this.c3z = c3.z;

			Vector3f uAxis = new Vector3f(c1).sub(c0);
			Vector3f vAxis = new Vector3f(c3).sub(c0);
			Vector3f normal = new Vector3f(uAxis).cross(vAxis).normalize();

			this.uAxisX = uAxis.x; this.uAxisY = uAxis.y; this.uAxisZ = uAxis.z;
			this.vAxisX = vAxis.x; this.vAxisY = vAxis.y; this.vAxisZ = vAxis.z;
			this.normalX = normal.x; this.normalY = normal.y; this.normalZ = normal.z;
		}
	}

	private static final Map<Direction, FaceData> FACE_DATA = new EnumMap<>(Direction.class);
	private static final FaceData[] FACE_DATA_ARRAY = new FaceData[6];
	private static final Direction[] DIRECTIONS = Direction.values();

	// 8 cube corner offsets
	private static final float[] CORNER_X = {0, 0, 1, 1, 0, 0, 1, 1};
	private static final float[] CORNER_Y = {0, 1, 1, 0, 0, 1, 1, 0};
	private static final float[] CORNER_Z = {0, 0, 0, 0, 1, 1, 1, 1};

	// 4 corner indices for each direction
	private static final int[][] FACE_CORNERS = {
		{4, 0, 3, 7}, // DOWN
		{1, 5, 6, 2}, // UP
		{3, 0, 1, 2}, // NORTH
		{4, 7, 6, 5}, // SOUTH
		{0, 4, 5, 1}, // WEST
		{7, 3, 2, 6}  // EAST
	};

	private static final int[] FACE_CORNERS_MASK = new int[6];

	// Precomputed stars
	private static final float[] STAR_X = new float[120];
	private static final float[] STAR_Y = new float[120];
	private static final float[] STAR_Z = new float[120];
	private static final float[] STAR_TWINKLE = new float[120];

	// Stars binned into 4 spatial quadrants for 4x faster lookup
	private static final int[][] QUADRANT_STARS = new int[4][120];
	private static final int[] QUADRANT_STAR_COUNTS = new int[4];

	// Precomputed sqrt lookup table for atmosphere dy in [0, 1]
	private static final float[] SQRT_LUT = new float[2048];

	// Shared scratch arrays on main render thread to eliminate GC pressure
	private static final int[] SHARED_CORNER_COLORS = new int[8];
	private static final int[] SHARED_NEAR_STAR_INDICES = new int[120];
	private static final float[] SHARED_DISC_QUAD_BUFFER = new float[6 * 20];

	// Fast flat hash cache for integer world coords (wx, wy, wz) -> atmosphere rgb
	private static final int CORNER_CACHE_SIZE = 8192;
	private static final int CORNER_CACHE_MASK = CORNER_CACHE_SIZE - 1;
	private static final long[] CORNER_CACHE_KEY = new long[CORNER_CACHE_SIZE];
	private static final int[] CORNER_CACHE_VAL = new int[CORNER_CACHE_SIZE];

	// Pre-calculated atmosphere static colors
	private static final float DAY_HR = 0.82f, DAY_HG = 0.90f, DAY_HB = 1.00f;
	private static final float DAY_ZR = 0.40f, DAY_ZG = 0.65f, DAY_ZB = 1.00f;
	private static final float DAY_GR = 0.45f, DAY_GG = 0.55f, DAY_GB = 0.65f;

	private static final float SUNSET_HR = 1.00f, SUNSET_HG = 0.48f, SUNSET_HB = 0.18f;
	private static final float SUNSET_ZR = 0.18f, SUNSET_ZG = 0.12f, SUNSET_ZB = 0.35f;
	private static final float SUNSET_GR = 0.10f, SUNSET_GG = 0.08f, SUNSET_GB = 0.14f;

	private static final float NIGHT_HR = 0.04f, NIGHT_HG = 0.07f, NIGHT_HB = 0.14f;
	private static final float NIGHT_ZR = 0.008f, NIGHT_ZG = 0.015f, NIGHT_ZB = 0.035f;
	private static final float NIGHT_GR = 0.005f, NIGHT_GG = 0.005f, NIGHT_GB = 0.010f;

	// Precomputed static celestial directions
	private static final float DAY_SUN_X, DAY_SUN_Y, DAY_SUN_Z;
	private static final float SUNSET_SUN_X, SUNSET_SUN_Y, SUNSET_SUN_Z;
	private static final float NIGHT_SUN_X, NIGHT_SUN_Y, NIGHT_SUN_Z;

	// Per-frame environmental cache
	private static java.lang.ref.WeakReference<Level> lastLevelRef = new java.lang.ref.WeakReference<>(null);
	private static float lastPartialTick = -1.0f;
	private static long lastGameTime = -1L;
	private static float cachedCelestialAngle = 0.0f;
	private static float cachedGameTime = 0.0f;
	private static float cachedRainLevel = 0.0f;
	private static boolean cachedIsSpaceSky = false;
	private static final int CYAN_COLOR = 0x98DDF9;
	private static boolean cachedDisableSkybox = false;
	private static float lastTwinkleGameTime = -999.0f;

	// =========================================================================
	// GLOBAL ROOM BATCHING BUFFERS
	// =========================================================================
	private static int atmoCapacity = 4096;
	private static float[] atmoPos = new float[atmoCapacity * 12];
	private static int[] atmoColors = new int[atmoCapacity * 4];
	private static int atmoQuadCount = 0;
	private static boolean atmoSubmitted = false;
	private static double atmoOriginX, atmoOriginY, atmoOriginZ;

	private static int starCapacity = 1024;
	private static float[] starPos = new float[starCapacity * 12];
	private static int[] starAlphas = new int[starCapacity];
	private static int starQuadCount = 0;
	private static boolean starSubmitted = false;
	private static double starOriginX, starOriginY, starOriginZ;

	private static int sunCapacity = 128;
	private static float[] sunVertexData = new float[sunCapacity * 20];
	private static int[] sunColors = new int[sunCapacity * 4];
	private static int sunQuadCount = 0;
	private static boolean sunSubmitted = false;
	private static double sunOriginX, sunOriginY, sunOriginZ;

	private static int moonCapacity = 128;
	private static float[] moonVertexData = new float[moonCapacity * 20];
	private static int[] moonColors = new int[moonCapacity * 4];
	private static int moonQuadCount = 0;
	private static boolean moonSubmitted = false;
	private static double moonOriginX, moonOriginY, moonOriginZ;

	private static int portalCapacity = 4096;
	private static float[] portalPos = new float[portalCapacity * 12];
	private static int portalQuadCount = 0;
	private static boolean portalSubmitted = false;
	private static double portalOriginX, portalOriginY, portalOriginZ;

	private static int gatewayCapacity = 1024;
	private static float[] gatewayPos = new float[gatewayCapacity * 12];
	private static int gatewayQuadCount = 0;
	private static boolean gatewaySubmitted = false;
	private static double gatewayOriginX, gatewayOriginY, gatewayOriginZ;

	static {
		FACE_DATA.put(Direction.DOWN, new FaceData(Direction.DOWN,
			new Vector3f(0, 0, 1), new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(1, 0, 1)));
		FACE_DATA.put(Direction.UP, new FaceData(Direction.UP,
			new Vector3f(0, 1, 0), new Vector3f(0, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 0)));
		FACE_DATA.put(Direction.NORTH, new FaceData(Direction.NORTH,
			new Vector3f(1, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), new Vector3f(1, 1, 0)));
		FACE_DATA.put(Direction.SOUTH, new FaceData(Direction.SOUTH,
			new Vector3f(0, 0, 1), new Vector3f(1, 0, 1), new Vector3f(1, 1, 1), new Vector3f(0, 1, 1)));
		FACE_DATA.put(Direction.WEST, new FaceData(Direction.WEST,
			new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 1), new Vector3f(0, 1, 0)));
		FACE_DATA.put(Direction.EAST, new FaceData(Direction.EAST,
			new Vector3f(1, 0, 1), new Vector3f(1, 0, 0), new Vector3f(1, 1, 0), new Vector3f(1, 1, 1)));

		for (int i = 0; i < 6; i++) {
			FACE_DATA_ARRAY[i] = FACE_DATA.get(DIRECTIONS[i]);
			int mask = 0;
			for (int c : FACE_CORNERS[i]) {
				mask |= (1 << c);
			}
			FACE_CORNERS_MASK[i] = mask;
		}

		for (int i = 0; i < 2048; i++) {
			SQRT_LUT[i] = (float) Math.sqrt(i / 2047.0);
		}

		// Initialize 120 stars
		Random rnd = new Random(10842L);
		for (int i = 0; i < 120; i++) {
			float theta = rnd.nextFloat() * ((float) Math.PI * 2.0f);
			float phi = rnd.nextFloat() * 1.45f + 0.04f;
			float x = (float) (Math.cos(theta) * Math.sin(phi));
			float y = (float) Math.cos(phi);
			float z = (float) (Math.sin(theta) * Math.sin(phi));
			STAR_X[i] = x;
			STAR_Y[i] = y;
			STAR_Z[i] = z;

			// Bin into spatial quadrants with overlap margin for 100% boundary safety
			boolean posX = x >= -0.25f;
			boolean negX = x <= 0.25f;
			boolean posZ = z >= -0.25f;
			boolean negZ = z <= 0.25f;

			if (negX && negZ) QUADRANT_STARS[0][QUADRANT_STAR_COUNTS[0]++] = i;
			if (posX && negZ) QUADRANT_STARS[1][QUADRANT_STAR_COUNTS[1]++] = i;
			if (negX && posZ) QUADRANT_STARS[2][QUADRANT_STAR_COUNTS[2]++] = i;
			if (posX && posZ) QUADRANT_STARS[3][QUADRANT_STAR_COUNTS[3]++] = i;
		}

		// Precompute static celestial directions matching original vectors
		Vector3f dSun = new Vector3f(0.20f, 0.95f, -0.15f).normalize();
		DAY_SUN_X = dSun.x; DAY_SUN_Y = dSun.y; DAY_SUN_Z = dSun.z;

		Vector3f sSun = new Vector3f(-0.98f, 0.07f, -0.18f).normalize();
		SUNSET_SUN_X = sSun.x; SUNSET_SUN_Y = sSun.y; SUNSET_SUN_Z = sSun.z;

		Vector3f nSun = new Vector3f(-0.20f, -0.95f, 0.15f).normalize();
		NIGHT_SUN_X = nSun.x; NIGHT_SUN_Y = nSun.y; NIGHT_SUN_Z = nSun.z;
	}

	private static float[] calcCelestialDir(float celestialAngle) {
		double angle = celestialAngle * (Math.PI * 2.0);
		float sy = (float) Math.cos(angle);
		float sx = (float) -Math.sin(angle);
		float sz = -0.20f * (float) Math.cos(angle);
		float invLen = 1.0f / (float) Math.sqrt(sx * sx + sy * sy + sz * sz);
		return new float[]{sx * invLen, sy * invLen, sz * invLen};
	}

	public SkyboxSimulationBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(MinigamesModBlockEntities.SKYBOX_SIMULATION.get(), SkyboxSimulationBlockEntityRenderer::new);
	}

	@Override
	public int getViewDistance() {
		return 64;
	}

	@Override
	public boolean shouldRenderOffScreen() {
		return false;
	}

	@Override
	public SkyboxSimulationRenderState createRenderState() {
		return new SkyboxSimulationRenderState();
	}

	private static void resetAllBatches() {
		Arrays.fill(CORNER_CACHE_KEY, 0L);
		atmoQuadCount = 0;
		atmoSubmitted = false;
		atmoMergeRun = 0;
		starQuadCount = 0;
		starSubmitted = false;
		sunQuadCount = 0;
		sunSubmitted = false;
		moonQuadCount = 0;
		moonSubmitted = false;
		portalQuadCount = 0;
		portalSubmitted = false;
		portalMergeRun = 0;
		gatewayQuadCount = 0;
		gatewaySubmitted = false;
		gatewayMergeRun = 0;
	}

	@Override
	public void extractRenderState(SkyboxSimulationBlockEntity be, SkyboxSimulationRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
		BlockEntityRenderState.extractBase(be, state, crumbling);
		BlockState blockState = be.getBlockState();
		state.skyType = blockState.getBlock() instanceof SkyboxSimulationBlock ? blockState.getValue(SkyboxSimulationBlock.SKY_TYPE) : SkyboxSimulationBlock.SkyType.END;
		state.blockPos = be.getBlockPos();
		state.cameraPos = cameraPos;
		state.partialTick = partialTick;

		Level level = be.getLevel();
		if (level != null) {
			if (be.cachedFaces == -1) {
				byte mask = 0;
				BlockPos pos = be.getBlockPos();
				for (int i = 0; i < 6; i++) {
					Direction dir = DIRECTIONS[i];
					BlockPos neighborPos = pos.relative(dir);
					BlockState neighborState = level.getBlockState(neighborPos);
					if (!(neighborState.getBlock() instanceof SkyboxSimulationBlock)) {
						if (!neighborState.isSolidRender() || !neighborState.canOcclude() || neighborState.isAir()) {
							mask |= (byte) (1 << i);
						}
					}
				}
				be.cachedFaces = mask;
			}

			state.facesMask = be.cachedFaces;
			if (be.cachedFaces == 0) {
				return;
			}

			long gameTime = level.getGameTime();
			Level prevLevel = lastLevelRef.get();
			if (level != prevLevel || partialTick != lastPartialTick || gameTime != lastGameTime) {
				lastLevelRef = new java.lang.ref.WeakReference<>(level);
				lastPartialTick = partialTick;
				lastGameTime = gameTime;

				if (level.dimensionType().hasFixedTime()) {
					cachedCelestialAngle = ((gameTime + partialTick) % 48000.0f) / 48000.0f;
				} else {
					cachedCelestialAngle = ((Math.floorMod(level.getOverworldClockTime(), 48000L) + partialTick) / 48000.0f);
				}
				cachedGameTime = (float) gameTime + partialTick;
				cachedRainLevel = level.getRainLevel(partialTick);
				try {
					cachedIsSpaceSky = "space".equalsIgnoreCase(MinigamesModVariables.MapVariables.get(level).sky);
				} catch (Throwable t) {
					cachedIsSpaceSky = false;
				}

				cachedDisableSkybox = net.mcreator.minigames.client.gui.options.MinigamesSettingsConfig.getBoolean("disableSkyboxBlock", false);

				// Reset the global batch and corner cache for the new frame
				resetAllBatches();
			}

			state.disableSkybox = cachedDisableSkybox;
			state.isSpaceSky = (state.skyType == SkyboxSimulationBlock.SkyType.DYNAMIC) && cachedIsSpaceSky;
			state.celestialAngle = cachedCelestialAngle;
			state.gameTime = cachedGameTime;
			state.rainLevel = cachedRainLevel;
		} else {
			state.facesMask = 0;
		}
	}

	private static float lastCamXRot = -999.0f;
	private static float lastCamYRot = -999.0f;
	private static float camForwardX = 0.0f;
	private static float camForwardY = 0.0f;
	private static float camForwardZ = 1.0f;

	private static void updateCameraForward(CameraRenderState camera) {
		float xRot = camera.xRot;
		float yRot = camera.yRot;
		if (xRot != lastCamXRot || yRot != lastCamYRot) {
			lastCamXRot = xRot;
			lastCamYRot = yRot;
			float radPitch = (float) Math.toRadians(xRot);
			float radYaw = (float) Math.toRadians(yRot);
			camForwardX = (float) (-Math.sin(radYaw) * Math.cos(radPitch));
			camForwardY = (float) -Math.sin(radPitch);
			camForwardZ = (float) (Math.cos(radYaw) * Math.cos(radPitch));
		}
	}

	@Override
	public void submit(SkyboxSimulationRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.facesMask == 0) return;

		double bx = state.blockPos.getX();
		double by = state.blockPos.getY();
		double bz = state.blockPos.getZ();
		double cx = state.cameraPos.x;
		double cy = state.cameraPos.y;
		double cz = state.cameraPos.z;

		// Fast comparisons for camera-facing visibility
		int visibleMask = 0;
		if (cy < by - 0.001) visibleMask |= (1 << 0); // DOWN
		if (cy > by + 1.001) visibleMask |= (1 << 1); // UP
		if (cz < bz - 0.001) visibleMask |= (1 << 2); // NORTH
		if (cz > bz + 1.001) visibleMask |= (1 << 3); // SOUTH
		if (cx < bx - 0.001) visibleMask |= (1 << 4); // WEST
		if (cx > bx + 1.001) visibleMask |= (1 << 5); // EAST

		int activeFacesMask = (state.facesMask & 0xFF) & visibleMask;
		if (activeFacesMask == 0) return;

		// Safe wide-angle view culling (culls blocks behind camera or >85 degrees off-axis)
		if (camera != null) {
			double fx = (bx + 0.5) - cx;
			double fy = (by + 0.5) - cy;
			double fz = (bz + 0.5) - cz;
			double distSqr = fx * fx + fy * fy + fz * fz;

			if (distSqr >= 9.0) { // Keep everything within 3m safe radius
				updateCameraForward(camera);
				double dot = fx * camForwardX + fy * camForwardY + fz * camForwardZ;
				if (dot <= 0.0 || (dot * dot) < distSqr * 0.0075) {
					return;
				}
			}
		}

		if (state.disableSkybox) {
			batchCyan(poseStack, collector, activeFacesMask, bx, by, bz);
			return;
		}

		if (state.skyType == SkyboxSimulationBlock.SkyType.END || state.isSpaceSky) {
			batchPortal(poseStack, collector, RenderTypes.endPortal(), false, activeFacesMask, bx, by, bz);
		} else if (state.skyType == SkyboxSimulationBlock.SkyType.GATEWAY) {
			batchPortal(poseStack, collector, RenderTypes.endGateway(), true, activeFacesMask, bx, by, bz);
		} else {
			batchAtmosphere(state, poseStack, collector, activeFacesMask, bx, by, bz, cx, cy, cz);
			batchStars(state, poseStack, collector, activeFacesMask, bx, by, bz, cx, cy, cz);
			batchSun(state, poseStack, collector, activeFacesMask, bx, by, bz, cx, cy, cz);
			batchMoon(state, poseStack, collector, activeFacesMask, bx, by, bz, cx, cy, cz);
		}
	}

	private void batchCyan(PoseStack poseStack, SubmitNodeCollector collector, int activeFacesMask, double bx, double by, double bz) {
		if (!atmoSubmitted) {
			atmoSubmitted = true;
			atmoOriginX = bx; atmoOriginY = by; atmoOriginZ = bz;
			collector.submitCustomGeometry(poseStack, RenderTypes.entitySolid(WHITE_TEXTURE), SkyboxSimulationBlockEntityRenderer::renderBatchedAtmosphere);
		}

		double dx = bx - atmoOriginX;
		double dy = by - atmoOriginY;
		double dz = bz - atmoOriginZ;

		for (int i = 0; i < 6; i++) {
			if ((activeFacesMask & (1 << i)) == 0) continue;
			FaceData data = FACE_DATA_ARRAY[i];

			addAtmoQuad(
				(float) (dx + data.c0x), (float) (dy + data.c0y), (float) (dz + data.c0z), CYAN_COLOR,
				(float) (dx + data.c1x), (float) (dy + data.c1y), (float) (dz + data.c1z), CYAN_COLOR,
				(float) (dx + data.c2x), (float) (dy + data.c2y), (float) (dz + data.c2z), CYAN_COLOR,
				(float) (dx + data.c3x), (float) (dy + data.c3y), (float) (dz + data.c3z), CYAN_COLOR
			);
		}
	}

	// =========================================================================
	// BATCHING: End Portal & Gateway
	// =========================================================================

	private void batchPortal(PoseStack poseStack, SubmitNodeCollector collector, RenderType renderType, boolean isGateway,
							 int activeFacesMask, double bx, double by, double bz) {
		if (isGateway) {
			if (!gatewaySubmitted) {
				gatewaySubmitted = true;
				gatewayOriginX = bx; gatewayOriginY = by; gatewayOriginZ = bz;
				collector.submitCustomGeometry(poseStack, renderType, SkyboxSimulationBlockEntityRenderer::renderBatchedGateway);
			}
			double dx = bx - gatewayOriginX;
			double dy = by - gatewayOriginY;
			double dz = bz - gatewayOriginZ;
			for (int i = 0; i < 6; i++) {
				if ((activeFacesMask & (1 << i)) != 0) {
					FaceData data = FACE_DATA_ARRAY[i];
					addGatewayQuad(
						(float) (dx + data.c0x), (float) (dy + data.c0y), (float) (dz + data.c0z),
						(float) (dx + data.c1x), (float) (dy + data.c1y), (float) (dz + data.c1z),
						(float) (dx + data.c2x), (float) (dy + data.c2y), (float) (dz + data.c2z),
						(float) (dx + data.c3x), (float) (dy + data.c3y), (float) (dz + data.c3z)
					);
				}
			}
		} else {
			if (!portalSubmitted) {
				portalSubmitted = true;
				portalOriginX = bx; portalOriginY = by; portalOriginZ = bz;
				collector.submitCustomGeometry(poseStack, renderType, SkyboxSimulationBlockEntityRenderer::renderBatchedPortal);
			}
			double dx = bx - portalOriginX;
			double dy = by - portalOriginY;
			double dz = bz - portalOriginZ;
			for (int i = 0; i < 6; i++) {
				if ((activeFacesMask & (1 << i)) != 0) {
					FaceData data = FACE_DATA_ARRAY[i];
					addPortalQuad(
						(float) (dx + data.c0x), (float) (dy + data.c0y), (float) (dz + data.c0z),
						(float) (dx + data.c1x), (float) (dy + data.c1y), (float) (dz + data.c1z),
						(float) (dx + data.c2x), (float) (dy + data.c2y), (float) (dz + data.c2z),
						(float) (dx + data.c3x), (float) (dy + data.c3y), (float) (dz + data.c3z)
					);
				}
			}
		}
	}

	private static int portalMergeRun = 0;
	private static int gatewayMergeRun = 0;

	private static void addPortalQuad(float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		if (portalQuadCount > 0 && portalMergeRun < 4) {
			int prevPosOff = (portalQuadCount - 1) * 12;
			if (portalPos[prevPosOff + 6] == x1 && portalPos[prevPosOff + 7] == y1 && portalPos[prevPosOff + 8] == z1
				&& portalPos[prevPosOff + 9] == x0 && portalPos[prevPosOff + 10] == y0 && portalPos[prevPosOff + 11] == z0) {
				portalPos[prevPosOff + 6] = x2;
				portalPos[prevPosOff + 7] = y2;
				portalPos[prevPosOff + 8] = z2;
				portalPos[prevPosOff + 9] = x3;
				portalPos[prevPosOff + 10] = y3;
				portalPos[prevPosOff + 11] = z3;
				portalMergeRun++;
				return;
			}
		}

		if (portalQuadCount >= portalCapacity) {
			portalCapacity *= 2;
			portalPos = Arrays.copyOf(portalPos, portalCapacity * 12);
		}
		int off = portalQuadCount * 12;
		portalPos[off] = x0; portalPos[off + 1] = y0; portalPos[off + 2] = z0;
		portalPos[off + 3] = x1; portalPos[off + 4] = y1; portalPos[off + 5] = z1;
		portalPos[off + 6] = x2; portalPos[off + 7] = y2; portalPos[off + 8] = z2;
		portalPos[off + 9] = x3; portalPos[off + 10] = y3; portalPos[off + 11] = z3;
		portalQuadCount++;
		portalMergeRun = 1;
	}

	private static void addGatewayQuad(float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		if (gatewayQuadCount > 0 && gatewayMergeRun < 4) {
			int prevPosOff = (gatewayQuadCount - 1) * 12;
			if (gatewayPos[prevPosOff + 6] == x1 && gatewayPos[prevPosOff + 7] == y1 && gatewayPos[prevPosOff + 8] == z1
				&& gatewayPos[prevPosOff + 9] == x0 && gatewayPos[prevPosOff + 10] == y0 && gatewayPos[prevPosOff + 11] == z0) {
				gatewayPos[prevPosOff + 6] = x2;
				gatewayPos[prevPosOff + 7] = y2;
				gatewayPos[prevPosOff + 8] = z2;
				gatewayPos[prevPosOff + 9] = x3;
				gatewayPos[prevPosOff + 10] = y3;
				gatewayPos[prevPosOff + 11] = z3;
				gatewayMergeRun++;
				return;
			}
		}

		if (gatewayQuadCount >= gatewayCapacity) {
			gatewayCapacity *= 2;
			gatewayPos = Arrays.copyOf(gatewayPos, gatewayCapacity * 12);
		}
		int off = gatewayQuadCount * 12;
		gatewayPos[off] = x0; gatewayPos[off + 1] = y0; gatewayPos[off + 2] = z0;
		gatewayPos[off + 3] = x1; gatewayPos[off + 4] = y1; gatewayPos[off + 5] = z1;
		gatewayPos[off + 6] = x2; gatewayPos[off + 7] = y2; gatewayPos[off + 8] = z2;
		gatewayPos[off + 9] = x3; gatewayPos[off + 10] = y3; gatewayPos[off + 11] = z3;
		gatewayQuadCount++;
		gatewayMergeRun = 1;
	}

	public static void renderBatchedPortal(PoseStack.Pose pose, VertexConsumer consumer) {
		for (int q = 0; q < portalQuadCount; q++) {
			int off = q * 12;
			consumer.addVertex(pose, portalPos[off], portalPos[off + 1], portalPos[off + 2]);
			consumer.addVertex(pose, portalPos[off + 3], portalPos[off + 4], portalPos[off + 5]);
			consumer.addVertex(pose, portalPos[off + 6], portalPos[off + 7], portalPos[off + 8]);
			consumer.addVertex(pose, portalPos[off + 9], portalPos[off + 10], portalPos[off + 11]);
		}
		portalQuadCount = 0;
		portalSubmitted = false;
		portalMergeRun = 0;
	}

	public static void renderBatchedGateway(PoseStack.Pose pose, VertexConsumer consumer) {
		for (int q = 0; q < gatewayQuadCount; q++) {
			int off = q * 12;
			consumer.addVertex(pose, gatewayPos[off], gatewayPos[off + 1], gatewayPos[off + 2]);
			consumer.addVertex(pose, gatewayPos[off + 3], gatewayPos[off + 4], gatewayPos[off + 5]);
			consumer.addVertex(pose, gatewayPos[off + 6], gatewayPos[off + 7], gatewayPos[off + 8]);
			consumer.addVertex(pose, gatewayPos[off + 9], gatewayPos[off + 10], gatewayPos[off + 11]);
		}
		gatewayQuadCount = 0;
		gatewaySubmitted = false;
		gatewayMergeRun = 0;
	}

	// =========================================================================
	// BATCHING: Atmosphere
	// =========================================================================

	private void batchAtmosphere(SkyboxSimulationRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
								 int activeFacesMask, double bx, double by, double bz, double cx, double cy, double cz) {
		SkyboxSimulationBlock.SkyType type = state.skyType;
		float hr, hg, hb, zr, zg, zb, gr, gg, gb;

		if (type == SkyboxSimulationBlock.SkyType.DAY) {
			hr = DAY_HR; hg = DAY_HG; hb = DAY_HB;
			zr = DAY_ZR; zg = DAY_ZG; zb = DAY_ZB;
			gr = DAY_GR; gg = DAY_GG; gb = DAY_GB;
		} else if (type == SkyboxSimulationBlock.SkyType.SUNSET) {
			hr = SUNSET_HR; hg = SUNSET_HG; hb = SUNSET_HB;
			zr = SUNSET_ZR; zg = SUNSET_ZG; zb = SUNSET_ZB;
			gr = SUNSET_GR; gg = SUNSET_GG; gb = SUNSET_GB;
		} else if (type == SkyboxSimulationBlock.SkyType.NIGHT) {
			hr = NIGHT_HR; hg = NIGHT_HG; hb = NIGHT_HB;
			zr = NIGHT_ZR; zg = NIGHT_ZG; zb = NIGHT_ZB;
			gr = NIGHT_GR; gg = NIGHT_GG; gb = NIGHT_GB;
		} else {
			double angle = state.celestialAngle * (Math.PI * 2.0);
			float sunY = (float) Math.cos(angle);
			float dayFactor = Math.clamp((sunY + 0.10f) / 0.28f, 0.0f, 1.0f);
			float sunsetFactor = (1.0f - Math.abs(dayFactor - 0.5f) * 2.0f) * (1.0f - state.rainLevel);

			hr = lerp3(0.04f, 1.00f, 0.82f, dayFactor, sunsetFactor);
			hg = lerp3(0.07f, 0.48f, 0.90f, dayFactor, sunsetFactor);
			hb = lerp3(0.14f, 0.18f, 1.00f, dayFactor, sunsetFactor);
			zr = lerp3(0.008f, 0.18f, 0.40f, dayFactor, sunsetFactor);
			zg = lerp3(0.015f, 0.12f, 0.65f, dayFactor, sunsetFactor);
			zb = lerp3(0.035f, 0.35f, 1.00f, dayFactor, sunsetFactor);
			gr = lerp3(0.005f, 0.10f, 0.45f, dayFactor, sunsetFactor);
			gg = lerp3(0.005f, 0.08f, 0.55f, dayFactor, sunsetFactor);
			gb = lerp3(0.010f, 0.14f, 0.65f, dayFactor, sunsetFactor);
		}

		float rainLerpInv = 1.0f, rainRTerm = 0.0f, rainGTerm = 0.0f, rainBTerm = 0.0f;
		float rainLevel = state.rainLevel;
		if (rainLevel > 0.0f) {
			float rf = rainLevel * 0.7f;
			rainLerpInv = 1.0f - rf;
			rainRTerm = 0.40f * rf;
			rainGTerm = 0.43f * rf;
			rainBTerm = 0.48f * rf;
		}

		// Find unique corners
		int neededCorners = 0;
		for (int i = 0; i < 6; i++) {
			if ((activeFacesMask & (1 << i)) != 0) {
				neededCorners |= FACE_CORNERS_MASK[i];
			}
		}

		int ibx = (int) bx;
		int iby = (int) by;
		int ibz = (int) bz;

		int[] cornerColors = SHARED_CORNER_COLORS;
		for (int c = 0; c < 8; c++) {
			if ((neededCorners & (1 << c)) != 0) {
				cornerColors[c] = getOrComputeCornerRgb(
					ibx + (int) CORNER_X[c], iby + (int) CORNER_Y[c], ibz + (int) CORNER_Z[c],
					cx, cy, cz, hr, hg, hb, zr, zg, zb, gr, gg, gb,
					rainLerpInv, rainRTerm, rainGTerm, rainBTerm);
			}
		}

		// Submit single batched render node for entire room
		if (!atmoSubmitted) {
			atmoSubmitted = true;
			atmoOriginX = bx; atmoOriginY = by; atmoOriginZ = bz;
			collector.submitCustomGeometry(poseStack, RenderTypes.entitySolid(WHITE_TEXTURE), SkyboxSimulationBlockEntityRenderer::renderBatchedAtmosphere);
		}

		double dx = bx - atmoOriginX;
		double dy = by - atmoOriginY;
		double dz = bz - atmoOriginZ;

		for (int i = 0; i < 6; i++) {
			if ((activeFacesMask & (1 << i)) == 0) continue;
			FaceData data = FACE_DATA_ARRAY[i];
			int[] corners = FACE_CORNERS[i];
			int c0 = cornerColors[corners[0]];
			int c1 = cornerColors[corners[1]];
			int c2 = cornerColors[corners[2]];
			int c3 = cornerColors[corners[3]];

			addAtmoQuad(
				(float) (dx + data.c0x), (float) (dy + data.c0y), (float) (dz + data.c0z), c0,
				(float) (dx + data.c1x), (float) (dy + data.c1y), (float) (dz + data.c1z), c1,
				(float) (dx + data.c2x), (float) (dy + data.c2y), (float) (dz + data.c2z), c2,
				(float) (dx + data.c3x), (float) (dy + data.c3y), (float) (dz + data.c3z), c3
			);
		}
	}

	private static int atmoMergeRun = 0;

	private static void addAtmoQuad(float x0, float y0, float z0, int c0,
									float x1, float y1, float z1, int c1,
									float x2, float y2, float z2, int c2,
									float x3, float y3, float z3, int c3) {
		if (atmoQuadCount > 0 && atmoMergeRun < 4) {
			int prevPosOff = (atmoQuadCount - 1) * 12;
			if (atmoPos[prevPosOff + 6] == x1 && atmoPos[prevPosOff + 7] == y1 && atmoPos[prevPosOff + 8] == z1
				&& atmoPos[prevPosOff + 9] == x0 && atmoPos[prevPosOff + 10] == y0 && atmoPos[prevPosOff + 11] == z0) {
				atmoPos[prevPosOff + 6] = x2;
				atmoPos[prevPosOff + 7] = y2;
				atmoPos[prevPosOff + 8] = z2;
				atmoPos[prevPosOff + 9] = x3;
				atmoPos[prevPosOff + 10] = y3;
				atmoPos[prevPosOff + 11] = z3;

				int prevColOff = (atmoQuadCount - 1) * 4;
				atmoColors[prevColOff + 2] = c2;
				atmoColors[prevColOff + 3] = c3;

				atmoMergeRun++;
				return;
			}
		}

		if (atmoQuadCount >= atmoCapacity) {
			atmoCapacity *= 2;
			atmoPos = Arrays.copyOf(atmoPos, atmoCapacity * 12);
			atmoColors = Arrays.copyOf(atmoColors, atmoCapacity * 4);
		}
		int posOff = atmoQuadCount * 12;
		int colOff = atmoQuadCount * 4;

		atmoPos[posOff] = x0; atmoPos[posOff + 1] = y0; atmoPos[posOff + 2] = z0;
		atmoPos[posOff + 3] = x1; atmoPos[posOff + 4] = y1; atmoPos[posOff + 5] = z1;
		atmoPos[posOff + 6] = x2; atmoPos[posOff + 7] = y2; atmoPos[posOff + 8] = z2;
		atmoPos[posOff + 9] = x3; atmoPos[posOff + 10] = y3; atmoPos[posOff + 11] = z3;

		atmoColors[colOff] = c0;
		atmoColors[colOff + 1] = c1;
		atmoColors[colOff + 2] = c2;
		atmoColors[colOff + 3] = c3;

		atmoQuadCount++;
		atmoMergeRun = 1;
	}

	public static void renderBatchedAtmosphere(PoseStack.Pose pose, VertexConsumer consumer) {
		Vector3f norm = pose.normal().transform(new Vector3f(0.0f, 1.0f, 0.0f));
		float nx = norm.x, ny = norm.y, nz = norm.z;

		for (int q = 0; q < atmoQuadCount; q++) {
			int posOff = q * 12;
			int colOff = q * 4;
			int c0 = atmoColors[colOff];
			int c1 = atmoColors[colOff + 1];
			int c2 = atmoColors[colOff + 2];
			int c3 = atmoColors[colOff + 3];

			consumer.addVertex(pose, atmoPos[posOff], atmoPos[posOff + 1], atmoPos[posOff + 2]).setColor((c0 >> 16) & 0xFF, (c0 >> 8) & 0xFF, c0 & 0xFF, 255).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, atmoPos[posOff + 3], atmoPos[posOff + 4], atmoPos[posOff + 5]).setColor((c1 >> 16) & 0xFF, (c1 >> 8) & 0xFF, c1 & 0xFF, 255).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, atmoPos[posOff + 6], atmoPos[posOff + 7], atmoPos[posOff + 8]).setColor((c2 >> 16) & 0xFF, (c2 >> 8) & 0xFF, c2 & 0xFF, 255).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, atmoPos[posOff + 9], atmoPos[posOff + 10], atmoPos[posOff + 11]).setColor((c3 >> 16) & 0xFF, (c3 >> 8) & 0xFF, c3 & 0xFF, 255).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
		}
		atmoQuadCount = 0;
		atmoSubmitted = false;
		atmoMergeRun = 0;
	}

	private static int getOrComputeCornerRgb(int wx, int wy, int wz,
											 double cx, double cy, double cz,
											 float hr, float hg, float hb, float zr, float zg, float zb, float gr, float gg, float gb,
											 float rainLerpInv, float rainRTerm, float rainGTerm, float rainBTerm) {
		long key = 0x8000000000000000L | (((long) wx & 0x3FFFFFFL) << 36) | (((long) (wy & 0xFFF)) << 24) | ((long) wz & 0xFFFFFFL);
		int hash = (int) (key ^ (key >>> 16) ^ (key >>> 32)) & CORNER_CACHE_MASK;
		if (CORNER_CACHE_KEY[hash] == key) {
			return CORNER_CACHE_VAL[hash];
		}

		double rx = (double) wx - cx;
		double ry = (double) wy - cy;
		double rz = (double) wz - cz;
		double len = Math.sqrt(rx * rx + ry * ry + rz * rz);
		if (len < 1e-6) len = 1e-6;
		double dy = ry / len;

		float r, g, b;
		if (dy >= 0.0) {
			int idx = (int) (dy * 2047.0);
			if (idx > 2047) idx = 2047;
			float t = SQRT_LUT[idx];
			r = hr + (zr - hr) * t;
			g = hg + (zg - hg) * t;
			b = hb + (zb - hb) * t;
		} else {
			float t = (float) Math.min(1.0, -dy * 2.2);
			r = hr + (gr - hr) * t;
			g = hg + (gg - hg) * t;
			b = hb + (gb - hb) * t;
		}

		if (rainLerpInv < 1.0f) {
			r = r * rainLerpInv + rainRTerm;
			g = g * rainLerpInv + rainGTerm;
			b = b * rainLerpInv + rainBTerm;
		}

		int ir = Math.clamp((int) (r * 255.0f), 0, 255);
		int ig = Math.clamp((int) (g * 255.0f), 0, 255);
		int ib = Math.clamp((int) (b * 255.0f), 0, 255);
		int rgb = (ir << 16) | (ig << 8) | ib;

		CORNER_CACHE_KEY[hash] = key;
		CORNER_CACHE_VAL[hash] = rgb;
		return rgb;
	}

	private static float lerp3(float nite, float sunset, float day, float dayFactor, float sunsetFactor) {
		float base = nite + (day - nite) * dayFactor;
		return base + (sunset - base) * sunsetFactor;
	}

	// =========================================================================
	// BATCHING: Stars
	// =========================================================================

	private void batchStars(SkyboxSimulationRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
							int activeFacesMask, double bx, double by, double bz, double cx, double cy, double cz) {
		SkyboxSimulationBlock.SkyType type = state.skyType;
		if (type == SkyboxSimulationBlock.SkyType.DAY) return;

		float starAlpha;
		if (type == SkyboxSimulationBlock.SkyType.NIGHT) {
			starAlpha = 1.0f;
		} else if (type == SkyboxSimulationBlock.SkyType.SUNSET) {
			starAlpha = 0.55f;
		} else {
			double angle = state.celestialAngle * (Math.PI * 2.0);
			float sy = (float) Math.cos(angle);
			float sx = (float) -Math.sin(angle);
			float sz = -0.20f * (float) Math.cos(angle);
			float invLen = 1.0f / (float) Math.sqrt(sx * sx + sy * sy + sz * sz);
			float sunY = sy * invLen;
			starAlpha = Math.clamp((-sunY - 0.05f) / 0.25f, 0.0f, 1.0f);
		}
		starAlpha *= (1.0f - state.rainLevel);
		if (starAlpha <= 0.01f) return;

		double fx = (bx + 0.5) - cx;
		double fy = (by + 0.5) - cy;
		double fz = (bz + 0.5) - cz;
		if (fy <= 0.0) return; // All stars are in the upper sky hemisphere (STAR_Y > 0)

		double distSqr = fx * fx + fy * fy + fz * fz;
		if (distSqr < 1e-4) return;
		float invDist = org.joml.Math.invsqrt((float) distSqr);
		float fDirX = (float) fx * invDist;
		float fDirY = (float) fy * invDist;
		float fDirZ = (float) fz * invDist;
		float minDot = Math.max(-1.0f, 1.0f - 0.45f * (invDist * invDist));

		// Only check stars in matching spatial quadrant
		int q = (fDirX >= 0.0f ? 1 : 0) | (fDirZ >= 0.0f ? 2 : 0);
		int qCount = QUADRANT_STAR_COUNTS[q];
		int[] qStars = QUADRANT_STARS[q];

		int nearCount = 0;
		int[] nearIndices = SHARED_NEAR_STAR_INDICES;
		for (int k = 0; k < qCount; k++) {
			int i = qStars[k];
			float dot = STAR_X[i] * fDirX + STAR_Y[i] * fDirY + STAR_Z[i] * fDirZ;
			if (dot >= minDot) {
				nearIndices[nearCount++] = i;
			}
		}
		if (nearCount == 0) return;

		updateTwinkle(state.gameTime);
		final float baseAlpha = starAlpha;

		if (!starSubmitted) {
			starSubmitted = true;
			starOriginX = bx; starOriginY = by; starOriginZ = bz;
			collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucentEmissive(WHITE_TEXTURE), SkyboxSimulationBlockEntityRenderer::renderBatchedStars);
		}

		double dx = bx - starOriginX;
		double dy = by - starOriginY;
		double dz = bz - starOriginZ;

		for (int f = 0; f < 6; f++) {
			if ((activeFacesMask & (1 << f)) == 0) continue;
			FaceData data = FACE_DATA_ARRAY[f];

			double w0x = bx + data.c0x;
			double w0y = by + data.c0y;
			double w0z = bz + data.c0z;
			double dotOriginCam = (w0x - cx) * data.normalX + (w0y - cy) * data.normalY + (w0z - cz) * data.normalZ;

			for (int k = 0; k < nearCount; k++) {
				int i = nearIndices[k];
				float starX = STAR_X[i];
				float starY = STAR_Y[i];
				float starZ = STAR_Z[i];

				float denom = starX * data.normalX + starY * data.normalY + starZ * data.normalZ;
				if (denom >= -0.0001f) continue;

				double t = dotOriginCam / denom;
				if (t <= 0.01) continue;

				double deltaX = (cx + t * starX) - w0x;
				double deltaY = (cy + t * starY) - w0y;
				double deltaZ = (cz + t * starZ) - w0z;

				float u = (float) (deltaX * data.uAxisX + deltaY * data.uAxisY + deltaZ * data.uAxisZ);
				if (u <= 0.0f || u >= 1.0f) continue;
				float v = (float) (deltaX * data.vAxisX + deltaY * data.vAxisY + deltaZ * data.vAxisZ);
				if (v <= 0.0f || v >= 1.0f) continue;

				float s = Math.max(0.016f, (float) t * 0.0045f);
				int a = Math.clamp((int) (baseAlpha * STAR_TWINKLE[i] * 255.0f), 0, 255);

				float u0 = u - s, u1 = u + s;
				float v0 = v - s, v1 = v + s;
				float noff = 0.0040f;

				float normXOff = data.normalX * noff;
				float normYOff = data.normalY * noff;
				float normZOff = data.normalZ * noff;

				float c0x = data.c0x + normXOff;
				float c0y = data.c0y + normYOff;
				float c0z = data.c0z + normZOff;

				float p0x = (float) (dx + c0x + data.uAxisX * u0 + data.vAxisX * v0);
				float p0y = (float) (dy + c0y + data.uAxisY * u0 + data.vAxisY * v0);
				float p0z = (float) (dz + c0z + data.uAxisZ * u0 + data.vAxisZ * v0);

				float p1x = (float) (dx + c0x + data.uAxisX * u1 + data.vAxisX * v0);
				float p1y = (float) (dy + c0y + data.uAxisY * u1 + data.vAxisY * v0);
				float p1z = (float) (dz + c0z + data.uAxisZ * u1 + data.vAxisZ * v0);

				float p2x = (float) (dx + c0x + data.uAxisX * u1 + data.vAxisX * v1);
				float p2y = (float) (dy + c0y + data.uAxisY * u1 + data.vAxisY * v1);
				float p2z = (float) (dz + c0z + data.uAxisZ * u1 + data.vAxisZ * v1);

				float p3x = (float) (dx + c0x + data.uAxisX * u0 + data.vAxisX * v1);
				float p3y = (float) (dy + c0y + data.uAxisY * u0 + data.vAxisY * v1);
				float p3z = (float) (dz + c0z + data.uAxisZ * u0 + data.vAxisZ * v1);

				addStarQuad(p0x, p0y, p0z, p1x, p1y, p1z, p2x, p2y, p2z, p3x, p3y, p3z, a);
			}
		}
	}

	private static void addStarQuad(float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, int a) {
		if (starQuadCount >= starCapacity) {
			starCapacity *= 2;
			starPos = Arrays.copyOf(starPos, starCapacity * 12);
			starAlphas = Arrays.copyOf(starAlphas, starCapacity);
		}
		int off = starQuadCount * 12;
		starPos[off] = x0; starPos[off + 1] = y0; starPos[off + 2] = z0;
		starPos[off + 3] = x1; starPos[off + 4] = y1; starPos[off + 5] = z1;
		starPos[off + 6] = x2; starPos[off + 7] = y2; starPos[off + 8] = z2;
		starPos[off + 9] = x3; starPos[off + 10] = y3; starPos[off + 11] = z3;
		starAlphas[starQuadCount] = a;
		starQuadCount++;
	}

	public static void renderBatchedStars(PoseStack.Pose pose, VertexConsumer consumer) {
		Vector3f norm = pose.normal().transform(new Vector3f(0.0f, 1.0f, 0.0f));
		float nx = norm.x, ny = norm.y, nz = norm.z;

		for (int q = 0; q < starQuadCount; q++) {
			int off = q * 12;
			int a = starAlphas[q];
			consumer.addVertex(pose, starPos[off], starPos[off + 1], starPos[off + 2]).setColor(255, 255, 255, a).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, starPos[off + 3], starPos[off + 4], starPos[off + 5]).setColor(255, 255, 255, a).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, starPos[off + 6], starPos[off + 7], starPos[off + 8]).setColor(255, 255, 255, a).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, starPos[off + 9], starPos[off + 10], starPos[off + 11]).setColor(255, 255, 255, a).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
		}
		starQuadCount = 0;
		starSubmitted = false;
	}

	private static void updateTwinkle(float gameTime) {
		if (gameTime != lastTwinkleGameTime) {
			lastTwinkleGameTime = gameTime;
			for (int i = 0; i < 120; i++) {
				STAR_TWINKLE[i] = 0.70f + 0.30f * (float) Math.sin(gameTime * 0.14f + i * 19.3f);
			}
		}
	}

	// =========================================================================
	// BATCHING: Sun & Moon
	// =========================================================================

	private void batchSun(SkyboxSimulationRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
						  int activeFacesMask, double bx, double by, double bz, double cx, double cy, double cz) {
		SkyboxSimulationBlock.SkyType type = state.skyType;
		if (type == SkyboxSimulationBlock.SkyType.NIGHT) return;

		float sunDirX, sunDirY, sunDirZ;
		if (type == SkyboxSimulationBlock.SkyType.DAY) {
			sunDirX = DAY_SUN_X; sunDirY = DAY_SUN_Y; sunDirZ = DAY_SUN_Z;
		} else if (type == SkyboxSimulationBlock.SkyType.SUNSET) {
			sunDirX = SUNSET_SUN_X; sunDirY = SUNSET_SUN_Y; sunDirZ = SUNSET_SUN_Z;
		} else {
			double angle = state.celestialAngle * (Math.PI * 2.0);
			float sy = (float) Math.cos(angle);
			float sx = (float) -Math.sin(angle);
			float sz = -0.20f * (float) Math.cos(angle);
			float invLen = 1.0f / (float) Math.sqrt(sx * sx + sy * sy + sz * sz);
			sunDirX = sx * invLen; sunDirY = sy * invLen; sunDirZ = sz * invLen;
		}

		if (sunDirY < -0.15f) return;

		float alpha = type == SkyboxSimulationBlock.SkyType.DAY || type == SkyboxSimulationBlock.SkyType.SUNSET ?
			1.0f : Math.clamp((sunDirY + 0.15f) / 0.25f, 0.0f, 1.0f);
		alpha *= (1.0f - state.rainLevel * 0.85f);
		if (alpha <= 0.01f) return;

		double fx = (bx + 0.5) - cx;
		double fy = (by + 0.5) - cy;
		double fz = (bz + 0.5) - cz;
		double distSqr = fx * fx + fy * fy + fz * fz;
		if (distSqr < 1e-4) return;

		// Fast bounding cone rejection (skips 95% of room blocks without clipping celestial disc)
		if (distSqr >= 4.0) {
			double dotEst = fx * sunDirX + fy * sunDirY + fz * sunDirZ;
			if (dotEst <= 0.0 || (dotEst * dotEst) < distSqr * 0.70) return;
		}

		int quadCount = prepareCelestialDisc(sunDirX, sunDirY, sunDirZ, 0.115f, 0.0080f,
			activeFacesMask, bx, by, bz, cx, cy, cz);
		if (quadCount == 0) return;

		int r = 255, g, b;
		if (type == SkyboxSimulationBlock.SkyType.DAY) {
			g = 255;
			b = 255;
		} else if (type == SkyboxSimulationBlock.SkyType.SUNSET) {
			g = 180;
			b = 100;
		} else {
			if (sunDirY > 0.35f) {
				g = 255;
				b = 255;
			} else if (sunDirY > 0.15f) {
				float f = (sunDirY - 0.15f) / 0.20f;
				g = (int) (225 + 30 * f);
				b = (int) (160 + 95 * f);
			} else {
				float f = Math.clamp((sunDirY + 0.05f) / 0.20f, 0.0f, 1.0f);
				g = (int) (140 + 85 * f);
				b = (int) (60 + 100 * f);
			}
		}

		int a = (int) (alpha * 255.0f);

		if (!sunSubmitted) {
			sunSubmitted = true;
			sunOriginX = bx; sunOriginY = by; sunOriginZ = bz;
			collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucentEmissive(SUN_TEXTURE), SkyboxSimulationBlockEntityRenderer::renderBatchedSun);
		}

		double dx = bx - sunOriginX;
		double dy = by - sunOriginY;
		double dz = bz - sunOriginZ;

		float[] buf = SHARED_DISC_QUAD_BUFFER;
		for (int q = 0; q < quadCount; q++) {
			int offset = q * 20;
			addSunQuad(buf, offset, dx, dy, dz, r, g, b, a);
		}
	}

	private static void addSunQuad(float[] buf, int offset, double dx, double dy, double dz, int r, int g, int b, int a) {
		if (sunQuadCount >= sunCapacity) {
			sunCapacity *= 2;
			sunVertexData = Arrays.copyOf(sunVertexData, sunCapacity * 20);
			sunColors = Arrays.copyOf(sunColors, sunCapacity * 4);
		}
		int destOff = sunQuadCount * 20;
		int colOff = sunQuadCount * 4;

		sunVertexData[destOff] = (float) (dx + buf[offset]);
		sunVertexData[destOff + 1] = (float) (dy + buf[offset + 1]);
		sunVertexData[destOff + 2] = (float) (dz + buf[offset + 2]);
		sunVertexData[destOff + 3] = buf[offset + 3];
		sunVertexData[destOff + 4] = buf[offset + 4];

		sunVertexData[destOff + 5] = (float) (dx + buf[offset + 5]);
		sunVertexData[destOff + 6] = (float) (dy + buf[offset + 6]);
		sunVertexData[destOff + 7] = (float) (dz + buf[offset + 7]);
		sunVertexData[destOff + 8] = buf[offset + 8];
		sunVertexData[destOff + 9] = buf[offset + 9];

		sunVertexData[destOff + 10] = (float) (dx + buf[offset + 10]);
		sunVertexData[destOff + 11] = (float) (dy + buf[offset + 11]);
		sunVertexData[destOff + 12] = (float) (dz + buf[offset + 12]);
		sunVertexData[destOff + 13] = buf[offset + 13];
		sunVertexData[destOff + 14] = buf[offset + 14];

		sunVertexData[destOff + 15] = (float) (dx + buf[offset + 15]);
		sunVertexData[destOff + 16] = (float) (dy + buf[offset + 16]);
		sunVertexData[destOff + 17] = (float) (dz + buf[offset + 17]);
		sunVertexData[destOff + 18] = buf[offset + 18];
		sunVertexData[destOff + 19] = buf[offset + 19];

		sunColors[colOff] = r; sunColors[colOff + 1] = g; sunColors[colOff + 2] = b; sunColors[colOff + 3] = a;
		sunQuadCount++;
	}

	public static void renderBatchedSun(PoseStack.Pose pose, VertexConsumer consumer) {
		Vector3f norm = pose.normal().transform(new Vector3f(0.0f, 1.0f, 0.0f));
		float nx = norm.x, ny = norm.y, nz = norm.z;

		for (int q = 0; q < sunQuadCount; q++) {
			int destOff = q * 20;
			int colOff = q * 4;
			int r = sunColors[colOff], g = sunColors[colOff + 1], b = sunColors[colOff + 2], a = sunColors[colOff + 3];

			consumer.addVertex(pose, sunVertexData[destOff], sunVertexData[destOff + 1], sunVertexData[destOff + 2]).setColor(r, g, b, a).setUv(sunVertexData[destOff + 3], sunVertexData[destOff + 4]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, sunVertexData[destOff + 5], sunVertexData[destOff + 6], sunVertexData[destOff + 7]).setColor(r, g, b, a).setUv(sunVertexData[destOff + 8], sunVertexData[destOff + 9]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, sunVertexData[destOff + 10], sunVertexData[destOff + 11], sunVertexData[destOff + 12]).setColor(r, g, b, a).setUv(sunVertexData[destOff + 13], sunVertexData[destOff + 14]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, sunVertexData[destOff + 15], sunVertexData[destOff + 16], sunVertexData[destOff + 17]).setColor(r, g, b, a).setUv(sunVertexData[destOff + 18], sunVertexData[destOff + 19]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
		}
		sunQuadCount = 0;
		sunSubmitted = false;
	}

	private void batchMoon(SkyboxSimulationRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
						   int activeFacesMask, double bx, double by, double bz, double cx, double cy, double cz) {
		SkyboxSimulationBlock.SkyType type = state.skyType;
		if (type == SkyboxSimulationBlock.SkyType.DAY) return;

		float moonDirX, moonDirY, moonDirZ;
		if (type == SkyboxSimulationBlock.SkyType.NIGHT) {
			moonDirX = -NIGHT_SUN_X; moonDirY = -NIGHT_SUN_Y; moonDirZ = -NIGHT_SUN_Z;
		} else if (type == SkyboxSimulationBlock.SkyType.SUNSET) {
			moonDirX = -SUNSET_SUN_X; moonDirY = -SUNSET_SUN_Y; moonDirZ = -SUNSET_SUN_Z;
		} else {
			double angle = state.celestialAngle * (Math.PI * 2.0);
			float sy = (float) Math.cos(angle);
			float sx = (float) -Math.sin(angle);
			float sz = -0.20f * (float) Math.cos(angle);
			float invLen = 1.0f / (float) Math.sqrt(sx * sx + sy * sy + sz * sz);
			moonDirX = -sx * invLen; moonDirY = -sy * invLen; moonDirZ = -sz * invLen;
		}

		if (moonDirY < -0.15f) return;

		float alpha = type == SkyboxSimulationBlock.SkyType.NIGHT ? 1.0f :
			(type == SkyboxSimulationBlock.SkyType.SUNSET ? 0.65f : Math.clamp((moonDirY + 0.15f) / 0.25f, 0.0f, 1.0f));
		alpha *= (1.0f - state.rainLevel * 0.85f);
		if (alpha <= 0.01f) return;

		double fx = (bx + 0.5) - cx;
		double fy = (by + 0.5) - cy;
		double fz = (bz + 0.5) - cz;
		double distSqr = fx * fx + fy * fy + fz * fz;
		if (distSqr < 1e-4) return;

		// Fast bounding cone rejection
		if (distSqr >= 4.0) {
			double dotEst = fx * moonDirX + fy * moonDirY + fz * moonDirZ;
			if (dotEst <= 0.0 || (dotEst * dotEst) < distSqr * 0.70) return;
		}

		int quadCount = prepareCelestialDisc(moonDirX, moonDirY, moonDirZ, 0.095f, 0.0080f,
			activeFacesMask, bx, by, bz, cx, cy, cz);
		if (quadCount == 0) return;

		int a = (int) (alpha * 255.0f);

		if (!moonSubmitted) {
			moonSubmitted = true;
			moonOriginX = bx; moonOriginY = by; moonOriginZ = bz;
			collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucentEmissive(MOON_TEXTURE), SkyboxSimulationBlockEntityRenderer::renderBatchedMoon);
		}

		double dx = bx - moonOriginX;
		double dy = by - moonOriginY;
		double dz = bz - moonOriginZ;

		float[] buf = SHARED_DISC_QUAD_BUFFER;
		for (int q = 0; q < quadCount; q++) {
			int offset = q * 20;
			addMoonQuad(buf, offset, dx, dy, dz, 255, 255, 255, a);
		}
	}

	private static void addMoonQuad(float[] buf, int offset, double dx, double dy, double dz, int r, int g, int b, int a) {
		if (moonQuadCount >= moonCapacity) {
			moonCapacity *= 2;
			moonVertexData = Arrays.copyOf(moonVertexData, moonCapacity * 20);
			moonColors = Arrays.copyOf(moonColors, moonCapacity * 4);
		}
		int destOff = moonQuadCount * 20;
		int colOff = moonQuadCount * 4;

		moonVertexData[destOff] = (float) (dx + buf[offset]);
		moonVertexData[destOff + 1] = (float) (dy + buf[offset + 1]);
		moonVertexData[destOff + 2] = (float) (dz + buf[offset + 2]);
		moonVertexData[destOff + 3] = buf[offset + 3];
		moonVertexData[destOff + 4] = buf[offset + 4];

		moonVertexData[destOff + 5] = (float) (dx + buf[offset + 5]);
		moonVertexData[destOff + 6] = (float) (dy + buf[offset + 6]);
		moonVertexData[destOff + 7] = (float) (dz + buf[offset + 7]);
		moonVertexData[destOff + 8] = buf[offset + 8];
		moonVertexData[destOff + 9] = buf[offset + 9];

		moonVertexData[destOff + 10] = (float) (dx + buf[offset + 10]);
		moonVertexData[destOff + 11] = (float) (dy + buf[offset + 11]);
		moonVertexData[destOff + 12] = (float) (dz + buf[offset + 12]);
		moonVertexData[destOff + 13] = buf[offset + 13];
		moonVertexData[destOff + 14] = buf[offset + 14];

		moonVertexData[destOff + 15] = (float) (dx + buf[offset + 15]);
		moonVertexData[destOff + 16] = (float) (dy + buf[offset + 16]);
		moonVertexData[destOff + 17] = (float) (dz + buf[offset + 17]);
		moonVertexData[destOff + 18] = buf[offset + 18];
		moonVertexData[destOff + 19] = buf[offset + 19];

		moonColors[colOff] = r; moonColors[colOff + 1] = g; moonColors[colOff + 2] = b; moonColors[colOff + 3] = a;
		moonQuadCount++;
	}

	public static void renderBatchedMoon(PoseStack.Pose pose, VertexConsumer consumer) {
		Vector3f norm = pose.normal().transform(new Vector3f(0.0f, 1.0f, 0.0f));
		float nx = norm.x, ny = norm.y, nz = norm.z;

		for (int q = 0; q < moonQuadCount; q++) {
			int destOff = q * 20;
			int colOff = q * 4;
			int r = moonColors[colOff], g = moonColors[colOff + 1], b = moonColors[colOff + 2], a = moonColors[colOff + 3];

			consumer.addVertex(pose, moonVertexData[destOff], moonVertexData[destOff + 1], moonVertexData[destOff + 2]).setColor(r, g, b, a).setUv(moonVertexData[destOff + 3], moonVertexData[destOff + 4]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, moonVertexData[destOff + 5], moonVertexData[destOff + 6], moonVertexData[destOff + 7]).setColor(r, g, b, a).setUv(moonVertexData[destOff + 8], moonVertexData[destOff + 9]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, moonVertexData[destOff + 10], moonVertexData[destOff + 11], moonVertexData[destOff + 12]).setColor(r, g, b, a).setUv(moonVertexData[destOff + 13], moonVertexData[destOff + 14]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
			consumer.addVertex(pose, moonVertexData[destOff + 15], moonVertexData[destOff + 16], moonVertexData[destOff + 17]).setColor(r, g, b, a).setUv(moonVertexData[destOff + 18], moonVertexData[destOff + 19]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0x00F000F0).setNormal(nx, ny, nz);
		}
		moonQuadCount = 0;
		moonSubmitted = false;
	}

	// =========================================================================
	// HELPER: Celestial Disc Quad Generator
	// =========================================================================

	private static int prepareCelestialDisc(float dirX, float dirY, float dirZ,
											float angularRadius, float normalOffset,
											int activeFacesMask, double bx, double by, double bz,
											double cx, double cy, double cz) {
		float[] buf = SHARED_DISC_QUAD_BUFFER;
		int quadCount = 0;

		for (int f = 0; f < 6; f++) {
			if ((activeFacesMask & (1 << f)) == 0) continue;
			FaceData data = FACE_DATA_ARRAY[f];

			double w0x = bx + data.c0x;
			double w0y = by + data.c0y;
			double w0z = bz + data.c0z;

			double dotOriginCam = (w0x - cx) * data.normalX + (w0y - cy) * data.normalY + (w0z - cz) * data.normalZ;
			float denom = dirX * data.normalX + dirY * data.normalY + dirZ * data.normalZ;

			if (denom >= -0.0001f) continue;

			double t = dotOriginCam / denom;
			if (t <= 0.01) continue;

			double hitX = cx + t * dirX;
			double hitY = cy + t * dirY;
			double hitZ = cz + t * dirZ;

			double deltaX = hitX - w0x;
			double deltaY = hitY - w0y;
			double deltaZ = hitZ - w0z;

			float uMid = (float) (deltaX * data.uAxisX + deltaY * data.uAxisY + deltaZ * data.uAxisZ);
			float vMid = (float) (deltaX * data.vAxisX + deltaY * data.vAxisY + deltaZ * data.vAxisZ);

			float radius = Math.max(0.18f, (float) t * angularRadius);
			if (uMid + radius <= 0.0f || uMid - radius >= 1.0f || vMid + radius <= 0.0f || vMid - radius >= 1.0f) {
				continue;
			}

			float u0 = Math.max(0.0f, uMid - radius);
			float u1 = Math.min(1.0f, uMid + radius);
			float v0 = Math.max(0.0f, vMid - radius);
			float v1 = Math.min(1.0f, vMid + radius);

			float diameter = 2.0f * radius;
			float invDiam = 1.0f / diameter;
			float uMidMinusR = uMid - radius;
			float vMidMinusR = vMid - radius;
			float texU0 = (u0 - uMidMinusR) * invDiam;
			float texU1 = (u1 - uMidMinusR) * invDiam;
			float texV0 = 1.0f - (v1 - vMidMinusR) * invDiam;
			float texV1 = 1.0f - (v0 - vMidMinusR) * invDiam;

			float normXOff = data.normalX * normalOffset;
			float normYOff = data.normalY * normalOffset;
			float normZOff = data.normalZ * normalOffset;

			float c0x = data.c0x + normXOff;
			float c0y = data.c0y + normYOff;
			float c0z = data.c0z + normZOff;

			// p0
			float p0x = c0x + data.uAxisX * u0 + data.vAxisX * v0;
			float p0y = c0y + data.uAxisY * u0 + data.vAxisY * v0;
			float p0z = c0z + data.uAxisZ * u0 + data.vAxisZ * v0;

			// p1
			float p1x = c0x + data.uAxisX * u1 + data.vAxisX * v0;
			float p1y = c0y + data.uAxisY * u1 + data.vAxisY * v0;
			float p1z = c0z + data.uAxisZ * u1 + data.vAxisZ * v0;

			// p2
			float p2x = c0x + data.uAxisX * u1 + data.vAxisX * v1;
			float p2y = c0y + data.uAxisY * u1 + data.vAxisY * v1;
			float p2z = c0z + data.uAxisZ * u1 + data.vAxisZ * v1;

			// p3
			float p3x = c0x + data.uAxisX * u0 + data.vAxisX * v1;
			float p3y = c0y + data.uAxisY * u0 + data.vAxisY * v1;
			float p3z = c0z + data.uAxisZ * u0 + data.vAxisZ * v1;

			int offset = quadCount * 20;
			buf[offset] = p0x; buf[offset + 1] = p0y; buf[offset + 2] = p0z; buf[offset + 3] = texU0; buf[offset + 4] = texV1;
			buf[offset + 5] = p1x; buf[offset + 6] = p1y; buf[offset + 7] = p1z; buf[offset + 8] = texU1; buf[offset + 9] = texV1;
			buf[offset + 10] = p2x; buf[offset + 11] = p2y; buf[offset + 12] = p2z; buf[offset + 13] = texU1; buf[offset + 14] = texV0;
			buf[offset + 15] = p3x; buf[offset + 16] = p3y; buf[offset + 17] = p3z; buf[offset + 18] = texU0; buf[offset + 19] = texV0;

			quadCount++;
		}
		return quadCount;
	}
}
