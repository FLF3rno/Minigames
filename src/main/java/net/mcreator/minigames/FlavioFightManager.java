package net.mcreator.minigames;

import io.netty.buffer.Unpooled;
import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.procedures.SpawnTwoMachinesProcedure;

import net.mcreator.minigames.world.inventory.FlavioPhase2Menu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;

@EventBusSubscriber
public class FlavioFightManager {

	public static int phase = 0;

	public static void nextPhase(LevelAccessor world) {
		phase++;

		if (phase == 2) {
			AnimationScreenTrigger.startAnimation(300, "fade_in_ascend", 1f);
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (world instanceof ServerLevel _level) {
					Entity cage = MinigamesModEntities.PLAYER_CAGE.get().spawn(
							_level,
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
				if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.IMMOBILIZED, 1000000, 1, false, false));


				MinigamesMod.queueServerWork(300, () -> {
					if (entityiterator instanceof ServerPlayer _ent) {
						BlockPos _bpos = BlockPos.containing(0, 0, 0);
						_ent.openMenu(new MenuProvider() {
							@Override
							public Component getDisplayName() {
								return Component.literal("FlavioPhase2");
							}

							@Override
							public boolean shouldTriggerClientSideContainerClosingOnOpen() {
								return false;
							}

							@Override
							public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
								return new FlavioPhase2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
							}
						}, _bpos);
					}
				});

			}

		}
		if (phase == 3 && world instanceof ServerLevel currentLevel) {

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
	}
}