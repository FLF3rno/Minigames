package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

public class TeleporterRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		Entity target = ReturnHoveredTeammateProcedure.execute(world, entity);
		if (target != null && target != entity) {
			ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
			Vec3 userPos = entity.position();
			Vec3 victimPos = target.position();
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(victimPos.x(), victimPos.y(), victimPos.z()), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.enderman.teleport")), SoundSource.PLAYERS, 1, (float) 1.2);
				} else {
					_level.playLocalSound((victimPos.x()), (victimPos.y()), (victimPos.z()), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.enderman.teleport")), SoundSource.PLAYERS, 1, (float) 1.2, false);
				}
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.enderman.teleport")), SoundSource.PLAYERS, 1, (float) 1.2);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.enderman.teleport")), SoundSource.PLAYERS, 1, (float) 1.2, false);
				}
			}
			{
				double _tx = (userPos.x());
				double _ty = (userPos.y());
				double _tz = (userPos.z());
				target.teleportTo(_tx, _ty, _tz);
				if (target instanceof ServerPlayer _serverPlayer)
					_serverPlayer.connection.teleport(_tx, _ty, _tz, target.getYRot(), target.getXRot());
			}
			{
				Entity _ent = entity;
				double _tx = (victimPos.x());
				double _ty = (victimPos.y());
				double _tz = (victimPos.z());
				_ent.teleportTo(_tx, _ty, _tz);
				if (_ent instanceof ServerPlayer _serverPlayer)
					_serverPlayer.connection.teleport(_tx, _ty, _tz, _ent.getYRot(), _ent.getXRot());
			}
		}
	}
}