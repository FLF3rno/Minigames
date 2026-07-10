package net.mcreator.minigames.block;

import net.neoforged.neoforge.common.util.DeferredSoundType;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

public class PolishedSulfurBlock extends Block {
	public PolishedSulfurBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(new DeferredSoundType(1.0f, 1.0f, () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_sulfur_break")),
				() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_sulfur_step")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_sulfur_place")),
				() -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:update_sulfur_hit")), () -> BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.generic.big_fall")))).strength(1f, 10f));
	}
}