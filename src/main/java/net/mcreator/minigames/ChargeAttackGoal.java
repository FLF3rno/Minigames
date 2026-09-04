package net.mcreator.minigames;

import java.util.Comparator;
import java.util.Optional;
import java.util.EnumSet; // Added for goal flags

import net.mcreator.minigames.entity.I.IDiggerMob;
import net.mcreator.minigames.entity.I.IChargerMob;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity; // Changed from Player
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;

public class ChargeAttackGoal<T extends PathfinderMob & IDiggerMob & IChargerMob> extends Goal {
	private final T mob;
	private final double speedModifier;
	private final int searchRadius;
	private final int chargeTicks;
	private LivingEntity targetEntity;
	private int chargeTick;

	public ChargeAttackGoal(T mob, double speedModifier, int searchRadius, int chargeTicks) {
		this.mob = mob;
		this.speedModifier = speedModifier;
		this.searchRadius = searchRadius;
		this.chargeTicks = chargeTicks;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.mob.isDigging() || this.findNearestCoarseDirt().isPresent()) {
			return false;
		}
		this.targetEntity = this.findVisibleTarget().orElse(null);
		return this.targetEntity != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.targetEntity != null && this.targetEntity.isAlive() && !this.mob.isDigging() && this.findNearestCoarseDirt().isEmpty() && this.mob.getSensing().hasLineOfSight(this.targetEntity);
	}

	@Override
	public void start() {
		this.chargeTick = 0;
		if (this.targetEntity != null) {
			this.mob.getNavigation().moveTo(this.targetEntity, this.speedModifier);
		}
	}

	@Override
	public void stop() {
		this.mob.getNavigation().stop();
		this.targetEntity = null;
		this.chargeTick = 0;
	}

	@Override
	public void tick() {
		if (this.targetEntity == null || this.mob.isDigging()) {
			return;
		}
		if (!this.targetEntity.isAlive() || this.findNearestCoarseDirt().isPresent() || !this.mob.getSensing().hasLineOfSight(this.targetEntity)) {
			this.stop();
			return;
		}
		double distanceSqr = this.mob.distanceToSqr(this.targetEntity);
		if (distanceSqr > 4.0D) {
			this.mob.getNavigation().moveTo(this.targetEntity, this.speedModifier);
			this.chargeTick = 0;
			return;
		}
		this.mob.getNavigation().stop();
		if (this.chargeTick == 0) {
			this.mob.startAttackAnimation();
		}
		this.chargeTick++;
		if (this.chargeTick >= this.chargeTicks) {
			if (this.targetEntity.distanceToSqr(this.mob) <= 4.0D && this.mob.getSensing().hasLineOfSight(this.targetEntity)) {
				this.mob.doChargedAttack(this.targetEntity);
			}
			this.mob.stopAttackAnimation();
			this.stop();
		}
	}

	private Optional<BlockPos> findNearestCoarseDirt() {
		BlockPos origin = this.mob.blockPosition();
		return BlockPos.betweenClosedStream(origin.offset(-this.searchRadius, -3, -this.searchRadius), origin.offset(this.searchRadius, 3, this.searchRadius))
				.filter(pos -> this.mob.level().getBlockState(pos).is(Blocks.COARSE_DIRT))
				.map(BlockPos::immutable)
				.min(Comparator.comparingDouble(pos -> pos.distSqr(origin)));
	}

	private Optional<LivingEntity> findVisibleTarget() {
		LivingEntity currentTarget = this.mob.getTarget();

		if (currentTarget != null && currentTarget.isAlive() && this.mob.getSensing().hasLineOfSight(currentTarget) && this.mob.distanceToSqr(currentTarget) <= (this.searchRadius * this.searchRadius)) {
			return Optional.of(currentTarget);
		}
		return Optional.empty();
	}
}