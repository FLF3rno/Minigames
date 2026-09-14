package net.mcreator.minigames.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.minigames.entity.FlavioOmegaLaserEntity;
import net.mcreator.minigames.network.OmegaLaserControlMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class OmegaLaserScreen extends Screen {
	private static final Identifier SHOOT_BUTTON_TEXTURE = Identifier.parse("minigames:textures/screens/shoot_button.png");
	private static final Identifier SHOOT_BUTTON_HOVER_TEXTURE = Identifier.parse("minigames:textures/screens/shoot_button_hover.png");
	private static final Identifier SHOOT_BUTTON_COOLDOWN_TEXTURE = Identifier.parse("minigames:textures/screens/shoot_button_cooldown.png");
	private static final Identifier KNOB_HORIZONTAL_TEXTURE = Identifier.parse("minigames:textures/screens/knob_horizontal.png");
	private static final Identifier KNOB_VERTICAL_TEXTURE = Identifier.parse("minigames:textures/screens/knob_vertical.png");

	private final int laserEntityId;
	private final int screenIndex;
	private final int totalPlayers;

	// Roles
	private boolean roleShoot = false;
	private boolean roleUpDown = false;
	private boolean roleLeftRight = false;
	private boolean roleCamera = false;

	private int udCenterY = 0;
	private int udCenterX = 0;
	private float udKnobY = 0;
	private boolean draggingUD = false;
	private int lrCenterY = 0;
	private int lrCenterX = 0;
	private float lrKnobX = 0;
	private boolean draggingLR = false;

	private int fireButtonX = 0;
	private int fireButtonY = 0;
	private static final int FIRE_BUTTON_DRAW_SIZE = 64;
	private static final int FIRE_BUTTON_HIT_RADIUS = 30;
	private boolean fireButtonHovered = false;

	private static final int JOYSTICK_TRACK_RADIUS = 60;
	private static final int KNOB_DRAW_SIZE = 64;
	private static final int KNOB_HIT_RADIUS = 28;

	private static final int KNOB_OFFSET_X = 10;
	private static final int KNOB_OFFSET_Y = 10;

	public OmegaLaserScreen(int laserEntityId, int screenIndex, int totalPlayers) {
		super(Component.literal("Omega Laser Cockpit"));
		this.laserEntityId = laserEntityId;
		this.screenIndex = screenIndex;
		this.totalPlayers = Math.max(1, Math.min(4, totalPlayers));
		calculateRoles();
	}

	private void calculateRoles() {
		if (totalPlayers == 1) {
			roleShoot = true;
			roleUpDown = true;
			roleLeftRight = true;
			roleCamera = true;
		} else if (totalPlayers == 2) {
			if (screenIndex == 0) {
				roleShoot = true;
				roleCamera = true;
			} else {
				roleUpDown = true;
			}
		} else if (totalPlayers == 3) {
			if (screenIndex == 0) {
				roleShoot = true;
				roleCamera = true;
			} else if (screenIndex == 1) {
				roleUpDown = true;
			} else {
				roleLeftRight = true;
			}
		} else {
			if (screenIndex == 0) {
				roleShoot = true;
			} else if (screenIndex == 1) {
				roleUpDown = true;
			} else if (screenIndex == 2) {
				roleLeftRight = true;
			} else {
				roleCamera = true;
			}
		}
	}

	@Override
	protected void init() {
		super.init();

		udCenterX = this.width / 4;
		udCenterY = this.height / 2;

		lrCenterX = (this.width * 3) / 4;
		lrCenterY = this.height / 2;

		fireButtonX = this.width / 2;
		fireButtonY = this.height / 2;

		if (roleCamera && this.minecraft != null) {
			Entity laser = this.minecraft.level != null ? this.minecraft.level.getEntity(laserEntityId) : null;
			if (laser != null) {
				this.minecraft.setCameraEntity(laser);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.minecraft != null && this.minecraft.level != null) {
			Entity laser = this.minecraft.level.getEntity(laserEntityId);
			if (laser == null || !laser.isAlive()) {
				this.onClose();
				return;
			}
			if (roleCamera && this.minecraft.getCameraEntity() != laser) {
				this.minecraft.setCameraEntity(laser);
			}
		}

		float deltaPitch = 0;
		float deltaYaw = 0;

		if (roleUpDown && draggingUD && Math.abs(udKnobY) > 0.05f) {
			float sign = Math.signum(udKnobY);
			float magnitude = Math.abs(udKnobY);
			deltaPitch = sign * (float) Math.pow(magnitude, 1.4) * 1.8f;
		}
		if (roleLeftRight && draggingLR && Math.abs(lrKnobX) > 0.05f) {
			float sign = Math.signum(lrKnobX);
			float magnitude = Math.abs(lrKnobX);
			deltaYaw = sign * (float) Math.pow(magnitude, 1.4) * 2.2f;
		}

		if (deltaPitch != 0 || deltaYaw != 0) {
			ClientPacketDistributor.sendToServer(new OmegaLaserControlMessage(laserEntityId, screenIndex, 0, deltaPitch, deltaYaw));
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		if (roleCamera) {
		} else {
			guiGraphics.fill(0, 0, this.width, this.height, 0xFF000000);
		}
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);

		FlavioOmegaLaserEntity laser = null;
		if (this.minecraft != null && this.minecraft.level != null) {
			Entity e = this.minecraft.level.getEntity(laserEntityId);
			if (e instanceof FlavioOmegaLaserEntity fle) {
				laser = fle;
			}
		}

		boolean isLocked = laser == null || laser.isControlsLocked();
		int mannedCount = laser != null ? laser.getMannedPlayerCount() : 0;
		int reload = laser != null ? laser.getReloadProgress() : 0;

		String header = "OMEGA LASER SCREEN #" + (screenIndex + 1);
		int headerW = this.font.width(header);
		guiGraphics.text(this.font, Component.literal(header), (this.width - headerW) / 2, 16, 0xFF00FFFF, true);

		if (isLocked) {
			guiGraphics.fill(0, 32, this.width, 52, 0x88CC0000);
			String lockedMsg = "⚠ CONTROLS LOCKED - WAITING FOR ALL PLAYERS (" + mannedCount + " / " + totalPlayers + " MANNED) ⚠";
			int lockedW = this.font.width(lockedMsg);
			guiGraphics.text(this.font, Component.literal(lockedMsg), (this.width - lockedW) / 2, 38, 0xFFFFFFFF, true);
		} else {
			guiGraphics.fill(0, 32, this.width, 52, 0x88008800);
			String readyMsg = "✔ ALL PLAYERS READY (" + mannedCount + " / " + totalPlayers + ") - CONTROLS ACTIVE";
			int readyW = this.font.width(readyMsg);
			guiGraphics.text(this.font, Component.literal(readyMsg), (this.width - readyW) / 2, 38, 0xFF55FF55, true);
		}


		if (roleUpDown) {
			renderVerticalJoystick(guiGraphics, udCenterX, udCenterY, "ELEVATION (UP/DOWN)", udKnobY);
		}

		if (roleLeftRight) {
			renderHorizontalJoystick(guiGraphics, lrCenterX, lrCenterY, "TRAVERSE (LEFT/RIGHT)", lrKnobX);
		}

		if (roleShoot) {
			renderFireButton(guiGraphics, fireButtonX, fireButtonY, reload, isLocked, mouseX, mouseY);
		}

		if (roleCamera) {
			renderCameraOverlay(guiGraphics);
		}

		String footer = "Press [ESC] to leave cockpit";
		int footerW = this.font.width(footer);
		guiGraphics.text(this.font, Component.literal(footer), (this.width - footerW) / 2, this.height - 20, 0xFF888888, false);
	}

	private void renderVerticalJoystick(GuiGraphicsExtractor guiGraphics, int cx, int cy, String label, float knobY) {
		int labelW = this.font.width(label);
		guiGraphics.text(this.font, Component.literal(label), cx - labelW / 2, cy - JOYSTICK_TRACK_RADIUS - 28, 0xFFFFFF00, false);

		guiGraphics.fill(cx - 10, cy - JOYSTICK_TRACK_RADIUS, cx + 10, cy + JOYSTICK_TRACK_RADIUS, 0xFF2A2A2A);
		guiGraphics.fill(cx - 8, cy - JOYSTICK_TRACK_RADIUS + 2, cx + 8, cy + JOYSTICK_TRACK_RADIUS - 2, 0xFF141414);

		guiGraphics.fill(cx - 16, cy - 1, cx + 16, cy + 1, 0xFF666666);

		int knobCenterY = cy + Math.round(knobY * JOYSTICK_TRACK_RADIUS);
		int drawX = (cx - KNOB_DRAW_SIZE / 2) + KNOB_OFFSET_X;
		int drawY = (knobCenterY - KNOB_DRAW_SIZE / 2) + KNOB_OFFSET_Y;

		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, KNOB_VERTICAL_TEXTURE, drawX, drawY, 0, 0, KNOB_DRAW_SIZE, KNOB_DRAW_SIZE, 64, 64);
	}

	private void renderHorizontalJoystick(GuiGraphicsExtractor guiGraphics, int cx, int cy, String label, float knobX) {
		int labelW = this.font.width(label);
		guiGraphics.text(this.font, Component.literal(label), cx - labelW / 2, cy - JOYSTICK_TRACK_RADIUS - 28, 0xFFFFFF00, false);

		guiGraphics.fill(cx - JOYSTICK_TRACK_RADIUS, cy - 10, cx + JOYSTICK_TRACK_RADIUS, cy + 10, 0xFF2A2A2A);
		guiGraphics.fill(cx - JOYSTICK_TRACK_RADIUS + 2, cy - 8, cx + JOYSTICK_TRACK_RADIUS - 2, cy + 8, 0xFF141414);

		guiGraphics.fill(cx - 1, cy - 16, cx + 1, cy + 16, 0xFF666666);

		int knobCenterX = cx + Math.round(knobX * JOYSTICK_TRACK_RADIUS);
		int drawX = (knobCenterX - KNOB_DRAW_SIZE / 2) + KNOB_OFFSET_X;
		int drawY = (cy - KNOB_DRAW_SIZE / 2) + KNOB_OFFSET_Y;

		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, KNOB_HORIZONTAL_TEXTURE, drawX, drawY, 0, 0, KNOB_DRAW_SIZE, KNOB_DRAW_SIZE, 64, 64);
	}

	private void renderFireButton(GuiGraphicsExtractor guiGraphics, int cx, int cy, int reload, boolean isLocked, int mouseX, int mouseY) {
		boolean ready = reload >= FlavioOmegaLaserEntity.MAX_RELOAD && !isLocked;
		double dist = Math.hypot(mouseX - cx, mouseY - cy);
		fireButtonHovered = (dist <= FIRE_BUTTON_HIT_RADIUS);

		int drawX = cx - FIRE_BUTTON_DRAW_SIZE / 2;
		int drawY = cy - FIRE_BUTTON_DRAW_SIZE / 2;

		Identifier buttonTexture;
		if (!ready) {
			buttonTexture = SHOOT_BUTTON_COOLDOWN_TEXTURE;
		} else if (fireButtonHovered) {
			buttonTexture = SHOOT_BUTTON_HOVER_TEXTURE;
		} else {
			buttonTexture = SHOOT_BUTTON_TEXTURE;
		}

		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, buttonTexture, drawX, drawY, 0, 0, FIRE_BUTTON_DRAW_SIZE, FIRE_BUTTON_DRAW_SIZE, 64, 64);

		int barW = 84;
		int barH = 7;
		int barX = cx - barW / 2;
		int barY = cy + FIRE_BUTTON_DRAW_SIZE / 2 + 10;
		guiGraphics.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, 0xFF444444);
		guiGraphics.fill(barX, barY, barX + barW, barY + barH, 0xFF181818);

		int fillW = (int) ((reload / (float) FlavioOmegaLaserEntity.MAX_RELOAD) * barW);
		if (fillW > 0) {
			guiGraphics.fill(barX, barY, barX + fillW, barY + barH, ready ? 0xFF00FF00 : 0xFFFF8800);
		}

		String reloadLabel = ready ? "READY TO FIRE" : "RELOADING (" + reload + "%)";
		int relW = this.font.width(reloadLabel);
		guiGraphics.text(this.font, Component.literal(reloadLabel), cx - relW / 2, barY + barH + 4, ready ? 0xFF55FF55 : 0xFFFFAA00, false);
	}

	private void renderCameraOverlay(GuiGraphicsExtractor guiGraphics) {
		int midX = this.width / 2;
		int midY = this.height / 2;
		if (!roleShoot) {
			int size = 16;
			guiGraphics.fill(midX - size, midY - 1, midX + size, midY + 1, 0x8800FF00);
			guiGraphics.fill(midX - 1, midY - size, midX + 1, midY + size, 0x8800FF00);
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == 0) {
			double mx = event.x();
			double my = event.y();

			if (roleShoot) {
				double dist = Math.hypot(mx - fireButtonX, my - fireButtonY);
				if (dist <= FIRE_BUTTON_HIT_RADIUS) {
					ClientPacketDistributor.sendToServer(new OmegaLaserControlMessage(laserEntityId, screenIndex, 1, 0, 0));
					return true;
				}
			}

			if (roleUpDown) {
				int currentKnobY = udCenterY + Math.round(udKnobY * JOYSTICK_TRACK_RADIUS);
				if (Math.abs(mx - udCenterX) <= KNOB_HIT_RADIUS && Math.abs(my - currentKnobY) <= KNOB_HIT_RADIUS) {
					draggingUD = true;
					return true;
				} else if (Math.abs(mx - udCenterX) <= 15 && Math.abs(my - udCenterY) <= JOYSTICK_TRACK_RADIUS + 10) {
					draggingUD = true;
					float offset = (float) (my - udCenterY);
					udKnobY = Math.max(-1.0f, Math.min(1.0f, offset / JOYSTICK_TRACK_RADIUS));
					return true;
				}
			}

			if (roleLeftRight) {
				int currentKnobX = lrCenterX + Math.round(lrKnobX * JOYSTICK_TRACK_RADIUS);
				if (Math.abs(mx - currentKnobX) <= KNOB_HIT_RADIUS && Math.abs(my - lrCenterY) <= KNOB_HIT_RADIUS) {
					draggingLR = true;
					return true;
				} else if (Math.abs(my - lrCenterY) <= 15 && Math.abs(mx - lrCenterX) <= JOYSTICK_TRACK_RADIUS + 10) {
					draggingLR = true;
					float offset = (float) (mx - lrCenterX);
					lrKnobX = Math.max(-1.0f, Math.min(1.0f, offset / JOYSTICK_TRACK_RADIUS));
					return true;
				}
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
		if (event.button() == 0) {
			if (draggingUD) {
				float offset = (float) (event.y() - udCenterY);
				udKnobY = Math.max(-1.0f, Math.min(1.0f, offset / JOYSTICK_TRACK_RADIUS));
				return true;
			}
			if (draggingLR) {
				float offset = (float) (event.x() - lrCenterX);
				lrKnobX = Math.max(-1.0f, Math.min(1.0f, offset / JOYSTICK_TRACK_RADIUS));
				return true;
			}
		}
		return super.mouseDragged(event, dragX, dragY);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (event.button() == 0) {
			if (draggingUD) {
				draggingUD = false;
				udKnobY = 0;
			}
			if (draggingLR) {
				draggingLR = false;
				lrKnobX = 0;
			}
		}
		return super.mouseReleased(event);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			this.onClose();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	public void onClose() {
		if (roleCamera && this.minecraft != null && this.minecraft.player != null) {
			this.minecraft.setCameraEntity(this.minecraft.player);
		}
		ClientPacketDistributor.sendToServer(new OmegaLaserControlMessage(laserEntityId, screenIndex, 2, 0, 0));
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
