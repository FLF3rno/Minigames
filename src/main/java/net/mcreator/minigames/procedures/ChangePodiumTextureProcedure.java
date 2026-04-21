package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import net.mcreator.minigames.entity.SpleefPodiumPlayerEntity;

import java.util.ArrayList;

public class ChangePodiumTextureProcedure {
	public static void execute(LevelAccessor world, double position, String uuid) {
		if (uuid == null)
			return;
		for (Entity entityiterator : new ArrayList<>(world.getEntities(null, new AABB(-30000000, -64, -30000000, 30000000, 320, 30000000)))) {
			if (entityiterator instanceof SpleefPodiumPlayerEntity) {
				if ((entityiterator instanceof SpleefPodiumPlayerEntity _datEntI ? _datEntI.getEntityData().get(SpleefPodiumPlayerEntity.DATA_position) : 0) == position) {
					if (entityiterator instanceof SpleefPodiumPlayerEntity _datEntSetS)
						_datEntSetS.getEntityData().set(SpleefPodiumPlayerEntity.DATA_display_uuid, uuid);
				}
			}
		}
	}
}
