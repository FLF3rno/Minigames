package net.mcreator.minigames.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.init.MinigamesModScreens;
import net.mcreator.minigames.network.SelectClassMessage;
import net.mcreator.minigames.world.inventory.ClassSelectionRoguelikeMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class ClassSelectionRoguelikeScreen extends AbstractContainerScreen<ClassSelectionRoguelikeMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;

	private static final int TOOLTIP_BG = 0xF0100010;
	private static final int TOOLTIP_BORDER_LIGHT = 0xA05000FF;
	private static final int TOOLTIP_BORDER_DARK = 0xA028007F;
	private static final int TOOLTIP_BORDER_HOVER_LIGHT = 0xFFFFFFFF;
	private static final int TOOLTIP_BORDER_HOVER_DARK = 0xFFDDDDDD;

	private static final int COLOR_GOLD = 0xFFFFAA00;

	public static Identifier renderingSkinOverride = null;

	public record ClassDef(
			String id,
			String displayName,
			int color,
			Identifier customSkin,
			ItemStack heldItem,
			String playstyle,
			List<ItemStack> startingItems
	) {}

	private final List<ClassDef> classes = new ArrayList<>();
	private int activeDetailIndex = -1;

	private final List<Button> selectButtons = new ArrayList<>();
	private Button backButton;

	public ClassSelectionRoguelikeScreen(ClassSelectionRoguelikeMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 176, 166);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;

		setupClasses();
	}

	private void setupClasses() {
		classes.clear();

		classes.add(new ClassDef(
				"warrior",
				"Warrior",
				0xFFFF001F,
				Identifier.parse("minigames:textures/animation/roguelike/skin/warrior.png"),
				new ItemStack(MinigamesModItems.HAMMER.get()),
				"Specializes in dealing heavy damage",
				List.of(
						new ItemStack(MinigamesModItems.BLANK_SWORD.get()),
						new ItemStack(MinigamesModItems.CHOICE_BAG_WARRIOR.get())
				)
		));

		classes.add(new ClassDef(
				"support",
				"Support",
				0xFF09E2F6,
				Identifier.parse("minigames:textures/animation/roguelike/skin/support.png"),
				new ItemStack(MinigamesModItems.BLESSED_CURSED_CROSSBOW.get()),
				"Specializes in healing, buffing, and applying debuffs to monsters",
				List.of(
						new ItemStack(MinigamesModItems.BLANK_LONG_SWORD.get()),
						new ItemStack(MinigamesModItems.CHOICE_BAG_SUPPORT.get())
				)
		));

		classes.add(new ClassDef(
				"thief",
				"Thief",
				0xFFFFB700,
				Identifier.parse("minigames:textures/animation/roguelike/skin/thief.png"),
				new ItemStack(MinigamesModItems.SILENT_ASSASSIN.get()),
				"Specializes in movement and looting",
				List.of(
						new ItemStack(MinigamesModItems.BLANK_DAGGER.get()),
						new ItemStack(MinigamesModItems.CHOICE_BAG_THIEF.get())
				)
		));
	}

	@Override
	public void init() {
		super.init();
		rebuildClassWidgets();
	}

	private void rebuildClassWidgets() {
		this.clearWidgets();
		selectButtons.clear();

		int screenW = this.width;
		int screenH = this.height;

		int tooltipH = Math.min(screenH - 24, 380);
		int tooltipTop = (screenH - tooltipH) / 2;

		if (activeDetailIndex == -1) {
			int colCount = classes.size();
			int colW = Math.min(170, (screenW - 40) / colCount - 16);
			int totalColsW = colCount * colW + (colCount - 1) * 16;
			int startX = (screenW - totalColsW) / 2;

			for (int i = 0; i < colCount; i++) {
				final ClassDef cDef = classes.get(i);
				int cX = startX + i * (colW + 16);

				int btnW = colW - 20;
				int btnH = 20;
				int btnX = cX + 10;
				int btnY = tooltipTop + tooltipH - btnH - 12;

				Button btn = Button.builder(Component.literal("Select"), b -> {
					chooseClass(cDef.id());
				}).bounds(btnX, btnY, btnW, btnH).build();

				selectButtons.add(btn);
				this.addRenderableWidget(btn);
			}
		} else {
			int leftColW = Math.min(160, screenW / 5);
			int leftColX = 14;

			int btnW = leftColW - 20;
			int btnH = 20;
			int btnX = leftColX + 10;
			int btnY = tooltipTop + tooltipH - btnH - 12;

			final ClassDef cDef = classes.get(activeDetailIndex);
			Button btn = Button.builder(Component.literal("Select"), b -> {
				chooseClass(cDef.id());
			}).bounds(btnX, btnY, btnW, btnH).build();

			selectButtons.add(btn);
			this.addRenderableWidget(btn);

			int rightPanelX = leftColX + leftColW + 14;
			int rightPanelW = screenW - rightPanelX - 14;

			int backBtnW = Math.max(120, rightPanelW - 40);
			int backBtnH = 20;
			int backBtnX = rightPanelX + (rightPanelW - backBtnW) / 2;
			int backBtnY = tooltipTop + tooltipH - backBtnH - 12;

			backButton = Button.builder(Component.literal("Back to Class Selection"), b -> {
				activeDetailIndex = -1;
				rebuildClassWidgets();
			}).bounds(backBtnX, backBtnY, backBtnW, backBtnH).build();

			this.addRenderableWidget(backButton);
		}
	}

	private void chooseClass(String classId) {
		ClientPacketDistributor.sendToServer(new SelectClassMessage(classId));
		if (this.minecraft != null && this.minecraft.player != null) {
			this.minecraft.player.closeContainer();
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);

		guiGraphics.fill(0, 0, this.width, this.height, 0xFF000000);

		int screenW = this.width;
		int screenH = this.height;
		int tooltipH = Math.min(screenH - 24, 380);
		int tooltipTop = (screenH - tooltipH) / 2;
		int tooltipBottom = tooltipTop + tooltipH;

		if (activeDetailIndex == -1) {
			int colCount = classes.size();
			int colW = Math.min(170, (screenW - 40) / colCount - 16);
			int totalColsW = colCount * colW + (colCount - 1) * 16;
			int startX = (screenW - totalColsW) / 2;

			int btnH = 20;
			int btnY = tooltipTop + tooltipH - btnH - 12;

			for (int i = 0; i < colCount; i++) {
				ClassDef cDef = classes.get(i);
				int cX = startX + i * (colW + 16);
				int cRight = cX + colW;

				boolean hovered = (mouseX >= cX && mouseX <= cRight && mouseY >= tooltipTop && mouseY < btnY);

				drawTooltipBox(guiGraphics, cX, tooltipTop, cRight, tooltipBottom, hovered);

				float scale = 1.3F;
				Component title = Component.literal(cDef.displayName());
				int titleW = this.font.width(title);
				guiGraphics.pose().pushMatrix();
				guiGraphics.pose().scale(scale, scale);
				int tx = Math.round((cX + (colW / 2.0f)) / scale - (titleW / 2.0f));
				int ty = Math.round((tooltipTop + 14) / scale);
				guiGraphics.text(this.font, title, tx, ty, cDef.color(), true);
				guiGraphics.pose().popMatrix();

				renderClassSkinEntity(guiGraphics, cX + colW / 2, tooltipTop, tooltipBottom, colW, cDef);
			}
		} else {
			ClassDef cDef = classes.get(activeDetailIndex);

			int leftColW = Math.min(160, screenW / 5);
			int leftColX = 14;
			int leftColRight = leftColX + leftColW;

			drawTooltipBox(guiGraphics, leftColX, tooltipTop, leftColRight, tooltipBottom, false);

			float scale = 1.3F;
			Component title = Component.literal(cDef.displayName());
			int titleW = this.font.width(title);
			guiGraphics.pose().pushMatrix();
			guiGraphics.pose().scale(scale, scale);
			int tx = Math.round((leftColX + (leftColW / 2.0f)) / scale - (titleW / 2.0f));
			int ty = Math.round((tooltipTop + 14) / scale);
			guiGraphics.text(this.font, title, tx, ty, cDef.color(), true);
			guiGraphics.pose().popMatrix();

			renderClassSkinEntity(guiGraphics, leftColX + leftColW / 2, tooltipTop, tooltipBottom, leftColW, cDef);

			int rightPanelX = leftColRight + 14;
			int rightPanelW = screenW - rightPanelX - 14;
			int rightPanelRight = rightPanelX + rightPanelW;

			drawTooltipBox(guiGraphics, rightPanelX, tooltipTop, rightPanelRight, tooltipBottom, false);

			float descScale = 1.4F;
			Component descTitle = Component.literal("Description");
			int descW = this.font.width(descTitle);
			guiGraphics.pose().pushMatrix();
			guiGraphics.pose().scale(descScale, descScale);
			int dtx = Math.round((rightPanelX + (rightPanelW / 2.0f)) / descScale - (descW / 2.0f));
			int dty = Math.round((tooltipTop + 16) / descScale);
			guiGraphics.text(this.font, descTitle, dtx, dty, cDef.color(), true);
			guiGraphics.pose().popMatrix();

			int rightPanelCenterX = rightPanelX + rightPanelW / 2;

			int descContentTop = tooltipTop + 36;
			int descContentBottom = tooltipTop + tooltipH - 36;
			int descAvailableH = descContentBottom - descContentTop;

			boolean isThief = "thief".equals(cDef.id());
			int totalContentH = (10 + 14) + 18 + (10 + 14 + (isThief ? 14 : 0)) + 18 + (10 + 22);
			int contentStartY = descContentTop + Math.max(0, (descAvailableH - totalContentH) / 2);

			int playstyleHeaderY = contentStartY;
			Component playstyleHeader = Component.literal("PLAYSTYLE").setStyle(Style.EMPTY.withBold(true));
			int playstyleHeaderW = this.font.width(playstyleHeader);
			guiGraphics.text(this.font, playstyleHeader, rightPanelCenterX - playstyleHeaderW / 2, playstyleHeaderY, COLOR_GOLD, true);

			Component playstyleDesc = Component.literal(cDef.playstyle());
			int playstyleDescW = this.font.width(playstyleDesc);
			guiGraphics.text(this.font, playstyleDesc, rightPanelCenterX - playstyleDescW / 2, playstyleHeaderY + 14, 0xFFFFFFFF, true);

			int passiveHeaderY = playstyleHeaderY + 14 + 18;
			Component passiveHeader = Component.literal("PASSIVE ABILITIES").setStyle(Style.EMPTY.withBold(true));
			int headerW = this.font.width(passiveHeader);
			guiGraphics.text(this.font, passiveHeader, rightPanelCenterX - headerW / 2, passiveHeaderY, COLOR_GOLD, true);

			Component line2 = Component.literal("Can hold ")
					.append(Component.literal(cDef.displayName()).setStyle(Style.EMPTY.withColor(cDef.color())))
					.append(Component.literal(" only items."));
			int line2W = this.font.width(line2);
			guiGraphics.text(this.font, line2, rightPanelCenterX - line2W / 2, passiveHeaderY + 14, 0xFFFFFFFF, true);

			int afterPassiveY = passiveHeaderY + 28;
			if (isThief) {
				Component line3 = Component.literal("Can find and use ")
						.append(Component.literal("STOLEN").setStyle(Style.EMPTY.withBold(true).withColor(COLOR_GOLD)))
						.append(Component.literal(" items from other classes."));
				int line3W = this.font.width(line3);
				guiGraphics.text(this.font, line3, rightPanelCenterX - line3W / 2, afterPassiveY, 0xFFFFFFFF, true);
				afterPassiveY += 14;
			}

			int startingHeaderY = afterPassiveY + 18;
			Component startingHeader = Component.literal("STARTING ITEMS").setStyle(Style.EMPTY.withBold(true));
			int startingHeaderW = this.font.width(startingHeader);
			guiGraphics.text(this.font, startingHeader, rightPanelCenterX - startingHeaderW / 2, startingHeaderY, COLOR_GOLD, true);

			List<ItemStack> items = cDef.startingItems();
			int itemCount = items.size();
			int itemSlotSize = 22;
			int itemSpacing = 8;
			int totalItemsW = itemCount * itemSlotSize + (itemCount - 1) * itemSpacing;
			int itemsStartX = rightPanelCenterX - totalItemsW / 2;
			int itemSlotsY = startingHeaderY + 14;

			for (int idx = 0; idx < itemCount; idx++) {
				ItemStack stack = items.get(idx);
				int slotX = itemsStartX + idx * (itemSlotSize + itemSpacing);

				guiGraphics.fill(slotX, itemSlotsY, slotX + itemSlotSize, itemSlotsY + itemSlotSize, 0x88000000);
				guiGraphics.fill(slotX - 1, itemSlotsY - 1, slotX + itemSlotSize + 1, itemSlotsY, 0xAA555555);
				guiGraphics.fill(slotX - 1, itemSlotsY + itemSlotSize, slotX + itemSlotSize + 1, itemSlotsY + itemSlotSize + 1, 0xAA333333);
				guiGraphics.fill(slotX - 1, itemSlotsY, slotX, itemSlotsY + itemSlotSize, 0xAA555555);
				guiGraphics.fill(slotX + itemSlotSize, itemSlotsY, slotX + itemSlotSize + 1, itemSlotsY + itemSlotSize, 0xAA333333);

				guiGraphics.item(stack, slotX + 3, itemSlotsY + 3);
			}
		}
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);

		if (activeDetailIndex == -1) {
			int screenW = this.width;
			int screenH = this.height;
			int tooltipH = Math.min(screenH - 24, 380);
			int tooltipTop = (screenH - tooltipH) / 2;
			int colCount = classes.size();
			int colW = Math.min(170, (screenW - 40) / colCount - 16);
			int totalColsW = colCount * colW + (colCount - 1) * 16;
			int startX = (screenW - totalColsW) / 2;

			int btnH = 20;
			int btnY = tooltipTop + tooltipH - btnH - 12;

			for (int i = 0; i < colCount; i++) {
				int cX = startX + i * (colW + 16);
				int cRight = cX + colW;
				if (mouseX >= cX && mouseX <= cRight && mouseY >= tooltipTop && mouseY < btnY) {
					guiGraphics.setTooltipForNextFrame(this.font, Component.literal("Open Description"), mouseX, mouseY);
					break;
				}
			}
		} else {
			ClassDef cDef = classes.get(activeDetailIndex);
			int screenW = this.width;
			int screenH = this.height;
			int tooltipH = Math.min(screenH - 24, 380);
			int tooltipTop = (screenH - tooltipH) / 2;

			int leftColW = Math.min(160, screenW / 5);
			int leftColX = 14;
			int rightPanelX = leftColX + leftColW + 14;
			int rightPanelW = screenW - rightPanelX - 14;
			int rightPanelCenterX = rightPanelX + rightPanelW / 2;

			int descContentTop = tooltipTop + 36;
			int descContentBottom = tooltipTop + tooltipH - 36;
			int descAvailableH = descContentBottom - descContentTop;

			boolean isThief = "thief".equals(cDef.id());
			int totalContentH = (10 + 14) + 18 + (10 + 14 + (isThief ? 14 : 0)) + 18 + (10 + 22);
			int contentStartY = descContentTop + Math.max(0, (descAvailableH - totalContentH) / 2);

			int playstyleHeaderY = contentStartY;
			int passiveHeaderY = playstyleHeaderY + 14 + 18;
			int afterPassiveY = passiveHeaderY + 28;
			if (isThief) {
				afterPassiveY += 14;
			}
			int startingHeaderY = afterPassiveY + 18;
			int itemSlotsY = startingHeaderY + 14;

			List<ItemStack> items = cDef.startingItems();
			int itemCount = items.size();
			int itemSlotSize = 22;
			int itemSpacing = 8;
			int totalItemsW = itemCount * itemSlotSize + (itemCount - 1) * itemSpacing;
			int itemsStartX = rightPanelCenterX - totalItemsW / 2;

			for (int idx = 0; idx < itemCount; idx++) {
				int slotX = itemsStartX + idx * (itemSlotSize + itemSpacing);
				if (mouseX >= slotX && mouseX <= slotX + itemSlotSize && mouseY >= itemSlotsY && mouseY <= itemSlotsY + itemSlotSize) {
					guiGraphics.setTooltipForNextFrame(this.font, items.get(idx), mouseX, mouseY);
					break;
				}
			}
		}
	}

	private void renderClassSkinEntity(GuiGraphicsExtractor guiGraphics, int centerX, int tooltipTop, int tooltipBottom, int colW, ClassDef cDef) {
		LivingEntity livingEntity = this.minecraft != null && this.minecraft.player != null ? this.minecraft.player : this.entity;
		if (livingEntity == null) return;

		int titleH = 30;
		int buttonSpace = 40;
		int availableTop = tooltipTop + titleH;
		int availableBottom = tooltipBottom - buttonSpace;
		int availableH = availableBottom - availableTop;

		int scale = Math.min(Math.round(availableH * 0.42f), Math.round(colW * 0.45f));
		if (scale < 15) scale = 15;

		int x0 = centerX - colW / 2;
		int x1 = centerX + colW / 2;
		int y0 = availableTop;
		int y1 = availableBottom;

		ItemStack originalMainHand = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
		renderingSkinOverride = cDef.customSkin();
		if (!cDef.heldItem().isEmpty()) {
			livingEntity.setItemInHand(InteractionHand.MAIN_HAND, cDef.heldItem().copy());
		}

		try {
			float yOffset = 0.0625f;

			InventoryScreen.renderEntityInInventoryFollowsAngle(
					guiGraphics,
					x0,
					y0,
					x1,
					y1,
					scale,
					yOffset,
					0f,
					0f,
					livingEntity
			);
		} finally {
			renderingSkinOverride = null;
			livingEntity.setItemInHand(InteractionHand.MAIN_HAND, originalMainHand);
		}
	}

	private void drawTooltipBox(GuiGraphicsExtractor guiGraphics, int left, int top, int right, int bottom, boolean hovered) {
		guiGraphics.fill(left, top, right, bottom, TOOLTIP_BG);

		int borderLight = hovered ? TOOLTIP_BORDER_HOVER_LIGHT : TOOLTIP_BORDER_LIGHT;
		int borderDark = hovered ? TOOLTIP_BORDER_HOVER_DARK : TOOLTIP_BORDER_DARK;

		guiGraphics.fill(left - 1, top - 1, right + 1, top, borderLight);
		guiGraphics.fill(left - 1, top, left, bottom, borderLight);

		guiGraphics.fill(left - 1, bottom, right + 1, bottom + 1, borderDark);
		guiGraphics.fill(right, top, right + 1, bottom, borderDark);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == 0) {
			double mouseX = event.x();
			double mouseY = event.y();

			int screenW = this.width;
			int screenH = this.height;
			int tooltipH = Math.min(screenH - 24, 380);
			int tooltipTop = (screenH - tooltipH) / 2;
			int tooltipBottom = tooltipTop + tooltipH;

			if (activeDetailIndex == -1) {
				int colCount = classes.size();
				int colW = Math.min(170, (screenW - 40) / colCount - 16);
				int totalColsW = colCount * colW + (colCount - 1) * 16;
				int startX = (screenW - totalColsW) / 2;

				int btnH = 20;
				int btnY = tooltipTop + tooltipH - btnH - 12;

				for (int i = 0; i < colCount; i++) {
					int cX = startX + i * (colW + 16);
					int cRight = cX + colW;

					if (mouseX >= cX && mouseX <= cRight && mouseY >= tooltipTop && mouseY <= tooltipBottom) {
						int btnX = cX + 10;
						int btnRight = btnX + (colW - 20);
						boolean insideButton = (mouseX >= btnX && mouseX <= btnRight && mouseY >= btnY && mouseY <= btnY + btnH);

						if (!insideButton) {
							activeDetailIndex = i;
							rebuildClassWidgets();
							return true;
						}
					}
				}
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			if (activeDetailIndex != -1) {
				activeDetailIndex = -1;
				rebuildClassWidgets();
				return true;
			}
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}
}
