package net.mcreator.minigames.death;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.*;

@EventBusSubscriber(modid = "minigames")
public class DeathEventHandler {

    public static final TagKey<Item> KEEP_ON_DEATH = ItemTags.create(
            ResourceLocation.fromNamespaceAndPath("minigames", "keep_on_death")
    );

    private static final Map<UUID, List<ItemStack>> keptItems = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerDeath(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        List<ItemStack> itemsToKeep = new ArrayList<>();
        Iterator<ItemEntity> iterator = event.getDrops().iterator();

        while (iterator.hasNext()) {
            ItemEntity drop = iterator.next();
            if (drop.getItem().is(KEEP_ON_DEATH)) {
                itemsToKeep.add(drop.getItem().copy());
                iterator.remove();
            }
        }

        if (!itemsToKeep.isEmpty()) {
            keptItems.put(player.getUUID(), itemsToKeep);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.Clone event) {
        UUID playerID = event.getEntity().getUUID();
        if (keptItems.containsKey(playerID)) {
            List<ItemStack> items = keptItems.remove(playerID);
            for (ItemStack stack : items) {
                event.getEntity().addItem(stack);
            }
        }
    }
}