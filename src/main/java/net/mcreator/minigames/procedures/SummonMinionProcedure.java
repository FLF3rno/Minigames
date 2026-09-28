package net.mcreator.minigames.procedures;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelAccessor;
import net.mcreator.minigames.client.ClientGlowManager;
import net.mcreator.minigames.entity.CandleheadEntity;
import net.mcreator.minigames.entity.DemonEntity;
import net.mcreator.minigames.entity.GravediggerEntity;
import net.mcreator.minigames.entity.GravediggerMinibossEntity;
import net.mcreator.minigames.entity.LaserStatueEntity;
import net.mcreator.minigames.entity.PreacherEntity;
import net.mcreator.minigames.entity.SculklingEntity;
import net.mcreator.minigames.entity.ShieldAngelEntity;
import net.mcreator.minigames.entity.WorshipperEntity;

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
			int dataID = ClientGlowManager.getEntityRoomId(summoner);
			if (dataID == 0) {
				dataID = summoner.getPersistentData().getIntOr("DataID", (int) summoner.getPersistentData().getDoubleOr("DataID", 0));
			}

			spawned.getPersistentData().putInt("DataID", dataID);
			spawned.getPersistentData().putDouble("DataID", dataID);

			if (spawned instanceof SculklingEntity sculk) {
				sculk.getEntityData().set(SculklingEntity.DATA_ID, dataID);
			} else if (spawned instanceof DemonEntity demon) {
				demon.getEntityData().set(DemonEntity.DATA_ID, dataID);
			} else if (spawned instanceof CandleheadEntity candle) {
				candle.getEntityData().set(CandleheadEntity.DATA_ID, dataID);
			} else if (spawned instanceof GravediggerEntity grave) {
				grave.getEntityData().set(GravediggerEntity.DATA_ID, dataID);
			} else if (spawned instanceof GravediggerMinibossEntity graveMini) {
				graveMini.getEntityData().set(GravediggerMinibossEntity.DATA_ID, dataID);
			} else if (spawned instanceof LaserStatueEntity laser) {
				laser.getEntityData().set(LaserStatueEntity.DATA_ID, dataID);
			} else if (spawned instanceof PreacherEntity preacher) {
				preacher.getEntityData().set(PreacherEntity.DATA_ID, dataID);
			} else if (spawned instanceof ShieldAngelEntity shield) {
				shield.getEntityData().set(ShieldAngelEntity.DATA_ID, dataID);
			} else if (spawned instanceof WorshipperEntity worship) {
				worship.getEntityData().set(WorshipperEntity.DATA_ID, dataID);
			}
		}

		level.addFreshEntity(spawned);
		OnMinionSummonProcedure.execute();
		return spawned;
	}
}