package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

import java.util.ArrayList;

@EventBusSubscriber
public class AdvanceWinAnimationCrownHuntProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (!(world instanceof ServerLevel))
			return;
		if (MinigamesModVariables.MapVariables.get(world).crownHuntWinDisplay == true) {
			MinigamesModVariables.MapVariables.get(world).winAnimationTick = MinigamesModVariables.MapVariables.get(world).winAnimationTick + 1;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 1) {
				MinigamesModVariables.winAnimation = 0;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 2) {
				MinigamesModVariables.winAnimation = 1;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 3) {
				MinigamesModVariables.winAnimation = 2;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 4) {
				MinigamesModVariables.winAnimation = 3;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 5) {
				MinigamesModVariables.winAnimation = 4;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 6) {
				MinigamesModVariables.winAnimation = 5;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 7) {
				MinigamesModVariables.winAnimation = 6;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 86) {
				MinigamesModVariables.winAnimation = 7;
				if (world instanceof ServerLevel _origLevel) {
					LevelAccessor _worldorig = world;
					world = _origLevel.getServer().getLevel(Level.OVERWORLD);
					if (world != null) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
									"/playsound minigames:teameliminated player @a ~ ~ ~ 1000000 1");
					}
					world = _worldorig;
				}
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 277) {
				MinigamesModVariables.winAnimation = 8;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 278) {
				MinigamesModVariables.winAnimation = 4;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 279) {
				MinigamesModVariables.winAnimation = 3;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 280) {
				MinigamesModVariables.winAnimation = 2;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 281) {
				MinigamesModVariables.winAnimation = 1;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick == 282) {
				MinigamesModVariables.winAnimation = 0;
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationTick >= 283) {
				MinigamesModVariables.winAnimation = -1;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"give @a minigames:game_compass");
				MinigamesModVariables.MapVariables.get(world).crownHuntWinDisplay = false;
				MinigamesModVariables.MapVariables.get(world).winAnimationTick = 0;
				MinigamesModVariables.MapVariables.get(world).CrownHuntInGame = false;
				MinigamesModVariables.MapVariables.get(world).minimap = true;
				MinigamesModVariables.MapVariables.get(world).waypoints = true;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					if ((entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
						{
							MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
							_vars.isCrowned = true;
							_vars.markSyncDirty();
						}
					}
				}
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"/clear @a minigames:crown_helmet_helmet");
			}
			if (MinigamesModVariables.MapVariables.get(world).winAnimationState != MinigamesModVariables.winAnimation) {
				MinigamesModVariables.MapVariables.get(world).winAnimationState = MinigamesModVariables.winAnimation;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
	}
}





