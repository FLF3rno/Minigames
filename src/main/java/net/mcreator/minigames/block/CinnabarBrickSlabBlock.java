package net.mcreator.minigames.block;

import net.neoforged.neoforge.common.util.DeferredSoundType;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

public class CinnabarBrickSlabBlock extends SlabBlock {
	public CinnabarBrickSlabBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(new DeferredSoundType(1.0f, 1.0f, () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_cinnabar_break")),
				() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_cinnabar_step")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_cinnabar_place")),
				() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_cinnabar_hit")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.big_fall")))).strength(1f, 10f));
	}
}