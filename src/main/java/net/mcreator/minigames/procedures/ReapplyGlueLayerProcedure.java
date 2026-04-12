package net.mcreator.minigames.procedures;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.network.MinigamesModVariables;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@EventBusSubscriber
public class ReapplyGlueLayerProcedure {
	private static final int GLUE_RADIUS = 20;
	private static final ResourceKey<net.minecraft.world.level.Level> SPLEEF_DIMENSION = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minigames:spleef_dimension"));

	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event.getLevel());
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		LevelAccessor world = event.getLevel();
		BlockPos pos = event.getPos();
		if (!shouldMaintainGlueAt(world, pos)) {
			return;
		}
		MinigamesMod.queueServerWork(1, () -> {
			if (shouldMaintainGlueAt(world, pos) && GlueHitsBlockProcedure.canGlueOccupy(world, pos)) {
				world.setBlock(pos, MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
			}
		});
	}

	public static void execute(LevelAccessor world) {
		if (!(world instanceof ServerLevel level)) {
			return;
		}
		if (!level.dimension().equals(SPLEEF_DIMENSION)) {
			return;
		}
		MinigamesModVariables.MapVariables mapVariables = MinigamesModVariables.MapVariables.get(world);

		Vec3 arenaCenter = mapVariables.spleefMapMiddleX;
		int minX = (int) arenaCenter.x() - GLUE_RADIUS;
		int maxX = (int) arenaCenter.x() + GLUE_RADIUS;
		int minZ = (int) arenaCenter.z() - GLUE_RADIUS;
		int maxZ = (int) arenaCenter.z() + GLUE_RADIUS;
		Map<Integer, Integer> activeLayers = getGlueLayers(world);
		if (activeLayers.isEmpty()) {
			return;
		}
		Map<Integer, Integer> nextLayers = new HashMap<>();
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (Map.Entry<Integer, Integer> entry : activeLayers.entrySet()) {
			int glueY = entry.getKey();
			for (int targetX = minX; targetX <= maxX; targetX++) {
				for (int targetZ = minZ; targetZ <= maxZ; targetZ++) {
					mutablePos.set(targetX, glueY, targetZ);
					if (GlueHitsBlockProcedure.canGlueOccupy(world, mutablePos)) {
						world.setBlock(mutablePos, MinigamesModBlocks.SPREADING_GLUE.get().defaultBlockState(), 3);
					}
				}
			}
			int remainingTicks = entry.getValue() - 1;
			if (remainingTicks > 0) {
				nextLayers.put(glueY, remainingTicks);
			} else {
				GlueHitsBlockProcedure.clearGlueLayer(world, glueY, minX, maxX, minZ, maxZ);
			}
		}
		storeGlueLayers(world, nextLayers);
		mapVariables.markSyncDirty();
	}

	private static boolean shouldMaintainGlueAt(LevelAccessor world, BlockPos pos) {
		MinigamesModVariables.MapVariables mapVariables = MinigamesModVariables.MapVariables.get(world);
		Vec3 arenaCenter = mapVariables.spleefMapMiddleX;
		int minX = (int) arenaCenter.x() - GLUE_RADIUS;
		int maxX = (int) arenaCenter.x() + GLUE_RADIUS;
		int minZ = (int) arenaCenter.z() - GLUE_RADIUS;
		int maxZ = (int) arenaCenter.z() + GLUE_RADIUS;
		return getGlueLayers(world).containsKey(pos.getY()) && pos.getX() >= minX && pos.getX() <= maxX && pos.getZ() >= minZ && pos.getZ() <= maxZ;
	}

	public static void setGlueLayerTicks(LevelAccessor world, int glueY, int ticks) {
		Map<Integer, Integer> activeLayers = getGlueLayers(world);
		activeLayers.put(glueY, ticks);
		storeGlueLayers(world, activeLayers);
	}

	private static Map<Integer, Integer> getGlueLayers(LevelAccessor world) {
		Map<Integer, Integer> layers = new HashMap<>();
		String serialized = MinigamesModVariables.MapVariables.get(world).activeGlueLayersSpleef;
		if (serialized == null || serialized.isEmpty()) {
			return layers;
		}
		for (String entry : serialized.split(";")) {
			String[] parts = entry.split("=");
			if (parts.length != 2) {
				continue;
			}
			try {
				int layerY = Integer.parseInt(parts[0]);
				int ticks = Integer.parseInt(parts[1]);
				if (ticks > 0) {
					layers.put(layerY, ticks);
				}
			} catch (NumberFormatException ignored) {
			}
		}
		return layers;
	}

	private static void storeGlueLayers(LevelAccessor world, Map<Integer, Integer> layers) {
		StringJoiner joiner = new StringJoiner(";");
		for (Map.Entry<Integer, Integer> entry : layers.entrySet()) {
			joiner.add(entry.getKey() + "=" + entry.getValue());
		}
		MinigamesModVariables.MapVariables.get(world).activeGlueLayersSpleef = joiner.toString();
	}
}
