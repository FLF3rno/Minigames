package net.mcreator.minigames;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AnimationManager {
	private static AnimationManager CURRENT_INSTANCE;

	private final int totalTicks;
	private final float speed;
	private float accumulatedTicks = 0.0f;
	private int currentTick = 0;
	private boolean finished = false;
	private final List<AnimationAction> actions = new ArrayList<>();
	private final List<SoundCue> soundCues = new ArrayList<>();

	private static class SoundCue {
		final int tick;
		final SoundEvent sound;
		final float volume;
		final float pitch;
		boolean played = false;

		SoundCue(int tick, SoundEvent sound, float volume, float pitch) {
			this.tick = tick;
			this.sound = sound;
			this.volume = volume;
			this.pitch = pitch;
		}
	}

	public AnimationManager(int lengthInTicks, float speed) {
		this.totalTicks = lengthInTicks;
		this.speed = speed;

		CURRENT_INSTANCE = this;
	}

	public static AnimationManager getInstance() {
		return CURRENT_INSTANCE;
	}

	public void tick() {
		if (finished) return;
		accumulatedTicks += speed;
		currentTick = (int) accumulatedTicks;
		if (currentTick >= totalTicks) {
			finished = true;
		}
		for (SoundCue cue : soundCues) {
			if (!cue.played && currentTick >= cue.tick) {
				cue.played = true;
				Minecraft.getInstance().getSoundManager().play(
						SimpleSoundInstance.forUI(cue.sound, cue.pitch, cue.volume)
				);
			}
		}
	}

	public void render(GuiGraphicsExtractor graphics, int screenWidth, int screenHeight) {
		if (finished) return;

		actions.sort(Comparator.comparingInt(AnimationAction::getLayer));

		for (AnimationAction action : actions) {
			if (action.isAlive(currentTick)) {
				action.render(graphics, currentTick, screenWidth, screenHeight);
			}
		}
	}

	public boolean isFinished() {
		return finished;
	}

	@FunctionalInterface
	public interface GuiRenderable {
		void render(GuiGraphicsExtractor graphics);
	}

	@FunctionalInterface
	public interface DisplayRenderable {
		void render(GuiGraphicsExtractor graphics, float x, float y, float scale, float rotation);
	}

	private static void renderTransform(
			GuiGraphicsExtractor g,
			float x, float y,
			float scale, float rotation,
			Runnable drawCall) {
		g.pose().pushMatrix();
		g.pose().translate(x, y);
		g.pose().scale(scale, scale);
		if (rotation != 0.0f) {
			g.pose().translate(8.0f, 8.0f);
			g.pose().rotate((float) Math.toRadians(rotation));
			g.pose().translate(-8.0f, -8.0f);
		}
		drawCall.run();
		g.pose().popMatrix();
	}

	public static void displayTransform(
			int fromTick, int toTick, Identifier texture,
			int fromX, int fromY, int toX, int toY,
			float startScale, float endScale,
			float startAngle, float endAngle,
			String clampType, int layer) {
		if (CURRENT_INSTANCE != null) {
			CURRENT_INSTANCE.actions.add(new AnimationAction() {
				@Override
				public boolean isAlive(int tick) {
					return tick >= fromTick && tick <= toTick;
				}

				@Override
				public int getLayer() {
					return layer;
				}

				@Override
				public void render(GuiGraphicsExtractor g, int tick, int sw, int sh) {
					float progress = clamp((float)(tick - fromTick) / (float)(toTick - fromTick), clampType);

					float currentX = fromX + (toX - fromX) * progress;
					float currentY = fromY + (toY - fromY) * progress;
					float currentScale = startScale + (endScale - startScale) * progress;
					float currentAngle = startAngle + (endAngle - startAngle) * progress;

					g.pose().pushMatrix();
					g.pose().translate(currentX, currentY);
					g.pose().scale(currentScale, currentScale);
					if (currentAngle != 0) {
						g.pose().translate(8.0f, 8.0f);
						g.pose().rotate((float) Math.toRadians(currentAngle));
						g.pose().translate(-8.0f, -8.0f);
					}

					g.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0, 0, 16, 16, 16, 16);
					g.pose().popMatrix();
				}
			});
		}
	}

	public static void displayTransform(
			int fromTick, int toTick, Identifier texture,
			int textureWidth, int textureHeight,
			int fromX, int fromY, int toX, int toY,
			float startScale, float endScale,
			float startAngle, float endAngle,
			String clampType, int layer) {

		if (CURRENT_INSTANCE != null) {
			CURRENT_INSTANCE.actions.add(new AnimationAction() {
				@Override
				public boolean isAlive(int tick) {
					return tick >= fromTick && tick <= toTick;
				}

				@Override
				public int getLayer() {
					return layer;
				}

				@Override
				public void render(
						GuiGraphicsExtractor g,
						int tick,
						int sw,
						int sh) {

					float progress = clamp(
							(float) (tick - fromTick)
									/ (float) (toTick - fromTick),
							clampType
					);

					float currentX =
							fromX + (toX - fromX) * progress;

					float currentY =
							fromY + (toY - fromY) * progress;

					float currentScale =
							startScale
									+ (endScale - startScale) * progress;

					float currentAngle =
							startAngle
									+ (endAngle - startAngle) * progress;

					g.pose().pushMatrix();

					g.pose().translate(
							currentX,
							currentY
					);

					g.pose().scale(
							currentScale,
							currentScale
					);

					if (currentAngle != 0.0f) {
						g.pose().translate(
								textureWidth / 2.0f,
								textureHeight / 2.0f
						);

						g.pose().rotate(
								(float) Math.toRadians(currentAngle)
						);

						g.pose().translate(
								-textureWidth / 2.0f,
								-textureHeight / 2.0f
						);
					}

					g.blit(
							net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
							texture,
							0,
							0,
							0,
							0,
							textureWidth,
							textureHeight,
							textureWidth,
							textureHeight
					);

					g.pose().popMatrix();
				}
			});
		}
	}
	public static void displayTransform(
			int fromTick, int toTick, Component text,
			int fromX, int fromY, int toX, int toY,
			float startScale, float endScale,
			float startAngle, float endAngle,
			String clampType, int layer) {

		displayTransform(
				fromTick, toTick,
				(g, x, y, scale, rotation) ->
						g.text(
								Minecraft.getInstance().font,
								text,
								0,
								0,
								0xFFFF0000,
								false
						),
				fromX, fromY,
				toX, toY,
				startScale, endScale,
				startAngle, endAngle,
				clampType, layer
		);
	}

	public static void displayTransform(
			int fromTick, int toTick, DisplayRenderable renderable,
			int fromX, int fromY, int toX, int toY,
			float startScale, float endScale,
			float startAngle, float endAngle,
			String clampType, int layer) {

		if (CURRENT_INSTANCE != null) {
			CURRENT_INSTANCE.actions.add(new AnimationAction() {
				@Override
				public boolean isAlive(int tick) {
					return tick >= fromTick && tick <= toTick;
				}

				@Override
				public int getLayer() {
					return layer;
				}

				@Override
				public void render(GuiGraphicsExtractor g, int tick, int sw, int sh) {
					float progress = clamp((float)(tick - fromTick) / (float)(toTick - fromTick), clampType);

					float currentX = fromX + (toX - fromX) * progress;
					float currentY = fromY + (toY - fromY) * progress;
					float currentScale = startScale + (endScale - startScale) * progress;
					float currentAngle = startAngle + (endAngle - startAngle) * progress;
					renderTransform(g, currentX, currentY, currentScale, currentAngle,
							() -> renderable.render(g, currentX, currentY, currentScale, currentAngle));
				}
			});
		}
	}
	public static void displayTransform(
			int fromTick, int toTick,
			Identifier texture,
			int textureWidth, int textureHeight,
			int fromCenterX, int fromCenterY,
			int toCenterX, int toCenterY,
			float startScale, float endScale,
			float startAngle, float endAngle,
			float startOpacity, float endOpacity,
			int startColor, int endColor,
			String clampType,
			int layer) {

		if (CURRENT_INSTANCE == null) {
			return;
		}

		CURRENT_INSTANCE.actions.add(new AnimationAction() {

			@Override
			public boolean isAlive(int tick) {
				return tick >= fromTick && tick <= toTick;
			}

			@Override
			public int getLayer() {
				return layer;
			}

			@Override
			public void render(
					GuiGraphicsExtractor g,
					int tick,
					int sw,
					int sh) {

				float progress;

				if (toTick == fromTick) {
					progress = 1.0f;
				} else {
					progress = clamp(
							(float) (tick - fromTick)
									/ (float) (toTick - fromTick),
							clampType
					);
				}


				float currentX = fromCenterX + (toCenterX - fromCenterX) * progress;
				float currentY = fromCenterY + (toCenterY - fromCenterY) * progress;
				float currentScale = startScale + (endScale - startScale) * progress;
				float currentAngle = startAngle + (endAngle - startAngle) * progress;
				float currentOpacity = startOpacity + (endOpacity - startOpacity) * progress;

				currentOpacity = Math.max(0.0f, Math.min(1.0f, currentOpacity));

				int currentColor = interpolateColor(startColor, endColor, progress);

				int r = (currentColor >> 16) & 0xFF;
				int green = (currentColor >> 8) & 0xFF;
				int b = currentColor & 0xFF;

				int alpha = Math.round(currentOpacity * 255.0f);

				int finalColor = (alpha << 24) | (r << 16) | (green << 8) | b;

				g.pose().pushMatrix();

				g.pose().translate(currentX, currentY);

				if (currentAngle != 0.0f) {g.pose().rotate((float) Math.toRadians(currentAngle));}

				g.pose().scale(currentScale, currentScale);

				g.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, -textureWidth / 2, -textureHeight / 2,
						0, 0, textureWidth, textureHeight, textureWidth, textureHeight,
						finalColor
				);

				g.pose().popMatrix();
			}
		});
	}
	public static void displayEntity(
			int fromTick, int toTick,
			Entity entity,
			int fromX, int fromY,
			int toX, int toY,
			float startScale, float endScale,
			float startAngle, float endAngle,
			String clampType, int layer) {

		if (CURRENT_INSTANCE == null || entity == null) {
			return;
		}

		CURRENT_INSTANCE.actions.add(new AnimationAction() {
			@Override
			public boolean isAlive(int tick) {
				return tick >= fromTick && tick <= toTick;
			}

			@Override
			public int getLayer() {
				return layer;
			}

			@Override
			public void render(
					GuiGraphicsExtractor g,
					int tick,
					int sw,
					int sh) {

				float progress;

				if (toTick == fromTick) {
					progress = 1.0f;
				} else {
					progress = clamp(
							(float) (tick - fromTick) /
									(float) (toTick - fromTick),
							clampType
					);
				}

				float currentX =
						fromX + (toX - fromX) * progress;

				float currentY =
						fromY + (toY - fromY) * progress;

				float currentScale =
						startScale + (endScale - startScale) * progress;

				float currentAngle =
						startAngle + (endAngle - startAngle) * progress;

				EntityRenderDispatcher dispatcher =
						Minecraft.getInstance()
								.getEntityRenderDispatcher();

				float partialTicks =
						Minecraft.getInstance()
								.getDeltaTracker()
								.getGameTimeDeltaPartialTick(true);

				EntityRenderState renderState =
						dispatcher.extractEntity(
								entity,
								partialTicks
						);

				float baseSize = 32.0f;
				float size = baseSize * currentScale;

				int x0 = (int) (currentX - size);
				int y0 = (int) (currentY - size);
				int x1 = (int) (currentX + size);
				int y1 = (int) (currentY + size);

				Quaternionf rotation =
						new Quaternionf()
								.rotateZ(
										(float) Math.toRadians(currentAngle)
								);

				g.entity(
						renderState,
						currentScale,
						new Vector3f(0.0f, 0.0f, 0.0f),
						rotation,
						null,
						x0, y0, x1, y1
				);
			}
		});
	}

	public static void displayColor(
			int fromTick, int toTick,
			int color,
			int layer) {

		if (CURRENT_INSTANCE == null) {
			return;
		}

		CURRENT_INSTANCE.actions.add(new AnimationAction() {
			@Override
			public boolean isAlive(int tick) {
				return tick >= fromTick && tick <= toTick;
			}

			@Override
			public int getLayer() {
				return layer;
			}

			@Override
			public void render(
					GuiGraphicsExtractor g,
					int tick,
					int sw,
					int sh) {

				g.fill(
						0,
						0,
						sw,
						sh,
						color
				);
			}
		});
	}
	public static void displayCenteredText(
			int fromTick, int toTick,
			Component text,
			int centerX, int y,
			float startScale, float endScale,
			float startAngle, float endAngle,
			String clampType,
			int layer) {

		if (CURRENT_INSTANCE == null) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();

		CURRENT_INSTANCE.actions.add(new AnimationAction() {
			@Override
			public boolean isAlive(int tick) {
				return tick >= fromTick && tick <= toTick;
			}

			@Override
			public int getLayer() {
				return layer;
			}

			@Override
			public void render(
					GuiGraphicsExtractor g,
					int tick,
					int sw,
					int sh) {

				float progress;

				if (toTick == fromTick) {
					progress = 1.0f;
				} else {
					progress = clamp(
							(float) (tick - fromTick)
									/ (float) (toTick - fromTick),
							clampType
					);
				}

				float currentScale =
						startScale + (endScale - startScale) * progress;

				float currentAngle =
						startAngle + (endAngle - startAngle) * progress;

				int textWidth = minecraft.font.width(text);

				float scaledWidth = textWidth * currentScale;

				float x = centerX - scaledWidth / 2.0f;

				g.pose().pushMatrix();
				g.pose().translate(x, y);
				g.pose().scale(currentScale, currentScale);

				if (currentAngle != 0.0f) {
					g.pose().translate(
							textWidth / 2.0f,
							minecraft.font.lineHeight / 2.0f
					);

					g.pose().rotate(
							(float) Math.toRadians(currentAngle)
					);

					g.pose().translate(
							-textWidth / 2.0f,
							-minecraft.font.lineHeight / 2.0f
					);
				}

				g.text(
						minecraft.font,
						text,
						0,
						0,
						0xFFFFFFFF,
						false
				);

				g.pose().popMatrix();
			}
		});
	}
	public static void displayCenteredTextWrapped(
			int fromTick, int toTick,
			Component text,
			int centerX, int centerY,
			int maxWidth,
			float startScale, float endScale,
			float startAngle, float endAngle,
			String clampType,
			int layer) {

		if (CURRENT_INSTANCE == null) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();

		CURRENT_INSTANCE.actions.add(new AnimationAction() {
			@Override
			public boolean isAlive(int tick) {
				return tick >= fromTick && tick <= toTick;
			}

			@Override
			public int getLayer() {
				return layer;
			}

			@Override
			public void render(
					GuiGraphicsExtractor g,
					int tick,
					int sw,
					int sh) {

				float progress;

				if (toTick == fromTick) {
					progress = 1.0f;
				} else {
					progress = clamp(
							(float) (tick - fromTick)
									/ (float) (toTick - fromTick),
							clampType
					);
				}

				float currentScale =
						startScale
								+ (endScale - startScale) * progress;

				float currentAngle =
						startAngle
								+ (endAngle - startAngle) * progress;

				List<net.minecraft.util.FormattedCharSequence> lines =
						minecraft.font.split(text, maxWidth);

				if (lines.isEmpty()) {
					return;
				}

				int lineHeight = minecraft.font.lineHeight;
				int totalHeight = lines.size() * lineHeight;

				float topY =
						centerY
								- (totalHeight * currentScale) / 2.0f;

				g.pose().pushMatrix();

				for (int i = 0; i < lines.size(); i++) {
					net.minecraft.util.FormattedCharSequence line =
							lines.get(i);

					int lineWidth =
							minecraft.font.width(line);

					float lineX =
							centerX
									- (lineWidth * currentScale) / 2.0f;

					float lineY =
							topY
									+ i * lineHeight * currentScale;

					g.pose().pushMatrix();

					g.pose().translate(lineX, lineY);
					g.pose().scale(
							currentScale,
							currentScale
					);

					if (currentAngle != 0.0f) {
						g.pose().translate(
								lineWidth / 2.0f,
								lineHeight / 2.0f
						);

						g.pose().rotate(
								(float) Math.toRadians(currentAngle)
						);

						g.pose().translate(
								-lineWidth / 2.0f,
								-lineHeight / 2.0f
						);
					}

					g.text(
							minecraft.font,
							line,
							0,
							0,
							0xFFFFFFFF,
							false
					);

					g.pose().popMatrix();
				}

				g.pose().popMatrix();
			}
		});
	}
	public static void displayCenteredTextWrappedScroll(
			int fromTick, int toTick,
			Component text,
			int centerX, int centerY,
			int maxWidth,
			float startScale, float endScale,
			float startAngle, float endAngle,
			String clampType,
			float scrollSpeed,
			int layer) {

		if (CURRENT_INSTANCE == null) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();

		CURRENT_INSTANCE.actions.add(new AnimationAction() {
			@Override
			public boolean isAlive(int tick) {
				return tick >= fromTick && tick <= toTick;
			}

			@Override
			public int getLayer() {
				return layer;
			}

			@Override
			public void render(
					GuiGraphicsExtractor g,
					int tick,
					int sw,
					int sh) {

				float progress;

				if (toTick == fromTick) {
					progress = 1.0f;
				} else {
					progress = clamp(
							(float) (tick - fromTick)
									/ (float) (toTick - fromTick),
							clampType
					);
				}

				float currentScale =
						startScale
								+ (endScale - startScale) * progress;

				float currentAngle =
						startAngle
								+ (endAngle - startAngle) * progress;

				List<net.minecraft.util.FormattedCharSequence> lines =
						minecraft.font.split(text, maxWidth);

				if (lines.isEmpty()) {
					return;
				}

				int lineHeight = minecraft.font.lineHeight;
				int totalHeight = lines.size() * lineHeight;

				float scrollOffset =
						(tick - fromTick) * scrollSpeed;

				g.pose().pushMatrix();

				g.pose().translate(centerX, centerY);

				g.pose().scale(
						currentScale,
						currentScale
				);

				if (currentAngle != 0.0f) {
					g.pose().rotate(
							(float) Math.toRadians(currentAngle)
					);
				}

				g.pose().translate(
						scrollOffset,
						0.0f
				);

				float startY = -totalHeight / 2.0f;

				for (int i = 0; i < lines.size(); i++) {

					var line = lines.get(i);

					int lineWidth =
							minecraft.font.width(line);

					float y =
							startY
									+ i * lineHeight;

					g.text(
							minecraft.font,
							line,
							-lineWidth / 2,
							(int) y,
							0xFFFFFFFF,
							false
					);
				}

				g.pose().popMatrix();
			}
		});
	}
	public static void fadeOut(
			int fromTick,
			int toTick,
			int color,
			int layer
	) {
		if (CURRENT_INSTANCE == null) {
			return;
		}

		CURRENT_INSTANCE.actions.add(new AnimationAction() {
			@Override
			public boolean isAlive(int tick) {
				return tick >= fromTick && tick <= toTick;
			}

			@Override
			public int getLayer() {
				return layer;
			}

			@Override
			public void render(
					GuiGraphicsExtractor g,
					int tick,
					int sw,
					int sh
			) {
				float progress;

				if (toTick == fromTick) {
					progress = 1.0f;
				} else {
					progress = (float) (tick - fromTick)
							/ (float) (toTick - fromTick);
				}

				progress = Math.max(0.0f, Math.min(1.0f, progress));

				int alpha = 255 - (int) (progress * 255.0f);

				int rgb = color & 0x00FFFFFF;
				int finalColor = (alpha << 24) | rgb;

				g.fill(
						0,
						0,
						sw,
						sh,
						finalColor
				);
			}
		});
	}

	public static void fadeIn(
			int fromTick,
			int toTick,
			int color,
			int layer
	) {
		if (CURRENT_INSTANCE == null) {
			return;
		}

		CURRENT_INSTANCE.actions.add(new AnimationAction() {
			@Override
			public boolean isAlive(int tick) {
				return tick >= fromTick && tick <= toTick;
			}

			@Override
			public int getLayer() {
				return layer;
			}

			@Override
			public void render(
					GuiGraphicsExtractor g,
					int tick,
					int sw,
					int sh
			) {
				float progress;

				if (toTick == fromTick) {
					progress = 1.0f;
				} else {
					progress = (float) (tick - fromTick)
							/ (float) (toTick - fromTick);
				}

				progress = Math.max(0.0f, Math.min(1.0f, progress));

				int alpha = (int) (progress * 255.0f);

				int rgb = color & 0x00FFFFFF;
				int finalColor = (alpha << 24) | rgb;

				g.fill(
						0,
						0,
						sw,
						sh,
						finalColor
				);
			}
		});
	}

	public static void displayStatic(int fromTick, int toTick, Identifier texture, int x, int y, int scale, int layer) {
		displayStatic(fromTick, toTick, texture, x, y, (float) scale, 0f, layer);
	}

	public static void displayStatic(int fromTick, int toTick, Identifier texture, int x, int y, float scale, float rotation, int layer) {
		displayTransform(fromTick, toTick, texture, x, y, x, y, scale, scale, rotation, rotation, "linear", layer);
	}

	public static void displayMove(int fromTick, int toTick, Identifier texture, int fromX, int fromY, int toX, int toY, int scale, String clampType, int layer) {
		displayMove(fromTick, toTick, texture, fromX, fromY, toX, toY, (float) scale, 0f, clampType, layer);
	}

	public static void displayMove(int fromTick, int toTick, Identifier texture, int fromX, int fromY, int toX, int toY, float scale, float rotation, String clampType, int layer) {
		displayTransform(fromTick, toTick, texture, fromX, fromY, toX, toY, scale, scale, rotation, rotation, clampType, layer);
	}

	public static void displayScale(int fromTick, int toTick, Identifier texture, int x, int y, float startScale, float endScale, String clampType, int layer) {
		displayScale(fromTick, toTick, texture, x, y, startScale, endScale, 0f, clampType, layer);
	}

	public static void displayScale(int fromTick, int toTick, Identifier texture, int x, int y, float startScale, float endScale, float rotation, String clampType, int layer) {
		displayTransform(fromTick, toTick, texture, x, y, x, y, startScale, endScale, rotation, rotation, clampType, layer);
	}

	public static void displayRotate(int fromTick, int toTick, Identifier texture, int x, int y, float startAngle, float endAngle, String clampType, int layer) {
		displayRotate(fromTick, toTick, texture, x, y, startAngle, endAngle, 1.0f, clampType, layer);
	}

	public static void displayRotate(int fromTick, int toTick, Identifier texture, int x, int y, float startAngle, float endAngle, float scale, String clampType, int layer) {
		displayTransform(fromTick, toTick, texture, x, y, x, y, scale, scale, startAngle, endAngle, clampType, layer);
	}

	public static void playSound(int atTick, SoundEvent sound, float volume, float pitch) {
		if (CURRENT_INSTANCE != null) {
			CURRENT_INSTANCE.soundCues.add(new SoundCue(atTick, sound, volume, pitch));
		}
	}

	public static void playSound(int atTick, Holder<SoundEvent> sound, float volume, float pitch) {
		playSound(atTick, sound.value(), volume, pitch);
	}

	public static void playSound(int atTick, SoundEvent sound) {
		playSound(atTick, sound, 1.0f, 1.0f);
	}
	public static void playSound(int atTick, Holder<SoundEvent> sound) {
		playSound(atTick, sound.value(), 1.0f, 1.0f);
	}

	private static float clamp(float t, String type) {
		t = Math.max(0.0f, Math.min(1.0f, t));
		return switch (type.toLowerCase()) {
			case "easein" -> t * t;
			case "easeout" -> t * (2 - t);
			case "easeinout" -> t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
			default -> t;
		};
	}
	private static int interpolateColor(
			int startColor,
			int endColor,
			float progress) {

		int startR = (startColor >> 16) & 0xFF;
		int startG = (startColor >> 8) & 0xFF;
		int startB = startColor & 0xFF;

		int endR = (endColor >> 16) & 0xFF;
		int endG = (endColor >> 8) & 0xFF;
		int endB = endColor & 0xFF;

		int r = Math.round(
				startR + (endR - startR) * progress
		);

		int g = Math.round(
				startG + (endG - startG) * progress
		);

		int b = Math.round(
				startB + (endB - startB) * progress
		);

		return (r << 16) | (g << 8) | b;
	}
}