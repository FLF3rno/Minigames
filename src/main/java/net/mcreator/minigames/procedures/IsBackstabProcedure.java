package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

public class IsBackstabProcedure {
	public static boolean execute(Entity attacker, Entity target) {
		if (attacker == null || target == null)
			return false;
		double AttackX = 0;
		double AttackZ = 0;
		double Length = 0;
		double dot = 0;
		AttackX = attacker.getX() - target.getX();
		AttackZ = attacker.getZ() - target.getZ();
		Length = Math.sqrt(AttackX * AttackX + AttackZ * AttackZ);
		AttackX = AttackX / Length;
		AttackZ = AttackZ / Length;
		dot = target.getLookAngle().x * AttackX + target.getLookAngle().z * AttackZ;
		return dot < -0.3;
	}
}