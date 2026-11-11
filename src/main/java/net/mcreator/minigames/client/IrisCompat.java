package net.mcreator.minigames.client;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Reflection-only bridge to the Iris API so the mod has no hard dependency on Iris.
 */
public final class IrisCompat {
	private static final MethodHandle IS_SHADER_PACK_IN_USE;

	static {
		MethodHandle handle = null;
		try {
			Class<?> apiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
			MethodHandles.Lookup lookup = MethodHandles.publicLookup();
			Object api = lookup.findStatic(apiClass, "getInstance", MethodType.methodType(apiClass)).invoke();
			handle = lookup.findVirtual(apiClass, "isShaderPackInUse", MethodType.methodType(boolean.class)).bindTo(api);
		} catch (Throwable ignored) {
			// Iris not installed (or API changed): treat as "no shader pack".
		}
		IS_SHADER_PACK_IN_USE = handle;
	}

	private IrisCompat() {
	}

	/** True when Iris is installed and a shader pack is currently active. */
	public static boolean isShaderPackInUse() {
		if (IS_SHADER_PACK_IN_USE == null) {
			return false;
		}
		try {
			return (boolean) IS_SHADER_PACK_IN_USE.invokeExact();
		} catch (Throwable t) {
			return false;
		}
	}
}
