package net.mcreator.minigames.client;

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.mcreator.minigames.MinigamesMod;
import org.joml.Matrix4f;

/**
 * Vanilla (no shader pack) "live sky" support for the Skybox Simulation block.
 *
 * Right after the sky pass finishes (and after any custom sky such as the "space" box is drawn),
 * the main framebuffer contains the real rendered sky. We render Minecraft's actual clouds
 * into the buffer, copy it into a snapshot texture (capturing the complete sky: sun, moon, stars,
 * custom resource packs, AND 3D Minecraft clouds), and restore the main framebuffer so world
 * rendering proceeds untouched.
 */
@EventBusSubscriber(modid = "minigames", value = Dist.CLIENT)
public final class SkyCaptureManager {
	public static final Identifier SNAPSHOT_ID = Identifier.fromNamespaceAndPath("minigames", "dynamic/sky_snapshot");

	public static final RenderPipeline SKY_PORTAL_PIPELINE = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
			.withLocation(Identifier.fromNamespaceAndPath("minigames", "pipeline/sky_portal"))
			.withVertexShader(Identifier.fromNamespaceAndPath("minigames", "core/sky_portal"))
			.withFragmentShader(Identifier.fromNamespaceAndPath("minigames", "core/sky_portal"))
			.withSampler("Sampler0")
			.withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.build();

	private static RenderType skyPortalRenderType;
	private static SnapshotTexture snapshot;
	private static SnapshotTexture skyBackup;
	private static volatile boolean hasCaptured = false;
	private static boolean loggedFirstCapture = false;

	private SkyCaptureManager() {
	}

	@SubscribeEvent
	public static void registerPipelines(RegisterRenderPipelinesEvent event) {
		event.registerPipeline(SKY_PORTAL_PIPELINE);
		MinigamesMod.LOGGER.info("[SkyCaptureManager] Registered sky_portal render pipeline");
	}

	public static RenderType skyPortalRenderType() {
		if (skyPortalRenderType == null) {
			ensureSnapshotRegistered();
			skyPortalRenderType = RenderType.create("minigames_sky_portal",
					RenderSetup.builder(SKY_PORTAL_PIPELINE).withTexture("Sampler0", SNAPSHOT_ID).createRenderSetup());
		}
		return skyPortalRenderType;
	}

	public static void requestCapture() {
		// Kept for API compatibility; capture runs on every AfterSky when shaders are disabled
	}

