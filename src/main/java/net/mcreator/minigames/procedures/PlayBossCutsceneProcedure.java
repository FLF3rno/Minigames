package net.mcreator.minigames.procedures;

public class PlayBossCutsceneProcedure {
	public static void execute() {
		net.mcreator.minigames.AnimationScreenTrigger.startAnimation(150, "roguelike_boss", 1.0f);
	}
}