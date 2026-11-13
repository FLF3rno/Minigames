package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;

import java.util.ArrayList;

public class ReturnDamageRoguelikeProcedure {
	public static double execute(LevelAccessor world, DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
		if (damagesource == null || entity == null || sourceentity == null)
			return 0;
		double damage = 0;
		Entity target = null;
		ArrayList<Object> entitiesinrange = new ArrayList<>();
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.BLESSED) && !damagesource.is(TagKey.create(Registries.DAMAGE_TYPE, Identifier.parse("minecraft:bypasses_cooldown")))) {
			return 0;
		} else if (MinigamesModVariables.MapVariables.get(world).playingDungeons && entity instanceof Player && sourceentity instanceof Player) {
			return 0;
		}
		damage = amount * (1 + (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("forged", 0) * 0.01
				+ (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDoubleOr("glitched", 0) * 0.01);
		if (CheckRelicProcedure.execute(sourceentity, new ItemStack(MinigamesModItems.BAT_ESSENCE.get()))) {
			damage = damage + damage * (net.mcreator.minigames.procedures.GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(sourceentity, new ItemStack(MinigamesModItems.BAT_ESSENCE.get())), "minigames:extra_damage") / 100);
		}
		if (IsBackstabProcedure.execute(sourceentity, entity) && CanBackstabProcedure.execute(sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) {
			damage = damage * net.mcreator.minigames.procedures.GetItemAttributeProcedure.execute((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY), "minigames:extra_damage");
		}
		if (sourceentity instanceof LivingEntity _livEnt12 && _livEnt12.hasEffect(MinigamesModMobEffects.DAMAGE_BOOST)) {
			damage = damage + damage * (((sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.DAMAGE_BOOST) ? _livEnt.getEffect(MinigamesModMobEffects.DAMAGE_BOOST).getAmplifier() : 0) + 1) / 100d);
		}
		if (entity instanceof LivingEntity _livEnt14 && _livEnt14.hasEffect(MinigamesModMobEffects.DAMAGE_RESISTANCE)) {
			damage = damage - damage * (((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.DAMAGE_RESISTANCE) ? _livEnt.getEffect(MinigamesModMobEffects.DAMAGE_RESISTANCE).getAmplifier() : 0) + 1) / 100d);
		}
		return damage;
	}
}