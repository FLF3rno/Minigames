package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

import java.util.ArrayList;

public class ExplorersCharmTickProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (!((MinigamesModVariables.MapVariables.get(world).currentRoomType).equals("fight") || (MinigamesModVariables.MapVariables.get(world).currentRoomType).equals("boss")
				|| (MinigamesModVariables.MapVariables.get(world).currentRoomType).equals("miniboss") || (MinigamesModVariables.MapVariables.get(world).currentRoomType).equals("challenge"))
				&& CheckRelicProcedure.execute(entity, new ItemStack(MinigamesModItems.EXPLORERS_CHARM.get()))) {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (entityiterator instanceof LivingEntity _entity) {
					AttributeModifier modifier = new AttributeModifier(Identifier.parse("minigames:explorerscharm"), (GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency") / 100), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
					if (!_entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(modifier.id())) {
						_entity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(modifier);
					}
				}
			}
		} else {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (entityiterator instanceof LivingEntity _entity) {
					_entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(Identifier.parse("minigames:explorerscharm"));
				}
			}
		}
	}
}