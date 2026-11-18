package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.entity.PriestEntity;
import net.mcreator.minigames.photon_plugin.SummonFXEntityServer;
import net.mcreator.minigames.photon_plugin.RemoveFXEntityServer;

import java.util.UUID;
import java.util.Comparator;

public class PriestOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!entity.isAlive()) {
			if (!entity.level().isClientSide() && entity instanceof PriestEntity priest && priest.fxSpawned) {
				RemoveFXEntityServer.destroy(entity).name("photon:priest").force(true).send();
				priest.fxSpawned = false;
			}
			return;
		}
		if ((entity instanceof PriestEntity _datEntI ? _datEntI.getEntityData().get(PriestEntity.DATA_ID) : 0) == MinigamesModVariables.MapVariables.get(world).currentRoomID) {
			if (entity instanceof PriestEntity _datEntSetI)
				_datEntSetI.getEntityData().set(PriestEntity.DATA_activeTick, (int) ((entity instanceof PriestEntity _datEntI ? _datEntI.getEntityData().get(PriestEntity.DATA_activeTick) : 0) + 1));
			if ((entity instanceof PriestEntity _datEntI ? _datEntI.getEntityData().get(PriestEntity.DATA_activeTick) : 0) == 20) {
				if (entity instanceof PriestEntity _ent4) {
					_ent4.getEntityData().set(PriestEntity.ANIM, 1000);
					_ent4.getEntityData().set(PriestEntity.ANIM, 0);
				}
			}
			if ((entity instanceof PriestEntity _datEntI ? _datEntI.getEntityData().get(PriestEntity.DATA_activeTick) : 0) == 74) {
				if (entity instanceof PriestEntity _ent6) {
					_ent6.getEntityData().set(PriestEntity.ANIM, 1000);
					_ent6.getEntityData().set(PriestEntity.ANIM, 1);
				}
			}
			if ((entity instanceof PriestEntity _datEntI ? _datEntI.getEntityData().get(PriestEntity.DATA_activeTick) : 0) > 74) {
				if ((entity instanceof PriestEntity _datEntI ? _datEntI.getEntityData().get(PriestEntity.DATA_activeTick) : 0) % 15 == 0) {
					{
						final Vec3 _center = new Vec3(x, y, z);
						for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(40 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
							if (entityiterator.isAlive() && (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1)
									&& !(entityiterator instanceof PriestEntity) && !(entityiterator instanceof Player)) {
								if ((entity instanceof PriestEntity _datEntI ? _datEntI.getEntityData().get(PriestEntity.DATA_ID) : 0) == entityiterator.getPersistentData().getDoubleOr("DataID", 0)) {
									if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) - (entityiterator instanceof LivingEntity _livEnt
											? _livEnt.getHealth()
											: -1) > ((world instanceof ServerLevel _level18
													? getEntityFromUUID(_level18, (entity instanceof PriestEntity _datEntS ? _datEntS.getEntityData().get(PriestEntity.DATA_target) : ""))
													: null) instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1)
													- ((world instanceof ServerLevel _level21
															? getEntityFromUUID(_level21, (entity instanceof PriestEntity _datEntS ? _datEntS.getEntityData().get(PriestEntity.DATA_target) : ""))
															: null) instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1)
											|| (world instanceof ServerLevel _level24 ? getEntityFromUUID(_level24, (entity instanceof PriestEntity _datEntS ? _datEntS.getEntityData().get(PriestEntity.DATA_target) : "")) : null) == null) {
										if (entity instanceof PriestEntity _datEntSetS)
											_datEntSetS.getEntityData().set(PriestEntity.DATA_target, (entityiterator.getStringUUID()));
									}
								}
							}
						}
					}
					Entity healTarget = world instanceof ServerLevel _level29 ? getEntityFromUUID(_level29, (entity instanceof PriestEntity _datEntS ? _datEntS.getEntityData().get(PriestEntity.DATA_target) : "")) : null;
					if (healTarget != null && healTarget.isAlive()) {
						DungeonHealProcedure.execute(entity.level(), healTarget, (healTarget instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) / 13d, "monster");
					}
				}
				if (world instanceof ServerLevel _levelFx && entity instanceof PriestEntity priest) {
					Entity healTarget = getEntityFromUUID(_levelFx, priest.getEntityData().get(PriestEntity.DATA_target));
					if (healTarget != null && healTarget.isAlive()) {
						priest.getEntityData().set(PriestEntity.DATA_target_id, healTarget.getId());
						if (!priest.fxSpawned) {
							Vec3 look = entity.getLookAngle();
							Vec3 forward = new Vec3(look.x, 0, look.z);
							forward = forward.lengthSqr() < 1.0E-4 ? new Vec3(0, 0, 1) : forward.normalize();
							int activeTick = priest.getEntityData().get(PriestEntity.DATA_activeTick);
							double pearlY = entity.getY() + 1.25 + getPearlAnimOffsetY(activeTick);
							Vec3 P0 = new Vec3(entity.getX(), pearlY, entity.getZ()).add(forward.scale(0.875));
							Vec3 relOffset = P0.subtract(entity.getEyePosition());
							SummonFXEntityServer.create(entity)
									.name("photon:priest")
									.offset(relOffset.x, relOffset.y, relOffset.z)
									.rotation(0, 0, 0)
									.forcedDeath(true)
									.allowMulti(false)
									.send();
							priest.fxSpawned = true;
						}
					} else {
						priest.getEntityData().set(PriestEntity.DATA_target_id, -1);
						if (priest.fxSpawned) {
							RemoveFXEntityServer.destroy(entity).name("photon:priest").force(true).send();
							priest.fxSpawned = false;
						}
					}
				}
			}
		}
	}

	public static double getPearlAnimOffsetY(double activeTick) {
		if (activeTick < 74)
			return 0.0;
		double time = ((activeTick - 74) * 0.05) % 4.2917;
		if (time < 0)
			time += 4.2917;
		double pixelY = 0.0;
		if (time < 1.1250) {
			pixelY = 2.0 * (time / 1.1250);
		} else if (time < 1.2917) {
			pixelY = 2.0 + (2.2 - 2.0) * ((time - 1.1250) / (1.2917 - 1.1250));
		} else if (time < 1.4583) {
			pixelY = 2.2 + (2.0 - 2.2) * ((time - 1.2917) / (1.4583 - 1.2917));
		} else if (time < 3.0417) {
			pixelY = 2.0 + (-1.0 - 2.0) * ((time - 1.4583) / (3.0417 - 1.4583));
		} else if (time < 3.2083) {
			pixelY = -1.0 + (-1.125 - -1.0) * ((time - 3.0417) / (3.2083 - 3.0417));
		} else if (time < 3.3750) {
			pixelY = -1.125 + (-1.0 - -1.125) * ((time - 3.2083) / (3.3750 - 3.2083));
		} else {
			pixelY = -1.0 + (0.0 - -1.0) * ((time - 3.3750) / (4.2917 - 3.3750));
		}
		return pixelY / 16.0;
	}

	private static Entity getEntityFromUUID(ServerLevel level, String uuid) {
		try {
			return level.getEntity(UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}