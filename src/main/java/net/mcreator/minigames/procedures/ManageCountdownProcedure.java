package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ManageCountdownProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (!(MinigamesModVariables.MapVariables.get(world).gameSeconds == 0 && MinigamesModVariables.MapVariables.get(world).gameMinutes == 0 && MinigamesModVariables.MapVariables.get(world).gameHours == 0)) {
			if (MinigamesModVariables.MapVariables.get(world).ShowCrownTimer == true) {
				if (MinigamesModVariables.MapVariables.get(world).MoveCrownTimer == true) {
					MinigamesModVariables.MapVariables.get(world).gameTick = MinigamesModVariables.MapVariables.get(world).gameTick - 1;
					if (MinigamesModVariables.MapVariables.get(world).gameTick < 0) {
						MinigamesModVariables.MapVariables.get(world).gameSeconds = MinigamesModVariables.MapVariables.get(world).gameSeconds - 1;
						MinigamesModVariables.MapVariables.get(world).gameTick = 59;
						MinigamesModVariables.MapVariables.get(world).markSyncDirty();
						if ((MinigamesModVariables.MapVariables.get(world).gameSeconds == 0 || MinigamesModVariables.MapVariables.get(world).gameSeconds == 2 || MinigamesModVariables.MapVariables.get(world).gameSeconds == 4
								|| MinigamesModVariables.MapVariables.get(world).gameSeconds == 6 || MinigamesModVariables.MapVariables.get(world).gameSeconds == 8 || MinigamesModVariables.MapVariables.get(world).gameSeconds == 10)
								&& MinigamesModVariables.MapVariables.get(world).gameMinutes == 0 && MinigamesModVariables.MapVariables.get(world).gameHours == 0) {
							if (!MinigamesModVariables.MapVariables.get(world).inGracePeriod) {
								if (world instanceof ServerLevel _level)
									_level.getServer().getCommands().performPrefixedCommand(
											new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
											"/playsound minigames:clock player @a ~ ~ ~ 1000000 1");
								MinigamesModVariables.MapVariables.get(world).showRedTimer = true;
								MinigamesModVariables.MapVariables.get(world).markSyncDirty();
							}
						} else {
							MinigamesModVariables.MapVariables.get(world).showRedTimer = false;
							MinigamesModVariables.MapVariables.get(world).markSyncDirty();
						}
					}
					if (MinigamesModVariables.MapVariables.get(world).gameSeconds < 0) {
						MinigamesModVariables.MapVariables.get(world).gameMinutes = MinigamesModVariables.MapVariables.get(world).gameMinutes - 1;
						MinigamesModVariables.MapVariables.get(world).gameSeconds = 59;
						MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					}
					if (MinigamesModVariables.MapVariables.get(world).gameMinutes < 0) {
						MinigamesModVariables.MapVariables.get(world).gameHours = MinigamesModVariables.MapVariables.get(world).gameHours - 1;
						MinigamesModVariables.MapVariables.get(world).gameMinutes = 59;
						MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					}
					if (MinigamesModVariables.MapVariables.get(world).gameTick == 59) {
						MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					}
				}
			}
		}
	}
}
