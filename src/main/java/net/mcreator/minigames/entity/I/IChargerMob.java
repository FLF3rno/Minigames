package net.mcreator.minigames.entity.I;

import net.minecraft.world.entity.LivingEntity;

public interface IChargerMob {
    void startAttackAnimation();
    void stopAttackAnimation();
    void doChargedAttack(LivingEntity target);
}