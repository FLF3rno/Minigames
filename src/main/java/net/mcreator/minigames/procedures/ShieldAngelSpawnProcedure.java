package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.ModDataAttachments;
import net.mcreator.minigames.entity.ShieldAngelEntity;
import net.mcreator.minigames.MinigamesMod;

public class ShieldAngelSpawnProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		MinigamesMod.queueServerWork(20, () -> {
			int dataId = entity instanceof ShieldAngelEntity _datEntI ? _datEntI.getEntityData().get(ShieldAngelEntity.DATA_ID) : 0;
			if (dataId != 0 && entity.level() instanceof Level level) {
				for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, new AABB(entity.blockPosition()).inflate(64.0D), living -> living != null && living.isAlive() && !isBlesser(living))) {
					if (target.getPersistentData().getIntOr("DataID", 0) == dataId) {
						target.setData(ModDataAttachments.BLESSED_DATA, new ModDataAttachments.BlessedData(dataId));
					}
				}
			}
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level().getServer(), _ent),
							("execute as @e[nbt={DataID:" + dataId + "},type=!#minigames:blesser] run data merge entity @s {DataID:" + dataId + "}"));
				}
			}
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level().getServer(), _ent),
							("execute as @e[nbt={DataID:" + dataId
									+ "},type=!#minigames:blesser] run effect give @s minigames:blessed infinite 1 true"));
				}
			}
		});
	}

	private static boolean isBlesser(Entity entity) {
		return entity.getTags().contains("minigames:blesser");
	}
}
