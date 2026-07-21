package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.MinigamesMod;

import java.util.ArrayList;

public class OnWinCrownHuntProcedure {
	public static void execute(LevelAccessor world) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/worldborder set 50000000");
		MinigamesModVariables.MapVariables.get(world).ShowCrownTimer = false;
		MinigamesModVariables.MapVariables.get(world).MoveCrownTimer = false;
		MinigamesModVariables.MapVariables.get(world).crownHuntWinDisplay = true;
		MinigamesModVariables.MapVariables.get(world).CrownHuntInGame = false;
		MinigamesModVariables.MapVariables.get(world).canGrabCrown = false;
		MinigamesModVariables.MapVariables.get(world).returnToCastle = false;
		MinigamesModVariables.MapVariables.get(world).showWinscreen = true;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		MinigamesModVariables.MapVariables.get(world).WinnerList.clear();
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (entityiterator instanceof LivingEntity _entity)
				_entity.removeAllEffects();
			if (entityiterator instanceof Player _player)
				_player.closeContainer();
			if (entityiterator instanceof ServerPlayer _player)
				_player.setGameMode(GameType.CREATIVE);
			{
				Entity _ent = entityiterator;
				if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
					_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
							LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound minigames:teameliminated ui @s ~ ~ ~ 1 1");
				}
			}
			if ((entityiterator instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
				if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.CROWNED, 99999999, 0, false, false));
				MinigamesModVariables.MapVariables.get(world).WinnerList.add((entityiterator.getStringUUID()));
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
		}
		MinigamesMod.queueServerWork(160, () -> {
			MinigamesModVariables.MapVariables.get(world).showWinscreen = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			MinigamesModVariables.MapVariables.get(world).WinnerList.clear();
		});
	}
}