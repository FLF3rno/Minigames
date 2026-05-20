package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class NameColorApplyProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		applyColor(event.getEntity().level(), event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		applyColor(event.getEntity().level(), event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		applyColor(event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!(world instanceof ServerLevel))
			return;
		if (!MinigamesModVariables.MapVariables.get(world).applyCustomNameColor)
			return;
		applyColor(world, entity);
		MinigamesModVariables.MapVariables.get(world).applyCustomNameColor = false;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}

	public static void applyColor(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!(world instanceof ServerLevel _level))
			return;
		String color = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).color;
		if (color == null || color.isEmpty())
			return;
		String hexColor = color.startsWith("#") ? color : "#" + color;
		TextColor parsedColor = TextColor.parseColor(hexColor).result().orElse(null);
		if (parsedColor == null)
			return;
		MutableComponent coloredName = Component.literal(entity.getName().getString()).setStyle(Style.EMPTY.withColor(parsedColor));
		entity.setCustomName(coloredName);
		entity.setCustomNameVisible(true);
	}
}
