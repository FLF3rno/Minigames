package net.mcreator.minigames.client.gui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.mcreator.minigames.util.EffectUtils;
import java.util.List;
import net.mcreator.minigames.init.MinigamesModScreens;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.procedures.DisplayYourselfProcedure;
import net.mcreator.minigames.world.inventory.DungeonInventoryMenu;


public class DungeonInventoryScreen extends AbstractContainerScreen<DungeonInventoryMenu>
		implements MinigamesModScreens.ScreenAccessor {


	private static final Identifier MAIN_LEFT =
			Identifier.parse("minigames:textures/gui/sprites/inventory/main_left.png");

	private static final Identifier MAIN_MIDDLE =
			Identifier.parse("minigames:textures/gui/sprites/inventory/main_middle.png");

	private static final Identifier MAIN_RIGHT =
			Identifier.parse("minigames:textures/gui/sprites/inventory/main_right.png");


	private static final Identifier BACKPACK_LEFT =
			Identifier.parse("minigames:textures/gui/sprites/inventory/backpack_left.png");

	private static final Identifier BACKPACK_MIDDLE =
			Identifier.parse("minigames:textures/gui/sprites/inventory/backpack_middle.png");

	private static final Identifier BACKPACK_RIGHT =
			Identifier.parse("minigames:textures/gui/sprites/inventory/backpack_right.png");


	private static final Identifier SLOT =
			Identifier.parse("minecraft:textures/gui/sprites/container/slot.png");

	private static final Identifier BACKPACK_SLOT =
			Identifier.parse("minigames:textures/gui/sprites/container/backpack_slot.png");
	private static final Identifier POTION_LEFT =
			Identifier.parse("minigames:textures/gui/sprites/inventory/potion_left.png");

	private static final Identifier POTION_MIDDLE =
			Identifier.parse("minigames:textures/gui/sprites/inventory/potion_middle.png");

	private static final Identifier POTION_RIGHT =
			Identifier.parse("minigames:textures/gui/sprites/inventory/potion_right.png");

	private static final int POTION_HEIGHT = 16;
	private static final int POTION_SPACING = 2;
	private static final int POTION_ICON_SIZE = 18;
	private static final int POTION_LEVEL_WIDTH = 12;

	private static final int POTION_BAR_HEIGHT = 6;

	private static final int SLOT_SPACING = 22;
	private static final int RELIC_COLOR_A = 0xFF8F25A8;
	private static final int RELIC_COLOR_B = 0xFFEEABEE;
	private static final int SLOT_SIZE = 18;
	private static final int PANEL_HEIGHT = 112;
	private static final java.util.Map<String, Integer> potionInitialDurations =
			new java.util.HashMap<>();
	private final Player player;

	public DungeonInventoryScreen(
			DungeonInventoryMenu menu,
			Inventory inventory,
			Component title
	) {
		super(menu, inventory, title);

		this.player = menu.entity;
	}


	private int playerSlots() {

		return Math.max(
				0,
				Math.min(
						9,
						(int) player.getData(
								MinigamesModVariables.PLAYER_VARIABLES
						).playerSlots
				)
		);
	}


	private int backpackSlots() {

		return Math.max(
				0,
				Math.min(
						27,
						(int) player.getData(
								MinigamesModVariables.PLAYER_VARIABLES
						).backpackSlots
				)
		);
	}


	private int mainWidth() {

		int visibleColumns = Math.max(5, playerSlots());

		return visibleColumns * SLOT_SPACING + 8;
	}


	private int backpackWidth() {

		int columns = Math.max(
				1,
				(int)Math.ceil(backpackSlots() / 3.0)
		);

		return columns * SLOT_SPACING + 8;
	}
	private int potionPanelWidth() {
		return mainWidth() + 10 + backpackWidth();
	}


	@Override
	public void extractRenderState(
			GuiGraphicsExtractor graphics,
			int mouseX,
			int mouseY,
			float partialTick
	) {
		super.extractRenderState(
				graphics,
				mouseX,
				mouseY,
				partialTick
		);

		int totalWidth =
				mainWidth()
						+ 10
						+ backpackWidth();

		leftPos =
				(width - totalWidth) / 2;

		topPos =
				(height - PANEL_HEIGHT) / 2;
	}


	@Override
	public void extractBackground(
			GuiGraphicsExtractor graphics,
			int mouseX,
			int mouseY,
			float partialTick
	) {

		super.extractBackground(
				graphics,
				mouseX,
				mouseY,
				partialTick
		);


		renderMainPanel(graphics);
		renderBackpackPanel(graphics);

		renderRelics(graphics);
		renderHotbar(graphics);
		renderBackpackSlots(graphics);

		renderPlayer(graphics);
		renderPotionEffects(graphics);
	}


	private void renderMainPanel(
			GuiGraphicsExtractor graphics
	) {

		int width = mainWidth();


		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				MAIN_LEFT,
				leftPos,
				topPos,
				0,
				0,
				4,
				PANEL_HEIGHT,
				4,
				PANEL_HEIGHT
		);


		for(int x = 0; x < width - 8; x++) {

			graphics.blit(
					RenderPipelines.GUI_TEXTURED,
					MAIN_MIDDLE,
					leftPos + 4 + x,
					topPos,
					0,
					0,
					1,
					PANEL_HEIGHT,
					1,
					PANEL_HEIGHT
			);
		}


		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				MAIN_RIGHT,
				leftPos + width - 4,
				topPos,
				0,
				0,
				4,
				PANEL_HEIGHT,
				4,
				PANEL_HEIGHT
		);
	}


	private void renderBackpackPanel(
			GuiGraphicsExtractor graphics
	) {

		int x =
				leftPos + mainWidth() + 10;

		int width =
				backpackWidth();


		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				BACKPACK_LEFT,
				x,
				topPos,
				0,
				0,
				4,
				112,
				4,
				112
		);


		for(int i = 0; i < width - 8; i++) {

			graphics.blit(
					RenderPipelines.GUI_TEXTURED,
					BACKPACK_MIDDLE,
					x + 4 + i,
					topPos,
					0,
					0,
					1,
					112,
					1,
					112
			);
		}


		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				BACKPACK_RIGHT,
				x + width - 4,
				topPos,
				0,
				0,
				4,
				112,
				4,
				112
		);
	}


	private void renderRelics(
			GuiGraphicsExtractor graphics
	) {

		drawSlotColor(
				graphics,
				leftPos + 70,
				topPos + 24
		);

		drawSlotColor(
				graphics,
				leftPos + 70,
				topPos + 42
		);
	}


	private void renderHotbar(
			GuiGraphicsExtractor graphics
	) {

		int slots = playerSlots();

		int startX =
				leftPos +
						(mainWidth() - slots * SLOT_SIZE) / 2;


		for(int i = 0; i < slots; i++) {

			drawSlot(
					graphics,
					startX + i * SLOT_SIZE,
					topPos + 88
			);
		}
	}


	private void renderBackpackSlots(
			GuiGraphicsExtractor graphics
	) {

		int slots = backpackSlots();

		int rows = 3;

		int columns = (int)Math.ceil(slots / 3.0);


		int slotSize = 18;
		int spacing = 22;


		int gridWidth = columns * spacing - (spacing - slotSize);
		int gridHeight = rows * spacing - (spacing - slotSize);


		int panelX =
				leftPos + mainWidth() + 10;


		int startX =
				panelX + 4 + ((backpackWidth() - 8) - gridWidth) / 2;


		int startY =
				topPos + 4 + ((PANEL_HEIGHT - 8) - gridHeight) / 2;


		for(int i = 0; i < slots; i++) {

			int column = i / rows;
			int row = i % rows;


			drawBackpackSlot(
					graphics,
					startX + column * spacing,
					startY + row * spacing
			);
		}
	}


	private void drawSlot(
			GuiGraphicsExtractor graphics,
			int x,
			int y
	) {

		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				SLOT,
				x,
				y,
				0,
				0,
				SLOT_SIZE,
				SLOT_SIZE,
				SLOT_SIZE,
				SLOT_SIZE
		);
	}
	private int getAnimatedRelicColor() {

		double seconds = System.currentTimeMillis() / 1000.0;

		float t = (float)((Math.sin(seconds * (2.0 * Math.PI / 8.0)) + 1.0) * 0.5);

		return lerpColor(RELIC_COLOR_A, RELIC_COLOR_B, t);
	}

	private static int lerpColor(int a, int b, float t) {

		int aa = (a >>> 24) & 0xFF;
		int ar = (a >>> 16) & 0xFF;
		int ag = (a >>> 8) & 0xFF;
		int ab = a & 0xFF;

		int ba = (b >>> 24) & 0xFF;
		int br = (b >>> 16) & 0xFF;
		int bg = (b >>> 8) & 0xFF;
		int bb = b & 0xFF;

		int alpha = (int)(aa + (ba - aa) * t);
		int red   = (int)(ar + (br - ar) * t);
		int green = (int)(ag + (bg - ag) * t);
		int blue  = (int)(ab + (bb - ab) * t);

		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}
	private void drawSlotColor(
			GuiGraphicsExtractor graphics,
			int x,
			int y
	) {

		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				SLOT,
				x,
				y,
				0,
				0,
				SLOT_SIZE,
				SLOT_SIZE,
				SLOT_SIZE,
				SLOT_SIZE,
				getAnimatedRelicColor()
		);
	}


	private void drawBackpackSlot(
			GuiGraphicsExtractor graphics,
			int x,
			int y
	) {

		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				BACKPACK_SLOT,
				x,
				y,
				0,
				0,
				SLOT_SIZE,
				SLOT_SIZE,
				SLOT_SIZE,
				SLOT_SIZE
		);
	}


	private void renderPlayer(
			GuiGraphicsExtractor graphics
	) {

		if(DisplayYourselfProcedure.execute(player)
				instanceof LivingEntity living) {


			InventoryScreen.renderEntityInInventoryFollowsAngle(
					graphics,
					leftPos + 5,
					topPos + 10,
					leftPos + 55,
					topPos + 75,
					32,
					0.0625F,
					0,
					0,
					living
			);
		}
	}
	private void renderPotionEffects(
			GuiGraphicsExtractor graphics
	) {

		List<MobEffectInstance> effects =
				player.getActiveEffects()
						.stream()
						.collect(java.util.stream.Collectors.toMap(
								e -> e.getEffect().value(),
								e -> e,
								(a, b) -> a.getAmplifier() >= b.getAmplifier() ? a : b
						))
						.values()
						.stream()
						.filter(effect -> !EffectUtils.isHiddenEffect(effect) && !EffectUtils.isHiddenRoguelikeEffect(effect))
						.toList();


		potionInitialDurations.keySet().removeIf(
				key -> effects.stream()
						.noneMatch(instance ->
								key.equals(getPotionKey(instance))
						)
		);


		if (effects.isEmpty()) {
			return;
		}


		int panelX = leftPos;
		int panelWidth = potionPanelWidth();

		int startY =
				topPos + PANEL_HEIGHT + 4;


		for (int i = 0; i < effects.size(); i++) {

			MobEffectInstance effect = effects.get(i);

			int y =
					startY +
							i * (POTION_HEIGHT + POTION_SPACING);


			renderPotionPanel(
					graphics,
					panelX,
					y,
					panelWidth
			);


			renderPotionEffect(
					graphics,
					effect,
					panelX,
					y,
					panelWidth
			);
		}
	}
	private String getPotionKey(MobEffectInstance effect) {

		return effect.getEffect().value().toString()
				+ ":"
				+ effect.getAmplifier();
	}


	private void renderPotionPanel(
			GuiGraphicsExtractor graphics,
			int x,
			int y,
			int width
	) {

		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				POTION_LEFT,
				x,
				y,
				0,
				0,
				4,
				POTION_HEIGHT,
				4,
				POTION_HEIGHT
		);


		for (int i = 0; i < width - 8; i++) {

			graphics.blit(
					RenderPipelines.GUI_TEXTURED,
					POTION_MIDDLE,
					x + 4 + i,
					y,
					0,
					0,
					1,
					POTION_HEIGHT,
					1,
					POTION_HEIGHT
			);
		}


		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				POTION_RIGHT,
				x + width - 4,
				y,
				0,
				0,
				4,
				POTION_HEIGHT,
				4,
				POTION_HEIGHT
		);
	}


	private void renderPotionEffect(
			GuiGraphicsExtractor graphics,
			MobEffectInstance effect,
			int panelX,
			int y,
			int panelWidth
	) {

		int level =
				effect.getAmplifier() + 1;


		graphics.text(
				this.font,
				String.valueOf(level),
				panelX + 3,
				y + 4,
				0xFFFFFFFF
		);


		int iconX = panelX + POTION_LEVEL_WIDTH + 2;


		renderEffectIcon(
				graphics,
				effect,
				iconX,
				y + 2
		);


		int barX =
				iconX +
						12 +
						3;


		int barWidth =
				panelX +
						panelWidth -
						4 -
						barX;


		if(barWidth > 0) {

			float progress =
					getPotionProgress(effect);


			int filled =
					(int)(barWidth * progress);


			graphics.fill(
					barX,
					y + 5,
					barX + filled,
					y + 11,
					getPotionColor(effect)
			);
		}
	}


	private float getPotionProgress(MobEffectInstance effect) {

		int duration = effect.getDuration();

		if (duration < 0) {
			return 1.0F;
		}


		String key = getPotionKey(effect);


		int initialDuration =
				potionInitialDurations.computeIfAbsent(
						key,
						k -> duration
				);


		return Math.max(
				0.0F,
				Math.min(
						1.0F,
						(float) duration / (float) initialDuration
				)
		);
	}


	private void renderEffectIcon(
			GuiGraphicsExtractor graphics,
			MobEffectInstance effect,
			int x,
			int y
	) {

		graphics.blitSprite(
				RenderPipelines.GUI_TEXTURED,
				Gui.getMobEffectSprite(effect.getEffect()),
				x,
				y,
				12,
				12
		);
	}


	private int getPotionColor(
			MobEffectInstance effect
	) {

		return 0xFF000000 |
				effect.getEffect()
						.value()
						.getColor();
	}


	@Override
	protected void extractLabels(
			GuiGraphicsExtractor graphics,
			int mouseX,
			int mouseY
	) {
	}

	@Override
	public void updateMenuState(
			int elementType,
			String name,
			Object elementState
	) {

	}
}