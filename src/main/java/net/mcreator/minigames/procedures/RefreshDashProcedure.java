package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
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

import javax.annotation.Nullable;

@EventBusSubscriber
public class RefreshDashProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).playingDungeons) {
			if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).canDash) {
				if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashCooldown == 0) {
					if (world.isClientSide()) {
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.note_block.basedrum")), SoundSource.NEUTRAL, (float) 0.4, 2);
							} else {
								_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.note_block.basedrum")), SoundSource.NEUTRAL, (float) 0.4, 2, false);
							}
						}
					}
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
							_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
									LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "xp set @s 741");
						}
					}
				} else if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashCooldown > 0) {
					if (world.isClientSide()) {
						if (entity instanceof Player _player)
							_player.giveExperiencePoints((int) (741 / entity.getData(MinigamesModVariables.PLAYER_VARIABLES).maxDashCooldown));
					}
				}
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.dashCooldown = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).dashCooldown - 1;
					_vars.markSyncDirty();
				}
			}
		}
	}
}