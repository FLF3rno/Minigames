package net.mcreator.minigames.block;

import net.neoforged.neoforge.common.util.DeferredSoundType;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public class CinnabarBrickStairsBlock extends StairBlock {
	public CinnabarBrickStairsBlock(BlockBehaviour.Properties properties) {
		super(Blocks.AIR.defaultBlockState(),
				properties
						.sound(new DeferredSoundType(1.0f, 1.0f, () -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:update_cinnabar_break")),
								() -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:update_cinnabar_step")), () -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:update_cinnabar_place")),
								() -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:update_cinnabar_hit")), () -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("entity.generic.big_fall"))))
						.strength(1f, 10f));
	}

	@Override
	public float getExplosionResistance() {
		return 10f;
	}
}