package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.arguments.EntityAnchorArgument;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.entity.RockThrowerChurchEntity;

import java.util.Comparator;

public class RockThrowerUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		Vec3 targetpos = Vec3.ZERO;
		if ((entity instanceof RockThrowerChurchEntity _datEntI ? _datEntI.getEntityData().get(RockThrowerChurchEntity.DATA_ID) : 0) == MinigamesModVariables.MapVariables.get(world).currentRoomID) {
			if (entity instanceof RockThrowerChurchEntity _datEntSetI)
				_datEntSetI.getEntityData().set(RockThrowerChurchEntity.DATA_attackTick, (int) ((entity instanceof RockThrowerChurchEntity _datEntI ? _datEntI.getEntityData().get(RockThrowerChurchEntity.DATA_attackTick) : 0) + 1));
			if ((entity instanceof RockThrowerChurchEntity _datEntI ? _datEntI.getEntityData().get(RockThrowerChurchEntity.DATA_attackTick) : 0) % 70 == 0) {
				if (entity instanceof RockThrowerChurchEntity _ent4) {
					_ent4.getEntityData().set(RockThrowerChurchEntity.ANIM, 1000);
					_ent4.getEntityData().set(RockThrowerChurchEntity.ANIM, 0);
				}
			}
			if ((entity instanceof RockThrowerChurchEntity _datEntI ? _datEntI.getEntityData().get(RockThrowerChurchEntity.DATA_attackTick) : 0) % 70 == 20) {
				ExplodeProcedure.execute(world, x, y, z, entity, false, true, 0, 0, 2, "groundbreaking");
			}
			if ((entity instanceof RockThrowerChurchEntity _datEntI ? _datEntI.getEntityData().get(RockThrowerChurchEntity.DATA_attackTick) : 0) % 70 >= 40
					&& (entity instanceof RockThrowerChurchEntity _datEntI ? _datEntI.getEntityData().get(RockThrowerChurchEntity.DATA_attackTick) : 0) % 70 <= 50) {
				if (findEntityInWorldRange(world, Player.class, x, y, z, 60) != null) {
					targetpos = (findEntityInWorldRange(world, Player.class, x, y, z, 60)).position();
					entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3((targetpos.x()), (targetpos.y()), (targetpos.z())));
				}
			}
			if ((entity instanceof RockThrowerChurchEntity _datEntI ? _datEntI.getEntityData().get(RockThrowerChurchEntity.DATA_attackTick) : 0) % 70 - 1 == 53) {
				if (findEntityInWorldRange(world, Player.class, x, y, z, 60) != null) {
					targetpos = (findEntityInWorldRange(world, Player.class, x, y, z, 60)).position();
					entity.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3((targetpos.x()), (targetpos.y()), (targetpos.z())));
				}
				ShootProjectileProcedure.execute(world, entity, 7, 0, 1, 0, 0.85, "boulder");
			}
		}
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}