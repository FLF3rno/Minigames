package net.mcreator.minigames.procedures;

import net.mcreator.minigames.entity.FlavioTrapdoor2Entity;
import net.mcreator.minigames.entity.FlavioTrapdoor3Entity;
import net.mcreator.minigames.entity.FlavioTrapdoorEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class TeslaTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		boolean attack = false;
		boolean telegraph = false;

		if (entity.tickCount < 35) {
			{
				entity.setYRot(entity.getYRot() + 10);
				entity.setXRot(0);
				entity.setYBodyRot(entity.getYRot());
				entity.setYHeadRot(entity.getYRot());
				entity.yRotO = entity.getYRot();
				entity.xRotO = entity.getXRot();

				if (entity instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}

			{
				double _ty = y + 0.12;

				entity.teleportTo(x, _ty, z);

				if (entity instanceof ServerPlayer _serverPlayer)
					_serverPlayer.connection.teleport(
							x,
							_ty,
							z,
							entity.getYRot(),
							entity.getXRot()
					);
			}
		} else if (entity.tickCount == 35) {
			{
				entity.setYRot(0);
				entity.setXRot(0);
				entity.setYBodyRot(entity.getYRot());
				entity.setYHeadRot(entity.getYRot());
				entity.yRotO = entity.getYRot();
				entity.xRotO = entity.getXRot();

				if (entity instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		} else if (entity.tickCount > 65) {
			if (entity.tickCount % 30 == 0) {
				attack = true;
			}

			if (entity.tickCount % 15 == 0 && entity.tickCount % 30 != 0) {
				lightning(x, y + 5, z, world, 0.5F, true);
				telegraph = true;
			}
		}

		if (attack) {
			boolean xPattern = ((entity.tickCount / 30) % 2 == 1);
			int lightningIndex = 0;

			if (xPattern) {
				for (int xpos = -20; xpos <= 20; xpos++) {
					if (xpos % 2 == 0) {
						double ypos = getStrikeY(
								world,
								x + xpos,
								y,
								z + xpos,
								entity
						);

						lightning(
								x + xpos,
								ypos,
								z + xpos,
								world,
								0.05F,
								lightningIndex % 2 == 0
						);

						lightningIndex++;

						ExplodeProcedure.execute(
								world,
								x + xpos,
								ypos,
								z + xpos,
								entity,
								true,
								true,
								5,
								0,
								2,
								"tesla"
						);
					}
				}

				for (int zpos = -20; zpos <= 20; zpos++) {
					if (zpos % 2 == 0) {
						double ypos = getStrikeY(
								world,
								x + zpos,
								y,
								z - zpos,
								entity
						);

						lightning(
								x + zpos,
								ypos,
								z - zpos,
								world,
								0.05F,
								lightningIndex % 2 == 0
						);

						lightningIndex++;

						ExplodeProcedure.execute(
								world,
								x + zpos,
								ypos,
								z - zpos,
								entity,
								true,
								true,
								5,
								0,
								2,
								"tesla"
						);
					}
				}
			} else {
				for (int xpos = -20; xpos <= 20; xpos++) {
					if (xpos % 2 == 0) {
						double ypos = getStrikeY(
								world,
								x + xpos,
								y,
								z,
								entity
						);

						lightning(
								x + xpos,
								ypos,
								z,
								world,
								0.05F,
								lightningIndex % 2 == 0
						);

						lightningIndex++;

						ExplodeProcedure.execute(
								world,
								x + xpos,
								ypos,
								z,
								entity,
								true,
								true,
								5,
								0,
								2,
								"tesla"
						);
					}
				}

				for (int zpos = -20; zpos <= 20; zpos++) {
					if (zpos % 2 == 0) {
						double ypos = getStrikeY(
								world,
								x,
								y,
								z + zpos,
								entity
						);

						lightning(
								x,
								ypos,
								z + zpos,
								world,
								0.05F,
								lightningIndex % 2 == 0
						);

						lightningIndex++;

						ExplodeProcedure.execute(
								world,
								x,
								ypos,
								z + zpos,
								entity,
								true,
								true,
								5,
								0,
								2,
								"tesla"
						);
					}
				}
			}
		}

		if (telegraph) {
			boolean xPattern =
					(((entity.tickCount + 15) / 30) % 2 == 1);

			if (xPattern) {
				for (int xpos = -20; xpos <= 20; xpos++) {
					ApplyTelegraphProcedure.execute(
							world,
							x + xpos,
							y,
							z + 1 + xpos,
							20,
							0
					);

					ApplyTelegraphProcedure.execute(
							world,
							x + xpos,
							y,
							z + xpos,
							20,
							0
					);

					ApplyTelegraphProcedure.execute(
							world,
							x + xpos,
							y,
							z - 1 + xpos,
							20,
							0
					);
				}

				for (int zpos = -20; zpos <= 20; zpos++) {
					ApplyTelegraphProcedure.execute(
							world,
							x + 1 + zpos,
							y,
							z - zpos,
							20,
							0
					);

					ApplyTelegraphProcedure.execute(
							world,
							x + zpos,
							y,
							z - zpos,
							20,
							0
					);

					ApplyTelegraphProcedure.execute(
							world,
							x + zpos - 1,
							y,
							z - zpos,
							20,
							0
					);
				}
			} else {
				for (int xpos = -20; xpos <= 20; xpos++) {
					ApplyTelegraphProcedure.execute(
							world,
							x + xpos,
							y,
							z + 1,
							20,
							0
					);

					ApplyTelegraphProcedure.execute(
							world,
							x + xpos,
							y,
							z,
							20,
							0
					);

					ApplyTelegraphProcedure.execute(
							world,
							x + xpos,
							y,
							z - 1,
							20,
							0
					);
				}

				for (int zpos = -20; zpos <= 20; zpos++) {
					ApplyTelegraphProcedure.execute(
							world,
							x + 1,
							y,
							z + zpos,
							20,
							0
					);

					ApplyTelegraphProcedure.execute(
							world,
							x,
							y,
							z + zpos,
							20,
							0
					);

					ApplyTelegraphProcedure.execute(
							world,
							x - 1,
							y,
							z + zpos,
							20,
							0
					);
				}
			}
		}
	}

	private static void lightning(
			double x,
			double y,
			double z,
			LevelAccessor world,
			float vol,
			boolean playSound
	) {
		if (world instanceof ServerLevel _level) {
			LightningBolt entityToSpawn =
					EntityType.LIGHTNING_BOLT.create(
							_level,
							EntitySpawnReason.TRIGGERED
					);

			if (entityToSpawn != null) {
				BlockPos pos = BlockPos.containing(x, y, z);

				entityToSpawn.snapTo(
						Vec3.atBottomCenterOf(pos)
				);

				entityToSpawn.setVisualOnly(true);
				entityToSpawn.setSilent(true);

				_level.addFreshEntity(entityToSpawn);

				if (playSound) {
					_level.playSound(
							null,
							pos,
							SoundEvents.LIGHTNING_BOLT_THUNDER,
							SoundSource.WEATHER,
							vol / 1.2f,
							1.0F
					);
					_level.playSound(
							null,
							pos,
							SoundEvents.LIGHTNING_BOLT_IMPACT,
							SoundSource.WEATHER,
							vol,
							1.0F
					);
				}
			}
		}
	}

	private static double getStrikeY(
			LevelAccessor world,
			double x,
			double y,
			double z,
			Entity source
	) {
		double highestY = Double.MIN_VALUE;

		if (world instanceof ServerLevel level) {
			AABB searchBox = new AABB(
					x - 0.05D,
					world.getMinY(),
					z - 0.05D,
					x + 0.05D,
					y + 2.0D,
					z + 0.05D
			);

			for (Entity target : level.getEntities(
					source,
					searchBox,
					e -> e != source
							&& (e instanceof FlavioTrapdoorEntity
							|| e instanceof FlavioTrapdoor2Entity
							|| e instanceof FlavioTrapdoor3Entity)
			)) {
				AABB box = target.getBoundingBox();

				if (x >= box.minX && x <= box.maxX
						&& z >= box.minZ && z <= box.maxZ) {

					highestY = Math.max(
							highestY,
							box.maxY
					);
				}
			}
		}

		int blockX = (int) Math.floor(x);
		int blockZ = (int) Math.floor(z);

		int startY = Math.min(
				(int) Math.floor(y),
				world.getMaxY() - 1
		);

		for (
				int currentY = startY;
				currentY >= world.getMinY();
				currentY--
		) {
			BlockPos pos =
					new BlockPos(
							blockX,
							currentY,
							blockZ
					);

			if (!world.getBlockState(pos).isAir()) {
				highestY = Math.max(
						highestY,
						currentY + 1
				);
				break;
			}
		}

		if (highestY != Double.MIN_VALUE) {
			return highestY;
		}

		return y;
	}
}