package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DestroySnowGrantSnowballSpleefProcedure {
	@SubscribeEvent
	public static void onBlockBreak(BreakBlockEvent event) {
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getPlayer());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
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
			if (event instanceof ICancellableEvent _cancellable) {
				_cancellable.setCanceled(true);
			}
			if (entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(MinigamesModMobEffects.HYPNOTIZED)) {
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
			}
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinigamesModItems.SYMMETRICAL_SHOVEL.get()) {
				if (world instanceof ServerLevel _level) {
					(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
					});
				}
				targetY = 100;
				BreakSnowProcedure.execute(world, targetX, targetY, targetZ, entity);
				layer = 0;
				for (int index477 = 0; index477 < (int) MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef; index477++) {
					layer = layer + 1;
					targetY = 100 + layer * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef;
					BreakSnowProcedure.execute(world, targetX, targetY, targetZ, entity);
				}
			} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinigamesModItems.SNOW_SHOVEL.get()) {
				if (world instanceof ServerLevel _level) {
					(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
					});
				}
				BreakSnowProcedure.execute(world, targetX, targetY, targetZ, entity);
				BreakSnowProcedure.execute(world, targetX + 1, targetY, targetZ, entity);
				BreakSnowProcedure.execute(world, targetX - 1, targetY, targetZ, entity);
				BreakSnowProcedure.execute(world, targetX, targetY, targetZ + 1, entity);
				BreakSnowProcedure.execute(world, targetX, targetY, targetZ - 1, entity);
				BreakSnowProcedure.execute(world, targetX, targetY + 1, targetZ, entity);
				BreakSnowProcedure.execute(world, targetX, targetY - 1, targetZ, entity);
			} else {
				BreakSnowProcedure.execute(world, targetX, targetY, targetZ, entity);
			}
		}
	}
}