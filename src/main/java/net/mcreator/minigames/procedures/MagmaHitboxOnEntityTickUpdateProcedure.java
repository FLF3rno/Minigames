package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.entity.MagmaHitboxEntity;
import net.mcreator.minigames.MinigamesMod;

public class MagmaHitboxOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double yaw = 0;
		{
			Entity _ent = entity;
			_ent.setYRot((float) (entity instanceof MagmaHitboxEntity _datEntI ? _datEntI.getEntityData().get(MagmaHitboxEntity.DATA_yaw) : 0));
			_ent.setXRot(0);
			_ent.setYBodyRot(_ent.getYRot());
			_ent.setYHeadRot(_ent.getYRot());
			_ent.yRotO = _ent.getYRot();
			_ent.xRotO = _ent.getXRot();
			if (_ent instanceof LivingEntity _entity) {
				_entity.yBodyRotO = _entity.getYRot();
				_entity.yHeadRotO = _entity.getYRot();
			}
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute as @e[type=minigames:magma_hitbox,limit=1,sort=nearest] at @s run tp @s ^ ^ ^2");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute as @e[type=minigames:magma_hitbox,limit=1,sort=nearest] at @s run fill ^1 ^3 ^1 ^-1 ^-1 ^-1 minigames:exploding_magma replace #minigames:spleefables");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minecraft:block.lava.extinguish block @a ~ ~ ~ 0.3 1.2");
		MinigamesMod.queueServerWork(50, () -> {
			if (!entity.level().isClientSide())
				entity.discard();
		});
	}
}