package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class NameColorApplyProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!(world instanceof ServerLevel _level))
			return;
		if (!MinigamesModVariables.MapVariables.get(world).applyCustomNameColor)
			return;
		applyColor(world, entity);
		MinigamesModVariables.MapVariables.get(world).applyCustomNameColor = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}

	public static void applyColor(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!(world instanceof ServerLevel _level))
			return;
		String color = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
		if (color == null || color.isEmpty())
			return;
		String teamName = (entity instanceof Player _player) ? _player.getGameProfile().getName() : entity.getStringUUID();
		if (_level.getScoreboard().getPlayerTeam(teamName) == null)
			_level.getScoreboard().addPlayerTeam(teamName);
		{
			Entity _entityTeam = entity;
			PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam(teamName);
			if (_pt != null) {
				if (_entityTeam instanceof Player _player)
					_entityTeam.level().getScoreboard().addPlayerToTeam(_player.getGameProfile().getName(), _pt);
				else
					_entityTeam.level().getScoreboard().addPlayerToTeam(_entityTeam.getStringUUID(), _pt);
				if (_pt.getColor() == null || !_pt.getColor().getName().equals(color)) {
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(entity.getX(), entity.getY(), entity.getZ()), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null)
									.withSuppressedOutput(),
							"/team modify " + teamName + " color " + color);
				}
			}
		}
	}
}
