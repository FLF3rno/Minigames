package net.mcreator.minigames.client.gui;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
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
import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.minigames.world.inventory.BattleBoxMenu;
import net.mcreator.minigames.procedures.GetDisplayNameProcedure;
import net.mcreator.minigames.procedures.DisplayYourselfProcedure;
import net.mcreator.minigames.init.MinigamesModScreens;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.multiplayer.PlayerInfo;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.gui.components.PlayerFaceRenderer;

public class BattleBoxScreen extends AbstractContainerScreen<BattleBoxMenu> implements MinigamesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = true;
	private static final ResourceLocation IMAGE_0 = ResourceLocation.parse("minigames:textures/screens/attack.png");
	private static final ResourceLocation IMAGE_1 = ResourceLocation.parse("minigames:textures/screens/defend.png");
	private static final ResourceLocation IMAGE_3 = ResourceLocation.parse("minigames:textures/screens/soul.png");

	public BattleBoxScreen(BattleBoxMenu container, Inventory inventory, Component text) {
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
		menuStateUpdateActive = false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		if (DisplayYourselfProcedure.execute(entity) instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + -1104, this.topPos + -883, this.leftPos + 896, this.topPos + 1117, 40, -livingEntity.getBbHeight() / (2.0f * livingEntity.getScale()), -2f, 0, livingEntity);
		}
		this.renderTooltip(guiGraphics, mouseX, 
		mouseY);
	}

	private void renderPlayerFace(GuiGraphics guiGraphics, Player player, int x, int y, int size, int color) {
		GameProfile profile = player.getGameProfile();

		PlayerSkin playerSkin = Minecraft.getInstance().getSkinManager().getInsecureSkin(profile);

		int tint = 0xFF000000 | color;

		PlayerFaceRenderer.draw(guiGraphics, playerSkin, x, y, size, tint);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		int tint;
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).selectedButtonBattleBox == 0) tint = 0xFFfdf502;
		else tint = 0xFFff9b00;
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_0, this.leftPos + 55, this.topPos + 167, 0, 0, 32, 32, 32, 32, tint);
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).selectedButtonBattleBox == 1) tint = 0xFFfdf502;
		else tint = 0xFFff9b00;
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_1, this.leftPos + 89, this.topPos + 167, 0, 0, 32, 32, 32, 32, tint);
		String colorString = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
		if (colorString.startsWith("#")) {
			colorString = colorString.substring(1);
		}
		int colorValue;
		try {
			colorValue = Integer.parseInt(colorString, 16);
		} catch (NumberFormatException e) {
			colorValue = 0xFFFFFF;
		}
		tint = 0xFF000000 | colorValue;
		Player player = Minecraft.getInstance().player;
		if (player != null) {
		renderPlayerFace(guiGraphics, player, this.leftPos + 80, this.topPos + 130, 16, tint);
		}
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, IMAGE_3, this.leftPos + 84, this.topPos + 78, 0, 0, 8, 8, 8, 8, tint);
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) //esc
		{
			return true;
		}
		if (key == 65 || key == 263) //65 is A, 263 is Left Arrow
		{
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.selectedButtonBattleBox = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).selectedButtonBattleBox - 1;
				_vars.markSyncDirty();
			}
			if (world.isClientSide()) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("block.note_block.bit")), SoundSource.UI, 1, 2);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("block.note_block.bit")), SoundSource.UI, 1, 2, false);
					}
				}
			}

		}
		if (key == 68 || key == 262) //68 is D, 262 is Right Arrow
		{
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.selectedButtonBattleBox = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).selectedButtonBattleBox + 1;
				_vars.markSyncDirty();
			}
			if (world.isClientSide()) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("block.note_block.bit")), SoundSource.UI, 1, 2);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("block.note_block.bit")), SoundSource.UI, 1, 2, false);
					}
				}
			}

		}
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).selectedButtonBattleBox < 0)
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.selectedButtonBattleBox = 1;
			_vars.markSyncDirty();
		}
		else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).selectedButtonBattleBox > 1)
		{
			MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.selectedButtonBattleBox = 0;
			_vars.markSyncDirty();
		}
		if (key == 90) //90 is Z
		{

		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, GetDisplayNameProcedure.execute(entity), 55, 152, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		long windowHandle = Minecraft.getInstance().getWindow().getWindow();
		GLFW.glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
	}
	@Override
	public void removed() {
		super.removed();
		long windowHandle = Minecraft.getInstance().getWindow().getWindow();
		GLFW.glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}
}