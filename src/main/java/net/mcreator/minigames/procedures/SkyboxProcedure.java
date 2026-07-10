package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.resources.Identifier;
import net.minecraft.client.Minecraft;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.client.RenderUtils;

import javax.annotation.Nullable;

@EventBusSubscriber(Dist.CLIENT)
public class SkyboxProcedure {
	@SubscribeEvent
	public static void onSkyRendered(RenderLevelStageEvent.AfterSky event) {
		Minecraft mc = Minecraft.getInstance();
		execute(event, mc.player.level(), event);
	}

	public static void execute(LevelAccessor world, RenderLevelStageEvent.AfterSky skyRenderEvent) {
		execute(null, world, skyRenderEvent);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, RenderLevelStageEvent.AfterSky skyRenderEvent) {
		if (skyRenderEvent == null)
			return;
		if ((MinigamesModVariables.MapVariables.get(world).sky).equals("normal")) {
		} else if ((MinigamesModVariables.MapVariables.get(world).sky).equals("space")) {
			RenderUtils.renderCustomSkybox(skyRenderEvent, Identifier.parse("minigames:textures/sky/space.png"), 0xffffff, Math.min(1, 1));
		}
	}
}