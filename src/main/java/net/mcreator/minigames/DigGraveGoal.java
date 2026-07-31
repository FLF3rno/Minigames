package net.mcreator.minigames;

import java.util.Comparator;
import java.util.Optional;
import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

import net.mcreator.minigames.entity.GravediggerEntity;

public class DigGraveGoal extends Goal {
	private final GravediggerEntity mob;
	private final int searchRadius;
	private BlockPos targetPos;

	public DigGraveGoal(GravediggerEntity mob, int searchRadius) {
		this.mob = mob;
		this.searchRadius = searchRadius;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.mob.isDigging()) {
			return false;
		}
		Optional<Player> visiblePlayer = this.findVisiblePlayer();
		if (visiblePlayer.isEmpty()) {
			return false;
		}
		this.targetPos = this.findNearestCoarseDirt().orElse(null);
		return this.targetPos != null && this.mob.distanceToSqr(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1.0D, this.targetPos.getZ() + 0.5D) <= 2.25D;
	}

	@Override
	public boolean canContinueToUse() {
		return this.mob.isDigging();
	}

	@Override
	public void start() {
		if (this.targetPos != null) {
			this.mob.getNavigation().stop();
			this.mob.beginDigging(this.targetPos);
		}
	}

	@Override
	public void stop() {
		this.targetPos = null;
	}

	private Optional<BlockPos> findNearestCoarseDirt() {
		BlockPos origin = this.mob.blockPosition();
		return BlockPos.betweenClosedStream(
						origin.offset(-this.searchRadius, -3, -this.searchRadius),
						origin.offset(this.searchRadius, 3, this.searchRadius)
				)
				.filter(pos -> this.mob.level().getBlockState(pos).is(Blocks.COARSE_DIRT))
				.map(BlockPos::immutable)
				.min(Comparator.comparingDouble(pos -> pos.distSqr(origin)));
	}

	private Optional<Player> findVisiblePlayer() {
		return this.mob.level().getEntitiesOfClass(
						Player.class,
						this.mob.getBoundingBox().inflate(this.searchRadius),
						player -> player != null && player.isAlive() && !player.isSpectator() && this.mob.getSensing().hasLineOfSight(player)
				).stream()
				.min(Comparator.comparingDouble(this.mob::distanceToSqr));
	}
}