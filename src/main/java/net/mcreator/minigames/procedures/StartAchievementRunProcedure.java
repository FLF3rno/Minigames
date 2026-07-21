package net.mcreator.minigames.procedures;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.MenuProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.mcreator.minigames.world.inventory.SelectCategoryAchievementMenu;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

import java.util.Optional;
import java.util.ArrayList;

import io.netty.buffer.Unpooled;

public class StartAchievementRunProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		long seed = RandomSource.create().nextLong();
		while (seed == 0L) {
			seed = RandomSource.create().nextLong();
		}
		MinigamesModVariables.MapVariables.get(world).AchievementSeed = seed;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (MinigamesModVariables.MapVariables.get(world).useOverworld1) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute in minigames:achievement_1 run spreadplayers 0 0 0 3 true @a");
		} else {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/execute in minigames:achievement_2 run spreadplayers 0 0 0 3 true @a");
		}
		waitForPlayersInDimension(world, () -> {
			RollAchievementProcedure.execute(world);
			MinigamesModVariables.MapVariables.get(world).playingAchievement = true;
			MinigamesModVariables.MapVariables.get(world).ShowTimer = true;
			MinigamesModVariables.MapVariables.get(world).minimap = false;
			MinigamesModVariables.MapVariables.get(world).waypoints = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.TimerColor = "C7C7C7";
					_vars.markSyncDirty();
				}
				if (entityiterator instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ());
					_ent.openMenu(new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("SelectCategoryAchievement");
						}

						@Override
						public boolean shouldTriggerClientSideContainerClosingOnOpen() {
							return false;
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new SelectCategoryAchievementMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.AchievementLobbyState = "";
					_vars.markSyncDirty();
				}
				if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 60, 19, false, false));
				if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.INSTANT_HEALTH, 60, 19, false, false));
				if (entityiterator instanceof ServerPlayer _player)
					_player.setGameMode(GameType.CREATIVE);
				if (entityiterator instanceof Player _player)
					_player.getInventory().clearContent();
				ClearEnderchestProcedure.execute(entityiterator);
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.timerHours = 0;
					_vars.timerMinutes = 0;
					_vars.timerSeconds = 0;
					_vars.timerTick = 0;
					_vars.timerSpeed = 0;
					_vars.markSyncDirty();
				}
			}
			MinigamesModVariables.MapVariables.get(world).removeEffects = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "worldborder set 50000000");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "xp set @a 0 levels ");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "xp set @a 0");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "advancement revoke @a everything");
			if (world instanceof ServerLevel _serverLevel)
				_serverLevel.getGameRules().set(GameRules.ADVANCE_WEATHER, true, world.getServer());
			if (world instanceof ServerLevel _serverLevel)
				_serverLevel.getGameRules().set(GameRules.ADVANCE_TIME, true, world.getServer());
			if (world instanceof ServerLevel _serverLevel)
				_serverLevel.getGameRules().set(GameRules.LOCATOR_BAR, false, world.getServer());
			if (world instanceof ServerLevel _level) {
				ServerClockManager _clockManager = _level.getServer().clockManager();
				Optional<Holder<WorldClock>> _clock = _level.dimensionType().defaultClock();
				if (_clock.isPresent())
					_clockManager.setTotalTicks(_clock.get(), 0);
			}
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/weather clear");
			if (MinigamesModVariables.MapVariables.get(world).AchievementModifier == 1) {
				if (world instanceof ServerLevel _level) {
					ServerClockManager _clockManager = _level.getServer().clockManager();
					Optional<Holder<WorldClock>> _clock = _level.dimensionType().defaultClock();
					if (_clock.isPresent())
						_clockManager.setTotalTicks(_clock.get(), 18000);
				}
				if (world instanceof ServerLevel _serverLevel)
					_serverLevel.getGameRules().set(GameRules.ADVANCE_TIME, false, world.getServer());
			} else if (MinigamesModVariables.MapVariables.get(world).AchievementModifier == 2) {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/weather thunder");
				if (world instanceof ServerLevel _serverLevel)
					_serverLevel.getGameRules().set(GameRules.ADVANCE_WEATHER, false, world.getServer());
			}
		});


	
	}
	private static void waitForPlayersInDimension(LevelAccessor world, Runnable callback) {
	MinecraftServer server = world.getServer();

	if (server == null)
		return;

	MinigamesMod.queueServerWork(5, () -> {

		ResourceKey<Level> targetDimension;

		if (MinigamesModVariables.MapVariables.get(world).useOverworld1) {
			targetDimension = ResourceKey.create(
					Registries.DIMENSION,
					Identifier.parse("minigames:achievement_1")
			);
		} else {
			targetDimension = ResourceKey.create(
					Registries.DIMENSION,
					Identifier.parse("minigames:achievement_2")
			);
		}


		boolean allLoaded = true;

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {

			if (player.level().dimension() != targetDimension) {
				allLoaded = false;
				break;
			}

			if (!player.isAlive() || !player.level().isLoaded(player.blockPosition())) {
				allLoaded = false;
				break;
			}
		}


		if (allLoaded) {
			callback.run();
		} else {
			waitForPlayersInDimension(world, callback);
		}
	});
}
}