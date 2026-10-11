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
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.MinigamesMod;
import net.mcreator.minigames.entity.CandleheadEntity;
import net.mcreator.minigames.entity.ChurchSharpenerEntity;
import net.mcreator.minigames.entity.CryptLurkerEntity;
import net.mcreator.minigames.entity.DemonEntity;
import net.mcreator.minigames.entity.GravediggerEntity;
import net.mcreator.minigames.entity.GravediggerMinibossEntity;
import net.mcreator.minigames.entity.LaserStatueEntity;
import net.mcreator.minigames.entity.PreacherEntity;
import net.mcreator.minigames.entity.SculklingEntity;
import net.mcreator.minigames.entity.ShieldAngelEntity;
import net.mcreator.minigames.entity.WorshipperEntity;

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

			double sizeX = MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x();
			double sizeZ = MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z();
			if (sizeX <= 0) {
				sizeX = 29;
			}
			if (sizeZ <= 0) {
				sizeZ = 29;
			}

			int minX = (int) Math.floor(structureX);
			int maxX = minX + (int) sizeX - 1;
			int minZ = (int) Math.floor(structureZ);
			int maxZ = minZ + (int) sizeZ - 1;

			if (rotation.equals("clockwise_90")) {
				minX = (int) Math.floor(structureX) - (int) sizeX + 1;
				maxX = (int) Math.floor(structureX);
			} else if (rotation.equals("counterclockwise_90")) {
				minZ = (int) Math.floor(structureZ) - (int) sizeZ + 1;
				maxZ = (int) Math.floor(structureZ);
			} else if (rotation.equals("180")) {
				minX = (int) Math.floor(structureX) - (int) sizeX + 1;
				maxX = (int) Math.floor(structureX);
				minZ = (int) Math.floor(structureZ) - (int) sizeZ + 1;
				maxZ = (int) Math.floor(structureZ);
			}

			int minY = 0;
			int maxY = 200;

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

						if (state.hasBlockEntity()) {
							BlockEntity be = _level.getBlockEntity(pos);
							if (be != null) {
								be.setChanged();
								_level.sendBlockUpdated(pos, state, state, 3);
							}
						} else {
							refreshBlock(_level, pos, state);
						}

						_level.updateNeighborsAt(pos, block);
						_level.scheduleTick(pos, block, 1);
					}
				}
			}

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
							+ minX
							+ ",y=0,z="
							+ minZ
							+ ",dx="
							+ ((int) sizeX - 1)
							+ ",dy=200,dz="
							+ ((int) sizeZ - 1)
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
					entityiterator.getPersistentData()
							.putDouble("DataID", roomId);
					if (entityiterator instanceof LaserStatueEntity _laser) {
						_laser.getEntityData().set(LaserStatueEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof CandleheadEntity _candle) {
						_candle.getEntityData().set(CandleheadEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof DemonEntity _demon) {
						_demon.getEntityData().set(DemonEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof GravediggerEntity _grave) {
						_grave.getEntityData().set(GravediggerEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof GravediggerMinibossEntity _graveMini) {
						_graveMini.getEntityData().set(GravediggerMinibossEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof PreacherEntity _preacher) {
						_preacher.getEntityData().set(PreacherEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof SculklingEntity _sculk) {
						_sculk.getEntityData().set(SculklingEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof ShieldAngelEntity _shield) {
						_shield.getEntityData().set(ShieldAngelEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof WorshipperEntity _worship) {
						_worship.getEntityData().set(WorshipperEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof CryptLurkerEntity _crypt) {
						_crypt.getEntityData().set(CryptLurkerEntity.DATA_ID, roomId);
					}
					if (entityiterator instanceof ChurchSharpenerEntity _sharpener) {
						_sharpener.getEntityData().set(ChurchSharpenerEntity.DATA_ID, roomId);
					}
				}
			}

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

							if (state.getBlock() == MinigamesModBlocks.FIGHT_DOORS.get()) {
								FightDoorsBlockedCheckProcedure.execute(_level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), state);
							} else if (state.getBlock() == MinigamesModBlocks.LOOT_DOORS.get()
									|| state.getBlock() == MinigamesModBlocks.MINIBOSS_DOORS.get()
									|| state.getBlock() == MinigamesModBlocks.BOSS_DOORS.get()
									|| state.getBlock() == MinigamesModBlocks.FLOOR_DOORS.get()) {
								ReplaceDoorProcedure.execute(_level, blockPos.getX(), blockPos.getY(), blockPos.getZ());
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