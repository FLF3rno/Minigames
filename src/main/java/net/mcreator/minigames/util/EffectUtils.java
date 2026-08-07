package net.mcreator.minigames.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectUtils {

    public static boolean isHiddenEffect(MobEffectInstance effect) {

        String id =
                BuiltInRegistries.MOB_EFFECT
                        .getKey(effect.getEffect().value())
                        .toString();


        return id.equals("xaerominimap:no_minimap")
                || id.equals("xaerominimap:no_waypoints")
                || id.equals("xaeroworldmap:no_world_map");
    }
}