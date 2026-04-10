package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DestroySnowGrantSnowballSpleefProcedure {
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getState(), event.getPlayer());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		execute(null, world, x, y, z, blockstate, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		double layer = 0;
		double targetX = 0;
		double targetZ = 0;
		double rng = 0;
		double targetY = 0;
		BlockState block = Blocks.AIR.defaultBlockState();
		if (MinigamesModVariables.MapVariables.get(world).playingSpleef) {
			targetX = x;
			targetZ = z;
			targetY = y;
			if (blockstate.is(BlockTags.create(ResourceLocation.parse("minigames:spleefables")))) {
				if (event instanceof ICancellableEvent _cancellable) {
					_cancellable.setCanceled(true);
				}
			}
			if (entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(MinigamesModMobEffects.HYPNOTIZED)) {
				rng = Mth.nextInt(RandomSource.create(), 1, 4);
				if (rng == 1) {
					targetX = targetX + 1;
				} else if (rng == 2) {
					targetX = targetX - 1;
				} else if (rng == 3) {
					targetZ = targetZ + 1;
				} else if (rng == 4) {
					targetZ = targetZ - 1;
				}
				if (!(entity instanceof ServerPlayer)) {
					world.levelEvent(2001, BlockPos.containing(targetX, targetY, targetZ), Block.getId((world.getBlockState(BlockPos.containing(targetX, targetY, targetZ)))));
				}
				world.setBlock(BlockPos.containing(targetX, targetY, targetZ), Blocks.AIR.defaultBlockState(), 3);
			}
			if (blockstate.is(BlockTags.create(ResourceLocation.parse("minigames:spleefables")))) {
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinigamesModItems.SYMMETRICAL_SHOVEL.get()) {
					if (world instanceof ServerLevel _level) {
						(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
						});
					}
					if (!(Blocks.AIR == (world.getBlockState(BlockPos.containing(targetX, targetY, targetZ))).getBlock())) {
						{
							MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
							_vars.snowballCountSpleef = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef + 1.25;
							_vars.markSyncDirty();
						}
					}
					SpleefPowerupProcedure.execute(world, entity);
					targetY = 100;
					if (!(entity instanceof ServerPlayer)) {
						world.levelEvent(2001, BlockPos.containing(targetX, targetY, targetZ), Block.getId((world.getBlockState(BlockPos.containing(targetX, targetY, targetZ)))));
					}
					world.setBlock(BlockPos.containing(targetX, targetY, targetZ), Blocks.AIR.defaultBlockState(), 3);
					layer = 0;
					for (int index0 = 0; index0 < (int) MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef; index0++) {
						layer = layer + 1;
						targetY = 100 + layer * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef;
						if (!(entity instanceof ServerPlayer)) {
							world.levelEvent(2001, BlockPos.containing(targetX, targetY, targetZ), Block.getId((world.getBlockState(BlockPos.containing(targetX, targetY, targetZ)))));
						}
						world.setBlock(BlockPos.containing(targetX, targetY, targetZ), Blocks.AIR.defaultBlockState(), 3);
					}
				} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinigamesModItems.SNOW_SHOVEL.get()) {
					if (world instanceof ServerLevel _level) {
						(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
						});
					}
					{
						MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
						_vars.snowballCountSpleef = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef + 3.75;
						_vars.markSyncDirty();
					}
					SpleefPowerupProcedure.execute(world, entity);
					if (!(entity instanceof ServerPlayer)) {
						world.levelEvent(2001, BlockPos.containing(targetX, y, targetZ), Block.getId((world.getBlockState(BlockPos.containing(targetX, targetY, targetZ)))));
					}
					world.setBlock(BlockPos.containing(targetX, targetY, targetZ), Blocks.AIR.defaultBlockState(), 3);
					world.setBlock(BlockPos.containing(targetX + 1, targetY, targetZ), Blocks.AIR.defaultBlockState(), 3);
					world.setBlock(BlockPos.containing(targetX - 1, targetY, targetZ), Blocks.AIR.defaultBlockState(), 3);
					world.setBlock(BlockPos.containing(targetX, targetY, targetZ + 1), Blocks.AIR.defaultBlockState(), 3);
					world.setBlock(BlockPos.containing(targetX, targetY, targetZ - 1), Blocks.AIR.defaultBlockState(), 3);
					targetY = y + 1;
					world.setBlock(BlockPos.containing(targetX, targetY, targetZ), Blocks.AIR.defaultBlockState(), 3);
					targetY = y - 1;
					world.setBlock(BlockPos.containing(targetX, targetY, targetZ), Blocks.AIR.defaultBlockState(), 3);
				} else {
					if (!(Blocks.AIR == (world.getBlockState(BlockPos.containing(targetX, targetY, targetZ))).getBlock())) {
						{
							MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
							_vars.snowballCountSpleef = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef + 0.75;
							_vars.markSyncDirty();
						}
					}
					SpleefPowerupProcedure.execute(world, entity);
					if (!(entity instanceof ServerPlayer)) {
						world.levelEvent(2001, BlockPos.containing(targetX, targetY, targetZ), Block.getId((world.getBlockState(BlockPos.containing(targetX, targetY, targetZ)))));
					}
					world.setBlock(BlockPos.containing(targetX, targetY, targetZ), Blocks.AIR.defaultBlockState(), 3);
				}
			}
		}
	}
}