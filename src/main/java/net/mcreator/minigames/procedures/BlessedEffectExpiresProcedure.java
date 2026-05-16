package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class BlessedEffectExpiresProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/effect clear @s minigames:blessed");
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/data merge entity @s {CustomName:'',CustomNameVisible:0b}");
			}
		}
		if (!(entity instanceof Player player) || entity.level().isClientSide()) {
			return;
		}
		AABB searchArea = new AABB(player.blockPosition()).inflate(256.0D);
		for (Mob mob : player.level().getEntitiesOfClass(Mob.class, searchArea, Mob::isAlive)) {
			Player nearestPlayer = player.level().getNearestPlayer(mob, -1.0D);
			if (nearestPlayer == player && mob.canAttack(player) && mob.hasLineOfSight(player)) {
				mob.setTarget(player);
			}
		}
	}
}
