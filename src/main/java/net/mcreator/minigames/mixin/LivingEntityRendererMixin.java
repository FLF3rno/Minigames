package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;

import org.objectweb.asm.Opcodes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.MinigamesModPlayerAnimationAPI;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	private String master = null;
	private Minecraft mc = Minecraft.getInstance();
	@Shadow
	@Final
	protected List<Object> layers;

	@Redirect(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;layers:Ljava/util/List;", opcode = Opcodes.GETFIELD))
	private List<Object> filterLayers(LivingEntityRenderer instance, LivingEntityRenderState entityRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
		if (master == null) {
			if (!MinigamesModPlayerAnimationAPI.animations.isEmpty())
				master = "minigames";
			else
				return layers;
		}
		if (!master.equals("minigames")) {
			return layers;
		}
		if (entityRenderState instanceof PlayerRenderState renderState && mc.options.getCameraType().isFirstPerson()) {
			Player player = (Player) renderState.getRenderData(MinigamesModPlayerAnimationAPI.ClientAttachments.PLAYER);
			if (player == null)
				return layers;
			if (mc.player == player && (mc.screen == null || mc.screen instanceof ChatScreen)) {
				CompoundTag playerData = player.getPersistentData();
				if (playerData.getBooleanOr("FirstPersonAnimation", false)) {
					playerData.putInt("setNullRender", 4);
					return layers.stream().filter(layer -> layer instanceof PlayerItemInHandLayer).toList();
				} else if (playerData.contains("setNullRender")) {
					if (playerData.getIntOr("setNullRender", 0) <= 0)
						playerData.remove("setNullRender");
					else {
						playerData.putInt("setNullRender", playerData.getIntOr("setNullRender", 0) - 1);
						return List.of();
					}
				}
			}
		}
		return layers;
	}
}