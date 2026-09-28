package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.advancements.AdvancementHolder;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class AchievementObtainedProcedure {
	@SubscribeEvent
	public static void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
		if (event != null && event.getEntity() instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel) {
			checkAndEndGame(serverLevel, player, event.getAdvancement());
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (event != null && !event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player && player.tickCount % 10 == 0 && player.level() instanceof ServerLevel serverLevel) {
			checkAndEndGame(serverLevel, player, null);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (world instanceof ServerLevel serverLevel && entity instanceof ServerPlayer player) {
			checkAndEndGame(serverLevel, player, null);
		}
	}

	private static void checkAndEndGame(ServerLevel serverLevel, ServerPlayer player, @Nullable AdvancementHolder earnedAdvancement) {
		if (!MinigamesModVariables.MapVariables.get(serverLevel).playingAchievement) {
			return;
		}
		if (MinigamesModVariables.MapVariables.get(serverLevel).achievementHunterMode) {
			String hunterUUID = MinigamesModVariables.MapVariables.get(serverLevel).hunterAchievementUUID;
			if (hunterUUID != null && !hunterUUID.isEmpty() && !hunterUUID.equals("\"\"")) {
				if (!player.getStringUUID().equals(hunterUUID)) {
					return;
				}
			}
		}
		AdvancementHolder targetAdvancement = getTargetAdvancement(serverLevel);
		if (targetAdvancement == null) {
			return;
		}
		boolean hasWon = false;
		if (earnedAdvancement != null) {
			if (earnedAdvancement.equals(targetAdvancement) || earnedAdvancement.id().equals(targetAdvancement.id())) {
				hasWon = true;
			}
		}
		if (!hasWon) {
			var progress = player.getAdvancements().getOrStartProgress(targetAdvancement);
			if (progress != null && progress.isDone()) {
				hasWon = true;
			}
		}
		if (hasWon) {
			AchievementGameEndProcedure.execute(serverLevel, player.getX(), player.getY(), player.getZ(), player);
		}
	}

	@Nullable
	public static AdvancementHolder getTargetAdvancement(ServerLevel serverLevel) {
		String rawPath = GetAchievementProcedure.execute(
				MinigamesModVariables.MapVariables.get(serverLevel).AchievementCategory,
				MinigamesModVariables.MapVariables.get(serverLevel).Achievement
		);
		if (rawPath == null || rawPath.isEmpty()) {
			return null;
		}
		String path = rawPath.replace(':', '_');
		if ("husbandry/good_as_new".equals(path)) {
			path = "husbandry/repair_wolf_armor";
		} else if ("adventure/spear_many_mobs".equals(path)) {
			path = "adventure/walk_on_powder_snow_with_leather_boots";
		}
		Identifier targetId = Identifier.tryParse("minecraft:" + path);
		if (targetId == null) {
			return null;
		}
		return serverLevel.getServer().getAdvancements().get(targetId);
	}
}