package net.mcreator.minigames.potion;

import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.MinigamesMod;

@EventBusSubscriber
public class ImmobilizedMobEffect extends MobEffect {
	public ImmobilizedMobEffect() {
		super(MobEffectCategory.NEUTRAL, -16777209);
		this.addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.immobilized_0"), -10, AttributeModifier.Operation.ADD_VALUE);
		this.addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.immobilized_1"), -10, AttributeModifier.Operation.ADD_VALUE);
		this.addAttributeModifier(Attributes.FLYING_SPEED, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.immobilized_2"), -10, AttributeModifier.Operation.ADD_VALUE);
		this.addAttributeModifier(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.immobilized_3"), 10, AttributeModifier.Operation.ADD_VALUE);
		this.addAttributeModifier(Attributes.JUMP_STRENGTH, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.immobilized_4"), -1000, AttributeModifier.Operation.ADD_VALUE);
		this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.immobilized_5"), 1000, AttributeModifier.Operation.ADD_VALUE);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.immobilized_6"), -1000, AttributeModifier.Operation.ADD_VALUE);
	}

	@SubscribeEvent
	public static void registerMobEffectExtensions(RegisterClientExtensionsEvent event) {
		event.registerMobEffect(new IClientMobEffectExtensions() {
			@Override
			public boolean isVisibleInInventory(MobEffectInstance effect) {
				return false;
			}

			@Override
			public boolean renderInventoryText(MobEffectInstance instance, AbstractContainerScreen<?> screen, GuiGraphicsExtractor guiGraphics, int x, int y, int blitOffset) {
				return false;
			}

			@Override
			public boolean isVisibleInGui(MobEffectInstance effect) {
				return false;
			}
		}, MinigamesModMobEffects.IMMOBILIZED.get());
	}
}