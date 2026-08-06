package net.mcreator.minigames.procedures;

public class PlayBossCutsceneProcedure {
	public static void execute() {
		net.mcreator.minigames.AnimationScreenTrigger.startAnimation(200, "roguelike_boss", 1f);
	}
}