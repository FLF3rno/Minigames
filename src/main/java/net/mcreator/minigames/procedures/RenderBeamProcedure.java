package net.mcreator.minigames.procedures;

import net.mcreator.minigames.ModDataAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class RenderBeamProcedure {
	public static void execute(Entity from, Entity to, double scale, double ticks, ResourceLocation texture) {
		if (from == null || to == null || texture == null)
			return;

		int durationTicks = Math.max(1, (int) Math.round(ticks));
		int startTick = from.tickCount;
		ModDataAttachments.BeamData beamData = new ModDataAttachments.BeamData(true, to.getId(), startTick, durationTicks, scale, texture.toString());
		from.setData(ModDataAttachments.BEAM_DATA, beamData);

		if (!from.level().isClientSide()) {
			if (from instanceof ServerPlayer serverPlayer) {
				serverPlayer.syncData(ModDataAttachments.BEAM_DATA.get());
			}
			if (to instanceof ServerPlayer targetPlayer) {
				targetPlayer.syncData(ModDataAttachments.BEAM_DATA.get());
			}
		}
	}
}
