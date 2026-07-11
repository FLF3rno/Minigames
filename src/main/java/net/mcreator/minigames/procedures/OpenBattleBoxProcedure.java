package net.mcreator.minigames.procedures;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.world.inventory.BattleBoxMenu;
import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

import io.netty.buffer.Unpooled;

@EventBusSubscriber
public class OpenBattleBoxProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).openBattleBox && !(entity instanceof Player _plr0 && _plr0.containerMenu instanceof BattleBoxMenu)) {
			if (entity instanceof ServerPlayer _ent) {
				BlockPos _bpos = BlockPos.containing(x, y, z);
				_ent.openMenu(new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("BattleBox");
					}

					@Override
					public boolean shouldTriggerClientSideContainerClosingOnOpen() {
						return false;
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
						return new BattleBoxMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
					}
				}, _bpos);
			}
			Minecraft.getInstance().execute(() -> {
				entity.getPersistentData().putDouble("guiScale", Minecraft.getInstance().getWindow().getGuiScale());
				Minecraft.getInstance().options.guiScale().set(0);
			});
		} else if (!entity.getData(MinigamesModVariables.PLAYER_VARIABLES).openBattleBox && entity instanceof Player _plr3 && _plr3.containerMenu instanceof BattleBoxMenu) {
			if (entity instanceof Player _player)
				_player.closeContainer();
			Minecraft.getInstance().execute(() -> {
				entity.getPersistentData().putDouble("guiScale", Minecraft.getInstance().getWindow().getGuiScale());
				Minecraft.getInstance().options.guiScale().set((int) entity.getPersistentData().getDoubleOr("guiScale", 0));
			});
		}
	}
}
