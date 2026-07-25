package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.MinigamesMod;

public class AddCrownToKillerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity sourceentity) {
		if (sourceentity == null)
			return;
		{
			MinigamesModVariables.PlayerVariables _vars = sourceentity.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.helmet = (sourceentity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).copy();
			_vars.markSyncDirty();
		}
		if (sourceentity instanceof LivingEntity _living) {
			_living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MinigamesModItems.CROWN_HELMET_HELMET.get()));
		}
		if (sourceentity instanceof Player) {
			MinigamesMod.queueServerWork(3, () -> {
				AnimateCrownPickupProcedure.execute(world);
			});
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((sourceentity.getDisplayName().getString() + " stole the crown!")).withColor(0xecb25d), false);
			}
		} else if (sourceentity instanceof Spider) {
			if (!sourceentity.level().isClientSide())
				sourceentity.discard();
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = MinigamesModEntities.GOLDEN_SPIDER.get().spawn(_level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setDeltaMovement(0, 0, 0);
				}
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1, false);
				}
			}
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((sourceentity.getDisplayName().getString() + " stole the crown!")).withColor(0xecb25d), false);
			}
		} else if (sourceentity instanceof LivingEntity) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1, false);
				}
			}
			if (world instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((sourceentity.getDisplayName().getString() + " stole the crown!")).withColor(0xecb25d), false);
			}
		}
	}
}