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
import java.util.Optional;

@EventBusSubscriber(value = Dist.CLIENT)
public class CustomRenderModifiers {

    public static final ContextKey<Float> TRANSPARENCY = new ContextKey<>(ResourceLocation.parse("minigames:transparency"));

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void registerModifiers(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
        }, (entity, state) -> {
            float transparencyValue = 0.0f; // 0 = fully opaque, 100 = fully invisible
            if (entity != null) {
                Object rawTransparency = entity.getPersistentData().getFloat("transparency");
                if (rawTransparency instanceof Optional<?> opt) {
                    if (opt.isPresent() && opt.get() instanceof Number num) {
                        transparencyValue = num.floatValue();
                    }
                } else if (rawTransparency instanceof Number num) {
                    transparencyValue = num.floatValue();
                }
            }
            state.setRenderData(TRANSPARENCY, transparencyValue);
        });
    }
}
