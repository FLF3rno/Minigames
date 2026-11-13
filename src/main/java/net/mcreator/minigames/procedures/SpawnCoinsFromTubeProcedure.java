package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.init.MinigamesModItems;

public class SpawnCoinsFromTubeProcedure {
	public static void execute(LevelAccessor world) {
		Vec3 rng = Vec3.ZERO;
		rng = new Vec3((Mth.nextInt(RandomSource.create(), (int) (-92.8), (int) (-91.2))), 110.5, (Mth.nextInt(RandomSource.create(), (int) (-84.8), (int) (-83.2))));
		if (world instanceof ServerLevel _level) {
			ItemEntity entityToSpawn = new ItemEntity(_level, (rng.x()), (rng.y()), (rng.z()), new ItemStack(MinigamesModItems.ROGUELIKE_COIN.get()));
			entityToSpawn.setPickUpDelay(10);
			entityToSpawn.setUnlimitedLifetime();
			_level.addFreshEntity(entityToSpawn);
		}
	}
}