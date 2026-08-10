package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

import java.util.ArrayList;

public class BoxOfFunRightclickedProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (entity instanceof Player _player)
			_player.getCooldowns().addCooldown(itemstack, (int) GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		for (Entity entityiterator : new ArrayList<>(world.players())) {
			ApplyEffectProcedure.execute(entityiterator, false,
					Mth.nextInt(RandomSource.create(), (int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"), (int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency_2")),
					Mth.nextInt(RandomSource.create(), (int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), (int) GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length_2")), RandomPotionProcedure.execute());
		}
	}
}