package net.mcreator.minigames.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.init.MinigamesModScreens;
import net.mcreator.minigames.network.ChoiceBagRewardMessage;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.world.inventory.ChoiceBundleMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChoiceBundleScreen extends AbstractContainerScreen<ChoiceBundleMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;

	private static final Identifier SINGULAR_SLOT = Identifier.parse("minigames:textures/screens/singular_slot.png");
	private static final Identifier HOTBAR_SLOT = Identifier.parse("minigames:textures/screens/hotbar.png");

	private static final int ITEM_SPACING = 36;
	private static final int NUM_COLUMNS = 3;

	private final ReelColumn[] columns = new ReelColumn[NUM_COLUMNS];
	private final Random random = new Random();
	private boolean initializedReels = false;
	private boolean itemClaimed = false;

	private static class ReelColumn {
		List<Item> pool = new ArrayList<>();
		List<Item> tape = new ArrayList<>();
		float position = 0f;
		float speed = 0f;
		float deceleration = 0f;
		float targetPosition = 0f;
		float totalDistance = 0f;
		float totalTicks = 0f;
		float elapsedTicks = 0f;
		boolean stopped = false;
		int targetIndex = 0;
		int lastPassedIndex = -1;
	}

	public ChoiceBundleScreen(ChoiceBundleMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 176, 166);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void init() {
		super.init();
		if (!initializedReels) {
			initReels();
			initializedReels = true;
		}
	}

	private void initReels() {
		String classDungeon = "";
		Player p = Minecraft.getInstance().player != null ? Minecraft.getInstance().player : this.entity;
		if (p != null) {
			classDungeon = p.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon;
		}
		if (classDungeon == null || classDungeon.isBlank()) {
			classDungeon = "warrior";
		}
		classDungeon = classDungeon.trim().toLowerCase();

		List<Item> classPool = new ArrayList<>();
		for (Item item : BuiltInRegistries.ITEM) {
			ItemStack testStack = new ItemStack(item);
			if (DungeonItemAccess.canClassPickUp(testStack, classDungeon) && DungeonItemAccess.isDungeonItem(testStack)) {
				String path = BuiltInRegistries.ITEM.getKey(item).getPath();
				if (!path.contains("choice_bag")) {
					classPool.add(item);
				}
			}
		}

		if (classPool.isEmpty()) {
			for (Item item : BuiltInRegistries.ITEM) {
				ItemStack testStack = new ItemStack(item);
				if (DungeonItemAccess.isDungeonItem(testStack)) {
					classPool.add(item);
				}
			}
		}

		for (int col = 0; col < NUM_COLUMNS; col++) {
			ReelColumn reel = new ReelColumn();
			reel.pool = new ArrayList<>(classPool);
			if (reel.pool.isEmpty()) {
				reel.pool.add(net.minecraft.world.item.Items.DIAMOND);
			}

			int tapeSize = 90 + col * 20;
			for (int i = 0; i < tapeSize; i++) {
				reel.tape.add(reel.pool.get(random.nextInt(reel.pool.size())));
			}

			reel.targetIndex = tapeSize - 10;

			reel.targetPosition = reel.targetIndex * ITEM_SPACING;
			reel.totalDistance = reel.targetPosition;
			reel.totalTicks = 70f + col * 25f; // ~3.5s to 6s for gradual deceleration
			reel.elapsedTicks = 0f;
			reel.position = 0f;
			reel.stopped = false;
			reel.lastPassedIndex = 0;

			columns[col] = reel;
		}
	}

	@Override
	protected void containerTick() {
		super.containerTick();

		boolean dingedThisTick = false;

		for (int col = 0; col < NUM_COLUMNS; col++) {
			ReelColumn reel = columns[col];
			if (reel == null || reel.stopped) continue;

			reel.elapsedTicks += 1.0f;
			float t = Math.min(1.0f, reel.elapsedTicks / reel.totalTicks);

			float progress = 1.0f - (float) Math.pow(1.0 - t, 4);
			reel.position = progress * reel.totalDistance;

			if (t >= 1.0f || reel.position >= reel.targetPosition) {
				reel.position = reel.targetPosition;
				reel.stopped = true;
				Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING, 2.0F));
				dingedThisTick = true;
			} else {
				int currentIndex = (int) Math.floor(reel.position / ITEM_SPACING);
				if (currentIndex > reel.lastPassedIndex) {
					reel.lastPassedIndex = currentIndex;
					if (!dingedThisTick) {
						float pitch = 1.0F + (col * 0.15F);
						Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_PLING, pitch));
						dingedThisTick = true;
					}
				}
			}
		}
	}

	private int getColumnCenterX(int col) {
		float screenCenter = this.width / 2.0f;
		float spacing = (this.width * 0.6f) / 4.0f; // spacing between column centers
		return Math.round(screenCenter + (col - 1) * spacing);
	}

	private int getMiddleY() {
		return this.height / 2;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);

		guiGraphics.fill(0, 0, this.width, this.height, 0xC0101015);

		int centerY = getMiddleY();

		for (int col = 0; col < NUM_COLUMNS; col++) {
			int cx = getColumnCenterX(col);

			guiGraphics.fill(cx - 26, centerY - 65, cx + 26, centerY + 65, 0xDD1a1a24);
			drawColumnBorder(guiGraphics, cx - 26, centerY - 65, 52, 130);

			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SINGULAR_SLOT, cx - 16, centerY - 16, 0, 0, 32, 32, 32, 32);

			ReelColumn reel = columns[col];
			if (reel == null) continue;

			float currentPos = reel.position;

			int centerItemIndex = Math.round(currentPos / ITEM_SPACING);
			for (int i = centerItemIndex - 3; i <= centerItemIndex + 3; i++) {
				if (i >= 0 && i < reel.tape.size()) {
					Item item = reel.tape.get(i);
					ItemStack stack = new ItemStack(item);

					float itemY = centerY + (i * ITEM_SPACING - currentPos);

					if (itemY >= centerY - 60 && itemY <= centerY + 60) {
						int drawX = cx - 8;
						int drawY = Math.round(itemY - 8);

						guiGraphics.item(stack, drawX, drawY);
					}
				}
			}

			if (reel.stopped) {
				boolean isHovered = (mouseX >= cx - 16 && mouseX <= cx + 16 && mouseY >= centerY - 16 && mouseY <= centerY + 16);
				int highlightColor = isHovered ? 0x5500FF00 : 0x22FFFFFF;
				guiGraphics.fill(cx - 16, centerY - 16, cx + 16, centerY + 16, highlightColor);
			}
		}

		renderHeaderTitles(guiGraphics);
	}

	private int getTitleColor() {
		String classDungeon = "";
		Player p = Minecraft.getInstance().player != null ? Minecraft.getInstance().player : this.entity;
		if (p != null) {
			classDungeon = p.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon;
		}
		if (classDungeon != null) {
			classDungeon = classDungeon.trim().toLowerCase();
			if (classDungeon.equals("warrior")) {
				return 0xFFFF001F; // Red
			} else if (classDungeon.equals("support")) {
				return 0xFF09E2F6; // Light Blue
			} else if (classDungeon.equals("thief")) {
				return 0xFFFFD700; // Gold / Yellow (as it currently was)
			} else if (classDungeon.equals("mage")) {
				return 0xFFFF7BFE; // Mage Purple / Pink
			}
		}
		return 0xFFFFD700;
	}

	private void renderHeaderTitles(GuiGraphicsExtractor guiGraphics) {
		float titleScale = 2.0F;
		Component titleComp = Component.literal("Choice Bundle Reward");
		int titleWidth = this.font.width(titleComp);

		guiGraphics.pose().pushMatrix();
		guiGraphics.pose().scale(titleScale, titleScale);
		int titleX = Math.round((this.width / 2.0f) / titleScale - (titleWidth / 2.0f));
		int titleY = Math.round(14.0f / titleScale);
		guiGraphics.text(this.font, titleComp, titleX, titleY, getTitleColor(), true);
		guiGraphics.pose().popMatrix();

		int finishedColumns = 0;
		for (int col = 0; col < NUM_COLUMNS; col++) {
			if (columns[col] != null && columns[col].stopped) {
				finishedColumns++;
			}
		}

		if (finishedColumns > 0) {
			float subScale = 1.35F;
			String fullText = "Choose an item";
			int fullTextWidth = this.font.width(fullText);

			float fullStartX = (this.width / 2.0f) / subScale - (fullTextWidth / 2.0f);
			int subY = Math.round((14.0f + this.font.lineHeight * titleScale + 6.0f) / subScale);

			String[] words = new String[]{"Choose", "an", "item"};

			guiGraphics.pose().pushMatrix();
			guiGraphics.pose().scale(subScale, subScale);

			float currentOffset = 0f;
			for (int w = 0; w < words.length; w++) {
				String word = words[w];
				if (w < finishedColumns) {
					int wordX = Math.round(fullStartX + currentOffset);
					guiGraphics.text(this.font, Component.literal(word), wordX, subY, 0xFFE0E0E0, true);
				}
				currentOffset += this.font.width(word + " ");
			}

			guiGraphics.pose().popMatrix();
		}
	}

	private void drawColumnBorder(GuiGraphicsExtractor guiGraphics, int x, int y, int w, int h) {
		int goldBorder = 0xFFD4AF37;
		int darkEdge = 0xFF4A3B10;
		guiGraphics.fill(x - 1, y - 1, x + w + 1, y, goldBorder);
		guiGraphics.fill(x - 1, y + h, x + w + 1, y + h + 1, darkEdge);
		guiGraphics.fill(x - 1, y, x, y + h, goldBorder);
		guiGraphics.fill(x + w, y, x + w + 1, y + h, darkEdge);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);

		int centerY = getMiddleY();

		for (int col = 0; col < NUM_COLUMNS; col++) {
			ReelColumn reel = columns[col];
			if (reel != null && reel.stopped) {
				int cx = getColumnCenterX(col);
				if (mouseX >= cx - 16 && mouseX <= cx + 16 && mouseY >= centerY - 16 && mouseY <= centerY + 16) {
					if (reel.targetIndex >= 0 && reel.targetIndex < reel.tape.size()) {
						ItemStack targetStack = new ItemStack(reel.tape.get(reel.targetIndex));
						guiGraphics.setTooltipForNextFrame(this.font, targetStack, mouseX, mouseY);
					}
				}
			}
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == 0 && !itemClaimed) {
			int centerY = getMiddleY();
			double mouseX = event.x();
			double mouseY = event.y();

			for (int col = 0; col < NUM_COLUMNS; col++) {
				ReelColumn reel = columns[col];
				if (reel != null && reel.stopped) {
					int cx = getColumnCenterX(col);
					if (mouseX >= cx - 16 && mouseX <= cx + 16 && mouseY >= centerY - 16 && mouseY <= centerY + 16) {
						if (reel.targetIndex >= 0 && reel.targetIndex < reel.tape.size()) {
							itemClaimed = true;
							Item chosenItem = reel.tape.get(reel.targetIndex);
							String itemId = BuiltInRegistries.ITEM.getKey(chosenItem).toString();

							ClientPacketDistributor.sendToServer(new ChoiceBagRewardMessage(itemId));

							if (this.minecraft != null && this.minecraft.player != null) {
								this.minecraft.player.closeContainer();
							}
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
			if (this.minecraft != null && this.minecraft.player != null) {
				this.minecraft.player.closeContainer();
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