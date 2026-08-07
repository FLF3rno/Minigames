package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.entity.DemonEntity;

import java.util.ArrayList;

public class DemonTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).currentRoomID == (entity instanceof DemonEntity _datEntI ? _datEntI.getEntityData().get(DemonEntity.DATA_ID) : 0)) {
			if ((entity instanceof DemonEntity _datEntI ? _datEntI.getEntityData().get(DemonEntity.DATA_cooldown) : 0) == 0) {
				if (entity instanceof DemonEntity _ent2) {
					_ent2.getEntityData().set(DemonEntity.ANIM, 1000);
					_ent2.getEntityData().set(DemonEntity.ANIM, 0);
				}
			} else if ((entity instanceof DemonEntity _datEntI ? _datEntI.getEntityData().get(DemonEntity.DATA_cooldown) : 0) == 27) {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/playsound minecraft:block.stone.break hostile @a ~ ~ ~ 1 1");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/playsound minecraft:block.sculk_shrieker.break hostile @a ~ ~ ~ 1 1");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/playsound minecraft:entity.warden.ambient hostile @a ~ ~ ~ 1 0.1");
			} else if ((entity instanceof DemonEntity _datEntI ? _datEntI.getEntityData().get(DemonEntity.DATA_cooldown) : 0) == 32) {
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					ApplyEffectProcedure.execute(entityiterator, false, 1, 100, "minecraft:darkness");
				}
				for (int index346 = 0; index346 < 6; index346++) {
					SummonMinionProcedure.execute(world, x, y + 1, z, entity, 0, Mth.nextInt(RandomSource.create(), -10, 10), Mth.nextInt(RandomSource.create(), -10, 10), Mth.nextInt(RandomSource.create(), -10, 10), 0, "minigames:sculkling");
				}
			} else if ((entity instanceof DemonEntity _datEntI ? _datEntI.getEntityData().get(DemonEntity.DATA_cooldown) : 0) == 50) {
				if (entity instanceof DemonEntity _ent13) {
					_ent13.getEntityData().set(DemonEntity.ANIM, 1000);
					_ent13.getEntityData().set(DemonEntity.ANIM, 1);
				}
			} else if ((entity instanceof DemonEntity _datEntI ? _datEntI.getEntityData().get(DemonEntity.DATA_cooldown) : 0) == 275) {
				if (entity instanceof DemonEntity _datEntSetI)
					_datEntSetI.getEntityData().set(DemonEntity.DATA_cooldown, 0);
			}
			if (entity instanceof DemonEntity _datEntSetI)
				_datEntSetI.getEntityData().set(DemonEntity.DATA_cooldown, (int) ((entity instanceof DemonEntity _datEntI ? _datEntI.getEntityData().get(DemonEntity.DATA_cooldown) : 0) + 1));
		}
	}
}