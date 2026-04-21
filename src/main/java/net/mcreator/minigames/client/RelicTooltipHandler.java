package net.mcreator.minigames.client;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import net.mcreator.minigames.DungeonItemAccess;

@EventBusSubscriber(value = Dist.CLIENT)
public class RelicTooltipHandler {
	private static final String RELIC_LABEL = "Relic";
	private static final int START_COLOR = 0xA020F0;
	private static final int END_COLOR = 0xC77DFF;

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (!DungeonItemAccess.isRelic(stack)) {
			return;
		}

		MutableComponent gradientLabel = gradientText(RELIC_LABEL, START_COLOR, END_COLOR);
		int insertIndex = Math.min(1, event.getToolTip().size());
		event.getToolTip().add(insertIndex, gradientLabel);
	}

	private static MutableComponent gradientText(String text, int startColor, int endColor) {
		MutableComponent result = Component.empty();
		int length = text.length();
		for (int i = 0; i < length; i++) {
			float t = length <= 1 ? 0.0f : (float) i / (float) (length - 1);
			int color = lerpColor(startColor, endColor, t);
			result.append(Component.literal(String.valueOf(text.charAt(i))).withColor(color));
		}
		return result;
	}

	private static int lerpColor(int startColor, int endColor, float t) {
		int sr = (startColor >> 16) & 0xFF;
		int sg = (startColor >> 8) & 0xFF;
		int sb = startColor & 0xFF;
		int er = (endColor >> 16) & 0xFF;
		int eg = (endColor >> 8) & 0xFF;
		int eb = endColor & 0xFF;
		int r = (int) (sr + (er - sr) * t);
		int g = (int) (sg + (eg - sg) * t);
		int b = (int) (sb + (eb - sb) * t);
		return (r << 16) | (g << 8) | b;
	}
}
