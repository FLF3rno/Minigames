package net.mcreator.minigames;

import java.util.Comparator;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import net.mcreator.minigames.entity.GravediggerEntity;

public class MoveToCoarseDirtGoal extends Goal {
	private final GravediggerEntity mob;
	private final double speedModifier;
	private final int searchRadius;
	private BlockPos targetPos;

	public MoveToCoarseDirtGoal(GravediggerEntity mob, double speedModifier, int searchRadius) {
		this.mob = mob;
		this.speedModifier = speedModifier;
		this.searchRadius = searchRadius;
	}

	@Override
	public boolean canUse() {
		if (this.mob.isDigging()) {
			return false;
		}
		if (this.findVisiblePlayer().isEmpty()) {
			return false;
		}
		return this.findNearestCoarseDirt().isPresent();
	}

	@Override
	public boolean canContinueToUse() {
		return !this.mob.isDigging() && this.targetPos != null && this.mob.level().getBlockState(this.targetPos).is(Blocks.COARSE_DIRT) && this.findVisiblePlayer().isPresent();
	}

	@Override
	public void start() {
		this.targetPos = this.findNearestCoarseDirt().orElse(null);
		if (this.targetPos != null) {
			this.mob.getNavigation().moveTo(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1.0D, this.targetPos.getZ() + 0.5D, this.speedModifier);
		}
	}

	@Override
	public void stop() {
		this.targetPos = null;
		this.mob.getNavigation().stop();
	}

	@Override
	public void tick() {
		if (this.targetPos == null || this.mob.isDigging()) {
			return;
		}
		if (this.findVisiblePlayer().isEmpty()) {
			this.mob.getNavigation().stop();
			this.targetPos = null;
			return;
		}
		PathNavigation navigation = this.mob.getNavigation();
		if (this.mob.distanceToSqr(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1.0D, this.targetPos.getZ() + 0.5D) <= 2.25D) {
			this.mob.beginDigging(this.targetPos);
			navigation.stop();
			this.targetPos = null;
			return;
		}
		if (navigation.isDone()) {
			navigation.moveTo(this.targetPos.getX() + 0.5D, this.targetPos.getY() + 1.0D, this.targetPos.getZ() + 0.5D, this.speedModifier);
		}
	}

	private Optional<BlockPos> findNearestCoarseDirt() {
		Level level = this.mob.level();
		BlockPos origin = this.mob.blockPosition();
		return BlockPos.betweenClosedStream(origin.offset(-this.searchRadius, -3, -this.searchRadius), origin.offset(this.searchRadius, 3, this.searchRadius))
			.filter(pos -> level.getBlockState(pos).is(Blocks.COARSE_DIRT))
			.map(BlockPos::immutable)
			.min(Comparator.comparingDouble(pos -> pos.distSqr(origin)));
	}

	private Optional<Player> findVisiblePlayer() {
		AABB searchBox = this.mob.getBoundingBox().inflate(this.searchRadius);
		return this.mob.level().getEntitiesOfClass(Player.class, searchBox, player -> player.isAlive() && this.mob.getSensing().hasLineOfSight(player)).stream()
			.min(Comparator.comparingDouble(this.mob::distanceToSqr));
	}
}
