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
						RemoveFXEntityServer.destroy(entity).name("photon:priest").force(true).send();
						Vec3 look = entity.getLookAngle();
						Vec3 forward = new Vec3(look.x, 0, look.z);
						forward = forward.lengthSqr() < 1.0E-4 ? new Vec3(0, 0, 1) : forward.normalize();
						int activeTick = (entity instanceof PriestEntity _datEntI ? _datEntI.getEntityData().get(PriestEntity.DATA_activeTick) : 0);
						double pearlY = entity.getY() + 1.25 + getPearlAnimOffsetY(activeTick);
						Vec3 P0 = new Vec3(entity.getX(), pearlY, entity.getZ()).add(forward.scale(0.875));
						Vec3 targetCenter = new Vec3(healTarget.getX(), healTarget.getY() + healTarget.getBbHeight() * 0.5, healTarget.getZ());
						double rawDist = P0.distanceTo(targetCenter);
						Vec3 toTargetInit = targetCenter.subtract(P0);
						Vec3 toTargetHInit = new Vec3(toTargetInit.x, 0, toTargetInit.z);
						Vec3 inDirInit = toTargetHInit.lengthSqr() < 1.0E-4 ? forward : toTargetHInit.normalize();
						double targetRadius = (healTarget.getBbWidth() * 0.5) + 0.15;
						Vec3 P3 = rawDist > targetRadius + 0.5 ? targetCenter.subtract(inDirInit.scale(targetRadius)) : targetCenter;
						double dist = P0.distanceTo(P3);
						if (dist > 0.5) {

							double lead = Math.min(3.5, Math.max(1.2, dist * 0.35));
							Vec3 P1 = P0.add(forward.scale(lead));
							Vec3 toTarget = P3.subtract(P1);
							Vec3 targetIn = new Vec3(toTarget.x, 0, toTarget.z);
							targetIn = targetIn.lengthSqr() < 1.0E-4 ? forward : targetIn.normalize();
							Vec3 P2 = P3.subtract(targetIn.scale(lead * 0.6));
							int segments = Math.max(6, (int) Math.round(dist * 2.0));
							for (int i = 1; i <= segments; i++) {
								double t = (double) i / (segments + 1);
								double u = 1.0 - t;
								double tt = t * t;
								double uu = u * u;
								double uuu = uu * u;
								double ttt = tt * t;
								Vec3 pt = P0.scale(uuu).add(P1.scale(3 * uu * t)).add(P2.scale(3 * u * tt)).add(P3.scale(ttt));
								Vec3 tangent = P1.subtract(P0).scale(3 * uu).add(P2.subtract(P1).scale(6 * u * t)).add(P3.subtract(P2).scale(3 * tt));
								if (tangent.lengthSqr() < 1.0E-6) {
									tangent = P3.subtract(P0);
								}
								Vec3 dir = tangent.normalize();
								double hDist = Math.hypot(dir.x, dir.z);
								double rotY = Math.toDegrees(Math.atan2(-dir.z, dir.x));
								double rotZ = -Math.toDegrees(Math.atan2(hDist, dir.y));
								Vec3 relOffset = pt.subtract(entity.position());
								SummonFXEntityServer.create(entity)
										.name("photon:priest")
										.offset(relOffset.x, relOffset.y, relOffset.z)
										.rotation(0, rotY, rotZ)
										.forcedDeath(true)
										.allowMulti(true)
										.duration(15)
										.send();
							}
						}
						DungeonHealProcedure.execute(entity.level(), healTarget, (healTarget instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) / 13d, "monster");
					} else {
						RemoveFXEntityServer.destroy(entity).name("photon:priest").force(true).send();
					}





				}
			}
		}
	}

	private static double getPearlAnimOffsetY(int activeTick) {
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