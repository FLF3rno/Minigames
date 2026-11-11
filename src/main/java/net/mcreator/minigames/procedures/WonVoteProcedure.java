package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Holder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.MinigamesMod;

import java.util.Optional;
import java.util.ArrayList;

public class WonVoteProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).respawningPlayers == 0) {
			if (MinigamesModVariables.MapVariables.get(world).voteType == 0) {
				StartAchievementRunProcedure.execute(world, x, y, z);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 1) {
				MinigamesModVariables.MapVariables.get(world).wantsToBeHuntedAchievement.clear();
				MinigamesModVariables.MapVariables.get(world).AchievementHunterOptInTimer = 200;
				MinigamesModVariables.MapVariables.get(world).achievementHunterMode = true;
				MinigamesModVariables.MapVariables.get(world).ShowOptInAchievement = true;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 2) {
				ResetCrownHuntProcedure.execute(world, x, y, z);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 3) {
				StartSpleefProcedure.execute(world, x, y, z);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 4) {
				StartRoomProcedure.execute(world);
				MinigamesModVariables.MapVariables.get(world).currentRoomType = "fight";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				ChangeCurrentRoomIDValueProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 5) {
				StartRoomProcedure.execute(world);
				MinigamesModVariables.MapVariables.get(world).currentRoomType = "loot";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				ChangeCurrentRoomIDValueProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 6) {
				StartRoomProcedure.execute(world);
				MinigamesModVariables.MapVariables.get(world).currentRoomType = "miniboss";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				ChangeCurrentRoomIDValueProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 7) {
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.IMMOBILIZED, 200, 1, false, false));
				}
				net.mcreator.minigames.network.PlayScreenAnimationMessage.sendToAll(world, 150, "roguelike_boss", 1.0f);
				MinigamesMod.queueServerWork(150, () -> {
					ChangeCurrentRoomIDValueProcedure.execute(world);
					StartBossProcedure.execute(world);
					StartRoomProcedure.execute(world);
					MinigamesModVariables.MapVariables.get(world).currentRoomType = "boss";
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				});
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 8) {
				StartRoomProcedure.execute(world);
				MinigamesModVariables.MapVariables.get(world).currentRoomType = "challenge";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				ChangeCurrentRoomIDValueProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 9) {
				StartRoomProcedure.execute(world);
				MinigamesModVariables.MapVariables.get(world).currentRoomType = "fight";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				ChangeCurrentRoomIDValueProcedure.execute(world);
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 10) {
				StartDungeonProcedure.execute(world, x, y, z);
				MinigamesModVariables.MapVariables.get(world).currentRoomType = "starting";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			} else if (MinigamesModVariables.MapVariables.get(world).voteType == 11) {
				if (world instanceof ServerLevel _level) {
					ServerClockManager _clockManager = _level.getServer().clockManager();
					Optional<Holder<WorldClock>> _clock = _level.dimensionType().defaultClock();
					if (_clock.isPresent())
						_clockManager.setTotalTicks(_clock.get(), 18000);
				}
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/execute in minigames:dungeon_dimension run tp @a -85 5 -69 facing -85 13 -100");
				SpawnShopRoomProcedure.execute(world, -100, 0, -100);
				MinigamesModVariables.MapVariables.get(world).currentRoomType = "shop";
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		} else {
			if (entity instanceof ServerPlayer _player)
				_player.sendSystemMessage(Component.literal("\u00A7cNot all players are alive!"), false);
		}
	}
}