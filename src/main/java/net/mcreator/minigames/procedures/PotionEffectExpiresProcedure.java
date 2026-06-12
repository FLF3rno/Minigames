package net.mcreator.minigames.procedures;

import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModItems;

import java.util.ArrayList;

public class PotionEffectExpiresProcedure {
	public static void execute(LevelAccessor world, Entity target, double level, String potion) {
		if (target == null || potion == null)
			return;
		String Potion = "";
		double Level = 0;
		Entity Target = null;
		Potion = potion;
		Target = target;
		Level = level - 1;
		if (CheckRelicProcedure.execute(target, new ItemStack(MinigamesModItems.CRYSTALLIZED_POTION.get()))) {
			for (Entity entityiterator : new ArrayList<>(world.players())) {
				DungeonHealProcedure.execute(entityiterator, ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1)
						* GetItemAttributeProcedure.execute(ReturnRelicItemstackProcedure.execute(target, new ItemStack(MinigamesModItems.CRYSTALLIZED_POTION.get())), "minigames:heal_amount")) / 100, "relic");
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(target.getX(), target.getY(), target.getZ()), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("block.amethyst_block.chime")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound((target.getX()), (target.getY()), (target.getZ()), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("block.amethyst_block.chime")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
		}
	}
}