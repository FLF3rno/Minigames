package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

public class WindScytheHitProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity, ItemStack itemstack) {
		if (entity == null || sourceentity == null)
			return;
		entity.setDeltaMovement(new Vec3(((sourceentity.getX() - sourceentity.getPersistentData().getDoubleOr("previousX", 0)) * (GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage") / 10)), 0.45,
				((sourceentity.getZ() - sourceentity.getPersistentData().getDoubleOr("previousZ", 0)) * (GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage") / 10))));
		entity.getPersistentData().putDouble("wind", 5);
		if (!world.isClientSide()) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("entity.breeze.wind_burst")), SoundSource.NEUTRAL, 1, (float) 0.7);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("entity.breeze.wind_burst")), SoundSource.NEUTRAL, 1, (float) 0.7, false);
				}
			}
		}
	}
}