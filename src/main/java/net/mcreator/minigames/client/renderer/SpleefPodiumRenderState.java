package net.mcreator.minigames.client.renderer;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import java.util.UUID;

public class SpleefPodiumRenderState extends HumanoidRenderState {
    public Identifier skinTexture = Identifier.parse("minigames:textures/entities/empty.png");
    public UUID playerUuid = null;
    public int position = 0;
}