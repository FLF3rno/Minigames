package net.mcreator.minigames.procedures;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class SpawnP1PedestalProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (MinigamesModVariables.MapVariables.get(world).SpawnItems) {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (entityiterator instanceof Player) {
					if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 1) {
						if ((entityiterator.level().dimension()) == ResourceKey.create(Registries.DIMENSION, Identifier.parse("minigames:dungeon_dimension"))) {
							BlockState pedestalState = BuiltInRegistries.BLOCK
									.getValue(Identifier.parse((("minigames:" + entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon + "_item_pedestal")).toLowerCase(java.util.Locale.ENGLISH))).defaultBlockState();
							if (pedestalState.getBlock().getStateDefinition().getProperty("owner") instanceof IntegerProperty ownerProperty
									&& ownerProperty.getPossibleValues().contains(1))
								pedestalState = pedestalState.setValue(ownerProperty, 1);
							world.setBlock(BlockPos.containing(x, y, z), pedestalState, 3);
						}
					}
				}
			}
		}
	}
}
