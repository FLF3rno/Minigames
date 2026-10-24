package net.mcreator.minigames.util;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DynamicAchievementList {
	private static final List<AdvancementHolder> CACHED_ADVANCEMENTS = new ArrayList<>();
	private static final List<Identifier> CACHED_IDS = new ArrayList<>();
	private static Identifier currentTargetId = null;

	public static void refresh(ServerLevel level) {
		CACHED_ADVANCEMENTS.clear();
		CACHED_IDS.clear();

		for (AdvancementHolder holder : level.getServer().getAdvancements().getAllAdvancements()) {
			Optional<DisplayInfo> display = holder.value().display();
			if (display.isPresent() && holder.value().parent().isPresent()) {
				CACHED_ADVANCEMENTS.add(holder);
				CACHED_IDS.add(holder.id());
			}
		}
	}

	public static List<AdvancementHolder> getAdvancements(ServerLevel level) {
		if (CACHED_ADVANCEMENTS.isEmpty()) {
			refresh(level);
		}
		return CACHED_ADVANCEMENTS;
	}

	public static List<Identifier> getIds(ServerLevel level) {
		if (CACHED_IDS.isEmpty()) {
			refresh(level);
		}
		return CACHED_IDS;
	}

	public static AdvancementHolder getByIndex(ServerLevel level, int index) {
		List<AdvancementHolder> list = getAdvancements(level);
		if (list.isEmpty()) {
			return null;
		}
		int clampedIndex = Math.max(0, Math.min(index, list.size() - 1));
		return list.get(clampedIndex);
	}

	public static Identifier getIdByIndex(ServerLevel level, int index) {
		List<Identifier> ids = getIds(level);
		if (ids.isEmpty()) {
			return null;
		}
		int clampedIndex = Math.max(0, Math.min(index, ids.size() - 1));
		return ids.get(clampedIndex);
	}

	public static String getIdString(int index) {
		if (index >= 0 && index < CACHED_IDS.size()) {
			return CACHED_IDS.get(index).toString();
		}
		return "";
	}

	public static AdvancementHolder getById(ServerLevel level, Identifier id) {
		return level.getServer().getAdvancements().get(id);
	}

	public static void setCurrentTarget(Identifier id) {
		currentTargetId = id;
	}

	public static Identifier getCurrentTargetId() {
		return currentTargetId;
	}

	public static AdvancementHolder getCurrentTarget(ServerLevel level) {
		if (currentTargetId != null) {
			AdvancementHolder holder = getById(level, currentTargetId);
			if (holder != null) {
				return holder;
			}
		}
		return null;
	}

	public static AdvancementHolder rollAchievement(ServerLevel level, double challengeRerollChance) {
		List<AdvancementHolder> list = getAdvancements(level);
		if (list.isEmpty()) {
			return null;
		}

		for (int attempts = 0; attempts < 100; attempts++) {
			int index = (int) (Math.random() * list.size());
			AdvancementHolder candidate = list.get(index);
			Optional<DisplayInfo> display = candidate.value().display();

			if (display.isPresent() && display.get().getType() == AdvancementType.CHALLENGE) {
				if (Math.random() < challengeRerollChance) {
					continue;
				}
			}
			setCurrentTarget(candidate.id());
			return candidate;
		}

		AdvancementHolder fallback = list.get((int) (Math.random() * list.size()));
		setCurrentTarget(fallback.id());
		return fallback;
	}

	public static boolean isPurpleChallenge(AdvancementHolder holder) {
		if (holder == null) {
			return false;
		}
		return holder.value().display()
				.map(display -> display.getType() == AdvancementType.CHALLENGE)
				.orElse(false);
	}
}
