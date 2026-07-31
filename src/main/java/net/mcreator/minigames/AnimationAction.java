package net.mcreator.minigames;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface AnimationAction {
    boolean isAlive(int currentTick);

    default int getLayer() {
        return 0;
    }

    void render(GuiGraphicsExtractor graphics, int currentTick, int screenWidth, int screenHeight);
}