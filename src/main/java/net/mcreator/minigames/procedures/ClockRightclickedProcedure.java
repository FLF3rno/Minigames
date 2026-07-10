package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.MinigamesMod;

public class ClockRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/playsound minigames:clock player @a ~ ~ ~ 1 .4");
		SlowDownTimeProcedure.execute(world, x, y, z, 20 * (GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage") / 100));
		MinigamesMod.queueServerWork((int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), () -> {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/playsound minigames:clock player @a ~ ~ ~ 1 1.8");
			SlowDownTimeProcedure.execute(world, x, y, z, 20 * (GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency") / 100));
		});
		MinigamesMod.queueServerWork((int) (GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length") + GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length_2")), () -> {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/playsound minigames:clock player @a ~ ~ ~ 1 1");
			SlowDownTimeProcedure.execute(world, x, y, z, 20);
		});
	}
}