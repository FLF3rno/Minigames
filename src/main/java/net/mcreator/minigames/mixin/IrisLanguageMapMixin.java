package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.mcreator.minigames.client.SolasShaderDynamicPatcher;

import java.util.Map;

@Pseudo
@Mixin(targets = "net.irisshaders.iris.shaderpack.LanguageMap", remap = false)
public class IrisLanguageMapMixin {
	@Inject(method = "getTranslations", at = @At("RETURN"), cancellable = true, require = 0)
	private void minigames$onGetTranslations(String language, CallbackInfoReturnable<Map<String, String>> cir) {
		if (SolasShaderDynamicPatcher.isSolasActive()) {
			Map<String, String> original = cir.getReturnValue();
			Map<String, String> patched = SolasShaderDynamicPatcher.patchTranslations(original);
			cir.setReturnValue(patched);
		}
	}
}
