package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class HitArmorStandProcedure {
	private static final String CROWN_STAND_CLAIMED_KEY = "minigames.crown_stand_claimed";

	@SubscribeEvent
	public static void onLeftClickEntity(AttackEntityEvent event) {
		if (event.getEntity() != null && event.getTarget() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getTarget());
		}
	}

	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getTarget());
		}
	}

	@SubscribeEvent
	public static void onRightClickEntitySpecific(PlayerInteractEvent.EntityInteractSpecific event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getTarget());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity, null);
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity target) {
		execute(null, world, x, y, z, entity, target);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(event, world, x, y, z, entity, null);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity target) {
		if (entity == null)
			return;
		if (entity.level().isClientSide())
			return;
		if (!(entity instanceof Player))
			return;
		if (!(target instanceof ArmorStand))
			return;
		if (target.getPersistentData().getBoolean(CROWN_STAND_CLAIMED_KEY).orElse(false))
			return;
		if ((target instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
			if (event instanceof ICancellableEvent _cancellable) {
				_cancellable.setCanceled(true);
			}
			if (MinigamesModVariables.MapVariables.get(world).canGrabCrown == true) {
				if (!target.level().isClientSide()) {
					target.getPersistentData().putBoolean(CROWN_STAND_CLAIMED_KEY, true);
					target.discard();
				}
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.helmet = (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).copy();
					_vars.markSyncDirty();
				}
				if (entity instanceof LivingEntity _living) {
					_living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MinigamesModItems.CROWN_HELMET_HELMET.get()));
				}
				if ((target instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getName().getString() : _teamEnt.getStringUUID()) != null
						? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getName().getString() : _teamEnt.getStringUUID()).getName()
						: "")
						.equals(entity instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getName().getString() : _teamEnt.getStringUUID()) != null
								? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getName().getString() : _teamEnt.getStringUUID()).getName()
								: "")) {
					RepickAnimateCrownProcedure.execute(world, x, y, z);
					if (world instanceof ServerLevel _level) {
						_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((entity.getDisplayName().getString() + " recovered the crown!")).withColor(0xecb25d), false);
					}
				} else {
					AnimateCrownPickupProcedure.execute(world);
					if (world instanceof ServerLevel _level) {
						_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((entity.getDisplayName().getString() + " stole the crown!")).withColor(0xecb25d), false);
					}
				}
			}
		}
	}
}




