package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;

public class PerformDashProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.IMMOBILIZED))) {
			if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashCooldown <= 0 && entity.getData(MinigamesModVariables.PLAYER_VARIABLES).canDash) {
				entity.setDeltaMovement(new Vec3((entity.getLookAngle().x * entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashLength), (entity.getLookAngle().y * entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashLength),
						(entity.getLookAngle().z * entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashLength)));
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.dashCooldown = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).maxDashCooldown;
					_vars.markSyncDirty();
				}
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:dash")), SoundSource.NEUTRAL, (float) 0.5, (float) 1.2);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(ResourceLocation.parse("minigames:dash")), SoundSource.NEUTRAL, (float) 0.5, (float) 1.2, false);
					}
				}
			}
		}
	}
}