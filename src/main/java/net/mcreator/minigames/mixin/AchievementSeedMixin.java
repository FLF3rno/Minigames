package net.mcreator.minigames.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minigames.network.MinigamesModVariables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class AchievementSeedMixin {

	@Inject(method = "getSeed", at = @At("HEAD"), cancellable = true)
	private void minigames$getAchievementSeed(CallbackInfoReturnable<Long> cir) {
		ServerLevel level = (ServerLevel) (Object) this;

		if (level.getSeedOverride().isPresent()) {
			long achievementSeed = (long) MinigamesModVariables.MapVariables.get((LevelAccessor) level).AchievementSeed;

			if (achievementSeed == 0L) {
				achievementSeed = 1L;
			}

			cir.setReturnValue(achievementSeed);
		}
	}
}