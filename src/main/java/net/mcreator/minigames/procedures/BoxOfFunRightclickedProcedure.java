package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModParticleTypes;

import java.util.ArrayList;

public class BoxOfFunRightclickedProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			ApplyEffectProcedure.execute(world, entityiterator, false,
					Mth.nextInt(RandomSource.create(), (int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"), (int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency_2")),
					Mth.nextInt(RandomSource.create(), (int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), (int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length_2")), RandomPotionProcedure.execute());
			if (world instanceof ServerLevel _level) {
				_level.sendParticles(MinigamesModParticleTypes.CONFETTI.get(), entityiterator.getX(), entityiterator.getY() + 0.5, entityiterator.getZ(), 80, 0.45, 0.7, 0.45, 0.4);
			}
			if (world.isClientSide()) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:party_explode")), SoundSource.PLAYERS, 1, 1);
					} else {
						_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:party_explode")), SoundSource.PLAYERS, 1, 1, false);
					}
				}
			}
		}
	}
}