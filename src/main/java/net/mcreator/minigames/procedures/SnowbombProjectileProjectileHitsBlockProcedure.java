package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class SnowbombProjectileProjectileHitsBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		boolean found = false;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		sx = -2;
		found = false;
		for (int _i1 = 0; _i1 < 4; _i1++) {
			sy = -2;
			for (int _i2 = 0; _i2 < 4; _i2++) {
				sz = -2;
				for (int _i3 = 0; _i3 < 4; _i3++) {
					world.setBlock(BlockPos.containing(x + sx, y + sy, z + sz), Blocks.AIR.defaultBlockState(), 3);
					sz = sz + 1;
				}
				sy = sy + 1;
			}
			sx = sx + 1;
		}
		if (world instanceof Level _level && !_level.isClientSide())
			_level.explode(null, x, y, z, 2, Level.ExplosionInteraction.TNT);
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/kill @e[type=item]");
	}
}