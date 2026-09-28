package net.mcreator.minigames.potion;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.MinigamesMod;

public class SpeedBoostMobEffect extends MobEffect {
	public SpeedBoostMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -14907461);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.speed_boost_0"), 0.001, AttributeModifier.Operation.ADD_VALUE);
	}
}