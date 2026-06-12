package net.mcreator.minigames.client;

import org.joml.Matrix4f;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.opengl.GlStateManager;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.neoforged.neoforge.client.event.InputEvent;
import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.network.DungeonItemPickupMessage;
import net.mcreator.minigames.network.MinigamesModVariables;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(Dist.CLIENT)
public class HoveredItemTooltipRenderer {
	private static final double MAX_DISTANCE = 8.0;
	private static final double MAX_DISTANCE_SQR = MAX_DISTANCE * MAX_DISTANCE;
	private static final float BASE_TEXT_SCALE = 0.02F;
	private static final int LINE_HEIGHT = 10;
	private static final int TEXT_COLOR = 0xFFFFFFFF;
	private static final float RIGHT_OFFSET = 30.0F;
	private static final int PADDING_X = 6;
	private static final int PADDING_Y = 6;
	private static final int PANEL_BACKGROUND = 0xF0100010;
	private static final int PANEL_BORDER_LIGHT = 0xA05000FF;
	private static final int PANEL_BORDER_DARK = 0xA028007F;
	private static final int WARRIOR_COLOR = 0xFF5555;
	private static final int THIEF_COLOR = 0xFFAA00;
	private static final int SUPPORT_COLOR = 0x55FFFF;
	private static final int MAGE_COLOR = 0xFF55FF;
	private static final int DAMAGE_COLOR = 0x55FF55;
	private static final int EXPLOSION_DAMAGE_COLOR = 0xFFAA33;
	private static final int SPEED_COLOR = 0x55FFFF;
	private static final int RELIC_GRADIENT_START = 0xA020F0;
	private static final int RELIC_GRADIENT_END = 0xC77DFF;
	private static final int CURSED_GRADIENT_START = 0xFF6666;
	private static final int CURSED_GRADIENT_END = 0x7A0000;
	private static ItemEntity currentTargetItem = null;

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		ClassInfo classInfo = getClassInfo(stack);
		if (classInfo != null) {
			applyDungeonTooltip(event.getToolTip(), stack, classInfo);
		}
	}

	private static void applyDungeonTooltip(List<Component> tooltip, ItemStack stack, ClassInfo classInfo) {
		removeVanillaAttributeLines(tooltip);
		int nextIndex = 0;
		if (!tooltip.isEmpty()) {
			tooltip.set(0, tooltip.get(0).copy().setStyle(tooltip.get(0).getStyle().withColor(classInfo.color())));
			tooltip.add(1, Component.empty());
			nextIndex = 2;
		}
		Double damage = getDisplayedDamage(stack);
		if (damage != null && damage != 0) {
			tooltip.add(nextIndex++, Component.literal(formatDamage(damage) + " Damage").setStyle(Style.EMPTY.withColor(DAMAGE_COLOR)));
		}
		Double explosionDamage = getDisplayedExplosionDamage(stack);
		if (explosionDamage != null && explosionDamage != 0) {
			tooltip.add(nextIndex++, Component.literal(formatDamage(explosionDamage) + " Explosion Damage").setStyle(Style.EMPTY.withColor(EXPLOSION_DAMAGE_COLOR)));
		}
		Double speed = getDisplayedAttackSpeed(stack);
		if (speed != null) {
			double adjustedSpeed = speed - 1.6;
			if (adjustedSpeed != 0) {
				int speedColor = adjustedSpeed < 0 ? 0xFF5555 : SPEED_COLOR;
				String prefix = adjustedSpeed > 0 ? "+" : "";
				tooltip.add(nextIndex++, Component.literal(prefix + formatDamage(adjustedSpeed) + " Attack Speed").setStyle(Style.EMPTY.withColor(speedColor)));
			}
		}
		tooltip.add(Component.empty());
		tooltip.add(Component.literal(classInfo.label()).setStyle(Style.EMPTY.withBold(true).withColor(classInfo.color())));
		if (isCursed(stack)) {
			tooltip.add(gradientText("CURSED", CURSED_GRADIENT_START, CURSED_GRADIENT_END, true));
		}
		if (isGlitched(stack)) {
			tooltip.add(glitchedText("GLITCHED"));
		}
		if (DungeonItemAccess.isStolen(stack)) {
			tooltip.add(Component.literal("STOLEN").setStyle(Style.EMPTY.withBold(true).withColor(0xFFAA00)));
		}
		if (DungeonItemAccess.Forged(stack) > 0) {
			tooltip.add(Component.literal(DungeonItemAccess.Forged(stack)+ "% FORGED").setStyle(Style.EMPTY.withBold(true).withColor(0xFA8A05)));
		}
	}

	@SubscribeEvent
	public static void onMouseButton(InputEvent.MouseButton.Post event) {
		if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && event.getAction() == GLFW.GLFW_PRESS && currentTargetItem != null) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.screen == null && mc.player != null) {
				if (!isFullForItem(mc.player, currentTargetItem.getItem())) {
					ClientPacketDistributor.sendToServer(new DungeonItemPickupMessage(currentTargetItem.getId()));
				}
			}
		}
	}

	private static boolean isInventoryFull(net.minecraft.world.entity.player.Player player) {
		double playerSlots = player.getData(MinigamesModVariables.PLAYER_VARIABLES).playerSlots;
		int nSlots = Math.max(0, Math.min(9, (int) playerSlots));
		for (int i = 0; i < nSlots; i++) {
			if (player.getInventory().getItem(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private static boolean areRelicSlotsFull(net.minecraft.world.entity.player.Player player) {
		return !player.getInventory().getItem(34).isEmpty() && !player.getInventory().getItem(35).isEmpty();
	}

	private static boolean isFullForItem(net.minecraft.world.entity.player.Player player, ItemStack stack) {
		return DungeonItemAccess.isRelic(stack) ? areRelicSlotsFull(player) : isInventoryFull(player);
	}

	@SubscribeEvent
	public static void renderTooltip(RenderLevelStageEvent.AfterTranslucentBlocks event) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null || minecraft.level == null) {
			currentTargetItem = null;
			return;
		}

		ItemEntity targetItem = null;
		double bestDistSq = 0.25; // 0.5 block radius (0.5 * 0.5 = 0.25)

		AABB searchBox = minecraft.player.getBoundingBox().inflate(MAX_DISTANCE);
		List<ItemEntity> items = minecraft.level.getEntitiesOfClass(ItemEntity.class, searchBox, entity -> isDungeonTooltipItem(entity.getItem()));

		Vec3 eyePos = minecraft.player.getEyePosition();
		Vec3 lookVec = minecraft.player.getViewVector(1.0F);

		for (ItemEntity itemEntity : items) {
			if (minecraft.player.distanceToSqr(itemEntity) > MAX_DISTANCE_SQR) {
				continue;
			}

			Vec3 itemCenter = itemEntity.getBoundingBox().getCenter();
			Vec3 rel = itemCenter.subtract(eyePos);
			double projectionLength = rel.dot(lookVec);

			if (projectionLength < 0)
				continue; // Entity is behind the player

			Vec3 projectedPoint = lookVec.scale(projectionLength);
			double distSq = rel.subtract(projectedPoint).lengthSqr();

			if (distSq < bestDistSq) {
				// Line of sight check
				net.minecraft.world.phys.BlockHitResult raytrace = minecraft.level.clip(new net.minecraft.world.level.ClipContext(eyePos, itemCenter,
						net.minecraft.world.level.ClipContext.Block.VISUAL, net.minecraft.world.level.ClipContext.Fluid.NONE, minecraft.player));

				if (raytrace.getType() == net.minecraft.world.phys.HitResult.Type.MISS) {
					bestDistSq = distSq;
					targetItem = itemEntity;
				}
			}
		}

		if (targetItem != null) {
			ItemStack stack = targetItem.getItem();
			if (stack.isEmpty()) {
				currentTargetItem = null;
				return;
			}
			List<Component> tooltip = stack.getTooltipLines(Item.TooltipContext.of(minecraft.level), minecraft.player,
					minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
			if (tooltip.isEmpty()) {
				currentTargetItem = null;
				return;
			}
			ClassInfo classInfo = getClassInfo(stack);
			renderTooltipAtItem(event, targetItem, tooltip, minecraft, classInfo);
		}
		currentTargetItem = targetItem;
	}

	private static void renderTooltipAtItem(RenderLevelStageEvent event, ItemEntity itemEntity, List<Component> tooltip, Minecraft minecraft, ClassInfo classInfo) {
		PoseStack poseStack = event.getPoseStack();
		EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
		Font font = minecraft.font;
		Vec3 cameraPosition = dispatcher.camera.getPosition();
		Vec3 itemPosition = itemEntity.getPosition(event.getPartialTick().getGameTimeDeltaPartialTick(false)).add(0.0, itemEntity.getBbHeight() + 0.5, 0.0);

		poseStack.pushPose();
		poseStack.translate(itemPosition.x - cameraPosition.x, itemPosition.y - cameraPosition.y, itemPosition.z - cameraPosition.z);
		poseStack.mulPose(dispatcher.cameraOrientation());
		float tooltipScale = getTooltipScale(minecraft);
		poseStack.scale(tooltipScale, -tooltipScale, tooltipScale);

		Matrix4f matrix = poseStack.last().pose();
		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

		int maxWidth = 0;
		for (Component line : tooltip) {
			maxWidth = Math.max(maxWidth, font.width(styleWorldTooltipLine(line, itemEntity.getItem(), classInfo)));
		}

		Style goldBold = Style.EMPTY.withColor(0xFFAA00).withBold(true);
		Style redBold = Style.EMPTY.withColor(0xFF5555).withBold(true);
		
		boolean canPickUp = canCurrentPlayerPickUp(itemEntity.getItem(), minecraft);
		boolean isRelic = DungeonItemAccess.isRelic(itemEntity.getItem());
		boolean full = isFullForItem(minecraft.player, itemEntity.getItem());
		List<WorldTooltipDefinitions.DefinitionCard> definitions = WorldTooltipDefinitions.getDefinitionCards(itemEntity.getItem());
		
		Style promptStyle = (canPickUp && !full) ? goldBold : redBold;
		String fullPrompt;
		if (!canPickUp) {
			fullPrompt = "WRONG CLASS";
		} else if (full) {
			fullPrompt = isRelic ? "RELIC SLOTS FULL" : "INVENTORY FULL";
		} else {
			fullPrompt = "RIGHT CLICK TO CHOOSE";
		}
		
		int fullPromptWidth = font.width(Component.literal(fullPrompt).setStyle(promptStyle));
		boolean oneLine = fullPromptWidth <= maxWidth;

		if (oneLine) {
			maxWidth = Math.max(maxWidth, fullPromptWidth);
		} else {
			// Recalculate maxWidth for multi-line if needed
			if (!canPickUp) {
				maxWidth = Math.max(maxWidth, font.width(Component.literal("WRONG").setStyle(promptStyle)));
				maxWidth = Math.max(maxWidth, font.width(Component.literal("CLASS").setStyle(promptStyle)));
			} else if (full) {
				if (isRelic) {
					maxWidth = Math.max(maxWidth, font.width(Component.literal("RELIC SLOTS").setStyle(promptStyle)));
					maxWidth = Math.max(maxWidth, font.width(Component.literal("FULL").setStyle(promptStyle)));
				} else {
					maxWidth = Math.max(maxWidth, font.width(Component.literal("INVENTORY").setStyle(promptStyle)));
					maxWidth = Math.max(maxWidth, font.width(Component.literal("FULL").setStyle(promptStyle)));
				}
			} else {
				maxWidth = Math.max(maxWidth, font.width(Component.literal("RIGHT CLICK").setStyle(promptStyle)));
				maxWidth = Math.max(maxWidth, font.width(Component.literal("TO CHOOSE").setStyle(promptStyle)));
			}
		}
		maxWidth += 2; 

		int panel1Height = getContentHeight(tooltip.size()) + PADDING_Y * 2;
		int panel2Height = (oneLine ? LINE_HEIGHT : LINE_HEIGHT * 2) + PADDING_Y * 2;
		int gap = 4;
		int definitionsHeight = WorldTooltipDefinitions.getDefinitionsHeight(definitions);
		float totalHeight = Math.max(panel1Height + gap + panel2Height, definitionsHeight);
		int baseTop = Math.round((totalHeight - (panel1Height + gap + panel2Height)) / 2.0F);

		poseStack.translate(RIGHT_OFFSET, -(totalHeight / 2.0F), 0.0F);
		matrix = poseStack.last().pose();

		GlStateManager._disableDepthTest();

		renderTooltipPanel(matrix, bufferSource, 0, baseTop, maxWidth + PADDING_X * 2, baseTop + panel1Height);
		int secondPanelTop = baseTop + panel1Height + gap;
		renderTooltipPanel(matrix, bufferSource, 0, secondPanelTop, maxWidth + PADDING_X * 2, secondPanelTop + panel2Height);

		poseStack.translate(0.0F, 0.0F, 0.01F);
		matrix = poseStack.last().pose();

		for (int i = 0; i < tooltip.size(); i++) {
			Component line = styleWorldTooltipLine(tooltip.get(i), itemEntity.getItem(), classInfo);
			float lineX = PADDING_X + 1;
			float lineY = PADDING_Y + getLineY(i) + 1;
			font.drawInBatch(line, lineX, lineY, TEXT_COLOR, false, matrix, bufferSource, Font.DisplayMode.SEE_THROUGH, 0,
					LightTexture.lightCoordsWithEmission(15728880, 2));
		}

		if (!definitions.isEmpty()) {
			int definitionLeft = maxWidth + PADDING_X * 2 + 8;
			int definitionTop = baseTop;
			WorldTooltipDefinitions.renderDefinitions(matrix, bufferSource, font, definitions, definitionLeft, definitionTop);
		}

		float fullWidth = maxWidth + PADDING_X * 2;
		if (oneLine) {
			Component prompt = (!canPickUp || full || !isRelic) ? Component.literal(fullPrompt).setStyle(promptStyle) : createRelicGradientText(fullPrompt);
			float promptX = (fullWidth - font.width(prompt)) / 2.0f;
			float promptY = secondPanelTop + PADDING_Y + 1;
			font.drawInBatch(prompt, promptX, promptY, 0xFFFFFFFF, false, matrix, bufferSource, Font.DisplayMode.SEE_THROUGH, 0,
					LightTexture.lightCoordsWithEmission(15728880, 2));
		} else {
			Component line1, line2;
			if (!canPickUp) {
				line1 = Component.literal("WRONG").setStyle(promptStyle);
				line2 = Component.literal("CLASS").setStyle(promptStyle);
			} else if (full) {
				line1 = Component.literal(isRelic ? "RELIC SLOTS" : "INVENTORY").setStyle(promptStyle);
				line2 = Component.literal("FULL").setStyle(promptStyle);
			} else {
				if (isRelic) {
					line1 = createRelicGradientText("RIGHT CLICK");
					line2 = createRelicGradientText("TO CHOOSE");
				} else {
					line1 = Component.literal("RIGHT CLICK").setStyle(promptStyle);
					line2 = Component.literal("TO CHOOSE").setStyle(promptStyle);
				}
			}
			float line1X = (fullWidth - font.width(line1)) / 2.0f;
			float line2X = (fullWidth - font.width(line2)) / 2.0f;
			float line1Y = secondPanelTop + PADDING_Y + 1;
			float line2Y = secondPanelTop + PADDING_Y + LINE_HEIGHT + 1;
			font.drawInBatch(line1, line1X, line1Y, 0xFFFFFFFF, false, matrix, bufferSource, Font.DisplayMode.SEE_THROUGH, 0,
					LightTexture.lightCoordsWithEmission(15728880, 2));
			font.drawInBatch(line2, line2X, line2Y, 0xFFFFFFFF, false, matrix, bufferSource, Font.DisplayMode.SEE_THROUGH, 0,
					LightTexture.lightCoordsWithEmission(15728880, 2));
		}

		bufferSource.endBatch();
		GlStateManager._enableDepthTest();
		poseStack.popPose();
	}

	private static void renderTooltipPanel(Matrix4f matrix, MultiBufferSource.BufferSource bufferSource, int left, int top, int right, int bottom) {
		var background = bufferSource.getBuffer(net.minecraft.client.renderer.RenderType.textBackgroundSeeThrough());
		addQuad(background, matrix, left, top, right, bottom, PANEL_BACKGROUND);

		var border = bufferSource.getBuffer(net.minecraft.client.renderer.RenderType.textBackgroundSeeThrough());
		addQuad(border, matrix, left - 1, top - 1, right + 1, top, PANEL_BORDER_LIGHT);
		addQuad(border, matrix, left - 1, bottom, right + 1, bottom + 1, PANEL_BORDER_DARK);
		addQuad(border, matrix, left - 1, top, left, bottom, PANEL_BORDER_LIGHT);
		addQuad(border, matrix, right, top, right + 1, bottom, PANEL_BORDER_DARK);
	}

	private static void addQuad(com.mojang.blaze3d.vertex.VertexConsumer consumer, Matrix4f matrix, int left, int top, int right, int bottom, int color) {
		consumer.addVertex(matrix, left, top, 0.0F).setColor(color).setLight(15728880);
		consumer.addVertex(matrix, left, bottom, 0.0F).setColor(color).setLight(15728880);
		consumer.addVertex(matrix, right, bottom, 0.0F).setColor(color).setLight(15728880);
		consumer.addVertex(matrix, right, top, 0.0F).setColor(color).setLight(15728880);
	}

	private static int getContentHeight(int lineCount) {
		return lineCount * LINE_HEIGHT;
	}

	private static int getLineY(int lineIndex) {
		return lineIndex * LINE_HEIGHT;
	}

	private static boolean isDungeonTooltipItem(ItemStack stack) {
		return DungeonItemAccess.isDungeonItem(stack);
	}

	private static ClassInfo getClassInfo(ItemStack stack) {
		if (stack.is(DungeonItemAccess.DUNGEON_WARRIOR)) {
			return new ClassInfo("WARRIOR ONLY", WARRIOR_COLOR);
		}
		if (stack.is(DungeonItemAccess.DUNGEON_THIEF)) {
			return new ClassInfo("THIEF ONLY", THIEF_COLOR);
		}
		if (stack.is(DungeonItemAccess.DUNGEON_SUPPORT)) {
			return new ClassInfo("SUPPORT ONLY", SUPPORT_COLOR);
		}
		if (stack.is(DungeonItemAccess.DUNGEON_MAGE)) {
			return new ClassInfo("MAGE ONLY", MAGE_COLOR);
		}
		return null;
	}

	private static boolean canCurrentPlayerPickUp(ItemStack stack, Minecraft minecraft) {
		if (minecraft.player == null) {
			return false;
		}
		String classDungeon = minecraft.player.getData(MinigamesModVariables.PLAYER_VARIABLES).classDungeon;
		return DungeonItemAccess.canClassPickUp(stack, classDungeon);
	}

	private static float getTooltipScale(Minecraft minecraft) {
		if (minecraft.player == null) {
			return BASE_TEXT_SCALE * 0.8F;
		}
		double tooltipSize = minecraft.player.getData(MinigamesModVariables.PLAYER_VARIABLES).tooltipSize;
		if (tooltipSize <= 0.0) {
			tooltipSize = 0.8;
		}
		return (float) (BASE_TEXT_SCALE * tooltipSize);
	}

	private static void removeVanillaAttributeLines(List<Component> tooltip) {
		tooltip.removeIf(line -> {
			String text = line.getString().trim().toLowerCase(Locale.ROOT);
			return hasTranslatableKey(line, "item.modifiers.") || hasTranslatableKey(line, "attribute.modifier.")
				|| text.startsWith("when in ") || text.startsWith("when equipped") || text.startsWith("+") || text.startsWith("-")
				|| text.endsWith(" attack damage") || text.endsWith(" attack speed")
				|| text.endsWith(" salvage value") || text.endsWith(" coins on kill");
		});
	}

	private static boolean hasTranslatableKey(Component component, String prefix) {
		if (component.getContents() instanceof TranslatableContents translatable && translatable.getKey().startsWith(prefix)) {
			return true;
		}
		for (Component sibling : component.getSiblings()) {
			if (hasTranslatableKey(sibling, prefix)) {
				return true;
			}
		}
		return false;
	}

	private static Double getDisplayedDamage(ItemStack stack) {
		ItemAttributeModifiers modifiers = stack.getOrDefault(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		double addValue = 0.0;
		double addMultipliedBase = 0.0;
		double addMultipliedTotal = 0.0;
		boolean hasDamage = false;

		for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
			if (entry.slot().test(net.minecraft.world.entity.EquipmentSlot.MAINHAND) && entry.attribute().is(Attributes.ATTACK_DAMAGE)) {
				hasDamage = true;
				double amount = entry.modifier().amount();
				switch (entry.modifier().operation()) {
					case ADD_VALUE -> addValue += amount;
					case ADD_MULTIPLIED_BASE -> addMultipliedBase += amount;
					case ADD_MULTIPLIED_TOTAL -> addMultipliedTotal += amount;
				}
			}
		}

		if (!hasDamage) {
			return null;
		}

		double damage = 1.0 + addValue;
		damage += damage * addMultipliedBase;
		damage *= 1.0 + addMultipliedTotal;
		return damage;
	}

	private static Double getDisplayedAttackSpeed(ItemStack stack) {
		ItemAttributeModifiers modifiers = stack.getOrDefault(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		double addValue = 0.0;
		double addMultipliedBase = 0.0;
		double addMultipliedTotal = 0.0;
		boolean hasSpeed = false;

		for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
			if (entry.slot().test(net.minecraft.world.entity.EquipmentSlot.MAINHAND) && entry.attribute().is(Attributes.ATTACK_SPEED)) {
				hasSpeed = true;
				double amount = entry.modifier().amount();
				switch (entry.modifier().operation()) {
					case ADD_VALUE -> addValue += amount;
					case ADD_MULTIPLIED_BASE -> addMultipliedBase += amount;
					case ADD_MULTIPLIED_TOTAL -> addMultipliedTotal += amount;
				}
			}
		}

		if (!hasSpeed) {
			return null;
		}

		double speed = 4.0 + addValue;
		speed += speed * addMultipliedBase;
		speed *= 1.0 + addMultipliedTotal;
		return speed;
	}

	private static Double getDisplayedExplosionDamage(ItemStack stack) {
		ItemAttributeModifiers modifiers = stack.getOrDefault(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
		double addValue = 0.0;
		double addMultipliedBase = 0.0;
		double addMultipliedTotal = 0.0;
		boolean hasExplosionDamage = false;

		for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
			if (entry.slot().test(net.minecraft.world.entity.EquipmentSlot.MAINHAND) && entry.attribute().is(MinigamesModAttributes.EXPLOSION_DAMAGE)) {
				hasExplosionDamage = true;
				double amount = entry.modifier().amount();
				switch (entry.modifier().operation()) {
					case ADD_VALUE -> addValue += amount;
					case ADD_MULTIPLIED_BASE -> addMultipliedBase += amount;
					case ADD_MULTIPLIED_TOTAL -> addMultipliedTotal += amount;
				}
			}
		}

		if (!hasExplosionDamage) {
			return null;
		}

		double explosionDamage = addValue;
		explosionDamage += explosionDamage * addMultipliedBase;
		explosionDamage *= 1.0 + addMultipliedTotal;
		return explosionDamage;
	}

	private static String formatDamage(double damage) {
		if (damage == Math.rint(damage)) {
			return Integer.toString((int) damage);
		}
		return String.format(Locale.ROOT, "%.2f", damage).replaceAll("0+$", "").replaceAll("\\.$", "");
	}

	private static Component createRelicGradientText(String text) {
		Component result = Component.empty();
		int len = text.length();
		for (int i = 0; i < len; i++) {
			float t = len <= 1 ? 0f : (float) i / (float) (len - 1);
			int color = lerpColor(RELIC_GRADIENT_START, RELIC_GRADIENT_END, t);
			result = result.copy().append(Component.literal(String.valueOf(text.charAt(i))).setStyle(Style.EMPTY.withBold(true).withColor(color)));
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

	private record ClassInfo(String label, int color) {
	}

	private static boolean isCursed(ItemStack stack) {
		return stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("cursed", false);
	}

	private static boolean isGlitched(ItemStack stack) {
		return stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("glitched", false);
	}

	private static MutableComponent gradientText(String text, int startColor, int endColor, boolean bold) {
		MutableComponent result = Component.empty();
		int length = text.length();
		for (int i = 0; i < length; i++) {
			float t = length <= 1 ? 0.0f : (float) i / (float) (length - 1);
			int color = lerpColor(startColor, endColor, t);
			result.append(Component.literal(String.valueOf(text.charAt(i))).setStyle(Style.EMPTY.withBold(bold).withColor(color)));
		}
		return result;
	}

	private static MutableComponent glitchedText(String text) {
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

	private static Component styleWorldTooltipLine(Component line, ItemStack stack, ClassInfo classInfo) {
		String normalized = line.getString().replace(" ", "").trim().toUpperCase(Locale.ROOT);
		if (classInfo != null && normalized.equals(classInfo.label().replace(" ", "").toUpperCase(Locale.ROOT))) {
			return Component.literal(classInfo.label()).setStyle(Style.EMPTY.withBold(true).withColor(classInfo.color()));
		}
		if (normalized.equals("CURSED")) {
			return gradientText("CURSED", CURSED_GRADIENT_START, CURSED_GRADIENT_END, true);
		}
		if (normalized.equals("GLITCHED")) {
			return glitchedText("GLITCHED");
		}
		if (normalized.equals("STOLEN")) {
			return Component.literal("STOLEN").setStyle(Style.EMPTY.withBold(true).withColor(0xFFAA00));
		}
		if (normalized.equals("FORGED")) {
			return Component.literal(DungeonItemAccess.Forged(stack) + "%FORGED").setStyle(Style.EMPTY.withBold(true).withColor(0xFA8A05));
		}
		return line;
	}

}
