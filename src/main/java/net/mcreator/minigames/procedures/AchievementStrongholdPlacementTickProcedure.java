package net.mcreator.minigames.procedures;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.util.AchievementStrongholdTargets;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;

@EventBusSubscriber(modid = "minigames")
public class AchievementStrongholdPlacementTickProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		if (!(event.getLevel() instanceof ServerLevel level)) {
			return;
		}
		execute(level);
	}

	public static void execute(Level world) {
		if (!(world instanceof ServerLevel level)) {
			return;
		}
		if (!MinigamesModVariables.MapVariables.get(level).playingAchievement) {
			return;
		}
		if (AchievementStrongholdTargets.isEmpty()) {
			return;
		}

		BlockPos pos = AchievementStrongholdTargets.getTargets().stream()
				.filter(candidate -> !AchievementStrongholdTargets.isPlaced(candidate))
				.filter(candidate -> level.players().stream().anyMatch(player -> player.distanceToSqr(candidate.getX() + 0.5, candidate.getY() + 0.5, candidate.getZ() + 0.5) <= 96 * 96))
				.findFirst()
				.orElse(null);

		if (pos == null) {
			return;
		}

		for (ServerPlayer player : level.players()) {
			if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 96 * 96) {
				player.sendSystemMessage(Component.literal("Achievement stronghold debug: player is in placement range at "
						+ pos.getX() + " " + pos.getY() + " " + pos.getZ()));
			}
		}

		forceLoadChunkSquare(level, pos, 20);

		level.getServer().getCommands().performPrefixedCommand(
				new CommandSourceStack(CommandSource.NULL, new Vec3(pos.getX(), pos.getY(), pos.getZ()), Vec2.ZERO, level,
						net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
				"execute in minecraft:overworld run place structure minecraft:stronghold " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
		AchievementStrongholdTargets.markPlaced(pos);

		MinigamesMod.queueServerWork(40, () -> releaseForceLoadChunkSquare(level, pos, 20));
	}

	private static void forceLoadChunkSquare(ServerLevel level, BlockPos center, int chunkDiameter) {
		int centerChunkX = center.getX() >> 4;
		int centerChunkZ = center.getZ() >> 4;
		int half = chunkDiameter / 2;
		for (int cx = centerChunkX - half; cx < centerChunkX - half + chunkDiameter; cx++) {
			for (int cz = centerChunkZ - half; cz < centerChunkZ - half + chunkDiameter; cz++) {
				level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(center.getX(), center.getY(), center.getZ()), Vec2.ZERO, level,
								net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
						"forceload add " + (cx << 4) + " " + (cz << 4));
			}
		}
	}

	private static void releaseForceLoadChunkSquare(ServerLevel level, BlockPos center, int chunkDiameter) {
		int centerChunkX = center.getX() >> 4;
		int centerChunkZ = center.getZ() >> 4;
		int half = chunkDiameter / 2;
		for (int cx = centerChunkX - half; cx < centerChunkX - half + chunkDiameter; cx++) {
			for (int cz = centerChunkZ - half; cz < centerChunkZ - half + chunkDiameter; cz++) {
				level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, new Vec3(center.getX(), center.getY(), center.getZ()), Vec2.ZERO, level,
								net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), level.getServer(), null).withSuppressedOutput(),
						"forceload remove " + (cx << 4) + " " + (cz << 4));
			}
		}
	}
}
