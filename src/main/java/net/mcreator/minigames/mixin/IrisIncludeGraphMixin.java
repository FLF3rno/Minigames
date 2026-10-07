package net.mcreator.minigames.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.mcreator.minigames.client.SolasShaderDynamicPatcher;

import java.nio.file.Path;

@Pseudo
@Mixin(targets = "net.irisshaders.iris.shaderpack.include.IncludeGraph", remap = false)
public class IrisIncludeGraphMixin {
	@Inject(method = "readFile", at = @At("RETURN"), cancellable = true, require = 0)
	private static void minigames$onReadFile(Path path, CallbackInfoReturnable<String> cir) {
		String result = cir.getReturnValue();
		if (result != null && SolasShaderDynamicPatcher.isSolasActive()) {
			String patched = SolasShaderDynamicPatcher.patchGlslFile(path, result);
			if (patched != null && !patched.equals(result)) {
				cir.setReturnValue(patched);
			}
		}
	}
}
