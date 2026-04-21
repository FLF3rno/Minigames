package net.mcreator.minigames.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import net.mcreator.minigames.world.inventory.DungeonInventoryMenu;
import net.mcreator.minigames.procedures.DisplayYourselfProcedure;
import net.mcreator.minigames.init.MinigamesModScreens;
import net.mcreator.minigames.network.MinigamesModVariables;

public class DungeonInventoryScreen extends AbstractContainerScreen<DungeonInventoryMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private static final ResourceLocation HOTBAR_SLOT_IMAGE = ResourceLocation.parse("minigames:textures/screens/inventorydungeon.png");
	private static final ResourceLocation BACKPACK_SLOT_IMAGE = ResourceLocation.parse("minigames:textures/screens/backpackdungeon.png");
	private static final ResourceLocation RELIC_SLOT_IMAGE = ResourceLocation.parse("minigames:textures/screens/relicdungeon.png");

	public DungeonInventoryScreen(DungeonInventoryMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RELIC_SLOT_IMAGE, this.leftPos + 25, this.topPos + 17, 0, 0, 20, 21, 20, 21);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RELIC_SLOT_IMAGE, this.leftPos + 49, this.topPos + 17, 0, 0, 20, 21, 20, 21);
		if (DisplayYourselfProcedure.execute(entity) instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + -953, this.topPos + -890, this.leftPos + 1047, this.topPos + 1110, 35, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()),
					0f + (float) Math.atan((this.leftPos + 47 - mouseX) / 40.0), (float) Math.atan((this.topPos + 61 - mouseY) / 40.0), livingEntity);
		}
		int playerSlots = Math.max(0, Math.min(9, (int) entity.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots));
		int backpackSlots = Math.max(0, Math.min(25, (int) entity.getData(MinigamesModVariables.PLAYER_VARIABLES).backpackSlots));
		renderPlayerSlotFrames(guiGraphics, playerSlots);
		renderBackpackSlotFrames(guiGraphics, backpackSlots);
	}

	private void renderPlayerSlotFrames(GuiGraphics guiGraphics, int playerSlots) {
		final int centerX = 77;
		final int y = 125;
		final int slotSpacing = 22;
		int startX = centerX - (playerSlots * slotSpacing) / 2;
		for (int i = 0; i < playerSlots; i++) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, HOTBAR_SLOT_IMAGE, this.leftPos + startX + i * slotSpacing, this.topPos + y, 0, 0, 20, 21, 20, 21);
		}
	}

	private void renderBackpackSlotFrames(GuiGraphics guiGraphics, int backpackSlots) {
		final int startX = 74;
		final int startY = 44;
		final int maxRowsPerColumn = 3;
		final int slotSpacing = 22;
		int maxSlots = Math.min(backpackSlots, 25);
		for (int i = 0; i < maxSlots; i++) {
			int column = i / maxRowsPerColumn;
			int row = i % maxRowsPerColumn;
			int x = startX + column * slotSpacing;
			int y = startY + row * slotSpacing;
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKPACK_SLOT_IMAGE, this.leftPos + x, this.topPos + y, 0, 0, 20, 21, 20, 21);
		}
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
	}
}
