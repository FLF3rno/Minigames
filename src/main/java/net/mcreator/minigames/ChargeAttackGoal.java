package net.mcreator.minigames;

import java.util.Comparator;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import net.mcreator.minigames.entity.GravediggerEntity;

public class ChargeAttackGoal extends Goal {
	private final GravediggerEntity mob;
	private final double speedModifier;
	private final int searchRadius;
	private final int chargeTicks;
	private Player targetPlayer;
	private int chargeTick;

	public ChargeAttackGoal(GravediggerEntity mob, double speedModifier, int searchRadius, int chargeTicks) {
		this.mob = mob;
		this.speedModifier = speedModifier;
		this.searchRadius = searchRadius;
		this.chargeTicks = chargeTicks;
	}

	@Override
	public boolean canUse() {
		if (this.mob.isDigging() || this.findNearestCoarseDirt().isPresent()) {
			return false;
		}
		this.targetPlayer = this.findVisiblePlayer().orElse(null);
		return this.targetPlayer != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.targetPlayer != null && this.targetPlayer.isAlive() && !this.mob.isDigging() && this.findNearestCoarseDirt().isEmpty() && this.mob.getSensing().hasLineOfSight(this.targetPlayer);
	}

	@Override
	public void start() {
		this.chargeTick = 0;
		if (this.targetPlayer != null) {
			this.mob.getNavigation().moveTo(this.targetPlayer, this.speedModifier);
		}
	}

	@Override
	public void stop() {
		this.mob.getNavigation().stop();
		this.targetPlayer = null;
		this.chargeTick = 0;
	}

	@Override
	public void tick() {
		if (this.targetPlayer == null || this.mob.isDigging()) {
			return;
		}
		if (!this.targetPlayer.isAlive() || this.findNearestCoarseDirt().isPresent() || !this.mob.getSensing().hasLineOfSight(this.targetPlayer)) {
			this.stop();
			return;
		}
		double distanceSqr = this.mob.distanceToSqr(this.targetPlayer);
		if (distanceSqr > 4.0D) {
			this.mob.getNavigation().moveTo(this.targetPlayer, this.speedModifier);
			this.chargeTick = 0;
			return;
		}
		this.mob.getNavigation().stop();
		if (this.chargeTick == 0) {
			this.mob.startAttackAnimation();
		}
		this.chargeTick++;
		if (this.chargeTick >= this.chargeTicks) {
			if (this.targetPlayer.distanceToSqr(this.mob) <= 4.0D && this.mob.getSensing().hasLineOfSight(this.targetPlayer)) {
				this.mob.doChargedAttack(this.targetPlayer);
			}
			this.mob.stopAttackAnimation();
			this.stop();
		}
	}

	private Optional<BlockPos> findNearestCoarseDirt() {
		BlockPos origin = this.mob.blockPosition();
		return BlockPos.betweenClosedStream(origin.offset(-this.searchRadius, -3, -this.searchRadius), origin.offset(this.searchRadius, 3, this.searchRadius))
			.filter(pos -> this.mob.level().getBlockState(pos).is(net.minecraft.world.level.block.Blocks.COARSE_DIRT))
			.map(BlockPos::immutable)
			.min(Comparator.comparingDouble(pos -> pos.distSqr(origin)));
	}

	private Optional<Player> findVisiblePlayer() {
		return this.mob.level().getEntitiesOfClass(Player.class, this.mob.getBoundingBox().inflate(this.searchRadius), player -> player.isAlive() && this.mob.getSensing().hasLineOfSight(player)).stream()
			.min(Comparator.comparingDouble(this.mob::distanceToSqr));
	}
}
