/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minigames.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.minigames.MinigamesMod;

public class MinigamesModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, MinigamesMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> COUNTDOWN = REGISTRY.register("countdown", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "countdown")));
	public static final DeferredHolder<SoundEvent, SoundEvent> START = REGISTRY.register("start", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "start")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLAUDIO = REGISTRY.register("rollaudio", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "rollaudio")));
	public static final DeferredHolder<SoundEvent, SoundEvent> NETHERBREAK = REGISTRY.register("netherbreak", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "netherbreak")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SANDBREAK = REGISTRY.register("sandbreak", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "sandbreak")));
	public static final DeferredHolder<SoundEvent, SoundEvent> STONEBREAK = REGISTRY.register("stonebreak", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "stonebreak")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DIRTBREAK = REGISTRY.register("dirtbreak", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "dirtbreak")));
	public static final DeferredHolder<SoundEvent, SoundEvent> PVP_SWORDSHIT = REGISTRY.register("pvp_swordshit", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "pvp_swordshit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> PVP_SWIND = REGISTRY.register("pvp_swind", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "pvp_swind")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MENU_SWITCH = REGISTRY.register("menu_switch", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "menu_switch")));
	public static final DeferredHolder<SoundEvent, SoundEvent> TEAMELIMINATED = REGISTRY.register("teameliminated", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "teameliminated")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLDICE = REGISTRY.register("rolldice", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "rolldice")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ROLLAUDIOCLEAN = REGISTRY.register("rollaudioclean", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "rollaudioclean")));
	public static final DeferredHolder<SoundEvent, SoundEvent> CLOCK = REGISTRY.register("clock", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "clock")));
	public static final DeferredHolder<SoundEvent, SoundEvent> THRUSTER = REGISTRY.register("thruster", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "thruster")));
	public static final DeferredHolder<SoundEvent, SoundEvent> INFLATE = REGISTRY.register("inflate", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "inflate")));
	public static final DeferredHolder<SoundEvent, SoundEvent> POP = REGISTRY.register("pop", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "pop")));
	public static final DeferredHolder<SoundEvent, SoundEvent> HYPNOTIZED = REGISTRY.register("hypnotized", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "hypnotized")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VOLCANIC_SPEW_LAND = REGISTRY.register("volcanic_spew_land", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "volcanic_spew_land")));
	public static final DeferredHolder<SoundEvent, SoundEvent> DASH = REGISTRY.register("dash", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "dash")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ASCENDING = REGISTRY.register("ascending", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "ascending")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_SULFUR_STEP = REGISTRY.register("update_sulfur_step", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_sulfur_step")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_SULFUR_PLACE = REGISTRY.register("update_sulfur_place", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_sulfur_place")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_SULFUR_HIT = REGISTRY.register("update_sulfur_hit", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_sulfur_hit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_SULFUR_BREAK = REGISTRY.register("update_sulfur_break", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_sulfur_break")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_POTENT_SULFUR_STEP = REGISTRY.register("update_potent_sulfur_step",
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_potent_sulfur_step")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_POTENT_SULFUR_HIT = REGISTRY.register("update_potent_sulfur_hit",
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_potent_sulfur_hit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_POTENT_SULFUR_BREAK = REGISTRY.register("update_potent_sulfur_break",
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_potent_sulfur_break")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_POTENT_SULFUR_PLACE = REGISTRY.register("update_potent_sulfur_place",
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_potent_sulfur_place")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_CINNABAR_BREAK = REGISTRY.register("update_cinnabar_break", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_cinnabar_break")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_CINNABAR_HIT = REGISTRY.register("update_cinnabar_hit", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_cinnabar_hit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_CINNABAR_PLACE = REGISTRY.register("update_cinnabar_place", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_cinnabar_place")));
	public static final DeferredHolder<SoundEvent, SoundEvent> UPDATE_CINNABAR_STEP = REGISTRY.register("update_cinnabar_step", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "update_cinnabar_step")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VOLLEYBOMB_HIT = REGISTRY.register("volleybomb_hit", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "volleybomb_hit")));
	public static final DeferredHolder<SoundEvent, SoundEvent> RED_EXPLOSION = REGISTRY.register("red_explosion", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "red_explosion")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ZAP = REGISTRY.register("zap", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("minigames", "zap")));
}