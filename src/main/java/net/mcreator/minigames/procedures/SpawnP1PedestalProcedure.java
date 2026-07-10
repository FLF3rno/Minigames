package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class SpawnP1PedestalProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (entityiterator instanceof Player || entityiterator instanceof ServerPlayer) {
				if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 1) {
					if ((entityiterator.level().dimension()) == ResourceKey.create(Registries.DIMENSION, Identifier.parse("minigames:dungeon_dimension"))) {
						world.setBlock(BlockPos.containing(x, y, z), BuiltInRegistries.BLOCK
								.getValue(Identifier.parse((("minigames:" + entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon + "_item_pedestal")).toLowerCase(java.util.Locale.ENGLISH))).defaultBlockState(), 3);
					}
				}
			}
		}
	}
}