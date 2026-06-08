package net.mcreator.minigames.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class EmptyCoarseDirtBlock extends Block {
	public EmptyCoarseDirtBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.GRAVEL).strength(-1, 3600000));
	}
}