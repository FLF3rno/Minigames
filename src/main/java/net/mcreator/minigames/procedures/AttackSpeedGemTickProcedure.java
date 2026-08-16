package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.init.MinigamesModItems;

public class AttackSpeedGemTickProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (CheckRelicProcedure.execute(entity, new ItemStack(MinigamesModItems.ATTACK_SPEED_GEM.get()))) {
			if (entity instanceof LivingEntity _entity) {
				AttributeModifier modifier = new AttributeModifier(Identifier.parse("minigames:attackspeedgem"), GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"), AttributeModifier.Operation.ADD_VALUE);
				if (!_entity.getAttribute(Attributes.ATTACK_SPEED).hasModifier(modifier.id())) {
					_entity.getAttribute(Attributes.ATTACK_SPEED).addPermanentModifier(modifier);
				}
			}
		} else {
			if (entity instanceof LivingEntity _entity) {
				_entity.getAttribute(Attributes.ATTACK_SPEED).removeModifier(Identifier.parse("minigames:attackspeedgem"));
			}
		}
	}
}