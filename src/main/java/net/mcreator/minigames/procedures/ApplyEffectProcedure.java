package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;

import java.util.ArrayList;

public class ApplyEffectProcedure {
	public static void execute(LevelAccessor world, Entity target, boolean hide, double level, double ticks, String effect) {
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
		if (CheckRelicProcedure.execute(target, new ItemStack(MinigamesModItems.WIRELESS_CAULDRON.get()))) {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				if (!(entityiterator == target)) {
					ApplyEffectProcedure.execute(world, entityiterator, hide, level, GetItemAttributeProcedure.execute(new ItemStack(MinigamesModItems.WIRELESS_CAULDRON.get()), "minigames:effect_length") / 20, effect);
				}
				if (world.isClientSide()) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.brewing_stand.brew")), SoundSource.PLAYERS,
									(float) 0.7, 2);
						} else {
							_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.brewing_stand.brew")), SoundSource.PLAYERS, (float) 0.7, 2,
									false);
						}
					}
				}
			}
		}
	}
}