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
public class ConquerTopLayerProcedure {
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
		double playerNumber = 0;
		double alivePlayersNumber = 0;
		if (MinigamesModVariables.MapVariables.get(world).playingSpleef) {
			if (MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef > 1) {
				if (MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef == 1) {
					if (entity.getY() >= (MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef - 1) * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef + 100) {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands()
										.performPrefixedCommand(
												new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(),
														_ent.getDisplayName(), _ent.level().getServer(), _ent),
												"/tellraw @a [{\"bold\":true,\"color\":\"gold\",\"selector\":\"@p\"},{\"color\":\"light_green\",\"text\":\" is about to win the layer!\"}]");
							}
						}
					}
				}
				if (MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef == 399) {
					if (entity.getY() >= (MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef - 1) * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef + 100) {
						{
							Entity _ent = entity;
							if (!_ent.level().isClientSide() && _ent.getServer() != null) {
								_ent.getServer().getCommands()
										.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
												_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent),
												"/tellraw @a [{\"bold\":true,\"color\":\"gold\",\"selector\":\"@p\"},{\"color\":\"light_purple\",\"text\":\" won the layer!\"}]");
							}
						}
						{
							MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
							_vars.snowballCountSpleef = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef + 50;
							_vars.markSyncDirty();
						}
						SpleefPowerupProcedure.execute(world, entity);
					}
				}
			}
		}
	}
}