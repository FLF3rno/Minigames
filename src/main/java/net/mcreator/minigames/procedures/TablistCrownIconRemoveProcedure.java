package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class TablistCrownIconRemoveProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity.level().isClientSide()) {
			return;
		}
		if (MinigamesModVariables.MapVariables.get(world).CrownHuntInGame) {
			if (entity.isAlive()) {
				if (!((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()))
				{
					String identifier = entity instanceof Player player
        				? player.getGameProfile().name()
        				: entity.getStringUUID();

					PlayerTeam crownedTeam =
        				entity.level().getScoreboard().getPlayerTeam("crowned");

					if (crownedTeam != null
        				&& entity.level().getScoreboard().getPlayersTeam(identifier) == crownedTeam)
					{
    					entity.level().getScoreboard().removePlayerFromTeam(identifier, crownedTeam);
					}
				}
			}
		}
	}
}