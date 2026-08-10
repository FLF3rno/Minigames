/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.MinigamesMod;

public class MinigamesModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, MinigamesMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> COUNTDOWN = REGISTRY.register("countdown", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "countdown")));
	public static final DeferredHolder<SoundEvent, SoundEvent> START = REGISTRY.register("start", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "start")));
	public static final DeferredHolder<SoundEvent, SoundEvent> PVP_SWORDSHIT = REGISTRY.register("pvp_swordshit", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "pvp_swordshit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> PVP_SWIND = REGISTRY.register("pvp_swind", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "pvp_swind")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MENU_SWITCH = REGISTRY.register("menu_switch", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "menu_switch")));
	public static final DeferredHolder<SoundEvent, SoundEvent> TEAMELIMINATED = REGISTRY.register("teameliminated", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "teameliminated")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLDICE = REGISTRY.register("rolldice", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "rolldice")));
	public static final DeferredHolder<SoundEvent, SoundEvent> CLOCK = REGISTRY.register("clock", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "clock")));
	public static final DeferredHolder<SoundEvent, SoundEvent> THRUSTER = REGISTRY.register("thruster", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "thruster")));
	public static final DeferredHolder<SoundEvent, SoundEvent> INFLATE = REGISTRY.register("inflate", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "inflate")));
	public static final DeferredHolder<SoundEvent, SoundEvent> POP = REGISTRY.register("pop", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "pop")));
	public static final DeferredHolder<SoundEvent, SoundEvent> HYPNOTIZED = REGISTRY.register("hypnotized", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "hypnotized")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VOLCANIC_SPEW_LAND = REGISTRY.register("volcanic_spew_land", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "volcanic_spew_land")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DASH = REGISTRY.register("dash", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "dash")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ASCENDING = REGISTRY.register("ascending", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "ascending")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_SULFUR_STEP = REGISTRY.register("update_sulfur_step", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_sulfur_step")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_SULFUR_PLACE = REGISTRY.register("update_sulfur_place", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_sulfur_place")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_SULFUR_HIT = REGISTRY.register("update_sulfur_hit", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_sulfur_hit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_SULFUR_BREAK = REGISTRY.register("update_sulfur_break", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_sulfur_break")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_POTENT_SULFUR_STEP = REGISTRY.register("update_potent_sulfur_step",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_potent_sulfur_step")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_POTENT_SULFUR_HIT = REGISTRY.register("update_potent_sulfur_hit",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_potent_sulfur_hit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_POTENT_SULFUR_BREAK = REGISTRY.register("update_potent_sulfur_break",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_potent_sulfur_break")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_POTENT_SULFUR_PLACE = REGISTRY.register("update_potent_sulfur_place",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_potent_sulfur_place")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_CINNABAR_BREAK = REGISTRY.register("update_cinnabar_break", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_cinnabar_break")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_CINNABAR_HIT = REGISTRY.register("update_cinnabar_hit", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_cinnabar_hit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_CINNABAR_PLACE = REGISTRY.register("update_cinnabar_place", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_cinnabar_place")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_CINNABAR_STEP = REGISTRY.register("update_cinnabar_step", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "update_cinnabar_step")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VOLLEYBOMB_HIT = REGISTRY.register("volleybomb_hit", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "volleybomb_hit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> RED_EXPLOSION = REGISTRY.register("red_explosion", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "red_explosion")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ZAP = REGISTRY.register("zap", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "zap")));
	public static final DeferredHolder<SoundEvent, SoundEvent> PARTY_EXPLODE = REGISTRY.register("party_explode", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("minigames", "party_explode")));
}