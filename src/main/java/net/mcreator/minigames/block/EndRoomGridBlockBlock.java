package net.mcreator.minigames.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.EndRoomGridBlockBlockAddedProcedure;

public class EndRoomGridBlockBlock extends Block {
	public EndRoomGridBlockBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.CALCITE).strength(-1, 3600000));
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Player entity) {
		return ItemStack.EMPTY;
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		EndRoomGridBlockBlockAddedProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}
}