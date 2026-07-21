package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModBlocks;

public class SpawnFloorProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		String spawnRoomName = "";
		String rotation = "";
		double structureX = 0;
		double structureZ = 0;
		double spawnRoomX = 0;
		double spawnRoomZ = 0;
		double neighbour = 0;
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/kill @e[type=!player]");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "/gamemode @a adventure");
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(
					new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, LevelBasedPermissionSet.OWNER, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					("execute in minigames:dungeon_dimension run fill -50 0 -50 "
							+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).dungeonSize.x() * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x() + 100) + " 200 "
							+ new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).dungeonSize.z() * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z() + 100) + " air"));
		spawnRoomZ = MinigamesModVariables.MapVariables.get(world).dungeonSize.z();
		for (int index8 = 0; index8 < (int) MinigamesModVariables.MapVariables.get(world).dungeonSize.z(); index8++) {
			spawnRoomX = MinigamesModVariables.MapVariables.get(world).dungeonSize.x();
			for (int index9 = 0; index9 < (int) MinigamesModVariables.MapVariables.get(world).dungeonSize.x(); index9++) {
				if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ))).getBlock() == MinigamesModBlocks.STARTING_ROOM_GRID_BLOCK.get()) {
					world.setBlock(BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z()),
							MinigamesModBlocks.SPAWN_STARTING_ROOM.get().defaultBlockState(), 3);
					if ((world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					} else if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
					{
						BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _booleanProp)
							world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
					}
				}
				if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ))).getBlock() == MinigamesModBlocks.BOSS_ROOM_GRID_BLOCK.get()) {
					world.setBlock(BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z()),
							MinigamesModBlocks.SPAWN_BOSS_ROOM.get().defaultBlockState(), 3);
					if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					} else if ((world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
					{
						BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _booleanProp)
							world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
					}
				}
				if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ))).getBlock() == MinigamesModBlocks.LOOT_ROOM_GRID_BLOCK.get()) {
					world.setBlock(BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z()),
							MinigamesModBlocks.SPAWN_LOOT_ROOM.get().defaultBlockState(), 3);
					if ((world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					} else if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					} else if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ + 1))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("flipped") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
					{
						BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _booleanProp)
							world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
					}
				}
				if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ))).getBlock() == MinigamesModBlocks.MINIBOSS_ROOM_GRID_BLOCK.get()) {
					world.setBlock(BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z()),
							MinigamesModBlocks.SPAWN_MINIBOSS_ROOM.get().defaultBlockState(), 3);
					if ((world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					} else if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					} else if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ + 1))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("flipped") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
					{
						BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _booleanProp)
							world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
					}
				}
				if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ))).getBlock() == MinigamesModBlocks.SECRET_ROOM_GRID_BLOCK.get()) {
					world.setBlock(BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z()),
							MinigamesModBlocks.SPAWN_SECRET_ROOM.get().defaultBlockState(), 3);
					if ((world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					} else if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					} else if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ + 1))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						{
							BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("flipped") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
					{
						BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _booleanProp)
							world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
					}
				}
				neighbour = 0;
				if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ))).getBlock() == MinigamesModBlocks.ROOM_GRID_BLOCK.get()
						|| (world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ))).getBlock() == MinigamesModBlocks.END_ROOM_GRID_BLOCK.get()) {
					if ((world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						neighbour = neighbour + 1;
					}
					if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						neighbour = neighbour + 1;
					}
					if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ + 1))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						neighbour = neighbour + 1;
					}
					if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ - 1))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
						neighbour = neighbour + 1;
					}
					if (neighbour == 2) {
						world.setBlock(BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z()),
								MinigamesModBlocks.SPAWN_TWO_DOOR_ROOM.get().defaultBlockState(), 3);
						if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))
								&& (world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
							{
								BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
								BlockState _bs = world.getBlockState(_pos);
								if (_bs.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _booleanProp)
									world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
							}
						} else if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ - 1))).is(BlockTags.create(Identifier.parse("minigames:room")))
								&& (world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ + 1))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
							{
								BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
								BlockState _bs = world.getBlockState(_pos);
								if (_bs.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _booleanProp)
									world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
							}
						} else {
							neighbour = 4;
						}
					} else if (neighbour == 1) {
						world.setBlock(BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z()),
								MinigamesModBlocks.SPAWN_ONE_DOOR_ROOM.get().defaultBlockState(), 3);
						if ((world.getBlockState(BlockPos.containing(spawnRoomX + 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
							{
								BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
								BlockState _bs = world.getBlockState(_pos);
								if (_bs.getBlock().getStateDefinition().getProperty("clockwise") instanceof BooleanProperty _booleanProp)
									world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
							}
						} else if ((world.getBlockState(BlockPos.containing(spawnRoomX - 1, 300, spawnRoomZ))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
							{
								BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
								BlockState _bs = world.getBlockState(_pos);
								if (_bs.getBlock().getStateDefinition().getProperty("anticlockwise") instanceof BooleanProperty _booleanProp)
									world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
							}
						} else if ((world.getBlockState(BlockPos.containing(spawnRoomX, 300, spawnRoomZ + 1))).is(BlockTags.create(Identifier.parse("minigames:room")))) {
							{
								BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
								BlockState _bs = world.getBlockState(_pos);
								if (_bs.getBlock().getStateDefinition().getProperty("flipped") instanceof BooleanProperty _booleanProp)
									world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
							}
						}
					}
					if (neighbour == 4 || neighbour == 3) {
						world.setBlock(BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z()),
								MinigamesModBlocks.SPAWN_FOUR_DOOR_ROOM.get().defaultBlockState(), 3);
					}
					{
						BlockPos _pos = BlockPos.containing(spawnRoomX * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.x(), 100, spawnRoomZ * MinigamesModVariables.MapVariables.get(world).dungeonRoomSize.z());
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("structure") instanceof BooleanProperty _booleanProp)
							world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
					}
				}
				spawnRoomX = spawnRoomX - 1;
			}
			spawnRoomZ = spawnRoomZ - 1;
		}
	}
}