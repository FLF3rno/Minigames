package net.mcreator.minigames.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.init.MinigamesModMobEffects;

public class ApplyEffectProcedure {
	public static void execute(Entity target, boolean hide, double level, double ticks, String effect) {
		if (target == null || effect == null)
			return;
		if ((effect).equals("minigames:stunned")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.STUNNED, (int) ticks, (int) (level - 1), (!hide), false));
		} else if ((effect).equals("minigames:blessed")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.BLESSED, (int) ticks, (int) (level - 1), (!hide), false));
		} else if ((effect).equals("minigames:phantom")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.PHANTOM, (int) ticks, (int) (level - 1), (!hide), false));
		} else if ((effect).equals("minecraft:strength")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.STRENGTH, (int) ticks, (int) (level - 1), (!hide), false));
		} else if ((effect).equals("minigames:extra_damage") || (effect).equals("minigames:damage_boost")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.DAMAGE_BOOST, (int) ticks, (int) (level - 1), (!hide), false));
		} else if ((effect).equals("minigames:bleed")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.BLEED, (int) ticks, (int) (level - 1), (!hide), false));
		} else if ((effect).equals("minigames:decay")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.DECAY, (int) ticks, (int) (level - 1), (!hide), false));
		} else if ((effect).equals("minecraft:luck") || (effect).equals("minigames:luck")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.LUCK, (int) ticks, (int) (level - 1), (!hide), false));
		} else if ((effect).equals("minecraft:unluck") || (effect).equals("minigames:unluck")) {
			if (target instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.UNLUCK, (int) ticks, (int) (level - 1), (!hide), false));
		} else {
			{
				Entity _ent = target;
				if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
					_ent.level().getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, LevelBasedPermissionSet.OWNER, _ent.getName().getString(),
									_ent.getDisplayName(), _ent.level().getServer(), _ent),
							("effect give @s " + effect + " " + new java.text.DecimalFormat("##").format(ticks / 20) + " " + new java.text.DecimalFormat("##").format(level - 1) + " " + hide));
				}
			}
		}
	}
}