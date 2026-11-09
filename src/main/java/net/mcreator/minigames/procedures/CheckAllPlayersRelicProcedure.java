package net.mcreator.minigames.procedures;

import net.mcreator.minigames.network.TeamRelicsSyncMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EventBusSubscriber
public class CheckAllPlayersRelicProcedure {
	private static volatile List<ItemStack> CLIENT_RELICS = Collections.emptyList();
	private static List<ItemStack> lastSyncedServerRelics = Collections.emptyList();

	public static void setClientRelics(List<ItemStack> relics) {
		CLIENT_RELICS = relics != null ? List.copyOf(relics) : Collections.emptyList();
	}

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post event) {
		MinecraftServer server = event.getServer();
		if (server.getTickCount() % 5 != 0) {
			return;
		}
		List<ServerPlayer> players = server.getPlayerList().getPlayers();
		if (players.isEmpty()) {
			return;
		}
		List<ItemStack> current = new ArrayList<>();
		for (ServerPlayer player : players) {
			ItemStack s34 = getEntitySlot(player, 34);
			if (!s34.isEmpty()) {
				current.add(s34.copy());
			}
			ItemStack s35 = getEntitySlot(player, 35);
			if (!s35.isEmpty()) {
				current.add(s35.copy());
			}
		}
		if (!stacksEqual(current, lastSyncedServerRelics)) {
			lastSyncedServerRelics = current;
			PacketDistributor.sendToAllPlayers(new TeamRelicsSyncMessage(current));
		}
	}

	private static boolean stacksEqual(List<ItemStack> a, List<ItemStack> b) {
		if (a.size() != b.size()) {
			return false;
		}
		for (int i = 0; i < a.size(); i++) {
			if (!ItemStack.matches(a.get(i), b.get(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean execute(LevelAccessor world, ItemStack item) {
		return !getRelicStack(item, world).isEmpty();
	}

	public static ItemStack getRelicStack(ItemStack item, LevelAccessor world) {
		if (item == null || item.isEmpty())
			return ItemStack.EMPTY;
		if (world instanceof ServerLevel _level) {
			for (Entity entityiterator : new ArrayList<>(_level.getServer().getPlayerList().getPlayers())) {
				ItemStack s34 = getEntitySlot(entityiterator, 34);
				if (s34.getItem() == item.getItem()) {
					return s34;
				}
				ItemStack s35 = getEntitySlot(entityiterator, 35);
				if (s35.getItem() == item.getItem()) {
					return s35;
				}
			}
		} else {
			for (ItemStack stack : CLIENT_RELICS) {
				if (stack.getItem() == item.getItem()) {
					return stack;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	private static ItemStack getEntitySlot(Entity entity, int slot) {
		if (entity != null) {
			ResourceHandler<ItemResource> resourceHandler = entity.getCapability(Capabilities.Item.ENTITY, null);
			if (resourceHandler != null) {
				return ItemUtil.getStack(resourceHandler, slot);
			}
		}
		return ItemStack.EMPTY;
	}
}
