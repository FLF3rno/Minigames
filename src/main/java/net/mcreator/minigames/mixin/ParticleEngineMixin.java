package net.mcreator.minigames.mixin;

import net.mcreator.minigames.client.gui.options.MinigamesSettingsConfig;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
	@Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
	private void minigames$suppressSteampunkSmoke(ParticleOptions options, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<Particle> cir) {
		if (!MinigamesSettingsConfig.getBoolean("ShowSteampunkSmoke", true)) {
			ParticleType<?> type = options.getType();
			if (type == ParticleTypes.CAMPFIRE_COSY_SMOKE || type == ParticleTypes.CAMPFIRE_SIGNAL_SMOKE) {
				Minecraft mc = Minecraft.getInstance();
				if (mc.level != null) {
					MinigamesModVariables.MapVariables mapVars = MinigamesModVariables.MapVariables.get(mc.level);
					if ((mapVars.currentMapSpleef != null && mapVars.currentMapSpleef.toLowerCase().contains("steampunk"))
							|| "minigames:spleef_dimension".equals(mc.level.dimension().identifier().toString())) {
						cir.setReturnValue(null);
					}
				}
			}
		}
	}
}
