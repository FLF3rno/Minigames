package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.entity.PlagueMiddleEntity;

import java.util.Comparator;

public class PlagueStarterRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		if (world instanceof ServerLevel _level) {
			Entity entityToSpawn = MinigamesModEntities.PLAGUE_MIDDLE.get().spawn(_level, BlockPos.containing(x, y + 1, z), EntitySpawnReason.MOB_SUMMONED);
			if (entityToSpawn != null) {
				entityToSpawn.setDeltaMovement(0, 0, 0);
			}
		}
		if ((findEntityInWorldRange(world, PlagueMiddleEntity.class, x, (y + 1), z, 1)) instanceof PlagueMiddleEntity _datEntSetI)
			_datEntSetI.getEntityData().set(PlagueMiddleEntity.DATA_range, (int) GetItemAttributeProcedure.execute(itemstack, "minigames:ability_range"));
		if ((findEntityInWorldRange(world, PlagueMiddleEntity.class, x, (y + 1), z, 1)) instanceof PlagueMiddleEntity _datEntSetI)
			_datEntSetI.getEntityData().set(PlagueMiddleEntity.DATA_duration, (int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"));
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minecraft:entity.splash_potion.break player @a ~ ~ ~ 1 1");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minecraft:block.bell.use player @a ~ ~ ~ 1 .4");
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}