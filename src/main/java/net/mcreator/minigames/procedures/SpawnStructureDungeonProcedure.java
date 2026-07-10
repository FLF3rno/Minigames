package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

public class SpawnStructureDungeonProcedure {
	public static void execute(LevelAccessor world, double ID, double structureX, double structureY, double structureZ, String rotation, String structure) {
		if (rotation == null || structure == null)
			return;
		final int roomId = (int) ID;
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("/execute in minigames:dungeon_dimension run place template minigames:" + structure + " " + new java.text.DecimalFormat("##").format(structureX) + " " + new java.text.DecimalFormat("##").format(structureY) + " "
							+ new java.text.DecimalFormat("##").format(structureZ) + " " + rotation));
		MinigamesMod.queueServerWork(20, () -> {
			if (world instanceof ServerLevel _level) {
				double dx = MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x();
				double dz = MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z();
				if (rotation.equals("clockwise_90")) {
					dx = -dx;
				} else if (rotation.equals("counterclockwise_90")) {
					dz = -dz;
				} else if (rotation.equals("clockwise_180")) {
					dx = -dx;
					dz = -dz;
				}
				_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(0, 0, 0), Vec2.ZERO, _level, net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						("/execute in minigames:dungeon_dimension as @e[type=#minigames:dungeon,x=" + new java.text.DecimalFormat("##").format(structureX) + ",y=0,z=" + new java.text.DecimalFormat("##").format(structureZ) + ",dx="
								+ new java.text.DecimalFormat("##").format(dx) + ",dy=200,dz=" + new java.text.DecimalFormat("##").format(dz) + "] run data modify entity @s DataID set value " + roomId));

				int minX = (int) Math.floor(Math.min(structureX, structureX + dx));
				int maxX = (int) Math.floor(Math.max(structureX, structureX + dx));
				int minZ = (int) Math.floor(Math.min(structureZ, structureZ + dz));
				int maxZ = (int) Math.floor(Math.max(structureZ, structureZ + dz));
				int minY = 0;
				int maxY = 200;
				TagKey<net.minecraft.world.entity.EntityType<?>> dungeonTag = TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:dungeon"));

				for (Entity entityiterator : _level.getAllEntities()) {
					if (entityiterator.is(dungeonTag)) {
						double ex = entityiterator.getX();
						double ey = entityiterator.getY();
						double ez = entityiterator.getZ();
						if (ex >= minX && ex <= maxX && ey >= minY && ey <= maxY && ez >= minZ && ez <= maxZ) {
							entityiterator.getPersistentData().putInt("DataID", roomId);
						}
					}
				}

				for (int xPos = minX; xPos <= maxX; xPos++) {
					for (int yPos = minY; yPos <= maxY; yPos++) {
						for (int zPos = minZ; zPos <= maxZ; zPos++) {
							BlockPos blockPos = BlockPos.containing(xPos, yPos, zPos);
							if (_level.getBlockState(blockPos).is(BlockTags.create(Identifier.parse("minigames:door")))) {
								BlockEntity blockEntity = _level.getBlockEntity(blockPos);
								if (blockEntity != null) {
									blockEntity.getPersistentData().putInt("DataID", roomId);
									blockEntity.setChanged();
									_level.sendBlockUpdated(blockPos, _level.getBlockState(blockPos), _level.getBlockState(blockPos), 3);
								}
							}
							if (_level.getBlockState(blockPos).is(BlockTags.create(Identifier.parse("minigames:room")))) {
								BlockEntity blockEntity = _level.getBlockEntity(blockPos);
								if (blockEntity != null) {
									blockEntity.getPersistentData().putInt("DataID", roomId);
									blockEntity.setChanged();
									_level.sendBlockUpdated(blockPos, _level.getBlockState(blockPos), _level.getBlockState(blockPos), 3);
								}
							}
						}
					}
				}
			}
		});
	}
}





