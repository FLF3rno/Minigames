package net.mcreator.minigames;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class ChargeExecuteGoal extends Goal {

	@FunctionalInterface
	public interface Procedure {
		void execute(Mob mob);
	}

	private final Mob mob;
	private final int waitTicks;
	private final Procedure procedure;

	private int timer;

	public ChargeExecuteGoal(Mob mob, int waitTicks, Procedure procedure) {
		this.mob = mob;
		this.waitTicks = waitTicks;
		this.procedure = procedure;
	}

	@Override
	public boolean canUse() {
		return mob.getTarget() != null;
	}

	@Override
	public boolean canContinueToUse() {
		return timer < waitTicks && mob.getTarget() != null;
	}

	@Override
	public void start() {
		timer = 0;
	}

	@Override
	public void tick() {
		if (++timer >= waitTicks) {
			procedure.execute(mob);
		}
	}

	@Override
	public void stop() {
		timer = 0;
	}
}