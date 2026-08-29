package net.mcreator.minigames.procedures;

import net.mcreator.minigames.ModDataAttachments;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public class RenderBeamProcedure {

	public static void execute(Entity from, Entity to, int scale, int tickLength, Identifier texture) {
		if (from == null || to == null || texture == null)
			return;

		int durationTicks = Math.max(1, tickLength);
		int startTick = from.tickCount;

		ModDataAttachments.BeamData beamData = new ModDataAttachments.BeamData(
				true,
				to.getId(),
				startTick,
				durationTicks,
				(double) scale,
				texture.toString(),
				0,
				0,
				0
		);

		from.setData(ModDataAttachments.BEAM_DATA, beamData);
		if (!from.level().isClientSide()) {
			from.syncData(ModDataAttachments.BEAM_DATA.get());
		}
	}
}