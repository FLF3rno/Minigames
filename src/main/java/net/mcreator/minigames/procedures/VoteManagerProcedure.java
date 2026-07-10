package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

import java.util.ArrayList;

@EventBusSubscriber
public class VoteManagerProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		double numberOfPlayersThatAreWillingToParticipateInYourBuffoonery = 0;
		double numberOfPlayersThatFuckingHateYourIdea = 0;
		double requiredNumberOfPlayers = 0;
		double requiredNoVotes = 0;
		if (world.isClientSide()) {
			return;
		}
		if (MinigamesModVariables.MapVariables.get(world).ActiveVote) {
			numberOfPlayersThatAreWillingToParticipateInYourBuffoonery = 0;
			numberOfPlayersThatFuckingHateYourIdea = 0;
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (entityiterator instanceof Player) {
					if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).voted) {
						if (entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).votedYes) {
							numberOfPlayersThatAreWillingToParticipateInYourBuffoonery = numberOfPlayersThatAreWillingToParticipateInYourBuffoonery + 1;
						}
						if (!entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES).votedYes) {
							numberOfPlayersThatFuckingHateYourIdea = numberOfPlayersThatFuckingHateYourIdea + 1;
						}
					}
				}
			}
			int onlinePlayers = ServerLifecycleHooks.getCurrentServer().getPlayerCount();
			requiredNumberOfPlayers = onlinePlayers - Math.floor((onlinePlayers - 1) / 3d);
			requiredNoVotes = (onlinePlayers - requiredNumberOfPlayers) + 1;
			if (numberOfPlayersThatAreWillingToParticipateInYourBuffoonery >= requiredNumberOfPlayers) {
				if (!(MinigamesModVariables.VotingEntity == null)) {
					MinigamesModVariables.MapVariables.get(world).ActiveVote = false;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					WonVoteProcedure.execute(world, 0, 0, 0, MinigamesModVariables.VotingEntity);
				} else {
					if (world instanceof ServerLevel _level) {
						_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Error: Vote Entity Null").withColor(0xe43939), false);
					}
				}
			} else if (numberOfPlayersThatFuckingHateYourIdea >= requiredNoVotes) {
				if (!(MinigamesModVariables.VotingEntity == null)) {
					MinigamesModVariables.MapVariables.get(world).ActiveVote = false;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					LostVoteProcedure.execute(MinigamesModVariables.VotingEntity);
				}
			}
		}
	}
}




