/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public class MinigamesModAttributes {
	public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MinigamesMod.MODID);
	public static final DeferredHolder<Attribute, Attribute> SALVAGE_VALUE = REGISTRY.register("salvage_value", () -> new RangedAttribute("attribute.minigames.salvage_value", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> COINS_ON_KILL = REGISTRY.register("coins_on_kill", () -> new RangedAttribute("attribute.minigames.coins_on_kill", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> SHOW_FULL_HEALTH_MOBS = REGISTRY.register("show_full_health_mobs", () -> new RangedAttribute("attribute.minigames.show_full_health_mobs", 0d, 0d, 1d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> IS_MOB_FULL_HP = REGISTRY.register("is_mob_full_hp", () -> new RangedAttribute("attribute.minigames.is_mob_full_hp", 0d, 0d, 1d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> STUN_FULL_HEALTH_MOBS = REGISTRY.register("stun_full_health_mobs", () -> new RangedAttribute("attribute.minigames.stun_full_health_mobs", 0d, 0d, 1d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> REPAIR_VALUE = REGISTRY.register("repair_value", () -> new RangedAttribute("attribute.minigames.repair_value", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> DROPPED_COINS = REGISTRY.register("dropped_coins", () -> new RangedAttribute("attribute.minigames.dropped_coins", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> EFFECT_LENGTH = REGISTRY.register("effect_length", () -> new RangedAttribute("attribute.minigames.effect_length", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> EXPLOSION_DAMAGE = REGISTRY.register("explosion_damage", () -> new RangedAttribute("attribute.minigames.explosion_damage", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> EXTRA_DAMAGE = REGISTRY.register("extra_damage", () -> new RangedAttribute("attribute.minigames.extra_damage", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> ABILITY_COOLDOWN = REGISTRY.register("ability_cooldown", () -> new RangedAttribute("attribute.minigames.ability_cooldown", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> ABILITY_RANGE = REGISTRY.register("ability_range", () -> new RangedAttribute("attribute.minigames.ability_range", 0d, 0d, 100d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> HEAL_AMOUNT = REGISTRY.register("heal_amount", () -> new RangedAttribute("attribute.minigames.heal_amount", 0d, 0d, 1000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> LOAD_TIME = REGISTRY.register("load_time", () -> new RangedAttribute("attribute.minigames.load_time", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> RANGED_DAMAGE = REGISTRY.register("ranged_damage", () -> new RangedAttribute("attribute.minigames.ranged_damage", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> ATTACK_COOLDOWN = REGISTRY.register("attack_cooldown", () -> new RangedAttribute("attribute.minigames.attack_cooldown", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> EFFECT_LENGTH_2 = REGISTRY.register("effect_length_2", () -> new RangedAttribute("attribute.minigames.effect_length_2", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> EFFECT_POTENCY = REGISTRY.register("effect_potency", () -> new RangedAttribute("attribute.minigames.effect_potency", 0d, 0d, 1000000d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> LUCK = REGISTRY.register("luck", () -> new RangedAttribute("attribute.minigames.luck", 0d, 0d, 99d).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> EFFECT_POTENCY_2 = REGISTRY.register("effect_potency_2", () -> new RangedAttribute("attribute.minigames.effect_potency_2", 0d, 0d, 1000000d).setSyncable(true));

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, SALVAGE_VALUE);
		event.add(EntityType.PLAYER, COINS_ON_KILL);
		event.add(EntityType.PLAYER, SHOW_FULL_HEALTH_MOBS);
		event.getTypes().forEach(entity -> event.add(entity, IS_MOB_FULL_HP));
		event.add(EntityType.PLAYER, STUN_FULL_HEALTH_MOBS);
		event.add(EntityType.PLAYER, REPAIR_VALUE);
		event.getTypes().forEach(entity -> event.add(entity, DROPPED_COINS));
		event.add(EntityType.PLAYER, EFFECT_LENGTH);
		event.add(EntityType.PLAYER, EXPLOSION_DAMAGE);
		event.add(EntityType.PLAYER, EXTRA_DAMAGE);
		event.add(EntityType.PLAYER, ABILITY_COOLDOWN);
		event.add(EntityType.PLAYER, ABILITY_RANGE);
		event.add(EntityType.PLAYER, HEAL_AMOUNT);
		event.add(EntityType.PLAYER, LOAD_TIME);
		event.add(EntityType.PLAYER, RANGED_DAMAGE);
		event.add(EntityType.PLAYER, ATTACK_COOLDOWN);
		event.add(EntityType.PLAYER, EFFECT_LENGTH_2);
		event.add(EntityType.PLAYER, EFFECT_POTENCY);
		event.add(EntityType.PLAYER, LUCK);
		event.add(EntityType.PLAYER, EFFECT_POTENCY_2);
	}
}