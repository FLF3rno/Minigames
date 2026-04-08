package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ConquerTopLayerWorldProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		double playerNumber = 0;
		double alivePlayersNumber = 0;
		double playerNumberLayerBottom = 0;
		if (MinigamesModVariables.MapVariables.get(world).playingSpleef) {
			if (world instanceof ServerLevel _origLevel) {
				LevelAccessor _worldorig = world;
				world = _origLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minigames:spleef_dimension")));
				if (world != null) {
					if (MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef > 1) {
						playerNumber = 0;
						playerNumberLayerBottom = 0;
						for (Entity entityiterator : world.getEntities(null,
								new AABB((-100), ((MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef - 1) * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef + 100), (-100), 100, 140, 100))) {
							if (entityiterator instanceof Player) {
								playerNumber = playerNumber + 1;
							}
						}
						for (Entity entityiterator : world.getEntities(null, new AABB((-100), ((MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef - 2) * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef + 100),
								(-100), 100, ((MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef - 1) * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef - 1 + 100), 100))) {
							if (entityiterator instanceof Player) {
								playerNumberLayerBottom = playerNumberLayerBottom + 1;
							}
						}
						if (playerNumber <= 1) {
							MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef = MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef + 0.25;
							MinigamesModVariables.MapVariables.get(world).markSyncDirty();
							if (MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef == 1) {
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"/execute as @a at @s run playsound minigames:menu_switch master @s ~ ~ ~ 1 1");
							}
							if (playerNumberLayerBottom == 0) {
								MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef = 400;
								MinigamesModVariables.MapVariables.get(world).markSyncDirty();
							}
						} else {
							MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef = 0;
							MinigamesModVariables.MapVariables.get(world).markSyncDirty();
						}
						if (MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef == 280) {
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										"/execute as @a at @s run playsound minigames:menu_switch master @s ~ ~ ~ 1 1");
						}
						if (MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef == 320) {
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										"/execute as @a at @s run playsound minigames:menu_switch master @s ~ ~ ~ 1 1");
						}
						if (MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef == 360) {
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										"/execute as @a at @s run playsound minigames:menu_switch master @s ~ ~ ~ 1 1");
						}
						if (MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef >= 400) {
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										("execute in minigames:spleef_dimension run fill -20 "
												+ new java.text.DecimalFormat("##").format((MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef - 1) * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef + 100)
												+ " -20 20 140 20 air"));
							MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef = 0;
							MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef = MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef - 1;
							MinigamesModVariables.MapVariables.get(world).markSyncDirty();
						}
					}
				}
				world = _worldorig;
			}
		}
	}
}