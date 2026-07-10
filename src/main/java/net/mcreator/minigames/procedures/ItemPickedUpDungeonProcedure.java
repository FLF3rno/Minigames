package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.init.MinigamesModItems;

import java.util.Comparator;

public class ItemPickedUpDungeonProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack item) {
		if (entity == null)
			return;
		double spawnRoomX = 0;
		double spawnRoomZ = 0;
		double range = 0;
		boolean explodeOtherPedestals = false;
		if ((world.getBlockState(BlockPos.containing(x, y - 0.5, z))).is(BlockTags.create(Identifier.parse("minigames:pedestal")))) {
			if (!item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr("pickedUp", false)) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y - 0.5, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null) {
						_blockEntity.getPersistentData().putBoolean("empty", true);
					}
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinigamesModItems.SNATCHING_CLAW.get()) {
					explodeOtherPedestals = false;
					if (world instanceof ServerLevel _level) {
						(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
						});
					}
				} else {
					explodeOtherPedestals = true;
				}
			}
			if (explodeOtherPedestals) {
				range = 12;
				spawnRoomX = x + Math.round(range / 2);
				spawnRoomZ = z + Math.round(range / 2);
				for (int index210 = 0; index210 < (int) range; index210++) {
					spawnRoomX = x + Math.round(range / 2);
					for (int index211 = 0; index211 < (int) range; index211++) {
						if (getBlockNBTNumber(world, BlockPos.containing(x, y - 0.5, z), "player") == getBlockNBTNumber(world, BlockPos.containing(spawnRoomX, y - 0.5, spawnRoomZ), "player")) {
							if (!getBlockNBTLogic(world, BlockPos.containing(spawnRoomX, y - 0.5, spawnRoomZ), "empty")) {
								ExplodeProcedure.execute(world, spawnRoomX, y, spawnRoomZ, entity, false, true, 0, 0, 0.5, "normal");
								if (!(findEntityInWorldRange(world, ItemEntity.class, spawnRoomX, y, spawnRoomZ, 2)).level().isClientSide())
									(findEntityInWorldRange(world, ItemEntity.class, spawnRoomX, y, spawnRoomZ, 2)).discard();
							}
						}
						spawnRoomX = spawnRoomX - 1;
					}
					spawnRoomZ = spawnRoomZ - 1;
				}
			}
		}
	}

	private static double getBlockNBTNumber(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDoubleOr(tag, 0);
		return -1;
	}

	private static boolean getBlockNBTLogic(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getBooleanOr(tag, false);
		return false;
	}

	private static Entity findEntityInWorldRange(LevelAccessor world, Class<? extends Entity> clazz, double x, double y, double z, double range) {
		return (Entity) world.getEntitiesOfClass(clazz, AABB.ofSize(new Vec3(x, y, z), range, range, range), e -> true).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
	}
}