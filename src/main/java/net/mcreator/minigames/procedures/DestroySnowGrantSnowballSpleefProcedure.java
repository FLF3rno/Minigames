package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;
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
		if (MinigamesModVariables.MapVariables.get(world).playingSpleef) {
			if (blockstate.is(BlockTags.create(ResourceLocation.parse("minigames:spleefables")))) {
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinigamesModItems.SYMMETRICAL_SHOVEL.get()) {
					if (world instanceof ServerLevel _level) {
						(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).hurtAndBreak(1, _level, null, _stkprov -> {
						});
					}
					world.setBlock(BlockPos.containing(x, 100, z), Blocks.AIR.defaultBlockState(), 3);
					layer = 0;
					for (int index0 = 0; index0 < (int) MinigamesModVariables.MapVariables.get(world).layersRemainingSpleef; index0++) {
						layer = layer + 1;
						world.setBlock(BlockPos.containing(x, 100 + layer * MinigamesModVariables.MapVariables.get(world).gapBetweenLayersSpleef, z), Blocks.AIR.defaultBlockState(), 3);
					}
					{
						MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
						_vars.snowballCountSpleef = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef + 1.25;
						_vars.markSyncDirty();
					}
					SpleefPowerupProcedure.execute(entity);
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/kill @e[type=item]");
				} else {
					{
						MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
						_vars.snowballCountSpleef = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef + 0.75;
						_vars.markSyncDirty();
					}
					SpleefPowerupProcedure.execute(entity);
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/kill @e[type=item]");
				}
			}
		}
	}
}