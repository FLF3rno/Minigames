package net.mcreator.minigames.potion;

import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.minigames.procedures.HypnotizedEffectStartedProcedure;
import net.mcreator.minigames.init.MinigamesModMobEffects;

@EventBusSubscriber
public class HypnotizedMobEffect extends MobEffect {
	public HypnotizedMobEffect() {
		super(MobEffectCategory.NEUTRAL, -16777216);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		HypnotizedEffectStartedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ());
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
		}, MinigamesModMobEffects.HYPNOTIZED.get());
	}
}