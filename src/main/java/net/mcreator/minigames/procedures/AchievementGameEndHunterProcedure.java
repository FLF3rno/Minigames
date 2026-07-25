package net.mcreator.minigames.procedures;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.MinigamesMod;

import java.util.ArrayList;

public class AchievementGameEndHunterProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof Level _level) {
			PlayerTeam _pt = _level.getScoreboard().getPlayerTeam("hunted");
			if (_pt != null)
				_level.getScoreboard().removePlayerTeam(_pt);
		}
		MinigamesModVariables.MapVariables.get(world).playingAchievement = false;
		MinigamesModVariables.MapVariables.get(world).achievementHunterMode = false;
		MinigamesModVariables.MapVariables.get(world).randomHunterAchievement = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (MinigamesModVariables.MapVariables.get(world).useOverworld1) {
			MinigamesModVariables.MapVariables.get(world).useOverworld1 = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		} else {
			MinigamesModVariables.MapVariables.get(world).useOverworld1 = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		}
		MinigamesModVariables.MapVariables.get(world).showWinscreen = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		MinigamesMod.queueServerWork(160, () -> {
			MinigamesModVariables.MapVariables.get(world).showWinscreen = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			MinigamesModVariables.MapVariables.get(world).WinnerList.clear();
			MinigamesModVariables.MapVariables.get(world).ShowTimer = false;
			MinigamesModVariables.MapVariables.get(world).waypoints = true;
			MinigamesModVariables.MapVariables.get(world).minimap = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				GrantGameCompassProcedure.execute(world, entityiterator);
			}
		});
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.timerSpeed = 0;
				_vars.TimerColor = "29FF2B";
				_vars.markSyncDirty();
			}
			if (entityiterator instanceof Player _player)
				_player.closeContainer();
			{
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.AchievementLobbyState = "";
				_vars.markSyncDirty();
			}
			if (entityiterator instanceof ServerPlayer _player)
				_player.setGameMode(GameType.CREATIVE);
			{
				Entity _ent = entityiterator;
				if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
					_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
							LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound minigames:teameliminated ui @s ~ ~ ~ 1 1");
				}
			}
			if (entityiterator instanceof LivingEntity _entity)
				_entity.removeAllEffects();
			MinigamesModVariables.MapVariables.get(world).WinnerList.clear();
			if (!(entityiterator.getStringUUID()).equals(MinigamesModVariables.MapVariables.get(world).hunterAchievementUUID)) {
				MinigamesModVariables.MapVariables.get(world).WinnerList.add((entityiterator.getStringUUID()));
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.CROWNED, 99999999, 0, false, false));
			}
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "worldborder set 50000000");
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.ADVANCE_WEATHER, true, world.getServer());
		if (world instanceof ServerLevel _serverLevel)
			_serverLevel.getGameRules().set(GameRules.ADVANCE_TIME, true, world.getServer());
	}
}