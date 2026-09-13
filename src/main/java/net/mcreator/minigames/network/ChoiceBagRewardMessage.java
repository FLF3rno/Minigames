package net.mcreator.minigames.network;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public record ChoiceBagRewardMessage(String itemId) implements CustomPacketPayload {
	public static final Type<ChoiceBagRewardMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "choice_bag_reward"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ChoiceBagRewardMessage> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, ChoiceBagRewardMessage message) -> buffer.writeUtf(message.itemId),
			(RegistryFriendlyByteBuf buffer) -> new ChoiceBagRewardMessage(buffer.readUtf())
	);

	@Override
	public Type<ChoiceBagRewardMessage> type() {
		return TYPE;
	}

	public static void handleData(final ChoiceBagRewardMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					giveReward(serverPlayer, message.itemId);
				}
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	private static void giveReward(ServerPlayer player, String itemIdStr) {
		Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(itemIdStr));
		if (item == null || item == Items.AIR) {
			return;
		}

		String path = BuiltInRegistries.ITEM.getKey(item).getPath();
		if (path.contains("choice_bag") || path.equals("blank_sword") || path.equals("blank_long_sword") || path.equals("blank_dagger")) {
			return;
		}

		ItemStack stack = new ItemStack(item);
		DungeonItemPickupMessage.tryPickupStack(player, stack, null);
		player.closeContainer();
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinigamesMod.addNetworkMessage(ChoiceBagRewardMessage.TYPE, ChoiceBagRewardMessage.STREAM_CODEC, ChoiceBagRewardMessage::handleData);
	}
}
