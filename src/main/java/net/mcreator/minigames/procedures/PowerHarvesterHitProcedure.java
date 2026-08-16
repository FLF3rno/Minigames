package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModMobEffects;

public class PowerHarvesterHitProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, ItemStack itemstack) {
		if (entity == null || sourceentity == null)
			return;
		if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.STUNNED)) {
			if (sourceentity instanceof LivingEntity _livEnt1 && _livEnt1.hasEffect(MinigamesModMobEffects.DAMAGE_BOOST)) {
				ApplyEffectProcedure.execute(world, sourceentity, true,
						GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage")
								+ (sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.DAMAGE_BOOST) ? _livEnt.getEffect(MinigamesModMobEffects.DAMAGE_BOOST).getAmplifier() : 0) + 1,
						GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), "minigames:damage_boost");
			} else {
				ApplyEffectProcedure.execute(world, sourceentity, true, GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage"), GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), "minigames:damage_boost");
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.note_block.guitar")), SoundSource.NEUTRAL, 1,
							(float) (1 + (sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.DAMAGE_BOOST) ? _livEnt.getEffect(MinigamesModMobEffects.DAMAGE_BOOST).getAmplifier() : 0) / 100d));
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.note_block.guitar")), SoundSource.NEUTRAL, 1,
							(float) (1 + (sourceentity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinigamesModMobEffects.DAMAGE_BOOST) ? _livEnt.getEffect(MinigamesModMobEffects.DAMAGE_BOOST).getAmplifier() : 0) / 100d), false);
				}
			}
		}
	}
}