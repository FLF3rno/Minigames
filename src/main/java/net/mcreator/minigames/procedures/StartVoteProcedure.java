package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.ArrayList;

public class StartVoteProcedure {
	public static void execute(LevelAccessor world, Entity entity, Entity voting_player, String type) {
		if (entity == null || voting_player == null || type == null)
			return;
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).voteCooldown <= 0) {
			MinigamesModVariables.VotingEntity = voting_player;
			if ((type).equals("achievement run")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 0;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to start a game of Achievement Run";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((type).equals("achievement hunt")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 1;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to start a game of Achievement Hunt";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((type).equals("crown hunt")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 2;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to start a game of Crown Hunt";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((type).equals("spleef")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 3;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to start a game of Spleef";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((type).equals("fight room")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 4;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to enter a Fight Room";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((type).equals("loot room")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 5;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to enter a Loot Room";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((type).equals("miniboss room")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 6;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to enter a Miniboss Room";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((type).equals("boss room")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 7;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to enter the Boss Room";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((type).equals("challenge room")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 8;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to start the Challenge";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if ((type).equals("floor")) {
				MinigamesModVariables.MapVariables.get(world).voteType = 9;
				MinigamesModVariables.MapVariables.get(world).VotingMessage = "Wants to start the Floor";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.votedYes = false;
					_vars.voted = false;
					_vars.markSyncDirty();
				}
			}
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.votedYes = true;
				_vars.voted = true;
				_vars.markSyncDirty();
			}
			MinigamesModVariables.MapVariables.get(world).ActiveVote = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("\u00A7cYou cannot call another vote for " + new java.text.DecimalFormat("##").format(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).voteCooldown / 20) + "s")), true);
		}
	}
}