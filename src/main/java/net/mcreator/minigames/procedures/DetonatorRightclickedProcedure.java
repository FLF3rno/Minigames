package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

public class DetonatorRightclickedProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		Entity target = ReturnHoveredTeammateProcedure.execute(world, entity);
		if (target != null) {
			ExplodeProcedure.execute(world, target.getX(), target.getY(), target.getZ(),
					target, false, true, GetItemAttributeProcedure.execute(itemstack, "minigames:explosion_damage"),
					Math.sqrt(GetItemAttributeProcedure.execute(itemstack, "minigames:explosion_damage")) * 0.1, 4, "normal");
			{
				if (target.level() instanceof ServerLevel _serverLevel) {
					target.hurtServer(_serverLevel, new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.parse("minigames:self_damage")))),
							(float) GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage"));
				}
			}
			ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		}
	}
}