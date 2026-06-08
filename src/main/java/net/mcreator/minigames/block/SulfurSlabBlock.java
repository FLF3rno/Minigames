package net.mcreator.minigames.block;

import net.neoforged.neoforge.common.util.DeferredSoundType;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

public class SulfurSlabBlock extends SlabBlock {
	public SulfurSlabBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(new DeferredSoundType(1.0f, 1.0f, () -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:update_sulfur_break")),
				() -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:update_sulfur_step")), () -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:update_sulfur_place")),
				() -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:update_sulfur_hit")), () -> BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("entity.generic.big_fall")))).strength(1f, 10f));
	}
}