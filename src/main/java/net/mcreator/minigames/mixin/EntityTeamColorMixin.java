package net.mcreator.minigames.mixin;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.util.ColorUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityTeamColorMixin {
	@Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
	private void minigames$customGlow(CallbackInfoReturnable<Integer> cir) {
		Entity entity = (Entity) (Object) this;
		if (!(entity instanceof Player player)) return;

		MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
		int rgb = ColorUtils.parseHex(vars.color);
		if (rgb != -1) {
			cir.setReturnValue(rgb);
		}
	}
}