package net.mcreator.minigames;

import io.netty.buffer.Unpooled;
import net.mcreator.minigames.entity.FlavioEntity;
import net.mcreator.minigames.entity.FlavioOmegaLaserEntity;
import net.mcreator.minigames.entity.PlayerCageEntity;
import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.procedures.SpawnTwoMachinesProcedure;

import net.mcreator.minigames.procedures.UpdateChunkProcedure;
import net.mcreator.minigames.world.inventory.FlavioPhase2Menu;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.Comparator;

@EventBusSubscriber
public class FlavioFightManager {

	public static float phase = 0;
	public static int playersDoneP2 = 0;
	public static int dispensersAlive = 2;
	public static LivingEntity flavio;

	public static void reset() {
		phase = 1;
		dispensersAlive = 2;
		playersDoneP2 = 0;
	}
	public static void nextPhase(LevelAccessor world) {
		if (!world.isClientSide()) {
            phase++;
        }
		switch ((int) phase) {
			case 2: startPhase2(world); flavio.getEntityData().set(FlavioEntity.ANIM, 1000); flavio.getEntityData().set(FlavioEntity.ANIM, 1); break;
			case 3: startPhase3(world); flavio.getEntityData().set(FlavioEntity.ANIM, 1000); flavio.getEntityData().set(FlavioEntity.ANIM, 1); break;
			case 4: startPhase4(world); flavio.getEntityData().set(FlavioEntity.ANIM, 1000); flavio.getEntityData().set(FlavioEntity.ANIM, 1); break;
			case 5: startPhase5(world); break;
			default: break;
		}
	}

	private static void startPhase2(LevelAccessor world) {
		if (!world.isClientSide()) {
            AnimationScreenTrigger.startAnimation(300, "fade_in_ascend", 1f);
        }

		for (Entity entityiterator : new ArrayList<>(world.players())) {
			if (world instanceof ServerLevel level) {
				Entity cage = MinigamesModEntities.PLAYER_CAGE.get().spawn(
						level,
						BlockPos.containing(
								entityiterator.getX(),
								entityiterator.getY() + 10,
								entityiterator.getZ()
						),
						EntitySpawnReason.MOB_SUMMONED
				);

				if (cage != null) {
					cage.snapTo(
							entityiterator.getX(),
							entityiterator.getY() + 10,
							entityiterator.getZ(),
							cage.getYRot(),
							cage.getXRot()
					);
				}
			}

			if (entityiterator instanceof LivingEntity living
					&& !living.level().isClientSide()) {
				living.addEffect(new MobEffectInstance(MinigamesModMobEffects.IMMOBILIZED, 1000000, 1, false, false));
				living.addEffect(new MobEffectInstance(MinigamesModMobEffects.BLOCK_HEAL, 1000000, 99, false, false));
			}

			MinigamesMod.queueServerWork(297, () -> {
				if (entityiterator instanceof ServerPlayer player) {
					BlockPos pos = BlockPos.containing(0, 0, 0);

					player.openMenu(new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("FlavioPhase2");
						}

						@Override
						public boolean shouldTriggerClientSideContainerClosingOnOpen() {
							return false;
						}

						@Override
						public AbstractContainerMenu createMenu(
								int id,
								Inventory inventory,
								Player player
						) {
							return new FlavioPhase2Menu(
									id,
									inventory,
									new FriendlyByteBuf(
											Unpooled.buffer()
									).writeBlockPos(pos)
							);
						}
					}, pos);
				}
			});
		}
	}

	private static void startPhase3(LevelAccessor world) {
		if (!(world instanceof ServerLevel currentLevel))
			return;

		MinecraftServer server = currentLevel.getServer();

		ResourceKey<Level> dungeonDimension = ResourceKey.create(
				Registries.DIMENSION,
				Identifier.parse("minigames:dungeon_dimension")
		);

		ServerLevel dungeonWorld = server.getLevel(dungeonDimension);

		if (dungeonWorld != null) {
			SpawnTwoMachinesProcedure.execute(dungeonWorld);
		}
	}
	private static void startPhase4(LevelAccessor world) {
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(1, 1, 1), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/execute as @e[type=minigames:flavio_trapdoor] at @s run summon minigames:flavio_omega_laser ~ ~-3.1 ~");

	}

	private static void startPhase5(LevelAccessor world) {
		if (flavio != null) {
			flavio.removeEffect(MinigamesModMobEffects.BLESSED);
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				MinigamesModVariables.PlayerVariables _vars = entityiterator.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.maximumLightLevel = 15;
				_vars.minimumLightLevel = 8;
				_vars.markSyncDirty();
			}
		}
	}

	public static void completePhase2(LevelAccessor world) {
		if (!world.isClientSide()) {
            playersDoneP2 ++;
        }
		if  (playersDoneP2 >= world.players().size()) {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (entityiterator instanceof LivingEntity _entity) {
					_entity.removeEffect(MinigamesModMobEffects.IMMOBILIZED);
					_entity.removeEffect(MinigamesModMobEffects.BLOCK_HEAL); }
				if (!(findEntityInWorldRange(world, PlayerCageEntity.class, (entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), 10)).level().isClientSide())
					(findEntityInWorldRange(world, PlayerCageEntity.class, (entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), 10)).discard();
			}
			nextPhase(world);
		}
	}
	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}

}