	/** True when the snapshot holds a valid captured sky. */
	public static boolean hasFreshCapture() {
		return hasCaptured && !IrisCompat.isShaderPackInUse();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onAfterSky(RenderLevelStageEvent.AfterSky event) {
		if (IrisCompat.isShaderPackInUse()) {
			hasCaptured = false;
			return;
		}
		net.minecraft.client.multiplayer.ClientLevel level = Minecraft.getInstance().level;
		if (level == null || level.dimensionType().skybox() == net.minecraft.world.level.dimension.DimensionType.Skybox.NONE) {
			hasCaptured = false;
			return;
		}
		try {
			RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
			GpuTexture source = main.getColorTexture();
			if (source == null) {
				return;
			}
			int width = source.getWidth(0);
			int height = source.getHeight(0);
			if (width <= 0 || height <= 0) {
				return;
			}

			CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

			// 1. Back up current sky colors before rendering clouds
			SnapshotTexture backup = ensureBackup(width, height, source.getFormat());
			encoder.copyTextureToTexture(source, backup.getTexture(), 0, 0, 0, 0, 0, width, height);

			// 2. Render Minecraft's actual clouds directly into the framebuffer
			boolean cloudsRendered = false;
			try {
				Minecraft mc = Minecraft.getInstance();
				CloudStatus cloudStatus = mc.options.getCloudStatus();
				LevelRenderer levelRenderer = event.getLevelRenderer();
				LevelRenderState lrs = event.getLevelRenderState();
				CloudRenderer cloudRenderer = levelRenderer != null ? levelRenderer.getCloudRenderer() : null;

				if (cloudRenderer != null && cloudStatus != CloudStatus.OFF && lrs != null
						&& lrs.cameraRenderState != null && lrs.cameraRenderState.pos != null) {
					int cloudColor = lrs.cloudColor;
					if ((cloudColor >>> 24) > 0) {
						float cloudHeight = lrs.cloudHeight;
						int cloudRange = mc.options.cloudRange().get();
						if (cloudRange <= 0) {
							cloudRange = mc.options.getEffectiveRenderDistance() * 16;
						}
						Vec3 camPos = lrs.cameraRenderState.pos;
						float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
						long gameTime = lrs.gameTime;

						Matrix4f prevMv = new Matrix4f(RenderSystem.getModelViewMatrix());
						if (event.getModelViewMatrix() != null) {
							RenderSystem.getModelViewMatrix().set(event.getModelViewMatrix());
						}
						try {
							cloudRenderer.render(cloudColor, cloudStatus, cloudHeight, cloudRange, camPos, gameTime, partialTick);
							cloudsRendered = true;
						} finally {
							RenderSystem.getModelViewMatrix().set(prevMv);
						}
					}
				}
			} catch (Throwable t) {
				// Don't crash capture if cloud renderer throws; capture base sky
			}

			// 3. Snapshot the complete sky (now including real 3D clouds)
			SnapshotTexture snap = ensureSnapshot(width, height, source.getFormat());
			encoder.copyTextureToTexture(source, snap.getTexture(), 0, 0, 0, 0, 0, width, height);

			// 4. Restore the main render target so the rest of the world frame (terrain, entities) is unaffected
			if (cloudsRendered) {
				encoder.copyTextureToTexture(backup.getTexture(), source, 0, 0, 0, 0, 0, width, height);
				if (main.getDepthTexture() != null) {
					encoder.clearDepthTexture(main.getDepthTexture(), 1.0);
				}
			}

			hasCaptured = true;
			if (!loggedFirstCapture) {
				loggedFirstCapture = true;
				MinigamesMod.LOGGER.info("[SkyCaptureManager] Successfully captured real skybox texture with native clouds ({}x{})", width, height);
			}
		} catch (Throwable t) {
			MinigamesMod.LOGGER.error("[SkyCaptureManager] Error capturing sky texture", t);
			hasCaptured = false;
		}
	}

	private static void ensureSnapshotRegistered() {
		if (snapshot == null) {
			snapshot = new SnapshotTexture();
			Minecraft.getInstance().getTextureManager().register(SNAPSHOT_ID, snapshot);
		}
	}

	private static SnapshotTexture ensureSnapshot(int width, int height, TextureFormat format) {
		ensureSnapshotRegistered();
		snapshot.ensureSize(width, height, format);
		return snapshot;
	}

	private static SnapshotTexture ensureBackup(int width, int height, TextureFormat format) {
		if (skyBackup == null) {
			skyBackup = new SnapshotTexture();
		}
		skyBackup.ensureSize(width, height, format);
		return skyBackup;
	}

	private static final class SnapshotTexture extends AbstractTexture {
		private int width = 1;
		private int height = 1;
		private TextureFormat format = TextureFormat.RGBA8;

		SnapshotTexture() {
			this.sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
			GpuDevice device = RenderSystem.getDevice();
			this.texture = device.createTexture(() -> "minigames sky snapshot init",
					GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_COPY_SRC | GpuTexture.USAGE_TEXTURE_BINDING, TextureFormat.RGBA8, 1, 1, 1, 1);
			this.textureView = device.createTextureView(this.texture);
		}

		void ensureSize(int w, int h, TextureFormat fmt) {
			if (this.texture != null && !this.texture.isClosed() && w == this.width && h == this.height && fmt == this.format) {
				return;
			}
			this.close();
			GpuDevice device = RenderSystem.getDevice();
			this.texture = device.createTexture(() -> "minigames sky snapshot",
					GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_COPY_SRC | GpuTexture.USAGE_TEXTURE_BINDING, fmt, w, h, 1, 1);
			this.textureView = device.createTextureView(this.texture);
			this.width = w;
			this.height = h;
			this.format = fmt;
		}
	}
}
