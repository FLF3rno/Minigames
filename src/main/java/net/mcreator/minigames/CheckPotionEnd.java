package net.mcreator.minigames;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import java.util.*;
import java.util.stream.Collectors;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.procedures.PotionEffectExpiresProcedure;

@EventBusSubscriber
public class CheckPotionEnd {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        if (!MinigamesModVariables.MapVariables.get(player.level()).playingDungeons) {
            return;
        }

        // 1. Get current active instances
        Collection<MobEffectInstance> currentInstances = player.getActiveEffects();
        Set<MobEffect> currentEffects = currentInstances.stream()
                .map(i -> i.getEffect().value())
                .collect(Collectors.toSet());

        // 2. Load previous state
        String lastEffectsData = player.getPersistentData().getString("last_effects").orElse("");
        Set<MobEffect> previousEffects = parseEffects(lastEffectsData);

        // 3. Detect expired
        for (MobEffect effect : previousEffects) {
            if (!currentEffects.contains(effect)) {
                // Find the instance that JUST expired to get the amplifier
                // Note: Since it's already gone, you may need to store amp in NBT 
                // or pass a default (0)
                int amp = 0; 
                String effectName = BuiltInRegistries.MOB_EFFECT.getKey(effect).toString();
                
                // Execute using 'player' as the target
                PotionEffectExpiresProcedure.execute(player.level(), player, (double) amp, effectName);
            }
        }

        // 4. Save state
        player.getPersistentData().putString("last_effects", serializeEffects(currentEffects));
    }

    private static String serializeEffects(Set<MobEffect> effects) {
        return effects.stream().map(e -> BuiltInRegistries.MOB_EFFECT.getKey(e).toString()).collect(Collectors.joining(","));
    }

    private static Set<MobEffect> parseEffects(String data) {
        if (data == null || data.isEmpty()) return new HashSet<>();
        return Arrays.stream(data.split(",")).filter(s -> !s.isEmpty())
                .map(id -> BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.parse(id)).get().value())
                .collect(Collectors.toSet());
    }
}