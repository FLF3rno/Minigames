package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.entity.SpleefPodiumPlayerEntity;

import java.util.ArrayList;

public class ChangePodiumTextureProcedure {
	public static void execute(LevelAccessor world, double position, String uuid) {
		if (uuid == null)
			return;
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (entityiterator instanceof SpleefPodiumPlayerEntity) {
				if ((entityiterator instanceof SpleefPodiumPlayerEntity _datEntI ? _datEntI.getEntityData().get(SpleefPodiumPlayerEntity.DATA_position) : 0) == position) {
					if (entityiterator instanceof SpleefPodiumPlayerEntity _datEntSetS)
						_datEntSetS.getEntityData().set(SpleefPodiumPlayerEntity.DATA_display_uuid, uuid);
				}
			}
		}
	}
}