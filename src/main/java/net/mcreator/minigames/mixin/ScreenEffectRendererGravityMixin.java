package net.mcreator.minigames.mixin;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.mcreator.minigames.gravity.GravityAccess;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererGravityMixin {

	@Inject(method = "getOverlayBlock", at = @At("HEAD"), cancellable = true)
	private static void minigames$gravityGetOverlayBlock(Player player, CallbackInfoReturnable<Pair<BlockState, BlockPos>> cir) {
		Direction gravity = GravityAccess.getGravity(player);
		if (gravity == Direction.DOWN) return;

		Vec3 eyePos = player.getEyePosition(1.0F);
		BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();

		for (int i = 0; i < 8; i++) {
			double xo = eyePos.x + ((i >> 0) % 2 - 0.5) * 0.1;
			double yo = eyePos.y + ((i >> 1) % 2 - 0.5) * 0.1;
			double zo = eyePos.z + ((i >> 2) % 2 - 0.5) * 0.1;
			testPos.set(xo, yo, zo);
			BlockState blockState = player.level().getBlockState(testPos);
			if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.isViewBlocking(player.level(), testPos)) {
				cir.setReturnValue(Pair.of(blockState, testPos.immutable()));
				return;
			}
		}
		cir.setReturnValue(null);
	}
}
