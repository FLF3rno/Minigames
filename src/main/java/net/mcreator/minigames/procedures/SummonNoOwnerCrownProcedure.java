package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.*;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import java.util.ArrayList;

@EventBusSubscriber
public class SummonNoOwnerCrownProcedure {

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			execute(event.getEntity().level(),
					event.getEntity(),
					event.getSource().getEntity());
		}
	}

	private static void execute(LevelAccessor world,
								Entity entity,
								Entity sourceEntity) {

		if (entity == null)
			return;

		if ((entity instanceof LivingEntity living
				? living.getItemBySlot(EquipmentSlot.HEAD)
				: ItemStack.EMPTY).getItem()
				!= MinigamesModItems.CROWN_HELMET_HELMET.get()) {
			return;
		}

		for (Entity player : new ArrayList<>(world.players())) {
			MinigamesModVariables.PlayerVariables vars =
					player.getData(MinigamesModVariables.PLAYER_VARIABLES);

			vars.timerSpeed = -1;
			vars.markSyncDirty();
		}

		if (sourceEntity instanceof Projectile projectile
				&& projectile.getOwner() != null) {
			sourceEntity = projectile.getOwner();
		}

		if (sourceEntity instanceof LivingEntity killer) {

			if (world instanceof ServerLevel level) {
				level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(
								CommandSource.NULL,
								Vec3.ZERO,
								Vec2.ZERO,
								level,
								LevelBasedPermissionSet.OWNER,
								"",
								Component.literal(""),
								level.getServer(),
								null
						).withSuppressedOutput(),
						"/kill @e[type=armor_stand,nbt={equipment:{head:{id:\"minigames:crown_helmet_helmet\",count:1}}}]"
				);
			}

			killer.setItemSlot(
					EquipmentSlot.HEAD,
					new ItemStack(
							MinigamesModItems.CROWN_HELMET_HELMET.get()
					)
			);
			AnimateCrownPickupProcedure.execute(world);
			return;
		}

		if (world instanceof ServerLevel level) {
			ArmorStand crown = EntityType.ARMOR_STAND.create(level, EntitySpawnReason.COMMAND);

			if (crown != null) {
				crown.setPos(entity.getX(), entity.getY(), entity.getZ());
				crown.setYRot(entity.getYRot());
				crown.setXRot(entity.getXRot());

				crown.setInvulnerable(true);
				crown.setGlowingTag(true);
				crown.setItemSlot(
						EquipmentSlot.HEAD,
						new ItemStack(MinigamesModItems.CROWN_HELMET_HELMET.get())
				);

				String ownerColor =
						entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;

				String normalizedColor =
						ownerColor == null ? "FFFFFF" : ownerColor.trim();

				if (normalizedColor.startsWith("#")) {
					normalizedColor = normalizedColor.substring(1);
				}

				if (!normalizedColor.matches("^[0-9a-fA-F]{6}$")) {
					normalizedColor = "FFFFFF";
				}

				crown.setCustomName(
						Component.literal("[glow:#" + normalizedColor + "]")
				);
				crown.setCustomNameVisible(false);

				level.addFreshEntity(crown);

				GlowColorSync.applyGlowTeam(crown, ownerColor);
			}
		}

		return;
	}
}





