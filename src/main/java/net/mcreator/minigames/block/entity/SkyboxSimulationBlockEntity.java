package net.mcreator.minigames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mcreator.minigames.init.MinigamesModBlockEntities;

public class SkyboxSimulationBlockEntity extends BlockEntity {
	public byte cachedFaces = -1;

	public SkyboxSimulationBlockEntity(BlockPos pos, BlockState state) {
		super(MinigamesModBlockEntities.SKYBOX_SIMULATION.get(), pos, state);
	}
}
