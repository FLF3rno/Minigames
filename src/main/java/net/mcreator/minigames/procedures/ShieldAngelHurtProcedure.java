package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.entity.ShieldAngelEntity;

public class ShieldAngelHurtProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof ShieldAngelEntity _datEntI ? _datEntI.getEntityData().get(ShieldAngelEntity.DATA_timeSinceLastHit) : 0) >= (entity instanceof ShieldAngelEntity _datEntI
				? _datEntI.getEntityData().get(ShieldAngelEntity.DATA_cooldown)
				: 0)) {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound minecraft:block.amethyst_block.break hostile @a ~ ~ ~ 0.3 1");
				}
			}
			if ((entity instanceof ShieldAngelEntity _datEntI ? _datEntI.getEntityData().get(ShieldAngelEntity.DATA_HP) : 0) > 1) {
				if (entity instanceof ShieldAngelEntity _datEntSetI)
					_datEntSetI.getEntityData().set(ShieldAngelEntity.DATA_HP, (int) ((entity instanceof ShieldAngelEntity _datEntI ? _datEntI.getEntityData().get(ShieldAngelEntity.DATA_HP) : 0) - 1));
			} else {
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/effect clear @e[type=!minecraft:player] minigames:blessed");
					}
				}
				if (!entity.level().isClientSide())
					entity.discard();
			}
		} else {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound minecraft:block.anvil.place hostile @a ~ ~ ~ 0.3 2");
				}
			}
		}
	}
}