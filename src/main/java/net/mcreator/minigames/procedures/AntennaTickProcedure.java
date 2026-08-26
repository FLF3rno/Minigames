package net.mcreator.minigames.procedures;

import net.mcreator.minigames.entity.FlavioTrapdoor2Entity;
import net.mcreator.minigames.entity.FlavioTrapdoor3Entity;
import net.mcreator.minigames.entity.FlavioTrapdoorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntennaTickProcedure {

	private static final Map<Integer, List<Vec3>> STRIKE_POSITIONS = new HashMap<>();

	public static void execute(
			LevelAccessor world,
			double x,
			double y,
			double z,
			Entity entity
	) {
		if (entity == null)
			return;

		boolean attack = false;
		boolean telegraph = false;

		List<Vec3> strikePositions =
				STRIKE_POSITIONS.computeIfAbsent(
						entity.getId(),
						id -> new ArrayList<>()
				);

		if (entity.tickCount < 35) {

			entity.setYRot(entity.getYRot() + 10.0F);
			entity.setXRot(0.0F);
			entity.setYBodyRot(entity.getYRot());
			entity.setYHeadRot(entity.getYRot());

			entity.yRotO = entity.getYRot();
			entity.xRotO = entity.getXRot();

			if (entity instanceof LivingEntity living) {
				living.yBodyRotO = living.getYRot();
				living.yHeadRotO = living.getYRot();
			}

			entity.teleportTo(x, y + 0.12D, z);

		} else if (entity.tickCount == 35) {

			entity.setYRot(0.0F);
			entity.setXRot(0.0F);
			entity.setYBodyRot(0.0F);
			entity.setYHeadRot(0.0F);

			entity.yRotO = 0.0F;
			entity.xRotO = 0.0F;

			if (entity instanceof LivingEntity living) {
				living.yBodyRotO = 0.0F;
				living.yHeadRotO = 0.0F;
			}

		} else if (entity.tickCount > 65) {

			if (entity.tickCount % 10 == 0) {
				attack = true;
			}

			if (entity.tickCount % 10 == 5) {
				telegraph = true;
			}
		}

		if (telegraph) {

			strikePositions.clear();

			for (Player player : world.players()) {

				double strikeX =
						Math.floor(player.getX()) + 0.5D;

				double strikeZ =
						Math.floor(player.getZ()) + 0.5D;

				double surfaceY = findSurfaceY(
						world,
						strikeX,
						strikeZ,
						player.getY(),
						entity
				);

				if (surfaceY == Double.MIN_VALUE)
					continue;

				Vec3 strikePos = new Vec3(
						strikeX,
						surfaceY,
						strikeZ
				);

				strikePositions.add(strikePos);

				ApplyTelegraphProcedure.execute(
						world,
						strikeX,
						surfaceY,
						strikeZ,
						15,
						0
				);
			}
		}

		if (attack && world instanceof ServerLevel serverLevel) {

			List<Vec3> positionsToAttack =
					new ArrayList<>(strikePositions);

			for (Vec3 pos : positionsToAttack) {

				double spawnX = pos.x;
				double spawnY = pos.y - 0.5D;
				double spawnZ = pos.z;

				String command =
						"summon minigames:spike_trap "
								+ spawnX + " "
								+ spawnY + " "
								+ spawnZ;

				serverLevel.getServer()
						.getCommands()
						.performPrefixedCommand(
								serverLevel.getServer()
										.createCommandSourceStack()
										.withLevel(serverLevel)
										.withPosition(
												new Vec3(
														spawnX,
														spawnY,
														spawnZ
												)
										)
										.withSuppressedOutput(),
								command
						);
			}

			strikePositions.clear();
		}
	}

	private static double findSurfaceY(
			LevelAccessor world,
			double x,
			double z,
			double startY,
			Entity source
	) {
		double highestEntityY = Double.MIN_VALUE;

		AABB searchBox = new AABB(
				x - 0.05D,
				world.getMinY(),
				z - 0.05D,
				x + 0.05D,
				startY + 2.0D,
				z + 0.05D
		);

		for (Entity target : world.getEntities(
				source,
				searchBox,
				e -> e != source
						&& (e instanceof FlavioTrapdoorEntity
						|| e instanceof FlavioTrapdoor2Entity
						|| e instanceof FlavioTrapdoor3Entity)
		)) {

			AABB box = target.getBoundingBox();

			if (x >= box.minX
					&& x <= box.maxX
					&& z >= box.minZ
					&& z <= box.maxZ) {

				highestEntityY = Math.max(
						highestEntityY,
						box.maxY
				);
			}
		}

		if (highestEntityY != Double.MIN_VALUE)
			return highestEntityY;

		int blockX = (int) Math.floor(x);
		int blockZ = (int) Math.floor(z);

		int currentY = Math.min(
				(int) Math.floor(startY),
				world.getMaxY() - 1
		);

		while (currentY >= world.getMinY()) {

			BlockPos pos = new BlockPos(
					blockX,
					currentY,
					blockZ
			);

			if (!world.getBlockState(pos).isAir())
				return currentY + 1.0D;

			currentY--;
		}

		return Double.MIN_VALUE;
	}
}