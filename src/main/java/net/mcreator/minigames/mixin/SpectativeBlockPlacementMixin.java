package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityGetter.class)
public interface SpectativeBlockPlacementMixin {

    @Redirect(
            method = "isUnobstructed(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/shapes/VoxelShape;)Z",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;blocksBuilding:Z")
    )
    private boolean minigames$ignoreSpectativeForBlockPlacement(Entity entity) {
        if (entity instanceof Player player && player.getData(MinigamesModVariables.PLAYER_VARIABLES).spectative) {
            return false;
        }
        return entity.blocksBuilding;
    }
}
