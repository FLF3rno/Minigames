package net.mcreator.minigames.procedures;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Holder;

import java.awt.*;
import java.util.Optional;

public class GetAchievementInfoProcedure {
	public static void execute(LevelAccessor world) {
		Identifier id = Identifier.parse("minecraft:" +
				GetAchievementProcedure.execute(MinigamesModVariables.MapVariables.get(world).AchievementCategory,
						MinigamesModVariables.MapVariables.get(world).Achievement)
		);

		AdvancementHolder advancement = ((ServerLevel) world)
				.getServer()
				.getAdvancements()
				.get(id);


		if (advancement != null) {
			var display = advancement.value().display().orElse(null);

			if (display != null) {
				MinigamesModVariables.MapVariables.get(world).AchievementTitle = display.getTitle().getString();
				MinigamesModVariables.MapVariables.get(world).AchievementDescription = display.getDescription().getString();
				MinigamesModVariables.MapVariables.get(world).AchievementIcon = display.getIcon().create();
			}
		}

		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}