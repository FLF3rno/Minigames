package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.server.ServerLifecycleHooks;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class SpawnP2PedestalProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((world instanceof Level _lvl ? _lvl.dimension() : (world instanceof WorldGenLevel _wgl ? _wgl.getLevel().dimension() : Level.OVERWORLD)) == ResourceKey.create(Registries.DIMENSION, Identifier.parse("minigames:dungeon_dimension"))) {
			if ((world.isClientSide() ? Minecraft.getInstance().getConnection().getOnlinePlayers().size() : ServerLifecycleHooks.getCurrentServer().getPlayerCount()) < 2) {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			} else {
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					if (entityiterator instanceof Player || entityiterator instanceof ServerPlayer) {
						if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).team == 2) {
							{
								BlockPos _bp = BlockPos.containing(x, y, z);
								BlockState _bs = BuiltInRegistries.BLOCK
										.getValue(Identifier.parse((("minigames:" + entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon + "_item_pedestal")).toLowerCase(java.util.Locale.ENGLISH))).defaultBlockState();
								BlockState _bso = world.getBlockState(_bp);
								for (Property<?> _propertyOld : _bso.getProperties()) {
									Property _propertyNew = _bs.getBlock().getStateDefinition().getProperty(_propertyOld.getName());
									if (_propertyNew != null && _bs.getValue(_propertyNew) != null)
										try {
											_bs = _bs.setValue(_propertyNew, _bso.getValue(_propertyOld));
										} catch (Exception e) {
										}
								}
								world.setBlock(_bp, _bs, 3);
							}
						}
					}
				}
			}
		}
	}
}