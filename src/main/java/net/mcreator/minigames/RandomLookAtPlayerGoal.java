package net.mcreator.minigames;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import java.util.EnumSet;
import java.util.List;

public class RandomLookAtPlayerGoal extends Goal {
    private final Mob mob;
    private Player target;

    public RandomLookAtPlayerGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        List<Player> players = this.mob.level().getEntitiesOfClass(Player.class, 
            this.mob.getBoundingBox().inflate(50.0D));
        
        if (players.isEmpty()) return false;

        this.target = players.get(this.mob.getRandom().nextInt(players.size()));
        this.mob.setTarget(this.target);
        return true;
    }

    @Override
    public void tick() {
        if (this.target != null && this.mob.distanceToSqr(this.target) < 900.0D) {
            this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        }
    }
}