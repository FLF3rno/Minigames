package net.mcreator.minigames.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class AzureFroglightBlock extends Block {
	public AzureFroglightBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.FROGLIGHT).strength(1f, 10f).lightLevel(blockstate -> 15).postProcess((bs, br, bp) -> bp).emissiveRendering((bs, br, bp) -> true));
	}
}