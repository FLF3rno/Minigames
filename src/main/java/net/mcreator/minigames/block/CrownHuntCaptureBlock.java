package net.mcreator.minigames.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.procedures.WalkOnCaptureCrownHuntProcedure;
import net.mcreator.minigames.procedures.ExpandCastleProcedure;

public class CrownHuntCaptureBlock extends Block {
	public CrownHuntCaptureBlock(BlockBehaviour.Properties properties) {
		super(properties.sound(SoundType.METAL).strength(-1, 3600000).lightLevel(blockstate -> 15).instrument(NoteBlockInstrument.BELL));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state) {
		return true;
	}

	@Override
	public int getLightDampening(BlockState state) {
		return 0;
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		ExpandCastleProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState blockstate, Entity entity) {
		super.stepOn(world, pos, blockstate, entity);
		WalkOnCaptureCrownHuntProcedure.execute(world, entity);
	}
}