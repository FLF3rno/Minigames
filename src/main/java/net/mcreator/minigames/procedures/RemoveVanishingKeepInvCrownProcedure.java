package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RemoveVanishingKeepInvCrownProcedure {
	@SubscribeEvent
	public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!(entity.getData(MinigamesModVariables.PLAYER_VARIABLES).helmet.getItem() == Blocks.AIR.asItem() || entity.getData(MinigamesModVariables.PLAYER_VARIABLES).helmet.getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get())) {
			if (MinigamesModVariables.MapVariables.get(world).CrownHuntInGame && world instanceof ServerLevel _serverLevelGR2 && _serverLevelGR2.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
				if (entity instanceof LivingEntity _living) {
					_living.setItemSlot(EquipmentSlot.HEAD, entity.getData(MinigamesModVariables.PLAYER_VARIABLES).helmet);
				}
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.helmet = new ItemStack(Blocks.AIR).copy();
					_vars.markSyncDirty();
				}
			}
		} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).helmet.getItem() == Blocks.AIR.asItem()) {
			if (entity instanceof LivingEntity _living) {
				_living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
			}
		}
	}
}