package net.mcreator.minigames.client.gui.options;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public class MinigamesSettingsScreen extends Screen {
	private final Screen lastScreen;
	private SettingsList list;
	private KeyMapping selectedKeyMapping = null;

	public MinigamesSettingsScreen(Screen lastScreen) {
		super(Component.literal("Minigames Settings"));
		this.lastScreen = lastScreen;
	}

	@Override
	protected void init() {
		super.init();
		this.list = new SettingsList(this.minecraft, this.width, this.height - 64, 32, 24);
		this.addRenderableWidget(this.list);

		Button doneButton = Button.builder(Component.translatable("gui.done"), b -> this.onClose())
			.bounds(this.width / 2 - 100, this.height - 28, 200, 20)
			.build();
		this.addRenderableWidget(doneButton);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.centeredText(this.font, this.title, this.width / 2, 12, 0xFFFFFFFF);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (this.selectedKeyMapping != null) {
			int key = InputConstants.getKey(event).getValue();
			if (key == 256) {
				this.selectedKeyMapping.setKey(InputConstants.UNKNOWN);
			} else {
				this.selectedKeyMapping.setKey(InputConstants.getKey(event));
			}
			KeyMapping.resetMapping();
			if (this.minecraft != null && this.minecraft.options != null) {
				this.minecraft.options.save();
			}
			this.selectedKeyMapping = null;
			if (this.list != null) {
				this.list.updateKeyEntries();
			}
			return true;
		}
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			this.onClose();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (this.selectedKeyMapping != null) {
			this.selectedKeyMapping.setKey(InputConstants.Type.MOUSE.getOrCreate(event.button()));
			KeyMapping.resetMapping();
			if (this.minecraft != null && this.minecraft.options != null) {
				this.minecraft.options.save();
			}
			this.selectedKeyMapping = null;
			if (this.list != null) {
				this.list.updateKeyEntries();
			}
			return true;
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public void onClose() {
		if (this.minecraft != null) {
			this.minecraft.setScreen(this.lastScreen);
		}
	}

	public class SettingsList extends ContainerObjectSelectionList<SettingsList.Entry> {
		public SettingsList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
			super(minecraft, width, height, y, itemHeight);
			for (MinigamesSettingsHelper.SettingRow row : MinigamesSettingsHelper.ROWS) {
				if (row instanceof MinigamesSettingsHelper.HeaderRow header) {
					this.addEntry(new HeaderEntry(header));
				} else if (row instanceof MinigamesSettingsHelper.BooleanSettingRow boolRow) {
					this.addEntry(new SettingEntry(boolRow));
				} else if (row instanceof MinigamesSettingsHelper.DoubleSliderSettingRow sliderRow) {
					this.addEntry(new SliderEntry(sliderRow));
				} else if (row instanceof MinigamesSettingsHelper.DoubleSettingRow doubleRow) {
					this.addEntry(new SettingEntry(doubleRow));
				} else if (row instanceof MinigamesSettingsHelper.KeyMappingSettingRow keyRow) {
					this.addEntry(new KeyMappingEntry(keyRow));
				}
			}
		}

		public void updateKeyEntries() {
			for (Entry entry : this.children()) {
				if (entry instanceof KeyMappingEntry keyEntry) {
					keyEntry.updateButtonMessages();
				}
			}
		}

		@Override
		public int getRowWidth() {
			return 340;
		}

		public abstract static class Entry extends ContainerObjectSelectionList.Entry<SettingsList.Entry> {
		}

		public class HeaderEntry extends SettingsList.Entry {
			private final MinigamesSettingsHelper.HeaderRow header;

			public HeaderEntry(MinigamesSettingsHelper.HeaderRow header) {
				this.header = header;
			}

			@Override
			public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTicks) {
				Component text = header.getText();
				int fontW = SettingsList.this.minecraft.font.width(text);
				int iconW = header.getIconWidth();
				int iconH = header.getIconHeight();
				int texW = header.getTexWidth();
				int texH = header.getTexHeight();
				Identifier leftTex = header.getLeftTexture();
				Identifier rightTex = header.getRightTexture();
				int spacing = 6;

				int totalW = fontW;
				if (leftTex != null) {
					totalW += iconW + spacing;
				}
				if (rightTex != null) {
					totalW += iconW + spacing;
				}

				int curX = header.isCentered() ? SettingsList.this.width / 2 - totalW / 2 : this.getContentX() + 6;
				int centerY = this.getContentYMiddle();

				if (leftTex != null) {
					graphics.blit(RenderPipelines.GUI_TEXTURED, leftTex, curX, centerY - iconH / 2, 0.0f, 0.0f, iconW, iconH, texW, texH);
					curX += iconW + spacing;
				}

				graphics.text(SettingsList.this.minecraft.font, text, curX, centerY - 9 / 2, 0xFFFFFF55, true);
				curX += fontW + spacing;

				if (rightTex != null) {
					graphics.blit(RenderPipelines.GUI_TEXTURED, rightTex, curX, centerY - iconH / 2, 0.0f, 0.0f, iconW, iconH, texW, texH);
				}
			}

			@Override
			public List<? extends GuiEventListener> children() {
				return List.of();
			}

			@Override
			public List<? extends NarratableEntry> narratables() {
				return List.of();
			}
		}

		public class SettingEntry extends SettingsList.Entry {
			private final Component label;
			private final Button button;

			public SettingEntry(MinigamesSettingsHelper.BooleanSettingRow row) {
				this.label = row.getLabel();
				this.button = Button.builder(row.getButtonMessage(), b -> {
					row.toggle();
					b.setMessage(row.getButtonMessage());
				}).bounds(0, 0, 130, 20).build();
			}

			public SettingEntry(MinigamesSettingsHelper.DoubleSettingRow row) {
				this.label = row.getLabel();
				this.button = Button.builder(row.getButtonMessage(), b -> {
					row.cycle();
					b.setMessage(row.getButtonMessage());
				}).bounds(0, 0, 130, 20).build();
			}

			@Override
			public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTicks) {
				int buttonW = 130;
				int buttonH = 20;
				int buttonX = this.getContentRight() - buttonW - 6;
				int buttonY = this.getContentYMiddle() - buttonH / 2;
				this.button.setPosition(buttonX, buttonY);
				this.button.setWidth(buttonW);
				this.button.setHeight(buttonH);
				this.button.extractRenderState(graphics, mouseX, mouseY, partialTicks);

				int textX = this.getContentX() + 6;
				int textY = this.getContentYMiddle() - 9 / 2;
				graphics.text(SettingsList.this.minecraft.font, this.label, textX, textY, 0xFFFFFFFF, true);
			}

			@Override
			public List<? extends GuiEventListener> children() {
				return List.of(this.button);
			}

			@Override
			public List<? extends NarratableEntry> narratables() {
				return List.of(this.button);
			}
		}

		public class SliderEntry extends SettingsList.Entry {
			private final Component label;
			private final AbstractSliderButton slider;

			public SliderEntry(MinigamesSettingsHelper.DoubleSliderSettingRow row) {
				this.label = row.getLabel();
				double min = row.getMin();
				double max = row.getMax();
				double currentVal = row.getValue();
				double initialNorm = max > min ? Math.max(0.0, Math.min(1.0, (currentVal - min) / (max - min))) : 0.0;

				this.slider = new AbstractSliderButton(0, 0, 130, 20, row.getButtonMessage(), initialNorm) {
					@Override
					protected void updateMessage() {
						double actual = min + this.value * (max - min);
						if (row.getStep() > 0) {
							actual = Math.round((actual - min) / row.getStep()) * row.getStep() + min;
							actual = Math.max(min, Math.min(max, actual));
						}
						this.setMessage(row.getDisplayMessage(actual));
					}

					@Override
					protected void applyValue() {
						double actual = min + this.value * (max - min);
						if (row.getStep() > 0) {
							actual = Math.round((actual - min) / row.getStep()) * row.getStep() + min;
							actual = Math.max(min, Math.min(max, actual));
						}
						row.setValue(actual);
					}
				};
				this.slider.setMessage(row.getButtonMessage());
			}

			@Override
			public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTicks) {
				int sliderW = 130;
				int sliderH = 20;
				int sliderX = this.getContentRight() - sliderW - 6;
				int sliderY = this.getContentYMiddle() - sliderH / 2;
				this.slider.setPosition(sliderX, sliderY);
				this.slider.setWidth(sliderW);
				this.slider.setHeight(sliderH);
				this.slider.extractRenderState(graphics, mouseX, mouseY, partialTicks);

				int textX = this.getContentX() + 6;
				int textY = this.getContentYMiddle() - 9 / 2;
				graphics.text(SettingsList.this.minecraft.font, this.label, textX, textY, 0xFFFFFFFF, true);
			}

			@Override
			public List<? extends GuiEventListener> children() {
				return List.of(this.slider);
			}

			@Override
			public List<? extends NarratableEntry> narratables() {
				return List.of(this.slider);
			}
		}

		public class KeyMappingEntry extends SettingsList.Entry {
			private final Component label;
			private final KeyMapping keyMapping;
			private final Button keyButton;
			private final Button resetButton;

			public KeyMappingEntry(MinigamesSettingsHelper.KeyMappingSettingRow row) {
				this.label = row.getLabel();
				this.keyMapping = row.getKeyMapping();
				this.keyButton = Button.builder(getKeyButtonMessage(), b -> {
					MinigamesSettingsScreen.this.selectedKeyMapping = this.keyMapping;
					MinigamesSettingsScreen.this.list.updateKeyEntries();
				}).bounds(0, 0, 82, 20).build();

				this.resetButton = Button.builder(Component.translatable("controls.reset"), b -> {
					Minecraft mc = Minecraft.getInstance();
					if (this.keyMapping != null) {
						this.keyMapping.setKey(this.keyMapping.getDefaultKey());
						KeyMapping.resetMapping();
						if (mc != null && mc.options != null) {
							mc.options.save();
						}
						MinigamesSettingsScreen.this.list.updateKeyEntries();
					}
				}).bounds(0, 0, 44, 20).build();

				if (this.keyMapping != null) {
					this.resetButton.active = !this.keyMapping.isDefault();
				}
			}

			public void updateButtonMessages() {
				this.keyButton.setMessage(getKeyButtonMessage());
				if (this.keyMapping != null) {
					this.resetButton.active = !this.keyMapping.isDefault();
				}
			}

			private Component getKeyButtonMessage() {
				if (this.keyMapping == null) {
					return Component.empty();
				}
				if (MinigamesSettingsScreen.this.selectedKeyMapping == this.keyMapping) {
					return Component.literal("> ")
						.append(this.keyMapping.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE))
						.append(" <")
						.withStyle(ChatFormatting.YELLOW);
				}
				boolean hasConflict = false;
				if (!this.keyMapping.isUnbound() && Minecraft.getInstance().options != null) {
					for (KeyMapping other : Minecraft.getInstance().options.keyMappings) {
						if (other != this.keyMapping && this.keyMapping.same(other)) {
							hasConflict = true;
							break;
						}
					}
				}
				if (hasConflict) {
					return this.keyMapping.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.RED);
				}
				return this.keyMapping.getTranslatedKeyMessage();
			}

			@Override
			public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTicks) {
				int resetW = 44;
				int keyW = 82;
				int h = 20;
				int right = this.getContentRight() - 6;
				int resetX = right - resetW;
				int keyX = resetX - 4 - keyW;
				int y = this.getContentYMiddle() - h / 2;

				this.keyButton.setPosition(keyX, y);
				this.keyButton.setWidth(keyW);
				this.keyButton.setHeight(h);
				this.keyButton.extractRenderState(graphics, mouseX, mouseY, partialTicks);

				this.resetButton.setPosition(resetX, y);
				this.resetButton.setWidth(resetW);
				this.resetButton.setHeight(h);
				this.resetButton.extractRenderState(graphics, mouseX, mouseY, partialTicks);

				int textX = this.getContentX() + 6;
				int textY = this.getContentYMiddle() - 9 / 2;
				graphics.text(SettingsList.this.minecraft.font, this.label != null ? this.label : Component.empty(), textX, textY, 0xFFFFFFFF, true);
			}

			@Override
			public List<? extends GuiEventListener> children() {
				return List.of(this.keyButton, this.resetButton);
			}

			@Override
			public List<? extends NarratableEntry> narratables() {
				return List.of(this.keyButton, this.resetButton);
			}
		}
	}
}
