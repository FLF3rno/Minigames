package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.entity.VolleybombEntityEntity;

import java.util.Comparator;

public class VolleyBombRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		ShootProjectileProcedure.execute(world, entity, 0, 0, 0, 0, 0.4, "volleybomb");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minigames:volleybomb_hit player @a ~ ~ ~ 0.4 1.1");
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entityiterator instanceof VolleybombEntityEntity) {
					entityiterator.getPersistentData().putDouble("explosionDamage", GetItemAttributeProcedure.execute(itemstack, "minigames:explosion_damage"));
					entityiterator.getPersistentData().putDouble("explosionSize", 2);
				}
			}
		}
	}
}