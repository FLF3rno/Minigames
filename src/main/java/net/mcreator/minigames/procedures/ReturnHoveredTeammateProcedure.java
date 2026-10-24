package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReturnHoveredTeammateProcedure {
	public static Entity execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return null;

		double maxDistance = 64.0;
		Vec3 eyePos = entity.getEyePosition(1.0f);
		Vec3 viewVec = entity.getViewVector(1.0f);
		Vec3 reachVec = eyePos.add(viewVec.scale(maxDistance));

		BlockHitResult blockHit = entity.level().clip(new ClipContext(eyePos, reachVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
		Vec3 endPos = reachVec;
		if (blockHit.getType() != HitResult.Type.MISS) {
			endPos = blockHit.getLocation();
		}

		// 1. Direct line-of-sight raycast: prioritize players directly in the crosshairs
		AABB searchBox = new AABB(eyePos, endPos).inflate(1.5);
		List<Player> candidates = entity.level().getEntitiesOfClass(
			Player.class,
			searchBox,
			p -> p != entity && p.isAlive() && !p.isSpectator()
		);

		Player directHitPlayer = null;
		double closestDistSq = Double.MAX_VALUE;

		for (Player candidate : candidates) {
			AABB hitBox = candidate.getBoundingBox().inflate(0.5);
			Optional<Vec3> hit = hitBox.clip(eyePos, endPos);
			if (hit.isPresent()) {
				double distSq = eyePos.distanceToSqr(hit.get());
				if (distSq < closestDistSq) {
					closestDistSq = distSq;
					directHitPlayer = candidate;
				}
			}
		}

		if (directHitPlayer != null) {
			return directHitPlayer;
		}

		// 2. Block hit check: if aiming at a block (e.g. ground at your feet or near a teammate),
		// find players close to the hit block (including the caster themselves)
		if (blockHit.getType() != HitResult.Type.MISS) {
			Vec3 hitLoc = blockHit.getLocation();
			AABB blockBox = AABB.ofSize(hitLoc, 7.0, 5.0, 7.0);

			List<Player> nearbyPlayers = entity.level().getEntitiesOfClass(
				Player.class,
				blockBox,
				p -> p.isAlive() && !p.isSpectator()
			);

			return nearbyPlayers.stream()
				.min(Comparator.comparingDouble(p -> p.distanceToSqr(hitLoc)))
				.orElse(null);
		}

		return null;
	}
}