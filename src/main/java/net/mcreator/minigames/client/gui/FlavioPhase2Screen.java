package net.mcreator.minigames.client.gui;

import net.mcreator.minigames.FlavioFightManager;
import net.mcreator.minigames.network.FailFlavioPacketMessage;
import net.mcreator.minigames.network.SucceedFlavioPacketMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import net.mcreator.minigames.init.MinigamesModScreens;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.world.inventory.FlavioPhase2Menu;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.Random;

public class FlavioPhase2Screen extends AbstractContainerScreen<FlavioPhase2Menu>
		implements MinigamesModScreens.ScreenAccessor {

	private final Level world;
	private final int x, y, z;
	private final Player entity;

	private final Identifier square = Identifier.fromNamespaceAndPath(
			"minigames", "textures/screens/square.png"
	);

	private final Identifier playerTop = Identifier.fromNamespaceAndPath(
			"minigames", "textures/screens/square.png"
	);

	private static final int OBJECTIVE = 100;
	private static final int GRID_WIDTH = 5;
	private static final float CELL_SIZE = 48f;
	private static final float PLAYER_SIZE = 48f;
	private static final float GRID_SCREEN_WIDTH = 0.70f;
	private static final float ROW_GAP = CELL_SIZE;
	private static final float ROW_HEIGHT = CELL_SIZE + ROW_GAP;

	private static final float MAZE_SCROLL_SPEED = 6.7f;
	private static final float PLAYER_TOP_LIMIT = 70f;
	private static final float X_EASE = 0.35f;
	private static final int CLIMB_DURATION_TICKS = 2;
	private static final int DEATH_ZONE_HEIGHT = 20;
	private static final int VICTORY_FADE_TICKS = 32;
	private static final int WALL_COLOR = 0xB3000000;

	private final Random random = new Random();
	private boolean gameStarted;
	private boolean gameOver;
	private boolean victory;
	private int ticksOpen;
	private int currentRow;
	private int rowsPassed;
	private float mazeOffset;
	private int playerGridX = 2;
	private float playerVisualX;
	private float playerTargetX;
	private float playerVisualY;
	private boolean isClimbing;
	private int climbTicks;
	private float climbStartY;
	private int[] maze;
	private boolean[][] visited = new boolean[OBJECTIVE][GRID_WIDTH];

	private boolean menuStateUpdateActive = false;
	private float victoryFade;
	private int victoryTicks;

	public FlavioPhase2Screen(
			FlavioPhase2Menu container,
			Inventory inventory,
			Component text
	) {
		super(container, inventory, text, 176, 166);

		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;

		generateMaze();
	}

	private void generateMaze() {
		maze = new int[OBJECTIVE];
		maze[0] = GRID_WIDTH / 2;

		for (int i = 1; i < OBJECTIVE; i++) {
			int previous = maze[i - 1];
			int next;

			do {
				next = random.nextInt(GRID_WIDTH);
			} while (next == previous);

			maze[i] = next;
		}
	}

	private void startGame() {
		if (gameStarted)
			return;

		gameStarted = true;

		playerGridX = GRID_WIDTH / 2;

		playerVisualX = getGridX(playerGridX);
		playerTargetX = playerVisualX;

		mazeOffset = 0f;

		currentRow = 0;
		rowsPassed = 0;

		visited = new boolean[OBJECTIVE][GRID_WIDTH];
		markVisited();

		isClimbing = false;
		climbTicks = 0;

		float centerYOffset = (getCellSize() - PLAYER_SIZE) / 2f;
		playerVisualY = getMazeScreenY(0) + centerYOffset;
	}

	private void markVisited() {
		if (currentRow >= 0 && currentRow < OBJECTIVE) {
			float cell = getCellSize();

			if (cell > 0) {
				float centerX = playerVisualX + PLAYER_SIZE / 2f;
				float relX = centerX - getGridStartX();

				int visualCol = (int) Math.floor(relX / cell);

				if (visualCol >= 0 && visualCol < GRID_WIDTH) {
					visited[currentRow][visualCol] = true;
				}
			}
		}
	}

	private float getCellSize() {
		return Math.min(
				CELL_SIZE,
				(width * GRID_SCREEN_WIDTH) / GRID_WIDTH
		);
	}

	private float getGridWidth() {
		return getCellSize() * GRID_WIDTH;
	}

	private float getGridStartX() {
		return (width - getGridWidth()) / 2f;
	}

	private float getGridX(int column) {
		return getGridStartX()
				+ column * getCellSize()
				+ (getCellSize() - PLAYER_SIZE) / 2f;
	}

	private void moveLeft() {
		if (!gameStarted || gameOver || victory || isClimbing)
			return;

		if (playerGridX <= 0)
			return;

		playerGridX--;
		playerTargetX = getGridX(playerGridX);
	}

	private void moveRight() {
		if (!gameStarted || gameOver || victory || isClimbing)
			return;

		if (playerGridX >= GRID_WIDTH - 1)
			return;

		playerGridX++;
		playerTargetX = getGridX(playerGridX);
	}

	private void checkCorrectColumn() {
		if (currentRow >= OBJECTIVE || isClimbing) {
			if (currentRow >= OBJECTIVE)
				beginVictory();

			return;
		}

		if (playerGridX == maze[currentRow]) {
			float cell = getCellSize();

			if (Math.abs(playerVisualX - playerTargetX) < cell * 0.4f) {
				completeMazeRow();
			}
		}
	}

	private void completeMazeRow() {
		if (currentRow >= OBJECTIVE || isClimbing)
			return;

		playerTargetX = getGridX(playerGridX);
		playerVisualX = playerTargetX;

		markVisited();

		currentRow++;
		rowsPassed++;

		if (currentRow >= OBJECTIVE) {
			beginVictory();
			return;
		}

		climbStartY = playerVisualY;
		isClimbing = true;
		climbTicks = 0;
	}

	private void updatePlayer() {
		float targetScreenY = getMazeScreenY(currentRow);

		if (targetScreenY < PLAYER_TOP_LIMIT) {
			float excess = PLAYER_TOP_LIMIT - targetScreenY;
			mazeOffset += excess;
		}

		if (!isClimbing) {
			playerVisualX = easeOut(
					playerVisualX,
					playerTargetX,
					X_EASE
			);
		} else {
			playerVisualX = getGridX(playerGridX);
		}

		markVisited();

		float centerYOffset =
				(getCellSize() - PLAYER_SIZE) / 2f;

		float targetY =
				getMazeScreenY(currentRow)
						+ centerYOffset;

		if (isClimbing) {
			climbTicks++;

			float progress =
					climbTicks / (float) CLIMB_DURATION_TICKS;

			if (progress >= 1f)
				progress = 1f;

			float smooth =
					(float) Math.sin(
							progress * Math.PI * 0.5f
					);

			playerVisualY =
					climbStartY
							+ (targetY - climbStartY) * smooth;

			if (progress >= 1f) {
				isClimbing = false;
				playerVisualY = targetY;
			}
		} else {
			playerVisualY = targetY;
		}
	}

	private void updateMaze() {
		mazeOffset += MAZE_SCROLL_SPEED;
	}

	private float getMazeScreenY(int row) {
		float firstRowY =
				height * 0.50f - ROW_HEIGHT;

		return firstRowY
				- row * ROW_HEIGHT
				+ mazeOffset;
	}

	private float easeOut(
			float current,
			float target,
			float amount
	) {
		float difference = target - current;

		if (Math.abs(difference) < 0.05f)
			return target;

		return current + difference * amount;
	}

	private void renderTrail(
			GuiGraphicsExtractor guiGraphics
	) {
		float cell = getCellSize();
		float startX = getGridStartX();

		int baseColor = getPlayerColor();

		int trailColor =
				(0x80 << 24)
						| (baseColor & 0x00FFFFFF);

		for (
				int r = 0;
				r <= Math.min(currentRow, OBJECTIVE - 1);
				r++
		) {
			float screenY =
					getMazeScreenY(r);

			if (
					screenY < -ROW_HEIGHT * 2f
							|| screenY > height + ROW_HEIGHT * 2f
			)
				continue;

			for (int c = 0; c < GRID_WIDTH; c++) {
				if (visited[r][c]) {
					int left =
							Math.round(
									startX + c * cell
							);

					int top =
							Math.round(screenY);

					int right =
							Math.round(
									startX + (c + 1) * cell
							);

					int bottom =
							Math.round(
									screenY + cell
							);

					guiGraphics.fill(
							left,
							top,
							right,
							bottom,
							trailColor
					);
				}
			}

			if (r < currentRow) {
				int column = maze[r];

				int left =
						Math.round(
								startX + column * cell
						);

				int top =
						Math.round(
								screenY - ROW_GAP
						);

				int right =
						Math.round(
								startX + (column + 1) * cell
						);

				int bottom =
						Math.round(screenY);

				guiGraphics.fill(
						left,
						top,
						right,
						bottom,
						trailColor
				);
			}
		}
	}

	private int getPlayerColor() {
		try {
			MinigamesModVariables.PlayerVariables vars =
					entity.getData(
							MinigamesModVariables.PLAYER_VARIABLES
					);

			String value = vars.classColor;

			if (
					value != null
							&& !value.isBlank()
							&& !value.equals("\"\"")
			) {
				String clean =
						value
								.replace("\"", "")
								.trim();

				if (
						clean.startsWith("0x")
								|| clean.startsWith("0X")
				) {
					clean = clean.substring(2);
				} else if (clean.startsWith("#")) {
					clean = clean.substring(1);
				}

				if (!clean.isEmpty()) {
					long parsed =
							Long.parseLong(clean, 16);

					return (int) (parsed & 0xFFFFFF);
				}
			}

			String classDungeon =
					vars.classDungeon;

			if (classDungeon != null) {
				switch (classDungeon.toLowerCase()) {
					case "warrior":
						return 0xFF001F;

					case "support":
						return 0x09E2F6;

					case "thief":
						return 0xFFFFB700;

					case "mage":
						return 0xFFFF7BFE;
				}
			}
		} catch (Exception ignored) {
		}

		return 0x09E2F6;
	}

	private void renderBackgroundGradient(
			GuiGraphicsExtractor guiGraphics
	) {
		float progress =
				Math.min(
						1f,
						rowsPassed / (float) OBJECTIVE
				);

		float gradientProgress =
				(float) Math.pow(
						progress,
						1.7
				);

		float virtualHeight =
				OBJECTIVE * ROW_HEIGHT;

		int strips = 160;

		for (int i = 0; i < strips; i++) {
			float worldPosition =
					(i / (float) strips)
							* virtualHeight
							+ mazeOffset;

			float normalized =
					worldPosition / virtualHeight;

			normalized =
					Math.max(
							0f,
							Math.min(
									1f,
									normalized
							)
					);

			float amount =
					Math.max(
							gradientProgress,
							normalized * gradientProgress
					);

			amount =
					(float) Math.pow(
							amount,
							1.35
					);

			int r =
					(int) (4 + 235 * amount);

			int g =
					(int) (7 + 242 * amount);

			int b =
					(int) (15 + 240 * amount);

			r = Math.min(255, r);
			g = Math.min(255, g);
			b = Math.min(255, b);

			int color =
					0xFF000000
							| (r << 16)
							| (g << 8)
							| b;

			int y1 =
					i * height / strips;

			int y2 =
					(i + 1) * height / strips;

			guiGraphics.fill(
					0,
					y1,
					width,
					y2,
					color
			);
		}
	}

	private void renderGrid(
			GuiGraphicsExtractor guiGraphics,
			float screenY
	) {
		float cell = getCellSize();
		float startX = getGridStartX();

		float progress =
				Math.min(
						1f,
						rowsPassed / (float) OBJECTIVE
				);

		float gp =
				(float) Math.pow(
						progress,
						1.7
				);

		int brightness =
				Math.max(
						0,
						Math.min(
								255,
								(int) ((1f - gp) * 255f)
						)
				);

		int gridColor =
				(0x66 << 24)
						| (brightness << 16)
						| (brightness << 8)
						| brightness;

		for (int column = 0; column < GRID_WIDTH; column++) {
			int left =
					Math.round(
							startX + column * cell
					);

			int top =
					Math.round(screenY);

			int right =
					Math.round(
							startX + (column + 1) * cell
					);

			int bottom =
					Math.round(
							screenY + cell
					);

			guiGraphics.fill(
					left,
					top,
					right,
					bottom,
					gridColor
			);
		}
	}

	private void renderConnections(
			GuiGraphicsExtractor guiGraphics,
			int row,
			float screenY
	) {
		if (row >= OBJECTIVE - 1)
			return;

		float cell = getCellSize();
		float startX = getGridStartX();

		float progress =
				Math.min(
						1f,
						rowsPassed / (float) OBJECTIVE
				);

		float gp =
				(float) Math.pow(
						progress,
						1.7
				);

		int brightness =
				Math.max(
						0,
						Math.min(
								255,
								(int) ((1f - gp) * 255f)
						)
				);

		int connectionColor =
				(0x99 << 24)
						| (brightness << 16)
						| (brightness << 8)
						| brightness;

		int correctColumn =
				maze[row];

		float connectionX =
				startX + correctColumn * cell;

		int left =
				Math.round(connectionX);

		int right =
				Math.round(connectionX + cell);

		int top =
				Math.round(screenY - ROW_GAP);

		int bottom =
				Math.round(screenY);

		guiGraphics.fill(
				left,
				top,
				right,
				bottom,
				connectionColor
		);
	}

	private void renderWalls(
			GuiGraphicsExtractor guiGraphics,
			int row,
			float screenY
	) {
		float cell = getCellSize();
		float startX = getGridStartX();

		int fromCol =
				(row == 0)
						? (GRID_WIDTH / 2)
						: maze[row - 1];

		int toCol =
				maze[row];

		int minCol =
				Math.min(
						fromCol,
						toCol
				);

		int maxCol =
				Math.max(
						fromCol,
						toCol
				);

		for (int column = 0; column < GRID_WIDTH; column++) {
			if (
					column >= minCol
							&& column <= maxCol
			)
				continue;

			int left =
					Math.round(
							startX + column * cell
					);

			int top =
					Math.round(screenY);

			int right =
					Math.round(
							startX + (column + 1) * cell
					);

			int bottom =
					Math.round(
							screenY + cell
					);

			guiGraphics.fill(
					left,
					top,
					right,
					bottom,
					WALL_COLOR
			);
		}
	}

	private void renderMaze(
			GuiGraphicsExtractor guiGraphics
	) {
		for (int row = 0; row < OBJECTIVE; row++) {
			float screenY =
					getMazeScreenY(row);

			if (
					screenY < -ROW_HEIGHT * 2f
							|| screenY > height + ROW_HEIGHT * 2f
			)
				continue;

			renderGrid(
					guiGraphics,
					screenY
			);

			if (row >= currentRow) {
				renderConnections(
						guiGraphics,
						row,
						screenY
				);

				renderWalls(
						guiGraphics,
						row,
						screenY
				);
			}
		}
	}

	private void renderPlayer(
			GuiGraphicsExtractor guiGraphics
	) {
		int drawX =
				Math.round(playerVisualX);

		int drawY =
				Math.round(playerVisualY);

		Identifier texture =
				victory
						? playerTop
						: square;

		guiGraphics.blit(
				RenderPipelines.GUI_TEXTURED,
				texture,
				drawX,
				drawY,
				0f,
				0f,
				48,
				48,
				48,
				48,
				0xFFFFFFFF
		);
	}

	private void renderDeathZone(
			GuiGraphicsExtractor guiGraphics
	) {
		int top =
				height - DEATH_ZONE_HEIGHT;

		guiGraphics.fill(
				0,
				top,
				width,
				height,
				0x88000000
		);

		guiGraphics.fill(
				0,
				top,
				width,
				top + 2,
				0xAA441111
		);
	}

	private boolean isInDeathZone() {
		return playerVisualY + PLAYER_SIZE
				>= height - DEATH_ZONE_HEIGHT;
	}

	private void loseGame() {
		if (gameOver)
			return;

		gameOver = true;

		Minecraft.getInstance().setScreen(null);
		if (world.isClientSide())
			ClientPacketDistributor.sendToServer(new FailFlavioPacketMessage(""));
	}

	private void beginVictory() {
		if (victory)
			return;

		victory = true;
		victoryTicks = 0;
		victoryFade = 0f;
	}

	private void updateVictory() {
		if (!victory)
			return;

		victoryTicks++;

		victoryFade +=
				1f / VICTORY_FADE_TICKS;

		victoryFade =
				Math.min(
						1f,
						victoryFade
				);

		if (
				victoryFade >= 1f
						&& victoryTicks > VICTORY_FADE_TICKS + 8
		) {
			Minecraft.getInstance().setScreen(null);
			ClientPacketDistributor.sendToServer(new SucceedFlavioPacketMessage(""));
		}
	}

	private void renderVictoryFade(
			GuiGraphicsExtractor guiGraphics
	) {
		if (
				!victory
						|| victoryFade <= 0f
		)
			return;

		int alpha =
				Math.max(
						0,
						Math.min(
								255,
								(int) (victoryFade * 255f)
						)
				);

		guiGraphics.fill(
				0,
				0,
				width,
				height,
				(alpha << 24) | 0xFFFFFF
		);
	}

	@Override
	public void containerTick() {
		super.containerTick();

		ticksOpen++;

		if (!gameStarted) {
			startGame();
			return;
		}

		if (gameOver)
			return;

		if (victory) {
			updateVictory();
			return;
		}

		updateMaze();
		updatePlayer();
		markVisited();
		checkCorrectColumn();

		if (isInDeathZone())
			loseGame();
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.key() == 256)
			return true;

		Minecraft minecraft =
				Minecraft.getInstance();

		KeyMapping left =
				minecraft.options.keyLeft;

		KeyMapping right =
				minecraft.options.keyRight;

		if (left.matches(event)) {
			moveLeft();
			return true;
		}

		if (right.matches(event)) {
			moveRight();
			return true;
		}

		return super.keyPressed(event);
	}

	@Override
	public void extractRenderState(
			GuiGraphicsExtractor guiGraphics,
			int mouseX,
			int mouseY,
			float partialTicks
	) {
		renderBackgroundGradient(guiGraphics);
		renderMaze(guiGraphics);
		renderTrail(guiGraphics);
		renderPlayer(guiGraphics);
		renderDeathZone(guiGraphics);
		renderVictoryFade(guiGraphics);
	}

	@Override
	public void extractBackground(
			GuiGraphicsExtractor guiGraphics,
			int mouseX,
			int mouseY,
			float partialTicks
	) {
	}

	@Override
	protected void extractLabels(
			GuiGraphicsExtractor guiGraphics,
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
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	@Override
	public void init() {
		super.init();
	}
}