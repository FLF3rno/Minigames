package net.mcreator.minigames.client;

import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.renderer.item.CustomPlayerItemInHandLayer;

import java.lang.reflect.Field;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class ItemSizeLayerRegistration {

    private static Field layersField = null;

    static {
        try {
            // Locate the layers field in LivingEntityRenderer
            for (Field f : net.minecraft.client.renderer.entity.LivingEntityRenderer.class.getDeclaredFields()) {
                if (List.class.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    layersField = f;
                    break;
                }
            }
        } catch (Exception e) {
            MinigamesMod.LOGGER.error("Failed to access LivingEntityRenderer layers field", e);
        }
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerModelType skinModel : event.getSkins()) {
            AvatarRenderer<?> renderer = event.getPlayerRenderer(skinModel);
            if (renderer != null && layersField != null) {
                try {
                    @SuppressWarnings("unchecked")
                    List<RenderLayer<?, ?>> layers = (List<RenderLayer<?, ?>>) layersField.get(renderer);
                    if (layers != null) {
                        // Replace existing PlayerItemInHandLayer with our CustomPlayerItemInHandLayer
                        int index = -1;
                        for (int i = 0; i < layers.size(); i++) {
                            if (layers.get(i) instanceof PlayerItemInHandLayer) {
                                index = i;
                                break;
                            }
                        }

                        CustomPlayerItemInHandLayer customLayer = new CustomPlayerItemInHandLayer(renderer);
                        if (index != -1) {
                            layers.set(index, customLayer);
                        } else {
                            layers.add(customLayer);
                        }
                    }
                } catch (Exception e) {
                    MinigamesMod.LOGGER.error("Failed to inject CustomPlayerItemInHandLayer for skin model {}", skinModel, e);
                }
            }
        }
    }
}
