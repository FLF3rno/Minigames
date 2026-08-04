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

		// 1. Set the attachment data on 'from'
		from.setData(ModDataAttachments.BEAM_DATA, beamData);

		// 2. Sync 'from' entity data to all tracking clients
		if (!from.level().isClientSide()) {
			// NeoForge extension method on Entity: syncs to self (if player) AND tracking clients!
			from.syncData(ModDataAttachments.BEAM_DATA.get());
		}
	}
}