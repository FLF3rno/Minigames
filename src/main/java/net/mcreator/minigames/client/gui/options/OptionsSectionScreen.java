package net.mcreator.minigames.client.gui.options;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public class OptionsSectionScreen extends Screen {
	private static final Identifier MINECRAFT_TEXTURE = Identifier.parse("minigames:textures/screens/options/minecraft.png");
	private static final Identifier MINIGAMES_TEXTURE = Identifier.parse("minigames:textures/screens/options/minigames.png");

	private final Screen lastScreen;
	private final OptionsScreen optionsScreen;
	private float leftDarkenAlpha = 0.0f;
	private float rightDarkenAlpha = 0.0f;
	private long lastFrameTime = System.currentTimeMillis();

	public OptionsSectionScreen(Screen lastScreen, OptionsScreen optionsScreen) {
		super(Component.literal("Options"));
		this.lastScreen = lastScreen;
		this.optionsScreen = optionsScreen;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		int halfW = this.width / 2;

		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, MINECRAFT_TEXTURE, 0, 0, 0.0f, 0.0f, halfW, this.height, 259, 246, 259, 246);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, MINIGAMES_TEXTURE, halfW, 0, 0.0f, 0.0f, this.width - halfW, this.height, 259, 246, 259, 246);

		Component mcText = Component.literal("Minecraft");
		Component mgText = Component.literal("Minigames");
		int mcX = Math.round((halfW / 4.0F) - (this.font.width(mcText) / 2.0F));
		int mgX = Math.round(((halfW + (this.width - halfW) / 2.0F) / 2.0F) - (this.font.width(mgText) / 2.0F));
		int textY = Math.round((this.height / 4.0F) - 4.5F);

		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().scale(2.0F, 2.0F);
		guiGraphics.text(this.font, mcText, mcX, textY, 0xFFFFFFFF, true);
		guiGraphics.text(this.font, mgText, mgX, textY, 0xFFFFFFFF, true);
		guiGraphics.pose().popMatrix();

		long now = System.currentTimeMillis();
		float delta = Math.min(0.1f, Math.max(0.001f, (now - lastFrameTime) / 1000.0f));
		lastFrameTime = now;

		float targetLeft = 0.0f;
		float targetRight = 0.0f;

		if (mouseY >= 0 && mouseY <= this.height && mouseX >= 0 && mouseX <= this.width) {
			if (mouseX < halfW) {
				targetRight = 1.0f;
			} else {
				targetLeft = 1.0f;
			}
		}

		float speed = 8.0f;
		if (leftDarkenAlpha < targetLeft) {
			leftDarkenAlpha = Math.min(targetLeft, leftDarkenAlpha + delta * speed);
		} else if (leftDarkenAlpha > targetLeft) {
			leftDarkenAlpha = Math.max(targetLeft, leftDarkenAlpha - delta * speed);
		}

		if (rightDarkenAlpha < targetRight) {
			rightDarkenAlpha = Math.min(targetRight, rightDarkenAlpha + delta * speed);
		} else if (rightDarkenAlpha > targetRight) {
			rightDarkenAlpha = Math.max(targetRight, rightDarkenAlpha - delta * speed);
		}

		if (leftDarkenAlpha > 0.001f) {
			int alpha = Math.round(leftDarkenAlpha * 160.0f);
			guiGraphics.fill(0, 0, halfW, this.height, (alpha << 24));
		}

		if (rightDarkenAlpha > 0.001f) {
			int alpha = Math.round(rightDarkenAlpha * 160.0f);
			guiGraphics.fill(halfW, 0, this.width, this.height, (alpha << 24));
		}

		guiGraphics.fill(halfW - 1, 0, halfW + 1, this.height, 0xFFFFFFFF);

		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (super.mouseClicked(event, doubleClick)) {
			return true;
		}

		if (event.button() == 0) {
			int halfW = this.width / 2;
			Minecraft mc = Minecraft.getInstance();
			mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

			if (event.x() < halfW) {
				OptionsInterceptor.allowVanillaOptions = true;
				mc.setScreen(new OptionsScreen(this, mc.options, false));
			} else {
				mc.setScreen(new MinigamesSettingsScreen(this));
			}
			return true;
		}

		return false;
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
		if (this.minecraft != null) {
			this.minecraft.setScreen(this.lastScreen);
		}
	}
}
