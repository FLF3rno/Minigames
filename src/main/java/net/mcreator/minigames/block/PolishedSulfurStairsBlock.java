package net.mcreator.minigames.block;

import net.neoforged.neoforge.common.util.DeferredSoundType;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

public class PolishedSulfurStairsBlock extends StairBlock {
	public PolishedSulfurStairsBlock(BlockBehaviour.Properties properties) {
		super(Blocks.AIR.defaultBlockState(),
				properties.sound(new DeferredSoundType(1.0f, 1.0f, () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_sulfur_break")),
						() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_sulfur_step")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_sulfur_place")),
						() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_sulfur_hit")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.big_fall")))).strength(1f, 10f));
	}

	@Override
	public float getExplosionResistance() {
		return 10f;
	}
}