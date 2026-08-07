package net.mcreator.minigames.procedures;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelAccessor;

public class SummonMinionProcedure {

	public static Entity execute(
			LevelAccessor world,
			double x,
			double y,
			double z,
			Entity summoner,
			float yaw,
			float pitch,
			double vx,
			double vy,
			double vz,
			String entityRegistryName
	) {
		if (!(world instanceof ServerLevel level))
			return null;

		EntityType<?> type = BuiltInRegistries.ENTITY_TYPE
				.get(Identifier.parse(entityRegistryName))
				.map(holder -> holder.value())
				.orElse(null);

		if (type == null)
			return null;

		Entity spawned = type.create(level, net.minecraft.world.entity.EntitySpawnReason.COMMAND);

		if (spawned == null)
			return null;

		spawned.setPos(x, y, z);
		spawned.setYRot(yaw);
		spawned.setXRot(pitch);

		spawned.setDeltaMovement(vx, vy, vz);

		if (summoner != null) {
			double dataID = summoner.getPersistentData()
					.getDouble("DataID")
					.orElse(0.0);

			spawned.getPersistentData().putDouble("DataID", dataID);
		}

		level.addFreshEntity(spawned);
		OnMinionSummonProcedure.execute();
		return spawned;
	}
}