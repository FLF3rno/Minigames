package net.mcreator.minigames.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.mcreator.minigames.AccountSwapManager;
import net.mcreator.minigames.block.GiantBellBlock;

@Mixin(Player.class)
public class PlayerIdentityMixin {

	@Inject(method = "blockActionRestricted", at = @At("HEAD"), cancellable = true)
	private void minigames$allowAdventurePunchGiantBell(Level level, BlockPos pos, GameType gameType, CallbackInfoReturnable<Boolean> cir) {
		if (level != null && pos != null && level.getBlockState(pos).getBlock() instanceof GiantBellBlock) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "isWithinBlockInteractionRange", at = @At("HEAD"), cancellable = true)
	private void minigames$giantBellInteractionRange(BlockPos pos, double extra, CallbackInfoReturnable<Boolean> cir) {
		Player player = (Player) (Object) this;
		if (player.level() != null && pos != null && player.level().getBlockState(pos).getBlock() instanceof GiantBellBlock) {
			AABB bellBounds = new AABB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 4.125, pos.getZ() + 2);
			double reach = player.blockInteractionRange() + extra;
			cir.setReturnValue(bellBounds.distanceToSqr(player.getEyePosition()) < reach * reach);
		}
	}

	@Inject(method = "getName", at = @At("RETURN"), cancellable = true)
	private void minigames$swappedName(CallbackInfoReturnable<Component> cir) {
		Player player = (Player) (Object) this;
		String swapped = AccountSwapManager.getSwappedName(player.getUUID());
		if (swapped != null && !swapped.isEmpty()) {
			cir.setReturnValue(Component.literal(swapped));
		}
	}

	@Inject(method = "getPlainTextName", at = @At("RETURN"), cancellable = true)
	private void minigames$swappedPlainTextName(CallbackInfoReturnable<String> cir) {
		Player player = (Player) (Object) this;
		String swapped = AccountSwapManager.getSwappedName(player.getUUID());
		if (swapped != null && !swapped.isEmpty()) {
			cir.setReturnValue(swapped);
		}
	}
}
