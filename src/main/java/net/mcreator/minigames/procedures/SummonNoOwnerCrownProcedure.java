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
        execute(event.getEntity().level(),
                event.getEntity(),
                event.getSource().getEntity());
    }

	private static void execute(LevelAccessor world, Entity entity, Entity sourceEntity) {

		if (!MinigamesModVariables.MapVariables.get(world).CrownHuntInGame) return;
		if (entity == null) return;
		if (world.isClientSide()) return;

		if ((entity instanceof LivingEntity living ? living.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() != MinigamesModItems.CROWN_HELMET_HELMET.get()) {
			return;
		}

		if (entity instanceof LivingEntity livingEntity) {
			livingEntity.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
		}

		if (sourceEntity instanceof Projectile projectile && projectile.getOwner() != null) {
			sourceEntity = projectile.getOwner();
		}

		if (!(sourceEntity instanceof LivingEntity)) {
			for (Entity player : new ArrayList<>(world.getServer().getPlayerList().getPlayers())) {
				MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);

				vars.timerSpeed = -1;
				vars.markSyncDirty();
			}
		}

		if (sourceEntity instanceof LivingEntity killer && killer != entity) {
			MinigamesModVariables.PlayerVariables vars = killer.getData(MinigamesModVariables.PLAYER_VARIABLES);
			vars.helmet = killer.getItemBySlot(EquipmentSlot.HEAD).copy();
			vars.markSyncDirty();
			killer.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MinigamesModItems.CROWN_HELMET_HELMET.get()));
			AnimateCrownPickupProcedure.execute(world);
			return;
		}

		if (world instanceof ServerLevel level) {
			ArmorStand crown = net.minecraft.world.entity.EntityTypes.ARMOR_STAND.create(level, EntitySpawnReason.COMMAND);

			if (crown != null) {
				crown.setPos(entity.getX(), entity.getY(), entity.getZ());
				crown.setYRot(entity.getYRot());
				crown.setXRot(entity.getXRot());

				crown.setPermanentlyInvulnerable(true);
				crown.setGlowingTag(true);
				crown.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MinigamesModItems.CROWN_HELMET_HELMET.get()));

				String ownerColor = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;

				String normalizedColor = ownerColor == null ? "FFFFFF" : ownerColor.trim();

				if (normalizedColor.startsWith("#")) {
					normalizedColor = normalizedColor.substring(1);
				}


				crown.setCustomName(Component.literal("[glow:#" + normalizedColor + "]"));
				crown.setCustomNameVisible(false);

				level.addFreshEntity(crown);

				GlowColorSync.applyGlowTeam(crown, ownerColor);
			}
		}
	}
}





