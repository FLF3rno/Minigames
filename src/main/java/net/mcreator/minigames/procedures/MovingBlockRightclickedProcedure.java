package net.mcreator.minigames.procedures;

import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.entity.MovingBlockEntity;

public class MovingBlockRightclickedProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (sourceentity instanceof Player _plr0 && _plr0.gameMode() == GameType.CREATIVE) {
			if (entity instanceof MovingBlockEntity _datEntSetS)
				_datEntSetS.getEntityData().set(MovingBlockEntity.DATA_block_id, (BuiltInRegistries.ITEM.getKey((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()).toString()));
			if (sourceentity instanceof ServerPlayer _player)
				_player.sendSystemMessage(Component.literal(("Block set as " + (BuiltInRegistries.ITEM.getKey((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()).toString()))), false);
		}
	}
}