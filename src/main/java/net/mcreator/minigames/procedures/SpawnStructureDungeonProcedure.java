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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.MinigamesMod;

public class SpawnStructureDungeonProcedure {

	public static void execute(
			LevelAccessor world,
			double ID,
			double structureX,
			double structureY,
			double structureZ,
			String rotation,
			String structure) {

		if (rotation == null || structure == null)
			return;

		final int roomId = (int) ID;

		if (world instanceof ServerLevel _level) {
			String placeCommand =
					"/execute in minigames:dungeon_dimension run place template minigames:"
							+ structure + " "
							+ new java.text.DecimalFormat("##").format(structureX) + " "
							+ new java.text.DecimalFormat("##").format(structureY) + " "
							+ new java.text.DecimalFormat("##").format(structureZ) + " "
							+ rotation
							+ " none 1.0 0 strict";

			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(
							CommandSource.NULL,
							new Vec3(0, 0, 0),
							Vec2.ZERO,
							_level,
							net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
							"",
							Component.literal(""),
							_level.getServer(),
							null
					).withSuppressedOutput(),
					placeCommand
			);
		}

		MinigamesMod.queueServerWork(20, () -> {
			if (!(world instanceof ServerLevel _level))
				return;

			double dx = MinigamesModVariables.MapVariables
					.get(world).dungeonRoomSize.x();

			double dz = MinigamesModVariables.MapVariables
					.get(world).dungeonRoomSize.z();

			if (rotation.equals("clockwise_90")) {
				dx = -dx;
			} else if (rotation.equals("counterclockwise_90")) {
				dz = -dz;
			} else if (rotation.equals("180")) {
				dx = -dx;
				dz = -dz;
			}

			int minX = (int) Math.floor(Math.min(structureX, structureX + dx));
			int maxX = (int) Math.floor(Math.max(structureX, structureX + dx));
			int minZ = (int) Math.floor(Math.min(structureZ, structureZ + dz));
			int maxZ = (int) Math.floor(Math.max(structureZ, structureZ + dz));

			int minY = 0;
			int maxY = 200;

			// init blocks
			for (int xPos = minX; xPos <= maxX; xPos++) {
				for (int yPos = minY; yPos <= maxY; yPos++) {
					for (int zPos = minZ; zPos <= maxZ; zPos++) {

						BlockPos pos = new BlockPos(xPos, yPos, zPos);
						BlockState state = _level.getBlockState(pos);
						Block block = state.getBlock();

						Identifier blockId =
								net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block);

						if (blockId == null ||
								!blockId.getNamespace().equals("minigames")) {
							continue;
						}

						refreshBlock(_level, pos, state);

						_level.updateNeighborsAt(pos, block);
						_level.scheduleTick(pos, block, 1);
					}
				}
			}

			// tag mobs
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(
							CommandSource.NULL,
							new Vec3(0, 0, 0),
							Vec2.ZERO,
							_level,
							net.minecraft.server.permissions.LevelBasedPermissionSet.OWNER,
							"",
							Component.literal(""),
							_level.getServer(),
							null
					).withSuppressedOutput(),

					"/execute in minigames:dungeon_dimension as @e[type=#minigames:dungeon,x="
							+ new java.text.DecimalFormat("##").format(structureX)
							+ ",y=0,z="
							+ new java.text.DecimalFormat("##").format(structureZ)
							+ ",dx="
							+ new java.text.DecimalFormat("##").format(dx)
							+ ",dy=200,dz="
							+ new java.text.DecimalFormat("##").format(dz)
							+ "] run data modify entity @s DataID set value "
							+ roomId
			);

			TagKey<net.minecraft.world.entity.EntityType<?>> dungeonTag =
					TagKey.create(
							Registries.ENTITY_TYPE,
							Identifier.parse("minigames:dungeon")
					);

			for (Entity entityiterator : _level.getAllEntities()) {
				if (!entityiterator.is(dungeonTag))
					continue;

				double ex = entityiterator.getX();
				double ey = entityiterator.getY();
				double ez = entityiterator.getZ();

				if (ex >= minX && ex <= maxX
						&& ey >= minY && ey <= maxY
						&& ez >= minZ && ez <= maxZ) {

					entityiterator.getPersistentData()
							.putInt("DataID", roomId);
				}
			}

			// tag blocks
			for (int xPos = minX; xPos <= maxX; xPos++) {
				for (int yPos = minY; yPos <= maxY; yPos++) {
					for (int zPos = minZ; zPos <= maxZ; zPos++) {

						BlockPos blockPos = new BlockPos(
								xPos,
								yPos,
								zPos
						);

						BlockState state = _level.getBlockState(blockPos);

						if (state.is(BlockTags.create(
								Identifier.parse("minigames:door")))) {

							BlockEntity blockEntity =
									_level.getBlockEntity(blockPos);

							if (blockEntity != null) {
								blockEntity.getPersistentData()
										.putInt("DataID", roomId);

								blockEntity.setChanged();

								_level.sendBlockUpdated(
										blockPos,
										state,
										state,
										3
								);
							}
						}

						if (state.is(BlockTags.create(
								Identifier.parse("minigames:room")))) {

							BlockEntity blockEntity =
									_level.getBlockEntity(blockPos);

							if (blockEntity != null) {
								blockEntity.getPersistentData()
										.putInt("DataID", roomId);

								blockEntity.setChanged();

								_level.sendBlockUpdated(
										blockPos,
										state,
										state,
										3
								);
							}
						}
					}
				}
			}
		});
	}
	private static void refreshBlock(
			ServerLevel level,
			BlockPos pos,
			BlockState state) {

		level.setBlock(
				pos,
				Blocks.AIR.defaultBlockState(),
				2
		);

		level.setBlock(
				pos,
				state,
				3
		);
	}
}