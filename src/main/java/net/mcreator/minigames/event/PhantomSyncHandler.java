package net.mcreator.minigames.event;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.PhantomStateSyncMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class PhantomSyncHandler {

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        MobEffectInstance effect = event.getEffectInstance();
        if (effect.is(MinigamesModMobEffects.PHANTOM)) {
            LivingEntity entity = event.getEntity();
            if (entity.level() instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersInDimension(serverLevel, new PhantomStateSyncMessage(entity.getId(), true));
            }
        }
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        MobEffectInstance effect = event.getEffectInstance();
        if (effect != null && effect.is(MinigamesModMobEffects.PHANTOM)) {
            LivingEntity entity = event.getEntity();
            if (entity.level() instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersInDimension(serverLevel, new PhantomStateSyncMessage(entity.getId(), false));
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        MobEffectInstance effect = event.getEffectInstance();
        if (effect != null && effect.is(MinigamesModMobEffects.PHANTOM)) {
            LivingEntity entity = event.getEntity();
            if (entity.level() instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersInDimension(serverLevel, new PhantomStateSyncMessage(entity.getId(), false));
            }
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity living && living.hasEffect(MinigamesModMobEffects.PHANTOM)) {
            if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
                PacketDistributor.sendToPlayer(player, new PhantomStateSyncMessage(living.getId(), true));
            }
        }
    }
}
