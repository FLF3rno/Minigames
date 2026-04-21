package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.init.MinigamesModEntities;
import net.mcreator.minigames.entity.StunnedEffectEntity;

public class StunnedEffectStartedappliedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			if (!_ent.level().isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "data merge entity @s {NoAI:1b}");
			}
		}
		if (world instanceof ServerLevel _level) {
			Entity entityToSpawn = MinigamesModEntities.STUNNED_EFFECT.get().spawn(_level, BlockPos.containing(x, y + entity.getBbHeight() + 0.5, z), EntitySpawnReason.MOB_SUMMONED);
			if (entityToSpawn instanceof StunnedEffectEntity _stunnedEffect) {
				_stunnedEffect.setDeltaMovement(0, 0, 0);
				_stunnedEffect.setParent(entity);
			}
		}
	}
}
