package net.mcreator.minigames.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.init.MinigamesModItems;

public class ImmovableTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (CheckRelicProcedure.execute(entity, new ItemStack(MinigamesModItems.IMMOVABLE.get()))) {
			if (entity instanceof LivingEntity _entity) {
				AttributeModifier modifier = new AttributeModifier(Identifier.parse("minigames:immovable"), 100, AttributeModifier.Operation.ADD_VALUE);
				if (!_entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).hasModifier(modifier.id())) {
					_entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(modifier);
				}
			}
		} else {
			if (entity instanceof LivingEntity _entity) {
				_entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(Identifier.parse("minigames:immovable"));
			}
		}
	}
}