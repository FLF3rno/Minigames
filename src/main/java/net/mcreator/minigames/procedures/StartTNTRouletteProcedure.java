package net.mcreator.minigames.procedures;

import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class StartTNTRouletteProcedure {
	public static void execute(LevelAccessor world) {
		double tnts = 0;
		double GridHeight = 0;
		double GridLenght = 0;
		double specials = 0;
		double powerup = 0;
		double rng = 0;
		double placed = 0;
		double row = 0;
		double column = 0;
		double currentID = 0;
		if (!world.isClientSide() && world.getServer() != null) {
			for (Entity entityiterator : new ArrayList<>(world.getServer().getPlayerList().getPlayers())) {
				if (entityiterator instanceof Player _player)
					_player.getInventory().clearContent();
				if (entityiterator instanceof ServerPlayer _player)
					_player.setGameMode(GameType.ADVENTURE);
			}
		}
		MinigamesModVariables.MapVariables.get(world).removeEffects = true;
		MinigamesModVariables.MapVariables.get(world).minimap = false;
		MinigamesModVariables.MapVariables.get(world).waypoints = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.clear();
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.PVP, false, world.getServer());
		MinigamesModVariables.MapVariables.get(world).playingTNTRoulette = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		tnts = MinigamesModVariables.MapVariables.get(world).TNTamountTNTRoulette;
		powerup = MinigamesModVariables.MapVariables.get(world).PowerupAmountTNTRoulette;
		GridHeight = MinigamesModVariables.MapVariables.get(world).TNTRouletteGridHeight;
		GridLenght = MinigamesModVariables.MapVariables.get(world).TNTRouletteGridLength;
		specials = tnts + powerup;
		for (int _i1 = 0; _i1 < (int) (GridHeight * GridLenght); _i1++) {
			MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.add("none");
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		placed = 0;
		while (tnts > placed) {
			column = Mth.nextInt(RandomSource.create(), 0, (int) (GridHeight - 1));
			row = Mth.nextInt(RandomSource.create(), 0, (int) (GridLenght - 1));
			currentID = row + column * GridLenght;
			if (!((MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) currentID) instanceof String _str8 ? _str8 : "").equals("none"))) {
				continue;
			}
			if (!(column == 0) && (MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) (currentID - GridLenght)) instanceof String _str9 ? _str9 : "").equals("tnt")) {
				continue;
			}
			if (!(column == GridHeight - 1) && (MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) (currentID + GridLenght)) instanceof String _str10 ? _str10 : "").equals("tnt")) {
				continue;
			}
			if (!(row == 0) && (MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) (currentID - 1)) instanceof String _str11 ? _str11 : "").equals("tnt")) {
				continue;
			}
			if (!(row == GridLenght - 1) && (MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) (currentID + 1)) instanceof String _str12 ? _str12 : "").equals("tnt")) {
				continue;
			}
			placed = placed + 1;
			MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.set((int) currentID, "tnt");
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		placed = 0;
		while (powerup > placed) {
			column = Mth.nextInt(RandomSource.create(), 0, (int) (GridHeight - 1));
			row = Mth.nextInt(RandomSource.create(), 0, (int) (GridLenght - 1));
			currentID = row + column * GridLenght;
			if (!((MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) currentID) instanceof String _str16 ? _str16 : "").equals("none"))) {
				continue;
			}
			if (!(column == 0) && (MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) (currentID - GridLenght)) instanceof String _str17 ? _str17 : "").equals("powerup")) {
				continue;
			}
			if (!(column == GridHeight - 1) && (MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) (currentID + GridLenght)) instanceof String _str18 ? _str18 : "").equals("powerup")) {
				continue;
			}
			if (!(row == 0) && (MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) (currentID - 1)) instanceof String _str19 ? _str19 : "").equals("powerup")) {
				continue;
			}
			if (!(row == GridLenght - 1) && (MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.get((int) (currentID + 1)) instanceof String _str20 ? _str20 : "").equals("powerup")) {
				continue;
			}
			placed = placed + 1;
			MinigamesModVariables.MapVariables.get(world).TNTRouletteGrid.set((int) currentID, "powerup");
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		TeleportToTNTRouletteProcedure.execute(world);
		GrantTNTRoulettePickaxeProcedure.execute(world);
	}
}