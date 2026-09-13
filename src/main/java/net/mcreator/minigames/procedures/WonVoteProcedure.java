package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.network.PlayScreenAnimationMessage;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.MinigamesMod;

import java.util.ArrayList;

public class WonVoteProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).respawningPlayers == 0) {
			if (MinigamesModVariables.MapVariables.get(world).voteType == 0) {
				StartAchievementRunProcedure.execute(world, x, y, z);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 1) {
				MinigamesModVariables.MapVariables.get(world).achievementHunterMode = true;
				MinigamesModVariables.MapVariables.get(world).randomHunterAchievement = true;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				StartAchievementRunProcedure.execute(world, x, y, z);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 2) {
				ResetCrownHuntProcedure.execute(world, x, y, z);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 3) {
				StartSpleefProcedure.execute(world, x, y, z);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 4) {
				StartRoomProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 5) {
				StartRoomProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 6) {
				StartRoomProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 7) {
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.IMMOBILIZED, 200, 1, false, false));
				}
				PlayScreenAnimationMessage.sendToAll(world, 150, "roguelike_boss", 1.0f);
				MinigamesMod.queueServerWork(200, () -> {
					StartBossProcedure.execute(world);
					StartRoomProcedure.execute(world);
				});
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 8) {
				StartRoomProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 9) {
				StartRoomProcedure.execute(world);
			}
		} else {
			if (entity instanceof ServerPlayer _player)
				_player.sendSystemMessage(Component.literal("\u00A7cNot all players are alive!"), false);
		}
	}
}