package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.mcreator.minigames.client.SolasShaderDynamicPatcher;

import java.nio.file.Path;

@Pseudo
@Mixin(targets = "net.irisshaders.iris.shaderpack.ShaderPack", remap = false)
public class IrisShaderPackMixin {
	@Inject(method = "readProperties", at = @At("RETURN"), cancellable = true, require = 0)
	private static void minigames$onReadProperties(Path path, String name, CallbackInfoReturnable<String> cir) {
		String result = cir.getReturnValue();
		if (result != null) {
			String patched = SolasShaderDynamicPatcher.patchProperties(name, result);
			if (patched != null && !patched.equals(result)) {
				cir.setReturnValue(patched);
			}
		}
	}
}
