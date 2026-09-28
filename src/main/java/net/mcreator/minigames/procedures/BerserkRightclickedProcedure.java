package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.CustomFireHelper;

public class BerserkRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity) {
			AttributeModifier modifier = new AttributeModifier(Identifier.parse("minigames:berserkatkspeed"), (GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency") / 100), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
			if (!_entity.getAttribute(Attributes.ATTACK_SPEED).hasModifier(modifier.id())) {
				_entity.getAttribute(Attributes.ATTACK_SPEED).addPermanentModifier(modifier);
			}
		}
		if (entity instanceof LivingEntity _entity) {
			AttributeModifier modifier = new AttributeModifier(Identifier.parse("minigames:berserkatkspeed"), (GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency_2") / 100), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
			if (!_entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(modifier.id())) {
				_entity.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(modifier);
			}
		}
		ApplyEffectProcedure.execute(world, entity, false, GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage"), GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), "minigames:lifesteal");
		SetOnFireProcedure.execute(world, entity, entity, 0, GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), "red");
		MinigamesMod.queueServerWork((int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), () -> {
			if (entity instanceof LivingEntity _entity) {
				_entity.getAttribute(Attributes.ATTACK_SPEED).removeModifier(Identifier.parse("minigames:berserkatkspeed"));
			}
			if (entity instanceof LivingEntity _entity) {
				_entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(Identifier.parse("minigames:berserkatkspeed"));
			}
			CustomFireHelper.clearFire(entity);
		});
		ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.blaze.shoot")), SoundSource.PLAYERS, 1, (float) 0.6);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.blaze.shoot")), SoundSource.PLAYERS, 1, (float) 0.6, false);
			}
		}
	}
}