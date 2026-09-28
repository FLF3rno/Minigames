package net.mcreator.minigames.potion;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.MinigamesMod;

public class SlowdownMobEffect extends MobEffect {
	public SlowdownMobEffect() {
		super(MobEffectCategory.HARMFUL, -20480);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.slowdown_0"), -0.001, AttributeModifier.Operation.ADD_VALUE);
		this.addAttributeModifier(Attributes.GRAVITY, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.slowdown_1"), -0.0008, AttributeModifier.Operation.ADD_VALUE);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.slowdown_2"), -0.04, AttributeModifier.Operation.ADD_VALUE);
	}
}