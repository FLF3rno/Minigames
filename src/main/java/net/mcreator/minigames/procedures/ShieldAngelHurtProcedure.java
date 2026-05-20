package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.entity.ShieldAngelEntity;

import java.util.Comparator;

public class ShieldAngelHurtProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
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
					final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
					for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
						if (entityiterator.getPersistentData().getDoubleOr("DataID", 0) == entity.getPersistentData().getDoubleOr("DataID", 0)) {
							{
								Entity _ent = entityiterator;
								if (!_ent.level().isClientSide() && _ent.getServer() != null) {
									_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
											4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/effect clear @s minigames:blessed");
								}
							}
						}
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