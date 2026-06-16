package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ManageMinimapProcedure {
	private static final String LAST_MINIMAP_STATE = "minigames_last_minimap_state";

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
		if (entity.level().isClientSide() || entity.getServer() == null)
			return;
		boolean minimapEnabled = MinigamesModVariables.MapVariables.get(world).minimap;
		boolean lastState = entity.getPersistentData().getBooleanOr(LAST_MINIMAP_STATE, !minimapEnabled);
		if (lastState == minimapEnabled)
			return;
		entity.getPersistentData().putBoolean(LAST_MINIMAP_STATE, minimapEnabled);
		Entity _ent = entity;
		CommandSourceStack source = new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
				_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent);
		if (!minimapEnabled) {
			_ent.getServer().getCommands().performPrefixedCommand(source, "/effect give @s xaerominimap:no_minimap infinite 1 true");
			_ent.getServer().getCommands().performPrefixedCommand(source, "/effect give @s xaeroworldmap:no_world_map infinite 1 true");
		} else {
			_ent.getServer().getCommands().performPrefixedCommand(source, "/effect clear @s xaerominimap:no_minimap");
			_ent.getServer().getCommands().performPrefixedCommand(source, "/effect clear @s xaeroworldmap:no_world_map");
		}
	}
}
