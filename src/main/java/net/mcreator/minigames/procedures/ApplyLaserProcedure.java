package net.mcreator.minigames.procedures;

import net.mcreator.minigames.photon_plugin.SummonFXEntityServer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;

public class ApplyLaserProcedure {
	public static void execute(Entity entity, Vec3 start, Vec3 target, double beamWidth, double ticks, String color) {
		if (entity == null || start == null || target == null || color == null)
			return;

		Vec3 eyePos = entity.getEyePosition();
		Vec3 offset = start.subtract(eyePos);
		Vec3 diff = target.subtract(start);
		double dist = diff.length();

		if (dist <= 1.0E-4)
			return;

		String clean = color.trim().replace("#", "").replace("0x", "").replace("0X", "");
		int rgb;
		try {
			rgb = Integer.parseInt(clean, 16);
		} catch (NumberFormatException e) {
			rgb = 0xFFFFFF;
		}

		double horizontalDist = Math.hypot(diff.x, diff.z);
		double rotY = Math.toDegrees(Math.atan2(-diff.z, diff.x));
		double rotZ = -Math.toDegrees(Math.atan2(horizontalDist, diff.y));

		SummonFXEntityServer.create(entity)
				.name("photon:laser")
				.offset(offset.x, offset.y, offset.z)
				.rotation(0, rotY, rotZ)
				.scale(beamWidth, dist * 0.5, beamWidth)
				.color(rgb)
				.allowMulti(true)
				.duration(ticks)
				.send();
	}

	public static void execute(Entity entity, double beamWidth, double ticks, String color, Vec3 target) {
		if (entity == null)
			return;
		execute(entity, entity.getEyePosition(), target, beamWidth, ticks, color);
	}
}
