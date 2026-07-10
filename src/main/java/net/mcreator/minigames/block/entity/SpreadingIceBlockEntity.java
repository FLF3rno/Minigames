package net.mcreator.minigames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.mcreator.minigames.init.MinigamesModBlockEntities;

public class SpreadingIceBlockEntity extends BlockEntity {
	public SpreadingIceBlockEntity(BlockPos pos, BlockState state) {
		super(MinigamesModBlockEntities.SPREADING_ICE.get(), pos, state);
	}
}
