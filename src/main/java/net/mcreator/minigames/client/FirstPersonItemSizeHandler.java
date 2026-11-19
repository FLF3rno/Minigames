package net.mcreator.minigames.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
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
                for (Method m : FirstPersonHandsAndItemsRenderer.class.getDeclaredMethods()) {
                    Class<?>[] params = m.getParameterTypes();
                    if (params.length == 11
                        && params[0] == PlayerRenderState.class
                        && params[1] == FirstPersonHandsAndItemsRenderState.class
                        && params[2] == float.class
                        && params[3] == float.class
                        && params[4] == InteractionHand.class
                        && params[5] == float.class
                        && params[6] == ItemStack.class
                        && params[7] == float.class
                        && params[8] == com.mojang.blaze3d.vertex.PoseStack.class
                        && SubmitNodeCollector.class.isAssignableFrom(params[9])
                        && params[10] == int.class) {
                        m.setAccessible(true);
                        renderArmWithItemMethod = m;
                        break;
                    }
                }
            } catch (Exception e) {
                MinigamesMod.LOGGER.error("Failed to find submitArmWithItem method on FirstPersonHandsAndItemsRenderer", e);
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

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }

        float scale = ItemSizeHelper.getItemScale(stack, event.getHand());
        float opacity = ItemSizeHelper.getItemOpacity(mc.level);
        if (scale == 1.0f && opacity >= 0.999f) {
            return;
        }

        Method renderArm = getRenderArmWithItemMethod();
        PlayerRenderState playerState = event.getPlayerState();
        if (renderArm == null || playerState == null || playerState.firstPersonHandsAndItems == null) {
            return;
        }

        event.setCanceled(true);

        try {
            boolean isMainHand = (event.getHand() == InteractionHand.MAIN_HAND);
            HumanoidArm arm = isMainHand ? mc.player.getMainArm() : mc.player.getMainArm().getOpposite();
            boolean isRightArm = (arm == HumanoidArm.RIGHT);
            float xDirection = isRightArm ? 1.0f : -1.0f;

            event.getPoseStack().pushPose();

            if (scale != 1.0f) {
                float diff = scale - 1.0f;
                // Move towards screen edge (+X right for right arm, -X left for left arm),
                // downwards (-Y), and further away from the eyes (-Z):
                float xOffset = xDirection * diff * 0.35f;
                float yOffset = -diff * 0.22f;
                float zOffset = -diff * 0.28f;
                event.getPoseStack().translate(xOffset, yOffset, zOffset);
            }

            ScalingSubmitNodeCollector scalingCollector = new ScalingSubmitNodeCollector(event.getSubmitNodeCollector(), scale, opacity);
            FirstPersonHandsAndItemsRenderer renderer = mc.gameRenderer.firstPersonHandsAndItemsRenderer;
            renderArm.invoke(
                renderer,
                playerState,
                playerState.firstPersonHandsAndItems,
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

            event.getPoseStack().popPose();
        } catch (Exception e) {
            MinigamesMod.LOGGER.error("Error invoking renderArmWithItem with scaling", e);
        }
    }
}
