package net.mcreator.minigames;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import java.util.EnumSet;
import java.util.List;
import java.util.Comparator;

public class RandomLookAtPlayerGoal extends Goal {
    private final Mob mob;
    private Player target;

    public RandomLookAtPlayerGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob instanceof net.mcreator.minigames.entity.PreacherEntity preacher) {
            int roomID = preacher.getRoomID();
            if (roomID > 0) {
                if (net.mcreator.minigames.network.MinigamesModVariables.MapVariables.get(preacher.level()).currentRoomID != roomID) {
                    this.target = null;
                    this.mob.setTarget(null);
                    return false;
                }
            }
        }

        List<Player> players = this.mob.level().getEntitiesOfClass(Player.class, 
            this.mob.getBoundingBox().inflate(50.0D),
            p -> p.isAlive() && !p.isSpectator());
        
        if (players.isEmpty()) return false;

        this.target = players.stream()
            .max(Comparator.comparingDouble(Player::getY))
            .orElse(null);
        if (this.target == null) return false;

        this.mob.setTarget(this.target);
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob instanceof net.mcreator.minigames.entity.PreacherEntity preacher) {
            int roomID = preacher.getRoomID();
            if (roomID > 0) {
                if (net.mcreator.minigames.network.MinigamesModVariables.MapVariables.get(preacher.level()).currentRoomID != roomID) {
                    this.target = null;
                    this.mob.setTarget(null);
                    return false;
                }
            }
        }
        return this.target != null && this.target.isAlive() && !this.target.isSpectator() && this.mob.distanceToSqr(this.target) < 900.0D;
    }

    @Override
    public void tick() {
        // Re-evaluate target periodically to ensure preacher keeps tracking the highest Y player
        if (this.mob.tickCount % 10 == 0) {
            List<Player> players = this.mob.level().getEntitiesOfClass(Player.class,
                this.mob.getBoundingBox().inflate(50.0D),
                p -> p.isAlive() && !p.isSpectator());
            if (!players.isEmpty()) {
                Player highestPlayer = players.stream()
                    .max(Comparator.comparingDouble(Player::getY))
                    .orElse(null);
                if (highestPlayer != null) {
                    this.target = highestPlayer;
                    this.mob.setTarget(highestPlayer);
                }
            }
        }

        if (this.target != null && this.mob.distanceToSqr(this.target) < 900.0D) {
            this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        }
    }
}