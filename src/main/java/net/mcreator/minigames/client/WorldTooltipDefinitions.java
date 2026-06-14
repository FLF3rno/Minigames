package net.mcreator.minigames.client;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.vertex.PoseStack;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joml.Matrix4f;

import net.mcreator.minigames.DungeonItemAccess;

public final class WorldTooltipDefinitions {
	private static final int THIEF_COLOR = 0xFFAA00;
	private static final int FORGED_COLOR = 0xFA8A05;
	private static final int MONSTER_COLOR = 0xF40B0D;
	private static final int PHANTOM_COLOR = 0x605AA5;
	private static final int BLESSED_COLOR = 0x84D4FF;
	private static final int DEFINITION_TEXT = 0xEAE6D8;
	private static final int PADDING_X = 6;
	private static final int PADDING_Y = 6;
	private static final int LINE_HEIGHT = 10;
	private static final int TITLE_BODY_GAP = 4;
	private static final int DEFINITION_BACKGROUND = 0xF0181028;
	private static final int DEFINITION_BORDER_LIGHT = 0xA05000FF;
	private static final int DEFINITION_BORDER_DARK = 0xA028007F;
	private static final int DEFINITION_TITLE_COLOR = 0xC77DFF;
	private static final int DEFINITION_GAP = 5;
	private static final List<DefinitionRule> DEFINITION_RULES = List.of(
			//DEFINITION DI PROPRIERTIES
			new DefinitionRule(10,
					stack -> DungeonItemAccess.isStolen(stack),
					stack -> 0,
					(stack, val) -> new DefinitionCard(10,
							DefinitionText.staticText("STOLEN", Style.EMPTY.withBold(true).withColor(THIEF_COLOR)),
							List.of(DefinitionText.fromComponent(centeredLine(partialColorText("Thief", THIEF_COLOR),
									partialColorText(" can use this item", 0xFFFFFFFF))))
					)
			),
			new DefinitionRule(11,
					stack -> DungeonItemAccess.Forged(stack) != 0 || DungeonItemAccess.isForged(stack),
					stack -> DungeonItemAccess.Forged(stack),
					(stack, amount) -> new DefinitionCard(11,
							DefinitionText.staticText("FORGED", Style.EMPTY.withBold(true).withColor(FORGED_COLOR)),
							List.of(
									DefinitionText.staticText("Numerical values for this", Style.EMPTY.withColor(0xFFFFFFFF)),
									DefinitionText.fromComponent(centeredLine(partialColorText("are buffed by ", 0xFFFFFFFF),
											partialColorText(amount + "%", FORGED_COLOR)))
							)
					)
			),
			new DefinitionRule(9,
					stack -> DungeonItemAccess.isGlitched(stack),
					stack -> 0,
					(stack, val) -> new DefinitionCard(9,
							DefinitionText.animatedGlitch("GLITCHED"),
							List.of(
									DefinitionText.staticText("Numerical values for this", Style.EMPTY.withColor(0xFFFFFFFF)),
									DefinitionText.staticText("item are randomized", Style.EMPTY.withColor(0xFFFFFFFF))
							)
					)
			),
			//DEFINITION DI STATUS EFFECTS
			new DefinitionRule(5,
					stack -> DungeonItemAccess.hasPhantom(stack),
					stack -> 0,
					(stack, val) -> new DefinitionCard(10,
							DefinitionText.staticText("PHANTOM", Style.EMPTY.withBold(true).withColor(PHANTOM_COLOR)),
							List.of(DefinitionText.fromComponent(centeredLine(partialColorText("Phase through entities", 0xFFFFFFFF))))
					)
			),
			new DefinitionRule(6,
					stack -> DungeonItemAccess.hasBlessed(stack),
					stack -> 0,
					(stack, val) -> new DefinitionCard(6,
							DefinitionText.staticText("BLESSED", Style.EMPTY.withBold(true).withColor(BLESSED_COLOR)),
							List.of(
									DefinitionText.fromComponent(Component.literal("Take no damage").withStyle(Style.EMPTY.withColor(0xFFFFFFFF))),
									DefinitionText.fromComponent(centeredLine(
											partialColorText("Monsters", MONSTER_COLOR),
											partialColorText(" cannot target you", 0xFFFFFFFF)
									))
							)
					)
			)
	);


