package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.server.level.ServerPlayer;

public class HammerHitProcedure {
	public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		double amp = 0;
		if (!(entity instanceof Player || entity instanceof ServerPlayer)) {
			if (entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(MobEffects.STRENGTH)) {
				amp = (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.STRENGTH) ? _livEnt.getEffect(MobEffects.STRENGTH).getAmplifier() : 0) + 2;
			} else {
				amp = 1;
			}
			ApplyEffectProcedure.execute(world, entity, false, amp, GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"), "minecraft:strength");
		}
	}
}