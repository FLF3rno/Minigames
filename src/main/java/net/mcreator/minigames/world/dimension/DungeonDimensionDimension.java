package net.mcreator.minigames.world.dimension;

import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.DimensionSpecialEffects;

import net.mcreator.minigames.network.MinigamesModVariables;

import org.joml.Matrix4f;

public class DungeonDimensionDimension {
	@EventBusSubscriber(Dist.CLIENT)
	public static class DungeonDimensionSpecialEffectsHandler {
		@SubscribeEvent
		public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
			DimensionSpecialEffects customEffect = new DimensionSpecialEffects(DimensionSpecialEffects.SkyType.OVERWORLD, false, false) {
				@Override
				public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
					return color;
				}

				@Override
				public boolean isFoggyAt(int x, int y) {
					return false;
				}

				@Override
				public boolean renderClouds(ClientLevel level, int ticks, float partialTick, double camX, double camY, double camZ, Matrix4f modelViewMatrix) {
					return "space".equals(MinigamesModVariables.MapVariables.get(level).sky);
				}
			};
			event.register(ResourceLocation.parse("minigames:dungeon_dimension"), customEffect);
		}
	}
}
