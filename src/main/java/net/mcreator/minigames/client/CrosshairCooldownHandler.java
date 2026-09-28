package net.mcreator.minigames.client;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import net.mcreator.minigames.DungeonItemAccess;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.init.MinigamesModItems;
import net.mcreator.minigames.procedures.CheckRelicProcedure;
import net.mcreator.minigames.item.TheFinisherItem;
import net.mcreator.minigames.network.MinigamesModVariables;

@EventBusSubscriber(value = Dist.CLIENT)
public class CrosshairCooldownHandler {
	private static final Identifier CROSSHAIR_SPRITE = Identifier.withDefaultNamespace("hud/crosshair");
	private static final Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_background");
	private static final Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_progress");

	private static final TagKey<Item> WEAPONTYPE_SWORD = TagKey.create(Registries.ITEM, Identifier.parse("minigames:weapontype_sword"));
	private static final TagKey<Item> DUNGEON_TYPE_WEAPON = TagKey.create(Registries.ITEM, Identifier.parse("minigames:dungeon_type_weapon"));

	@SubscribeEvent
	public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
		if (!event.getName().equals(VanillaGuiLayers.CROSSHAIR)) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.options.hideGui || !mc.options.getCameraType().isFirstPerson()) {
			return;
		}

		Player player = mc.player;
		if (player == null || player.isSpectator() || player.level() == null) {
			return;
		}

		if (!MinigamesModVariables.MapVariables.get(player.level()).playingDungeons) {
			return;
		}

		if (mc.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR)) {
			return;
		}

		ItemStack stack = player.getMainHandItem();
		boolean isBoxing = net.mcreator.minigames.procedures.BoxingMasteryHandler.isBoxing(player);
		if ((!isSword(stack) && !isBoxing) || !isCooldown(player, stack, isBoxing)) {
			return;
		}

		event.setCanceled(true);

		GuiGraphicsExtractor guiGraphics = event.getGuiGraphics();
		guiGraphics.nextStratum();

		int x = (guiGraphics.guiWidth() - 15) / 2;
		int y = (guiGraphics.guiHeight() - 15) / 2;

		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, CROSSHAIR_SPRITE, x, y, 15, 15, 0xFFFF2020);

		float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
		float progress = player.getAttackStrengthScale(0.0F);
		if (player.getCooldowns().isOnCooldown(stack)) {
			float cdPercent = player.getCooldowns().getCooldownPercent(stack, partialTick);
			progress = Math.min(progress, 1.0F - cdPercent);
		}
		progress = Math.max(0.0F, Math.min(progress, 1.0F));

		if (progress < 1.0F) {
			int j = guiGraphics.guiHeight() / 2 - 7 + 16;
			int k = guiGraphics.guiWidth() / 2 - 8;
			guiGraphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE, k, j, 16, 4);
			int l = (int) (progress * 17.0F);
			if (l > 0) {
				guiGraphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE, 16, 4, 0, 0, k, j, Math.min(l, 16), 4);
			}
		}
	}

	private static boolean isSword(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		if (stack.is(ItemTags.SWORDS)
				|| stack.is(WEAPONTYPE_SWORD)
				|| stack.is(DUNGEON_TYPE_WEAPON)
				|| DungeonItemAccess.isDungeonWeapon(stack)
				|| stack.getItem() instanceof TheFinisherItem) {
			return true;
		}
		return getAttackCooldownAttribute(stack) > 0;
	}

	private static boolean isCooldown(Player player, ItemStack stack, boolean isBoxing) {
		if (isBoxing) {
			return player.getAttackStrengthScale(0.0F) < 1.0F;
		}
		if (player.getCooldowns().isOnCooldown(stack)) {
			return true;
		}
		return player.getAttackStrengthScale(0.0F) < 1.0F;
	}

	private static double getAttackCooldownAttribute(ItemStack stack) {
		ItemAttributeModifiers modifiers = stack.getOrDefault(
				DataComponents.ATTRIBUTE_MODIFIERS,
				ItemAttributeModifiers.EMPTY
		);

		double total = 0.0;
		for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
			if (entry.slot() != EquipmentSlotGroup.MAINHAND) {
				continue;
			}
			if (entry.attribute().is(MinigamesModAttributes.ATTACK_COOLDOWN)) {
				AttributeModifier modifier = entry.modifier();
				total += modifier.amount();
			}
		}

		return total;
	}
}
