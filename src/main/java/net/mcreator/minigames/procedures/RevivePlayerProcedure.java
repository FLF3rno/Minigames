package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RevivePlayerProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity(), event.getSource().getDirectEntity());
		}
	}

	public static void execute(Entity entity, Entity immediatesourceentity) {
		execute(null, entity, immediatesourceentity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity immediatesourceentity) {
		if (entity == null || immediatesourceentity == null)
			return;
		if ((BuiltInRegistries.ENTITY_TYPE.getKey(immediatesourceentity.getType()).toString()).equals("minecraft:server_player") || (BuiltInRegistries.ENTITY_TYPE.getKey(immediatesourceentity.getType()).toString()).equals("minecraft:player")
				|| (BuiltInRegistries.ENTITY_TYPE.getKey(immediatesourceentity.getType()).toString()).equals("minigames:blessed_arrow")) {
			if (entity instanceof LivingEntity _livEnt3 && _livEnt3.hasEffect(MinigamesModMobEffects.ASCENDING)) {
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.removeEffectsSingleTarget = true;
					_vars.markSyncDirty();
				}
				entity.setDeltaMovement(new Vec3((immediatesourceentity.getLookAngle().x * 2), (immediatesourceentity.getLookAngle().y * 2), (immediatesourceentity.getLookAngle().z * 2)));
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.ascendingTimer = 0;
					_vars.markSyncDirty();
				}
			}
		}
	}
}