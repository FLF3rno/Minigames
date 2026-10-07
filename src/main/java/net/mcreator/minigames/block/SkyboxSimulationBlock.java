package net.mcreator.minigames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mcreator.minigames.block.entity.SkyboxSimulationBlockEntity;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class SkyboxSimulationBlock extends Block implements EntityBlock {
	public enum SkyType implements StringRepresentable {
		END("end", "End Cosmic Void"),
		GATEWAY("gateway", "End Gateway Portal"),
		DAY("day", "Daytime Sky"),
		SUNSET("sunset", "Sunset Sky"),
		NIGHT("night", "Starry Night Sky"),
		DYNAMIC("dynamic", "Dynamic World Sky");

		private final String name;
		private final String displayName;

		SkyType(String name, String displayName) {
			this.name = name;
			this.displayName = displayName;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public String getDisplayName() {
			return this.displayName;
		}

		public SkyType next() {
			SkyType[] values = values();
			return values[(this.ordinal() + 1) % values.length];
		}
	}

	public static final EnumProperty<SkyType> SKY_TYPE = EnumProperty.create("sky_type", SkyType.class);

	public SkyboxSimulationBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1.5f, 6.0f).lightLevel(SkyboxSimulationBlock::getLightEmission).noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(SKY_TYPE, SkyType.END));
	}

	public static int getLightEmission(BlockState state) {
		SkyType type = state.getValue(SKY_TYPE);
		return switch (type) {
			case DAY -> 15;
			case SUNSET -> 7;
			case NIGHT -> 1;
			case END -> 1;
			case GATEWAY -> 6;
			case DYNAMIC -> 15;
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(SKY_TYPE);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SkyboxSimulationBlockEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0f;
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, net.minecraft.world.level.redstone.Orientation orientation, boolean movedByPiston) {
		super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
		if (level.isClientSide()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof SkyboxSimulationBlockEntity sbe) {
				sbe.cachedFaces = -1;
			}
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		return changeSkyboxSetting(state, level, pos, player);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		return changeSkyboxSetting(state, level, pos, player);
	}

	private InteractionResult changeSkyboxSetting(BlockState state, Level level, BlockPos startPos, Player player) {
		if (player.isSpectator() || (player instanceof ServerPlayer sp ? sp.gameMode.getGameModeForPlayer() == GameType.ADVENTURE : !player.mayBuild())) {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide()) {
			SkyType current = state.getValue(SKY_TYPE);
			SkyType next = current.next();
			if (player.isShiftKeyDown()) {
				level.setBlock(startPos, state.setValue(SKY_TYPE, next), 3);
			} else {
				Set<BlockPos> visited = new HashSet<>();
				Queue<BlockPos> queue = new ArrayDeque<>();
				queue.add(startPos);
				visited.add(startPos);
				int maxBlocks = 4096;
				while (!queue.isEmpty() && visited.size() <= maxBlocks) {
					BlockPos currentPos = queue.poll();
					BlockState currentState = level.getBlockState(currentPos);
					if (currentState.getBlock() == this) {
						level.setBlock(currentPos, currentState.setValue(SKY_TYPE, next), 3);
						for (Direction dir : Direction.values()) {
							BlockPos neighbor = currentPos.relative(dir);
							if (!visited.contains(neighbor) && level.getBlockState(neighbor).getBlock() == this) {
								visited.add(neighbor);
								queue.add(neighbor);
							}
						}
					}
				}
			}
			player.sendSystemMessage(Component.literal("Skybox Mode: " + next.getDisplayName()));
		}
		return InteractionResult.SUCCESS;
	}
}