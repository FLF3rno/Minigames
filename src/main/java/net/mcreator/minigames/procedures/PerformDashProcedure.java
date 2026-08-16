package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;

public class PerformDashProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.IMMOBILIZED)) && !(entity instanceof LivingEntity _livEnt1 && _livEnt1.hasEffect(MinigamesModMobEffects.ASCENDING))) {
			if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashCooldown <= 0 && entity.getData(MinigamesModVariables.PLAYER_VARIABLES).canDash) {
				entity.setDeltaMovement(new Vec3((entity.getLookAngle().x * entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashLength), (entity.getLookAngle().y * entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashLength),
						(entity.getLookAngle().z * entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashLength)));
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.dashCooldown = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).maxDashCooldown;
					_vars.markSyncDirty();
				}
				if (!world.isClientSide()) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:dash")), SoundSource.NEUTRAL, (float) 0.4, (float) 1.2);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("minigames:dash")), SoundSource.NEUTRAL, (float) 0.4, (float) 1.2, false);
						}
					}
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
						_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
								LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "xp set @s 0");
					}
				}
				if (CheckRelicProcedure.execute(entity, new ItemStack(MinigamesModItems.PHASE_CLOAK.get()))) {
					ApplyEffectProcedure.execute(world, entity, true, 1, 3, "minigames:phantom");
					ApplyEffectProcedure.execute(world, entity, true, 1, 3, "minigames:blessed");
				}
			}
		}
	}
}