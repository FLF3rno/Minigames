package net.mcreator.minigames.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.client.renderer.ScalingSubmitNodeCollector;

import java.lang.reflect.Method;

@EventBusSubscriber(modid = MinigamesMod.MODID, value = Dist.CLIENT)
public class FirstPersonItemSizeHandler {

    private static Method renderArmWithItemMethod = null;
    private static boolean reflectionAttempted = false;

    private static Method getRenderArmWithItemMethod() {
        if (!reflectionAttempted) {
            reflectionAttempted = true;
            try {
                for (Method m : ItemInHandRenderer.class.getDeclaredMethods()) {
                    Class<?>[] params = m.getParameterTypes();
                    if (params.length == 10
                        && params[0] == AbstractClientPlayer.class
                        && params[1] == float.class
                        && params[2] == float.class
                        && params[3] == InteractionHand.class
                        && params[4] == float.class
                        && params[5] == ItemStack.class
                        && params[6] == float.class
                        && params[7] == com.mojang.blaze3d.vertex.PoseStack.class
                        && SubmitNodeCollector.class.isAssignableFrom(params[8])
                        && params[9] == int.class) {
                        m.setAccessible(true);
                        renderArmWithItemMethod = m;
                        break;
                    }
                }
            } catch (Exception e) {
                MinigamesMod.LOGGER.error("Failed to find renderArmWithItem method on ItemInHandRenderer", e);
            }
        }
        return renderArmWithItemMethod;
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || stack.getItem() instanceof MapItem) {
            return;
        }

        float scale = ItemSizeHelper.getItemScale(stack, event.getHand());
        if (scale == 1.0f) {
            return;
        }

        Method renderArm = getRenderArmWithItemMethod();
        if (renderArm == null) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        event.setCanceled(true);

        try {
            ScalingSubmitNodeCollector scalingCollector = new ScalingSubmitNodeCollector(event.getSubmitNodeCollector(), scale);
            ItemInHandRenderer renderer = mc.gameRenderer.itemInHandRenderer;
            renderArm.invoke(
                renderer,
                mc.player,
                event.getPartialTick(),
                event.getInterpolatedPitch(),
                event.getHand(),
                event.getSwingProgress(),
                stack,
                event.getEquipProgress(),
                event.getPoseStack(),
                scalingCollector,
                event.getPackedLight()
            );
        } catch (Exception e) {
            MinigamesMod.LOGGER.error("Error invoking renderArmWithItem with scaling", e);
        }
    }
}
