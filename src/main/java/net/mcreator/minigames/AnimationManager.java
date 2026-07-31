package net.mcreator.minigames;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AnimationManager {
	private static AnimationManager CURRENT_INSTANCE;

	private final int totalTicks;
	private final float speed;
	private int currentTick = 0;
	private boolean finished = false;
	private final List<AnimationAction> actions = new ArrayList<>();

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
		currentTick++;
		if (currentTick >= totalTicks) {
			finished = true;
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

					// Push local transformation
					g.pose().pushMatrix();

					// 1. Position on screen
					g.pose().translate(currentX, currentY);

					// 2. Scale
					g.pose().scale(currentScale, currentScale);

					// 3. Rotation (Around center of a 16x16 image)
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

	private static float clamp(float t, String type) {
		t = Math.max(0.0f, Math.min(1.0f, t));
		return switch (type.toLowerCase()) {
			case "easein" -> t * t;
			case "easeout" -> t * (2 - t);
			case "easeinout" -> t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
			default -> t;
		};
	}
}