package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;

public class AttackSpeedGemDroppedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity) {
			_entity.getAttribute(Attributes.ATTACK_SPEED).removeModifier(Identifier.parse("minigames:attackspeedgem"));
		}
	}
}