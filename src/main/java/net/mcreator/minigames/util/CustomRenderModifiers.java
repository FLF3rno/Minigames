package net.mcreator.minigames.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.context.ContextKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;

import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import com.google.common.reflect.TypeToken;
import net.minecraft.nbt.CompoundTag;
import java.util.Optional;

@EventBusSubscriber(value = Dist.CLIENT)
public class CustomRenderModifiers {

    public static final ContextKey<Float> TRANSPARENCY = new ContextKey<>(ResourceLocation.parse("minigames:transparency"));

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void registerModifiers(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
        }, (entity, state) -> {
            float transparencyValue = 0.0f; // Default (0 = fully solid/opaque)
            
            try {
                Object persistentDataObj = entity.getPersistentData();
                CompoundTag persistentData = null;

                // Safely unwrap the persistent data compound if MCreator wraps it in an Optional
                if (persistentDataObj instanceof Optional<?> opt && opt.isPresent()) {
                    persistentData = (CompoundTag) opt.get();
                } else if (persistentDataObj instanceof CompoundTag tag) {
                    persistentData = tag;
                }

                if (persistentData != null) {
                    // Capture MCreator's custom Optional return value from getFloat
                    Object rawFloat = persistentData.getFloat("transparency");

                    if (rawFloat instanceof Optional<?> opt) {
                        if (opt.isPresent() && opt.get() instanceof Number num) {
                            transparencyValue = num.floatValue();
                        }
                    } else if (rawFloat instanceof Number num) {
                        transparencyValue = num.floatValue();
                    }
                }
            } catch (Exception e) {
                transparencyValue = 0.0f;
            }

            state.setRenderData(TRANSPARENCY, Float.valueOf(transparencyValue));
        });
    }
}