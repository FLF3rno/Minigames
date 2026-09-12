package net.mcreator.minigames.procedures;

public class PlayIntroCutsceneProcedure {
	public static void execute() {
		net.mcreator.minigames.AnimationScreenTrigger.startAnimation(150, "roguelike_intro", 1.0f);
	}
}