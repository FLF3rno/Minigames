package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DoExplosionKnockbackProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	public static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
    if (entity == null)
        return;
    
    var playerVars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
    Vec3 kb = playerVars.performKnockback;

    if (kb.x() != 0 || kb.y() != 0 || kb.z() != 0) {
        
        entity.setDeltaMovement(kb);
        entity.hasImpulse = true; 


        if (entity instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(entity));
        }

        playerVars.performKnockback = Vec3.ZERO;
        playerVars.markSyncDirty();
    }
}
}