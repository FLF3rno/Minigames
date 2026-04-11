package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

public class ThrustersRightclickedProcedure {
	private static final double SPLEEF_MAX_COORD = 30;

	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (isOutOfBoundsInSpleef(entity))
			return;
		if (!entity.level().isClientSide()) {
			itemstack.shrink(1);
			{
				MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.thrusterDirection = new Vec3((entity.getLookAngle().x), (entity.getLookAngle().y), (entity.getLookAngle().z));
				_vars.thrusterTicks = 10;
				_vars.markSyncDirty();
			}
			{
				Entity _ent = entity;
				if (_ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound minigames:thruster player @s ~ ~ ~ 1 1");
				}
			}
		}
	}

	private static boolean isOutOfBoundsInSpleef(Entity entity) {
		ResourceLocation spleefDimension = ResourceLocation.parse("minigames:spleef_dimension");
		if (!entity.level().dimension().location().equals(spleefDimension))
			return false;
		Vec3 arenaCenter = MinigamesModVariables.MapVariables.get(entity.level()).spleefMapMiddleX;
		return Math.abs(entity.getX() - arenaCenter.x()) > SPLEEF_MAX_COORD || Math.abs(entity.getZ() - arenaCenter.z()) > SPLEEF_MAX_COORD;
	}
}
