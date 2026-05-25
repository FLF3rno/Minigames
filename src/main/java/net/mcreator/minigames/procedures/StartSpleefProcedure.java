package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.MinigamesMod;

import java.util.ArrayList;

public class StartSpleefProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((world.isClientSide() ? Minecraft.getInstance().getConnection().getOnlinePlayers().size() : ServerLifecycleHooks.getCurrentServer().getPlayerCount()) >= 2) {
			MinigamesModVariables.MapVariables.get(world).spleefAlivePlayers = world.players().size();
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/clear @a");
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				{
					MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.snowballCountSpleef = 0;
					_vars.markSyncDirty();
				}
				if (entityiterator.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler) {
					ItemStack _setstack = new ItemStack(MinigamesModItems.SPLEEF_SHOVEL.get()).copy();
					_setstack.setCount(1);
					_modHandler.setStackInSlot(0, _setstack);
				}
			}
			MinigamesModVariables.MapVariables.get(world).nightVision = false;
			MinigamesModVariables.MapVariables.get(world).minimap = false;
			MinigamesModVariables.MapVariables.get(world).waypoints = false;
			MinigamesModVariables.MapVariables.get(world).removeEffects = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"execute in minigames:spleef_dimension run time set day");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/gamerule locatorBar false");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/gamerule doWeatherCycle false");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/gamerule doMobSpawning false");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"/gamemode survival @a");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"gamerule doDaylightCycle false");
			MinigamesModVariables.MapVariables.get(world).layerCountdownSpleef = 0;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			PlaceRandomMapSpleefProcedure.execute(world, x, y, z);
			MinigamesModVariables.MapVariables.get(world).playingSpleef = true;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			MinigamesMod.queueServerWork(10, () -> {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/effect give @a minecraft:instant_health 10 100 true");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/effect give @a minecraft:saturation 10 100 true");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/effect give @a minecraft:resistance infinite 100 true");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/effect give @a minigames:immobilized 1 1 true");
				GameCountdownProcedure.execute(world, x, y, z);
			});
			MinigamesModVariables.firstSpleef = null;
			MinigamesModVariables.secondSpleef = null;
			MinigamesModVariables.thirdSpleef = null;
		} else {
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Not enough players to start Spleef (minimum of 2)").withColor(0xff0000), false);
			}
		}
	}
}