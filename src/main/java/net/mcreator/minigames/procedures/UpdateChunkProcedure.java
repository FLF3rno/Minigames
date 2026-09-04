package net.mcreator.minigames.procedures;

import net.minecraft.client.Minecraft;

public class UpdateChunkProcedure {
	public static void execute(double x, double z) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level != null && mc.player != null) {
			int px = mc.player.blockPosition().getX() >> 4;
			int pz = mc.player.blockPosition().getZ() >> 4;
			for (int cx = px - 8; cx <= px + 8; cx++) {
				for (int cz = pz - 8; cz <= pz + 8; cz++) {
					for (int sy = mc.level.getMinSectionY(); sy < mc.level.getMaxSectionY(); sy++) {
						mc.levelRenderer.setSectionDirty(cx, sy, cz);
					}
				}
			}
		}
	}
}