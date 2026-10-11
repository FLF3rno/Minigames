package net.mcreator.minigames.potion;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;

import net.mcreator.minigames.procedures.StunnedOnEffectActiveTickProcedure;
import net.mcreator.minigames.procedures.GoldenEffectStartedappliedProcedure;
import net.mcreator.minigames.MinigamesMod;

public class ObsidianificationMobEffect extends MobEffect {
	public ObsidianificationMobEffect() {
		super(MobEffectCategory.NEUTRAL, -16777216);
		this.addAttributeModifier(Attributes.ARMOR, Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "effect.obsidianification_0"), 100, AttributeModifier.Operation.ADD_VALUE);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		GoldenEffectStartedappliedProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
		StunnedOnEffectActiveTickProcedure.execute(entity);
		return super.applyEffectTick(level, entity, amplifier);
	}
}