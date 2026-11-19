package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.MinigamesModPlayerAnimationAPI;

import com.mojang.blaze3d.vertex.PoseStack;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class FirstPersonHandsAndItemsRendererMixin {
	private String master = null;
	private Minecraft mc = Minecraft.getInstance();
	private EntityRenderDispatcher dispatcher = null;

	@Inject(method = "submitHandsWithItems", at = @At("HEAD"), cancellable = true)
	private void renderHandsWithItems(float f, PoseStack poseStack, SubmitNodeCollector collector, PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, CallbackInfo ci) {
		if (master == null) {
			if (!MinigamesModPlayerAnimationAPI.animations.isEmpty())
				master = "minigames";
			else
				return;
		}
		if (!master.equals("minigames"))
			return;
		if (playerState.avatarRenderState != null) {
			Player player = (Player) playerState.avatarRenderState.getRenderData(MinigamesModPlayerAnimationAPI.ClientAttachments.PLAYER);
			if (player == null)
				return;
			if (mc.player != player)
				return;
			if (dispatcher == null)
				dispatcher = mc.getEntityRenderDispatcher();
			CompoundTag playerData = player.getPersistentData();
			// Hack to make animations progress when in first person without first person mode enabled
			if (!playerData.getStringOr("PlayerCurrentAnimation", "").isEmpty() && (!playerData.getBooleanOr("FirstPersonAnimation", false) || playerData.getBooleanOr("ResetPlayerAnimation", false))) {
				AvatarRenderer renderer = (AvatarRenderer) dispatcher.getRenderer((AbstractClientPlayer) player);
				PlayerModel model = (PlayerModel) renderer.getModel();
				AvatarRenderState renderState = (AvatarRenderState) renderer.createRenderState((AbstractClientPlayer) player, f);
				renderState.ageInTicks = player.tickCount + f;
				model.setupAnim(renderState);
			}
			if (playerData.contains("setNullRender")) {
				int ticks = playerData.getIntOr("setNullRender", 0);
				if (ticks <= 0)
					playerData.remove("setNullRender");
				else
					playerData.putInt("setNullRender", ticks - 1);
			}
			if (playerData.getBooleanOr("FirstPersonAnimation", false) && (mc.gui.screen() == null || mc.gui.screen() instanceof ChatScreen))
				ci.cancel();
		}
	}
}