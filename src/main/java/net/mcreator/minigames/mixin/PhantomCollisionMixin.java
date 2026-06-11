package net.mcreator.minigames.mixin;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class PhantomCollisionMixin {

    @Unique
    private static final Scoreboard MINIGAMES$SCOREBOARD = new Scoreboard();

    @Unique
    private static final PlayerTeam MINIGAMES$PHANTOM_TEAM;

    static {
        MINIGAMES$PHANTOM_TEAM =
                new PlayerTeam(MINIGAMES$SCOREBOARD, "phantom_bypass");
        MINIGAMES$PHANTOM_TEAM.setCollisionRule(Team.CollisionRule.NEVER);
    }

    @Inject(method = "getTeam", at = @At("HEAD"), cancellable = true)
    private void minigames$phantomTeam(CallbackInfoReturnable<Team> cir) {
        if ((Object) this instanceof Player player) {
            if (player.hasEffect(MinigamesModMobEffects.PHANTOM)) {
                cir.setReturnValue(MINIGAMES$PHANTOM_TEAM);
            }
        }
    }
}