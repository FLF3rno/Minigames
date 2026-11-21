package net.mcreator.minigames.block;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.RouletteGridDestroyedProcedure;

public class RouletteGridBlock extends Block {
	public static final EnumProperty<GridstateProperty> GRIDSTATE = EnumProperty.create("gridstate", GridstateProperty.class);

	public RouletteGridBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f));
		this.registerDefaultState(this.stateDefinition.any().setValue(GRIDSTATE, GridstateProperty.NONE));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(GRIDSTATE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state == null)
			return null;
		return state.setValue(GRIDSTATE, GridstateProperty.NONE);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Player entity) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState blockstate, Level world, BlockPos pos, Player entity, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
		boolean retval = super.onDestroyedByPlayer(blockstate, world, pos, entity, toolStack, willHarvest, fluid);
		RouletteGridDestroyedProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), blockstate);
		return retval;
	}

	public enum GridstateProperty implements StringRepresentable {
		NONE("none"), TNT("tnt"), POWERUP("powerup"), MINED_NONE("mined_none"), MINED_TNT("mined_tnt"), MINED_POWERUP("mined_powerup");

		private final String name;

		private GridstateProperty(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}