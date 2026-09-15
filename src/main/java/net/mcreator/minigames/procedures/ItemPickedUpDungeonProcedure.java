package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
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
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinigamesModItems.SNATCHING_CLAW.get()
						&& !((getPropertyByName((world.getBlockState(BlockPos.containing(x, y - 0.5, z))), "owner") instanceof IntegerProperty _getip7 ? (world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getValue(_getip7) : -1) == 0)) {
					explodeOtherPedestals = false;
					if (world instanceof ServerLevel _level) {
						(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
						});
					}
				} else if ((getPropertyByName((world.getBlockState(BlockPos.containing(x, y - 0.5, z))), "owner") instanceof IntegerProperty _getip11 ? (world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getValue(_getip11) : -1) == 0) {
					explodeOtherPedestals = false;
					if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinigamesModItems.SNATCHING_CLAW.get()) {
						if (entity instanceof ServerPlayer _player)
							_player.sendSystemMessage(Component.literal("\u00A7cUse this item in multiple choice pedestals!"), true);
						if (world.isClientSide()) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.enderman.teleport")), SoundSource.NEUTRAL, (float) 0.8, (float) 0.1);
								} else {
									_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.enderman.teleport")), SoundSource.NEUTRAL, (float) 0.8, (float) 0.1, false);
								}
							}
						}
					}
				} else {
					explodeOtherPedestals = true;
				}
			}
			if (explodeOtherPedestals) {
				range = 12;
				spawnRoomX = x + Math.round(range / 2);
				spawnRoomZ = z + Math.round(range / 2);
				for (int _i1 = 0; _i1 < (int) range; _i1++) {
					spawnRoomX = x + Math.round(range / 2);
					for (int _i2 = 0; _i2 < (int) range; _i2++) {
						if (getBlockNBTNumber(world, BlockPos.containing(x, y - 0.5, z), "player") == getBlockNBTNumber(world, BlockPos.containing(spawnRoomX, y - 0.5, spawnRoomZ), "player")) {
							if (!getBlockNBTLogic(world, BlockPos.containing(spawnRoomX, y - 0.5, spawnRoomZ), "empty")) {
								if ((getPropertyByName((world.getBlockState(BlockPos.containing(spawnRoomX, y - 0.5, spawnRoomZ))), "owner") instanceof IntegerProperty _getip21
										? (world.getBlockState(BlockPos.containing(spawnRoomX, y - 0.5, spawnRoomZ))).getValue(_getip21)
										: -1) == (getPropertyByName((world.getBlockState(BlockPos.containing(x, y - 0.5, z))), "owner") instanceof IntegerProperty _getip23
												? (world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getValue(_getip23)
												: -1)) {
									ExplodeProcedure.execute(world, spawnRoomX, y, spawnRoomZ, entity, false, true, 0, 0, 0.5, "normal");
									if (!(findEntityInWorldRange(world, ItemEntity.class, spawnRoomX, y, spawnRoomZ, 2)).level().isClientSide())
										(findEntityInWorldRange(world, ItemEntity.class, spawnRoomX, y, spawnRoomZ, 2)).discard();
								}
							}
						}
						spawnRoomX = spawnRoomX - 1;
					}
					spawnRoomZ = spawnRoomZ - 1;
				}
			}
		}
	}

	private static Property<?> getPropertyByName(BlockState state, String name) {
		for (Property<?> property : state.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
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