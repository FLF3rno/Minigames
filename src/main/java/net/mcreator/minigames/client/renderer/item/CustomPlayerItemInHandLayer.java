package net.mcreator.minigames.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.effects.SpearAnimations;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import net.mcreator.minigames.client.ItemSizeHelper;

public class CustomPlayerItemInHandLayer extends PlayerItemInHandLayer<AvatarRenderState, PlayerModel> {

    public CustomPlayerItemInHandLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
        super(renderer);
    }

    @Override
    protected void submitArmWithItem(
        AvatarRenderState state,
        ItemStackRenderState item,
        ItemStack itemStack,
        HumanoidArm arm,
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        int lightCoords
    ) {
        if (item.isEmpty()) {
            return;
        }

        EquipmentSlot slot = (arm == state.mainArm) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        float scale = ItemSizeHelper.getItemScale(itemStack, slot);

        InteractionHand currentHand = arm == state.mainArm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (state.isUsingItem && state.useItemHand == currentHand && state.attackTime < 1.0E-5F && !state.heldOnHead.isEmpty()) {
            if (scale != 1.0f) {
                poseStack.pushPose();
                poseStack.scale(scale, scale, scale);
                super.submitArmWithItem(state, item, itemStack, arm, poseStack, submitNodeCollector, lightCoords);
                poseStack.popPose();
            } else {
                super.submitArmWithItem(state, item, itemStack, arm, poseStack, submitNodeCollector, lightCoords);
            }
            return;
        }

        // Custom third person render positioning so scaling occurs directly around the item in hand
        poseStack.pushPose();
        this.getParentModel().translateToHand(state, arm, poseStack);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        boolean isLeftHand = arm == HumanoidArm.LEFT;
        float offsetX = isLeftHand ? -1.0F : 1.0F;
        float offsetY = 2.0F;
        float offsetZ = -10.0F;
        poseStack.translate(offsetX / 16.0F, offsetY / 16.0F, offsetZ / 16.0F);

        if (state.attackTime > 0.0F && state.attackArm == arm && state.swingAnimationType == SwingAnimationType.STAB) {
            SpearAnimations.thirdPersonAttackItem(state, poseStack);
        }

        float ticksUsingItem = state.ticksUsingItem(arm);
        if (ticksUsingItem != 0.0F) {
            (arm == HumanoidArm.RIGHT ? state.rightArmPose : state.leftArmPose).animateUseItem(state, poseStack, ticksUsingItem, arm, itemStack);
        }

        if (scale != 1.0f) {
            poseStack.scale(scale, scale, scale);
        }

        item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}
