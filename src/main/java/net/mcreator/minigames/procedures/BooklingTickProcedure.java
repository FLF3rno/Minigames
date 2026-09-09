package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.network.PacketDistributor;

import net.mcreator.minigames.entity.BooklingEntity;
import net.mcreator.minigames.network.SpawnCustomParticleMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BooklingTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (world.isClientSide())
			return;

		ArrayList<Vec3> bookshelves = new ArrayList<>();
		ArrayList<Vec3> selectedBookshelves = new ArrayList<>();
		double index = 0;
		double rng = 0;

		if (!(entity instanceof BooklingEntity _datEntL0 && _datEntL0.getEntityData().get(BooklingEntity.DATA_reloading)) && (entity instanceof BooklingEntity _datEntI ? _datEntI.getEntityData().get(BooklingEntity.DATA_ammo) : 0) == 0) {
			if (entity instanceof BooklingEntity _datEntSetL)
				_datEntSetL.getEntityData().set(BooklingEntity.DATA_reloading, true);
			if (entity instanceof BooklingEntity _datEntSetI)
				_datEntSetI.getEntityData().set(BooklingEntity.DATA_reloadTick, 0);
			if (entity instanceof BooklingEntity _datEntSetI)
				_datEntSetI.getEntityData().set(BooklingEntity.DATA_maxAmmo, 10);
		}

		if (entity instanceof BooklingEntity _datEntL5 && _datEntL5.getEntityData().get(BooklingEntity.DATA_reloading)) {
			int currentReloadTick = (entity instanceof BooklingEntity _datEntI ? _datEntI.getEntityData().get(BooklingEntity.DATA_reloadTick) : 0) + 1;
			if (entity instanceof BooklingEntity _datEntSetI)
				_datEntSetI.getEntityData().set(BooklingEntity.DATA_reloadTick, currentReloadTick);

			if (currentReloadTick == 60) {
				if (entity instanceof BooklingEntity bookling) {
					bookling.setNoAi(true);
				}
				bookshelves.clear();
				int centerX = (int) Math.floor(x);
				int centerY = (int) Math.floor(y);
				int centerZ = (int) Math.floor(z);
				for (int dx = -25; dx <= 25; dx++) {
					for (int dy = -15; dy <= 15; dy++) {
						for (int dz = -25; dz <= 25; dz++) {
							int curX = centerX + dx;
							int curY = centerY + dy;
							int curZ = centerZ + dz;
							if (world.getBlockState(new BlockPos(curX, curY, curZ)).is(Blocks.BOOKSHELF)) {
								bookshelves.add(new Vec3(curX + 0.5D, curY + 0.5D, curZ + 0.5D));
							}
						}
					}
				}
				index = Math.round(((double) (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) / (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1))
						* (entity instanceof BooklingEntity _datEntI ? _datEntI.getEntityData().get(BooklingEntity.DATA_maxAmmo) : 0));
				if (index <= 0) {
					index = 1;
				}
				for (int _i1 = 0; _i1 < (int) index; _i1++) {
					if (!bookshelves.isEmpty()) {
						rng = Mth.nextInt(RandomSource.create(), 0, bookshelves.size() - 1);
						selectedBookshelves.add(bookshelves.get((int) rng));
						bookshelves.remove((int) rng);
					}
				}
				if (entity instanceof BooklingEntity _ent23) {
					_ent23.getEntityData().set(BooklingEntity.ANIM, 1000);
					_ent23.getEntityData().set(BooklingEntity.ANIM, 0);
				}
				if (entity instanceof BooklingEntity _datEntSetI)
					_datEntSetI.getEntityData().set(BooklingEntity.DATA_ammo, (int) index);
				if (!selectedBookshelves.isEmpty()) {
					for (int _i1 = 0; _i1 < selectedBookshelves.size(); _i1++) {
						ParticleFlowHelperProcedure.execute(world, 1, 120, "linear", "minigames:custom_enchant", selectedBookshelves.get(_i1), entity.position().add(0, 0.5D, 0));
					}
				}
			}
			if (currentReloadTick >= 180) {
				if (entity instanceof BooklingEntity bookling) {
					bookling.setNoAi(false);
				}
				if (entity instanceof BooklingEntity _datEntSetL)
					_datEntSetL.getEntityData().set(BooklingEntity.DATA_reloading, false);
			}
		}
		if (entity instanceof BooklingEntity bookling) {
			if (bookling.getEntityData().get(BooklingEntity.DATA_ammo) > 0 && !bookling.getEntityData().get(BooklingEntity.DATA_reloading)) {
				if (!(world instanceof ServerLevel serverLevel))
					return;

				LivingEntity rawTarget = bookling.getTarget();
				if (!(rawTarget instanceof Player playerTarget) || !playerTarget.isAlive() || playerTarget.isCreative() || playerTarget.isSpectator() || bookling.distanceToSqr(playerTarget) > 256.0D) {
					Player nearest = serverLevel.getNearestPlayer(bookling, 16.0D);
					if (nearest != null && !nearest.isCreative() && !nearest.isSpectator() && nearest.isAlive()) {
						bookling.setTarget(nearest);
						rawTarget = nearest;
					} else {
						bookling.setTarget(null);
						if (bookling.aimTicks > 0) {
							bookling.aimTicks = 0;
							if (bookling.getEntityData().get(BooklingEntity.ANIM) == 1) {
								bookling.getEntityData().set(BooklingEntity.ANIM, 1000);
								bookling.getEntityData().set(BooklingEntity.ANIM, -2);
							}
						}
						return;
					}
				}
				Player target = (Player) rawTarget;

				// Only require line of sight to start; continue firing on selected target even when line of sight is broken
				if (bookling.aimTicks == 0 && !bookling.hasLineOfSight(target)) {
					bookling.getNavigation().moveTo(target, 1.0D);
					return;
				}

				// Look at target & face it with 180-degree offset (compensating for model rotation)
				Vec3 targetCenter = target.getBoundingBox().getCenter();
				Vec3 shootFrom = bookling.position().add(0, 0.45D, 0);
				Vec3 aimDir = targetCenter.subtract(shootFrom);
				if (aimDir.lengthSqr() < 1.0E-6D) {
					aimDir = new Vec3(0, 0, 1);
				}
				float yaw = Mth.wrapDegrees((float) Math.toDegrees(Math.atan2(-aimDir.x, aimDir.z)) + 180.0F);
				bookling.setYRot(yaw);
				bookling.setYHeadRot(yaw);
				bookling.setYBodyRot(yaw);
				bookling.getLookControl().setLookAt(bookling.getX() - aimDir.x, target.getEyeY(), bookling.getZ() - aimDir.z, 30.0F, 30.0F);

				// Root in place during aiming and firing
				bookling.getNavigation().stop();
				bookling.setDeltaMovement(0, bookling.getDeltaMovement().y, 0);

				int totalAmmo = bookling.getEntityData().get(BooklingEntity.DATA_ammo);
				int countInterval = 5;
				int aimThreshold = totalAmmo * countInterval + 6;

				// Aiming & counting shots phase
				bookling.aimTicks++;

				if (bookling.aimTicks < aimThreshold) {
					if (bookling.aimTicks % countInterval == 0) {
						int count = bookling.aimTicks / countInterval;
						if (count <= totalAmmo) {
							PacketDistributor.sendToPlayersInDimension(serverLevel,
									new SpawnCustomParticleMessage("minigames:custom_enchant", shootFrom.x, shootFrom.y, shootFrom.z, 0.0D, 0.06D, 0.0D, 15));
							float pitch = 1.2F + ((float) count / Math.max(1, totalAmmo)) * 0.6F;
							serverLevel.playSound(null, bookling.getX(), bookling.getY(), bookling.getZ(),
									BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.enchantment_table.use")), SoundSource.HOSTILE, 0.45F, pitch);
						}
					}
					return;
				}

				// Shooting phase (aimTicks >= aimThreshold)
				if (bookling.shootCooldown > 0) {
					bookling.shootCooldown--;
				} else {
					bookling.shootCooldown = 14;

					// Play shoot animation each time a shot is fired
					bookling.getEntityData().set(BooklingEntity.ANIM, 1000);
					bookling.getEntityData().set(BooklingEntity.ANIM, 1);

					RandomSource rand = bookling.getRandom();
					double spread = 0.04D;
					Vec3 spreadDir = aimDir.normalize().add(
							(rand.nextDouble() - 0.5D) * spread,
							(rand.nextDouble() - 0.5D) * spread,
							(rand.nextDouble() - 0.5D) * spread
					).normalize();

					double speed = 1.25D;
					Vec3 velocity = spreadDir.scale(speed);

					// Raycast collision for block obstacles (walls, floors, obstacles)
					Vec3 reach = shootFrom.add(spreadDir.scale(16.0D));
					ClipContext clipCtx = new ClipContext(shootFrom, reach, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, bookling);
					BlockHitResult blockHit = serverLevel.clip(clipCtx);
					Vec3 endPoint = blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation() : reach;

					// Clamp particle lifetime so particles stop at the wall and never go through
					double distanceToWall = shootFrom.distanceTo(endPoint);
					int particleLifetime = Math.max(1, (int) Math.round(distanceToWall / speed));

					// Spawn custom_enchant particle stream packet
					PacketDistributor.sendToPlayersInDimension(serverLevel,
							new SpawnCustomParticleMessage("minigames:custom_enchant", shootFrom.x, shootFrom.y, shootFrom.z, velocity.x, velocity.y, velocity.z, particleLifetime));

					// Shoot sound
					serverLevel.playSound(null, bookling.getX(), bookling.getY(), bookling.getZ(),
							BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.arrow.shoot")), SoundSource.HOSTILE, 0.7F, 1.4F + rand.nextFloat() * 0.4F);

					// Piercing entity damage: damages all valid entities along the stream up to the wall
					AABB searchBox = new AABB(shootFrom, endPoint).inflate(1.0D);
					List<Entity> candidates = serverLevel.getEntities(bookling, searchBox,
							e -> e instanceof LivingEntity living && living.isAlive() && !living.isAlliedTo(bookling) && !(e instanceof Player p && (p.isCreative() || p.isSpectator())));

					for (Entity e : candidates) {
						AABB bb = e.getBoundingBox().inflate(0.3D);
						Optional<Vec3> intersection = bb.clip(shootFrom, endPoint);
						if (intersection.isPresent()) {
							e.hurtServer(serverLevel, serverLevel.damageSources().mobAttack(bookling), 1.0F);
						}
					}

					// Decrement ammo
					int currentAmmo = bookling.getEntityData().get(BooklingEntity.DATA_ammo) - 1;
					bookling.getEntityData().set(BooklingEntity.DATA_ammo, currentAmmo);

					// Out of ammo: stop firing & unfreeze
					if (currentAmmo <= 0) {
						bookling.aimTicks = 0;
						bookling.shootCooldown = 0;
						bookling.getEntityData().set(BooklingEntity.ANIM, 1000);
						bookling.getEntityData().set(BooklingEntity.ANIM, -2);
					}
				}
			}
		}
	}
}