	private WorldTooltipDefinitions() {
	}

	public static List<DefinitionCard> getDefinitionCards(ItemStack stack) {
		List<DefinitionCard> definitions = new ArrayList<>();
		for (DefinitionRule rule : DEFINITION_RULES) {
			if (rule.matches(stack)) {
				definitions.add(rule.createCard(stack));
			}
		}
		definitions.sort(Comparator.comparingInt(DefinitionCard::priority));
		return definitions;
	}

	public static int getDefinitionsWidth(Font font, List<DefinitionCard> definitions) {
		int width = 0;
		for (DefinitionCard definition : definitions) {
			width = Math.max(width, definition.width(font));
		}
		return width;
	}

	public static int getDefinitionsHeight(List<DefinitionCard> definitions) {
		int height = 0;
		for (DefinitionCard definition : definitions) {
			height += definition.height();
		}
		if (!definitions.isEmpty()) {
			height += DEFINITION_GAP * (definitions.size() - 1);
		}
		return height;
	}

	public static void renderDefinitions(Matrix4f matrix, MultiBufferSource.BufferSource bufferSource, Font font, List<DefinitionCard> definitions,
			int left, int top) {
		int definitionTop = top;
		for (DefinitionCard definition : definitions) {
			int definitionWidth = definition.width(font) + PADDING_X * 2;
			int definitionHeight = definition.height();
			renderDefinitionPanel(matrix, bufferSource, left, definitionTop, left + definitionWidth, definitionTop + definitionHeight);
			definition.draw(font, matrix, bufferSource, left, definitionTop);
			definitionTop += definitionHeight + DEFINITION_GAP;
		}
	}

	private static void renderDefinitionPanel(Matrix4f matrix, MultiBufferSource.BufferSource bufferSource, int left, int top, int right, int bottom) {
		var background = bufferSource.getBuffer(net.minecraft.client.renderer.RenderType.textBackgroundSeeThrough());
		addQuad(background, matrix, left, top, right, bottom, DEFINITION_BACKGROUND);

		var border = bufferSource.getBuffer(net.minecraft.client.renderer.RenderType.textBackgroundSeeThrough());
		addQuad(border, matrix, left - 1, top - 1, right + 1, top, DEFINITION_BORDER_LIGHT);
		addQuad(border, matrix, left - 1, bottom, right + 1, bottom + 1, DEFINITION_BORDER_DARK);
		addQuad(border, matrix, left - 1, top, left, bottom, DEFINITION_BORDER_LIGHT);
		addQuad(border, matrix, right, top, right + 1, bottom, DEFINITION_BORDER_DARK);
	}

	private static void addQuad(com.mojang.blaze3d.vertex.VertexConsumer consumer, Matrix4f matrix, int left, int top, int right, int bottom, int color) {
		consumer.addVertex(matrix, left, top, 0.0F).setColor(color).setLight(15728880);
		consumer.addVertex(matrix, left, bottom, 0.0F).setColor(color).setLight(15728880);
		consumer.addVertex(matrix, right, bottom, 0.0F).setColor(color).setLight(15728880);
		consumer.addVertex(matrix, right, top, 0.0F).setColor(color).setLight(15728880);
	}

	private static Component centeredLine(Component... parts) {
		MutableComponent result = Component.empty();
		for (Component part : parts) {
			result.append(part);
		}
		return result;
	}

	private static Component partialColorText(String text, int color) {
		return Component.literal(text).setStyle(Style.EMPTY.withBold(false).withColor(color));
	}

	private static MutableComponent glitchText(String text) {
		MutableComponent result = Component.empty();
		int time = (int) (System.currentTimeMillis() / 90L);
		for (int i = 0; i < text.length(); i++) {
			if (shouldHideGlitchChar(text, i, time)) {
				result.append(Component.literal(" ").setStyle(Style.EMPTY.withBold(true).withColor(0x66CCFF)));
				continue;
			}
			int color = randomGlitchColor(text, i, time);
			result.append(Component.literal(String.valueOf(text.charAt(i))).setStyle(Style.EMPTY.withBold(true).withColor(color)));
		}
		return result;
	}

	private static boolean shouldHideGlitchChar(String text, int index, int time) {
		Random random = new Random(0xBADC0DE ^ text.hashCode() ^ (index * 53) ^ (time * 17L));
		return random.nextInt(10) == 0;
	}

