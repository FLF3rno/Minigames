package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.entity.ShieldAngelEntity;
import net.mcreator.minigames.MinigamesMod;

public class ShieldAngelSpawnProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		MinigamesMod.queueServerWork(20, () -> {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
					_ent.level().getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, LevelBasedPermissionSet.OWNER, _ent.getName().getString(),
									_ent.getDisplayName(), _ent.level().getServer(), _ent),
							("execute as @e[nbt={DataID:" + (entity instanceof ShieldAngelEntity _datEntI ? _datEntI.getEntityData().get(ShieldAngelEntity.DATA_ID) : 0)
									+ "},type=!#minigames:blesser] run effect give @s minigames:blessed infinite 1 true"));
				}
			}
		});
	}
}