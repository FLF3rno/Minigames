package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;

@EventBusSubscriber
public class AscendingActiveProcedure {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        execute(event, event.getEntity().getY(), event.getEntity());
    }

    public static void execute(double y, Entity entity) {
        execute(null, y, entity);
    }

    private static void execute(@Nullable Event event, double y, Entity entity) {
        if (entity == null) return;

        CompoundTag nbt = entity.getPersistentData();

        if (entity instanceof LivingEntity livEnt && livEnt.hasEffect(MinigamesModMobEffects.ASCENDING)) {
            if (entity instanceof Player player) {
                boolean slotSaved = nbt.getBoolean("ascendingSlotSaved").orElse(false);
                if (!slotSaved) {
                    nbt.putInt("ascendingPreviousSlot", player.getInventory().getSelectedSlot());
                    nbt.putBoolean("ascendingSlotSaved", true);
                }

                if (player.getInventory().getSelectedSlot() != 8) {
                    player.getInventory().setSelectedSlot(8);
                }
            }

            var effectInstance = livEnt.getEffect(MinigamesModMobEffects.ASCENDING);
            if (effectInstance != null) {
                double amp = effectInstance.getAmplifier();
                double up = (amp / 100d);
                entity.setDeltaMovement(new Vec3(0, up, 0));
            }
        } else if (entity instanceof Player player) {
            if (nbt.getBoolean("ascendingSlotSaved").orElse(false)) {
                int previousSlot = nbt.getInt("ascendingPreviousSlot").orElse(0);
                if (previousSlot < 0 || previousSlot > 8) previousSlot = 0;
                player.getInventory().setSelectedSlot(previousSlot);
                nbt.remove("ascendingPreviousSlot");
                nbt.putBoolean("ascendingSlotSaved", false);
                AscendingEffectExpiresProcedure.execute(player);
            } else if (entity.level().isClientSide() && !nbt.contains("InAscenscionBeam") && "minigames:ascend".equals(nbt.getStringOr("PlayerCurrentAnimation", ""))) {
                nbt.remove("PlayerCurrentAnimation");
                nbt.putBoolean("ResetPlayerAnimation", true);
            }
        }
    }
}

