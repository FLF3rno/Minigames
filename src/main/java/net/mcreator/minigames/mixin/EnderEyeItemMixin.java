package net.mcreator.minigames.mixin;

import net.mcreator.minigames.util.AchievementStrongholdLocator;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void minigames$redirectAchievementEye(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		if (!MinigamesModVariables.MapVariables.get(level).playingAchievement) {
			return;
		}

		BlockPos target = AchievementStrongholdLocator.getNearestTarget(serverLevel, player.blockPosition());
		if (target == null) {
			return;
		}

		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEmpty()) {
			return;
		}

		EyeOfEnder eye = new EyeOfEnder(level, player.getX(), player.getEyeY(), player.getZ());
		eye.setItem(stack.copyWithCount(1));
		eye.signalTo(net.minecraft.world.phys.Vec3.atCenterOf(target));
		level.addFreshEntity(eye);

		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
		}

		cir.setReturnValue(InteractionResult.CONSUME);
	}
}
