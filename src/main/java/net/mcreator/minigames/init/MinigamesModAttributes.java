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
	public static final DeferredHolder<Attribute, Attribute> SALVAGE_VALUE = REGISTRY.register("salvage_value", () -> new RangedAttribute("attribute.minigames.salvage_value", 0, 0, 1000000).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> COINS_ON_KILL = REGISTRY.register("coins_on_kill", () -> new RangedAttribute("attribute.minigames.coins_on_kill", 100, 0, 1000000).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> SHOW_FULL_HEALTH_MOBS = REGISTRY.register("show_full_health_mobs", () -> new RangedAttribute("attribute.minigames.show_full_health_mobs", 0, 0, 1).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> IS_MOB_FULL_HP = REGISTRY.register("is_mob_full_hp", () -> new RangedAttribute("attribute.minigames.is_mob_full_hp", 0, 0, 1).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> STUN_FULL_HEALTH_MOBS = REGISTRY.register("stun_full_health_mobs", () -> new RangedAttribute("attribute.minigames.stun_full_health_mobs", 0, 0, 1).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> REPAIR_VALUE = REGISTRY.register("repair_value", () -> new RangedAttribute("attribute.minigames.repair_value", 0, 0, 1000000).setSyncable(true));

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, SALVAGE_VALUE);
		event.add(EntityType.PLAYER, COINS_ON_KILL);
		event.add(EntityType.PLAYER, SHOW_FULL_HEALTH_MOBS);
		event.getTypes().forEach(entity -> event.add(entity, IS_MOB_FULL_HP));
		event.add(EntityType.PLAYER, STUN_FULL_HEALTH_MOBS);
		event.add(EntityType.PLAYER, REPAIR_VALUE);
	}
}