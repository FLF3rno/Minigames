package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class BlockInteractionBypassMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void minigames$skipVanillaDoorClicks(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (player == null || player.level() == null) {
            return;
        }

        if (!MinigamesModVariables.MapVariables.get(player.level()).playingDungeons) {
            return;
        }

        BlockPos clickedPos = hitResult.getBlockPos();
        BlockState clickedState = player.level().getBlockState(clickedPos);
        if (!isVanillaBlockedBlock(clickedState)) {
            return;
        }

        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }

        cir.setReturnValue(itemStack.use(player.level(), player, hand));
    }

    private static boolean isVanillaBlockedBlock(BlockState state) {
        String blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
        return state.is(BlockTags.DOORS)
                || state.is(BlockTags.TRAPDOORS)
                || state.is(BlockTags.SIGNS)
                || state.is(BlockTags.CEILING_HANGING_SIGNS)
                || blockId.endsWith("_hanging_sign")
                || blockId.endsWith("_sign")
                || blockId.endsWith("_shelf");
    }
}
