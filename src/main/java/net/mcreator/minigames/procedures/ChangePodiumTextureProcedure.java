package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.entity.SpleefPodiumPlayerEntity;

public class ChangePodiumTextureProcedure {
	public static void execute(LevelAccessor world, double position, String uuid) {
		if (uuid == null)
			return;
		if (!(world instanceof ServerLevel level))
			return;
		MinecraftServer server = level.getServer();
		if (server == null)
			return;
		ResourceKey<net.minecraft.world.level.Level> spleefDim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minigames:spleef_dimension"));
		ServerLevel targetLevel = server.getLevel(spleefDim);
		if (targetLevel == null)
			return;
		for (Entity entity : targetLevel.getAllEntities()) {
			if (entity instanceof SpleefPodiumPlayerEntity podium) {
				if (podium.getEntityData().get(SpleefPodiumPlayerEntity.DATA_position) == (int) position) {
					podium.getEntityData().set(SpleefPodiumPlayerEntity.DATA_display_uuid, uuid);
					return;
				}
			}
		}
	}
}
