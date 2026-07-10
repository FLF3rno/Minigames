package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModBlocks;

public class FightDoorsRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean rotatedX = false;
		double middleOffset = 0;
		double pos = 0;
		if (!MinigamesModVariables.MapVariables.get(world).inCombat) {
			if (!MinigamesModVariables.MapVariables.get(world).ActiveVote) {
				MinigamesModVariables.MapVariables.get(world).DoorPosition = new Vec3(x, y, z);
				MinigamesModVariables.MapVariables.get(world).DoorOffset = new Vec3(x, y, z);
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				if ((entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(4.5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getDirection()) == Direction.NORTH) {
					MinigamesModVariables.MapVariables.get(world).DoorOffset = new Vec3((x + 0.5), (MinigamesModVariables.MapVariables.get(world).DoorOffset.y()), (z + 2));
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					rotatedX = true;
					ChangeCurrentRoomIDValueProcedure.execute(world, getBlockNBTNumber(world, BlockPos.containing(x, y, z + 1), "DataID"));
				} else if ((entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(4.5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity))
						.getDirection()) == Direction.SOUTH) {
					MinigamesModVariables.MapVariables.get(world).DoorOffset = new Vec3((x + 0.5), (MinigamesModVariables.MapVariables.get(world).DoorOffset.y()), (z - 1));
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					rotatedX = true;
					ChangeCurrentRoomIDValueProcedure.execute(world, getBlockNBTNumber(world, BlockPos.containing(x, y, z - 1), "DataID"));
				} else if ((entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(4.5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity))
						.getDirection()) == Direction.WEST) {
					MinigamesModVariables.MapVariables.get(world).DoorOffset = new Vec3((x + 2), (MinigamesModVariables.MapVariables.get(world).DoorOffset.y()), (z + 0.5));
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					rotatedX = false;
					ChangeCurrentRoomIDValueProcedure.execute(world, getBlockNBTNumber(world, BlockPos.containing(x + 1, y, z), "DataID"));
				} else if ((entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(4.5)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity))
						.getDirection()) == Direction.EAST) {
					MinigamesModVariables.MapVariables.get(world).DoorOffset = new Vec3((x - 1), (MinigamesModVariables.MapVariables.get(world).DoorOffset.y()), (z + 0.5));
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					rotatedX = false;
					ChangeCurrentRoomIDValueProcedure.execute(world, getBlockNBTNumber(world, BlockPos.containing(x - 1, y, z), "DataID"));
				}
				middleOffset = 0;
				pos = 0;
				for (int index161 = 0; index161 < 5; index161++) {
					pos = pos + 1;
					if ((world.getBlockState(BlockPos.containing(x, y - pos, z))).is(BlockTags.create(Identifier.parse("minigames:door")))) {
						MinigamesModVariables.MapVariables.get(world).DoorOffset = new Vec3((MinigamesModVariables.MapVariables.get(world).DoorOffset.x()), (MinigamesModVariables.MapVariables.get(world).DoorOffset.y() - 1),
								(MinigamesModVariables.MapVariables.get(world).DoorOffset.z()));
						MinigamesModVariables.MapVariables.get(world).markSyncDirty();
					} else {
						break;
					}
				}
				if (rotatedX) {
					pos = 0;
					for (int index162 = 0; index162 < 2; index162++) {
						pos = pos + 1;
						if ((world.getBlockState(BlockPos.containing(x + pos, y, z))).is(BlockTags.create(Identifier.parse("minigames:door")))) {
							middleOffset = middleOffset + 1;
						}
					}
					pos = 0;
					for (int index163 = 0; index163 < 2; index163++) {
						pos = pos + 1;
						if ((world.getBlockState(BlockPos.containing(x - pos, y, z))).is(BlockTags.create(Identifier.parse("minigames:door")))) {
							middleOffset = middleOffset - 1;
						}
					}
					MinigamesModVariables.MapVariables.get(world).DoorOffset = new Vec3((MinigamesModVariables.MapVariables.get(world).DoorOffset.x() + middleOffset), (MinigamesModVariables.MapVariables.get(world).DoorOffset.y()),
							(MinigamesModVariables.MapVariables.get(world).DoorOffset.z()));
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				} else {
					pos = 0;
					for (int index164 = 0; index164 < 2; index164++) {
						pos = pos + 1;
						if ((world.getBlockState(BlockPos.containing(x, y, z + pos))).is(BlockTags.create(Identifier.parse("minigames:door")))) {
							middleOffset = middleOffset + 1;
						}
					}
					pos = 0;
					for (int index165 = 0; index165 < 2; index165++) {
						pos = pos + 1;
						if ((world.getBlockState(BlockPos.containing(x, y, z - pos))).is(BlockTags.create(Identifier.parse("minigames:door")))) {
							middleOffset = middleOffset - 1;
						}
					}
					MinigamesModVariables.MapVariables.get(world).DoorOffset = new Vec3((MinigamesModVariables.MapVariables.get(world).DoorOffset.x()), (MinigamesModVariables.MapVariables.get(world).DoorOffset.y()),
							(MinigamesModVariables.MapVariables.get(world).DoorOffset.z() + middleOffset));
					MinigamesModVariables.MapVariables.get(world).markSyncDirty();
				}
				if (MinigamesModBlocks.FIGHT_DOORS.get() == (world.getBlockState(BlockPos.containing(x, y, z))).getBlock()) {
					StartVoteProcedure.execute(world, entity, entity, "fight room");
				} else if (MinigamesModBlocks.LOOT_DOORS.get() == (world.getBlockState(BlockPos.containing(x, y, z))).getBlock()) {
					StartVoteProcedure.execute(world, entity, entity, "loot room");
				} else if (MinigamesModBlocks.MINIBOSS_DOORS.get() == (world.getBlockState(BlockPos.containing(x, y, z))).getBlock()) {
					StartVoteProcedure.execute(world, entity, entity, "miniboss room");
				} else if (MinigamesModBlocks.BOSS_DOORS.get() == (world.getBlockState(BlockPos.containing(x, y, z))).getBlock()) {
					StartVoteProcedure.execute(world, entity, entity, "boss room");
				} else if (MinigamesModBlocks.FLOOR_DOORS.get() == (world.getBlockState(BlockPos.containing(x, y, z))).getBlock()) {
					StartVoteProcedure.execute(world, entity, entity, "floor");
				}
			} else {
				if (entity instanceof ServerPlayer _player)
					_player.sendSystemMessage(Component.literal("\u00A7cA vote is already active!"), true);
			}
		} else {
			if (entity instanceof ServerPlayer _player)
				_player.sendSystemMessage(Component.literal("\u00A7cFinish combat first!"), true);
		}
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}
}