	private static int randomGlitchColor(String text, int index, int time) {
		Random random = new Random(0xC0FFEE ^ text.hashCode() ^ (index * 31) ^ time);
		int mix = random.nextInt(100);
		if (mix < 45) {
			int gray = 80 + random.nextInt(70);
			return (gray << 16) | (gray << 8) | gray;
		}
		int r = 35 + random.nextInt(95);
		int g = 70 + random.nextInt(130);
		int b = 90 + random.nextInt(150);
		int dominance = random.nextInt(3);
		if (dominance == 0) {
			r += 20;
		} else if (dominance == 1) {
			g += 45;
		} else {
			b += 55;
		}
		return (r << 16) | (g << 8) | b;
	}
	private record DefinitionRule(int priority, Predicate<ItemStack> matcher,
								  java.util.function.Function<ItemStack, Integer> valueProvider,
								  java.util.function.BiFunction<ItemStack, Integer, DefinitionCard> cardFactory) {

		boolean matches(ItemStack stack) {
			return matcher.test(stack);
		}

		DefinitionCard createCard(ItemStack stack) {
			return cardFactory.apply(stack, valueProvider.apply(stack));
		}
	}

	public record DefinitionCard(int priority, DefinitionText title, List<DefinitionText> lines) {
		int width(Font font) {
			int width = font.width(title.render());
			for (DefinitionText line : lines) {
				width = Math.max(width, font.width(line.render()));
			}
			return width;
		}

		int height() {
			return PADDING_Y * 2 + LINE_HEIGHT * (1 + lines.size());
		}

		void draw(Font font, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource, int left, int top) {
			Component renderedTitle = title.render();
			float innerWidth = width(font);
			if (renderedTitle.getStyle().getColor() == null) {
				renderedTitle = renderedTitle.copy().setStyle(renderedTitle.getStyle().withColor(0xFFFFFFFF));
			}
			float titleX = left + PADDING_X + (innerWidth - font.width(renderedTitle)) / 2.0F;
			float titleY = top + PADDING_Y + 1;
			font.drawInBatch(renderedTitle, titleX, titleY, 0xFFFFFFFF, false, matrix, bufferSource, Font.DisplayMode.SEE_THROUGH, 0,
					LightTexture.lightCoordsWithEmission(15728880, 2));
			int bodyHeight = lines.size() * LINE_HEIGHT;
			int bodyAreaTop = top + PADDING_Y + LINE_HEIGHT + TITLE_BODY_GAP;
			int bodyAreaHeight = height() - (PADDING_Y * 2) - LINE_HEIGHT - TITLE_BODY_GAP;
			int bodyStartY = bodyAreaTop + ((bodyAreaHeight - bodyHeight) / 2);
			for (int i = 0; i < lines.size(); i++) {
				Component line = lines.get(i).render();
				if (line.getStyle().getColor() == null) {
					line = line.copy().setStyle(line.getStyle().withColor(0xFFFFFFFF));
				}
				float lineX = left + PADDING_X + (innerWidth - font.width(line)) / 2.0F;
				float lineY = bodyStartY + (i * LINE_HEIGHT) + 1;
				font.drawInBatch(line, lineX, lineY, 0xFFFFFFFF, false, matrix, bufferSource, Font.DisplayMode.SEE_THROUGH, 0,
						LightTexture.lightCoordsWithEmission(15728880, 2));
			}
		}
	}

	private sealed interface DefinitionText permits StaticDefinitionText, AnimatedGlitchDefinitionText {
		Component render();

		static DefinitionText staticText(String text, Style style) {
			return new StaticDefinitionText(Component.literal(text).setStyle(style));
		}

		static DefinitionText animatedGlitch(String text) {
			return new AnimatedGlitchDefinitionText(text);
		}

		static DefinitionText fromComponent(Component component) {
			return new StaticDefinitionText(component);
		}
	}


	private record StaticDefinitionText(Component component) implements DefinitionText {
		@Override
		public Component render() {
			return component;
		}
	}

	private record AnimatedGlitchDefinitionText(String text) implements DefinitionText {
		@Override
		public Component render() {
			return glitchText(text);
		}
	}
}
