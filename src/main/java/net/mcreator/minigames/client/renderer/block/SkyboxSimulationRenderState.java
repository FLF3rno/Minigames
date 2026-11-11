package net.mcreator.minigames.client.renderer.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.mcreator.minigames.block.SkyboxSimulationBlock;

public class SkyboxSimulationRenderState extends BlockEntityRenderState {
	public SkyboxSimulationBlock.SkyType skyType = SkyboxSimulationBlock.SkyType.END;
	public BlockPos blockPos = BlockPos.ZERO;
	public Vec3 cameraPos = Vec3.ZERO;
	public float partialTick = 0.0f;
	public float celestialAngle = 0.0f;
	public float gameTime = 0.0f;
	public float rainLevel = 0.0f;
	public boolean isSpaceSky = false;
	public byte facesMask = 0;
}
