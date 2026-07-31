package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PassCrownProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingDamageEvent.Pre event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getEntity(), event.getOriginalDamage());
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, double amount) {
		execute(null, world, x, y, z, entity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, double amount) {
		if (entity == null || sourceentity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).CrownHuntInGame) {
			if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.isBlocking())) {
				if (amount * (1 - Math
						.min(20, Math.max((entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity1.getAttribute(Attributes.ARMOR).getValue() : 0) / 5,
								(entity instanceof LivingEntity _livingEntity2 && _livingEntity2.getAttributes().hasAttribute(Attributes.ARMOR) ? _livingEntity2.getAttribute(Attributes.ARMOR).getValue() : 0) - (4 * amount)
										/ ((entity instanceof LivingEntity _livingEntity3 && _livingEntity3.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS) ? _livingEntity3.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue() : 0) + 8)))
						/ 25) > (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1)) {
					if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == MinigamesModItems.CROWN_HELMET_HELMET.get()) {
						{
							MinigamesModVariables.PlayerVariables _vars = sourceentity.getData(MinigamesModVariables.PLAYER_VARIABLES);
							_vars.helmet = (sourceentity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).copy();
							_vars.markSyncDirty();
						}
						if (sourceentity instanceof LivingEntity _living) {
							_living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MinigamesModItems.CROWN_HELMET_HELMET.get()));
						}
						if (entity instanceof LivingEntity _living) {
							_living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.AIR));
						}
						if (!(sourceentity == null)) {
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(
										new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										"/kill @e[type=armor_stand,nbt={equipment:{head:{id:\"minigames:crown_helmet_helmet\",count:1}}}]");
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1);
								} else {
									_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1, false);
								}
							}
							if (world instanceof ServerLevel _level) {
								_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((entity.getDisplayName().getString() + " dropped the crown!")).withColor(0xecb25d), false);
							}
							if (sourceentity instanceof Player) {
								MinigamesMod.queueServerWork(3, () -> {
									AnimateCrownPickupProcedure.execute(world);
								});
								if (world instanceof ServerLevel _level) {
									_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((sourceentity.getDisplayName().getString() + " stole the crown!")).withColor(0xecb25d), false);
								}
							} else if (sourceentity instanceof Spider) {
								if (!sourceentity.level().isClientSide())
									sourceentity.discard();
								if (world instanceof ServerLevel _level) {
									Entity entityToSpawn = MinigamesModEntities.GOLDEN_SPIDER.get().spawn(_level, BlockPos.containing(x, y, z), EntitySpawnReason.MOB_SUMMONED);
									if (entityToSpawn != null) {
										entityToSpawn.setDeltaMovement(0, 0, 0);
									}
								}
								if (world instanceof Level _level) {
									if (!_level.isClientSide()) {
										_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1);
									} else {
										_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1, false);
									}
								}
								if (world instanceof ServerLevel _level) {
									_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((sourceentity.getDisplayName().getString() + " stole the crown!")).withColor(0xecb25d), false);
								}
							} else if (sourceentity instanceof LivingEntity) {
								if (world instanceof Level _level) {
									if (!_level.isClientSide()) {
										_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1);
									} else {
										_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:teameliminated")), SoundSource.NEUTRAL, 1000000, 1, false);
									}
								}
								if (world instanceof ServerLevel _level) {
									_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal((sourceentity.getDisplayName().getString() + " stole the crown!")).withColor(0xecb25d), false);
								}
							}
						}
					}
				}
			}
		}
	}
}