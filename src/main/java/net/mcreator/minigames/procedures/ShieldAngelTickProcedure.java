package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.entity.ShieldAngelEntity;

public class ShieldAngelTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof ShieldAngelEntity _datEntSetI)
			_datEntSetI.getEntityData().set(ShieldAngelEntity.DATA_timeSinceLastHit, (int) ((entity instanceof ShieldAngelEntity _datEntI ? _datEntI.getEntityData().get(ShieldAngelEntity.DATA_timeSinceLastHit) : 0) + 1));
	}
}