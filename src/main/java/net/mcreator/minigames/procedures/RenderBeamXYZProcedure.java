package net.mcreator.minigames.procedures;

import net.mcreator.minigames.ModDataAttachments;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public class RenderBeamXYZProcedure {
	public static void execute(Entity owner, boolean emissive, double fromX, double fromY, double fromZ, int scale, int tickLength, double toX, double toY, double toZ, String type, Identifier texture) {
		if (owner == null || texture == null) return;
		int durationTicks = Math.max(1, tickLength);
		int startTick = owner.tickCount;
		String renderType = type == null ? "beam" : type;
		ModDataAttachments.BeamXYZData beamData = new ModDataAttachments.BeamXYZData(true, fromX, fromY, fromZ, toX, toY, toZ, startTick, durationTicks, (double) scale, texture.toString(), renderType, emissive);
		owner.setData(ModDataAttachments.BEAM_XYZ_DATA, beamData);
		if (!owner.level().isClientSide()) owner.syncData(ModDataAttachments.BEAM_XYZ_DATA.get());
	}
}