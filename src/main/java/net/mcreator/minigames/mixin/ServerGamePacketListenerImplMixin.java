package net.mcreator.minigames.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.mcreator.minigames.block.GiantBellBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
	@Shadow
	public ServerPlayer player;

	@Redirect(method = "handleUseItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;subtract(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"))
	private Vec3 minigames$bypassGiantBellHitDistance(Vec3 location, Vec3 blockCenter, ServerboundUseItemOnPacket packet) {
		BlockHitResult hitResult = packet.getHitResult();
		BlockPos pos = hitResult.getBlockPos();
		if (this.player != null && this.player.level().getBlockState(pos).getBlock() instanceof GiantBellBlock) {
			return Vec3.ZERO;
		}
		return location.subtract(blockCenter);
	}
}
