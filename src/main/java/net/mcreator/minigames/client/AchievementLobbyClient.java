package net.mcreator.minigames.client;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AchievementLobbyClient {
	private static final Map<UUID, String> LOBBY_STATES = new ConcurrentHashMap<>();
	private static final Map<UUID, Integer> TEAMS = new ConcurrentHashMap<>();

	public static void update(UUID uuid, String state, int team) {
		if (uuid == null)
			return;
		if (uuid.getMostSignificantBits() == 0 && uuid.getLeastSignificantBits() == 0 && "RESET_ALL".equals(state)) {
			clear();
			return;
		}
		if (state == null || state.isEmpty() || "\"\"".equals(state)) {
			LOBBY_STATES.remove(uuid);
		} else {
			LOBBY_STATES.put(uuid, state);
		}
		TEAMS.put(uuid, team);
	}

	public static String getState(UUID uuid, String defaultState) {
		return LOBBY_STATES.getOrDefault(uuid, defaultState != null ? defaultState : "");
	}

	public static int getTeam(UUID uuid, int defaultTeam) {
		return TEAMS.getOrDefault(uuid, defaultTeam);
	}

	public static void clear() {
		LOBBY_STATES.clear();
		TEAMS.clear();
	}
}
