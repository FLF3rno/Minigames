package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.GlowColorSync;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class SummonNoOwnerCrownProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingDamageEvent.Pre event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity(), event.getOriginalDamage());
		}
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			executeOnDeath(event, event.getEntity().level(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, Entity entity, double amount) {
		execute(null, world, entity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, double amount) {
		if (entity == null)
			return;
		if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.isBlocking())) {
			if (amount * (1 - Math
					.min(20, Math.max((entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity1.getAttribute(Attributes.ARMOR).getValue() : 0) / 5,
							(entity instanceof LivingEntity _livingEntity2 && _livingEntity2.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity2.getAttribute(Attributes.ARMOR).getValue() : 0) - (4 * amount)
									/ ((entity instanceof LivingEntity _livingEntity3 && _livingEntity3.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS) ? _livingEntity3.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue() : 0) + 8)))
					/ 25) > (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1)) {
				if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
					MinigamesModVariables.MapVariables.get(world).MoveCrownTimer = false;
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					spawnNoOwnerCrown(entity, false);
				}
			}
		}
	}

	private static void executeOnDeath(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null)
			return;
		if (sourceentity != null)
			return;
	if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
			MinigamesModVariables.MapVariables.get(world).MoveCrownTimer = false;
			MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			spawnNoOwnerCrown(entity, true);
		}
	}

	private static void spawnNoOwnerCrown(Entity entity, boolean announceDrop) {
		if (announceDrop) {
			if (entity.level() instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1);
				} else {
					_level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1, false);
				}
			}
			if (entity.level() instanceof ServerLevel _level) {
				_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((entity.getDisplayName().getString() + " dropped the crown!")).withColor(0xecb25d), false);
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
				_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent),
						"/kill @e[type=armor_stand,nbt={equipment:{head:{id:\"minigames:crown_helmet_helmet\",count:1}}}]");
				_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent),
						"/summon armor_stand ~ ~ ~ {Invulnerable:1b,Glowing:1b,equipment:{head:{id:\"minigames:crown_helmet_helmet\",count:1}}}");
			}
		}
		MinigamesMod.queueServerWork(1, () -> {
			ArmorStand nearest = null;
			double bestDist = Double.MAX_VALUE;
			for (Entity e : entity.level().getEntities(entity, entity.getBoundingBox().inflate(8.0))) {
				if (e instanceof ArmorStand stand) {
					ItemStack head = stand.getItemBySlot(EquipmentSlot.HEAD);
					if (head.getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
						double d = stand.distanceToSqr(entity);
						if (d < bestDist) {
							bestDist = d;
							nearest = stand;
						}
					}
				}
			}
			if (nearest != null) {
				String ownerColor = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
				String normalizedColor = ownerColor == null ? "FFFFFF" : ownerColor.trim();
				if (normalizedColor.startsWith("#")) {
					normalizedColor = normalizedColor.substring(1);
				}
				if (!normalizedColor.matches("^[0-9a-fA-F]{6}$")) {
					normalizedColor = "FFFFFF";
				}
				nearest.setCustomName(Component.literal("[glow:#" + normalizedColor + "]"));
				nearest.setCustomNameVisible(false);
				GlowColorSync.applyGlowTeam(nearest, ownerColor);
			}
		});
	}
}






