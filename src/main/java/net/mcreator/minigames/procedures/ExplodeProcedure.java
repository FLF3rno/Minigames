package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

public class ExplodeProcedure {
	public static void execute(Entity OwnedBy, boolean DamagePlayers, boolean OwnerImmune, double ExplosionDamage, double ExplosionSize, String ExplosionType) {
		if (OwnedBy == null || ExplosionType == null)
			return;
		String type = "";
		double damage = 0;
		double size = 0;
		Entity origin = null;
		boolean canDamagePlayers = false;
		boolean ownerImmune = false;
		type = ExplosionType;
		damage = ExplosionDamage;
		size = ExplosionSize;
		canDamagePlayers = DamagePlayers;
		origin = OwnedBy;
		ownerImmune = OwnerImmune;
	}
}