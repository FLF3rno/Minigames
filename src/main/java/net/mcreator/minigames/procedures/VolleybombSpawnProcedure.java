package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.entity.VolleybombEntityEntity;
import net.mcreator.minigames.MinigamesMod;

public class VolleybombSpawnProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/attribute @s minecraft:gravity base set 0.01");
			}
		}
		if (entity instanceof VolleybombEntityEntity _datEntSetL)
			_datEntSetL.getEntityData().set(VolleybombEntityEntity.DATA_exploding, true);
		MinigamesMod.queueServerWork(30, () -> {
			if (entity instanceof VolleybombEntityEntity _datEntSetL)
				_datEntSetL.getEntityData().set(VolleybombEntityEntity.DATA_exploding, false);
		});
	}
}