package net.mcreator.minigames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mcreator.minigames.block.entity.CopycatBlockEntity;

public class CopycatCornerSliceBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty C0 = BooleanProperty.create("c0");
	public static final BooleanProperty C1 = BooleanProperty.create("c1");
	public static final BooleanProperty C2 = BooleanProperty.create("c2");
	public static final BooleanProperty C3 = BooleanProperty.create("c3");
	public static final BooleanProperty C4 = BooleanProperty.create("c4");
	public static final BooleanProperty C5 = BooleanProperty.create("c5");
	public static final BooleanProperty C6 = BooleanProperty.create("c6");
	public static final BooleanProperty C7 = BooleanProperty.create("c7");

	public static final BooleanProperty[] CORNERS = new BooleanProperty[] { C0, C1, C2, C3, C4, C5, C6, C7 };

	private static final VoxelShape[] SHAPES = new VoxelShape[256];

	static {
		VoxelShape[] oct = new VoxelShape[8];
		for (int i = 0; i < 8; i++) {
			double x0 = (i & 1) != 0 ? 8 : 0;
			double z0 = (i & 2) != 0 ? 8 : 0;
			double y0 = (i & 4) != 0 ? 8 : 0;
			oct[i] = Block.box(x0, y0, z0, x0 + 8, y0 + 8, z0 + 8);
		}
		for (int m = 0; m < 256; m++) {
			VoxelShape shape = Shapes.empty();
			for (int i = 0; i < 8; i++) {
				if ((m & (1 << i)) != 0) {
					shape = Shapes.or(shape, oct[i]);
				}
			}
			SHAPES[m] = shape;
		}
	}

	public static boolean isFull(BlockState state) {
		for (int i = 0; i < 8; i++) {
			if (!state.getValue(CORNERS[i])) {
				return false;
			}
		}
		return true;
	}

	public CopycatCornerSliceBlock(BlockBehaviour.Properties properties) {
		super(properties.strength(1f, 10f).noOcclusion().lightLevel(state -> state.hasProperty(CopycatHelper.LIGHT_LEVEL) ? state.getValue(CopycatHelper.LIGHT_LEVEL) : 0));
		BlockState defaultState = this.defaultBlockState();
		for (int i = 0; i < 8; i++) {
			defaultState = defaultState.setValue(CORNERS[i], i == 0);
		}
		this.registerDefaultState(defaultState.setValue(CopycatHelper.LIGHT_LEVEL, 0).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(C0, C1, C2, C3, C4, C5, C6, C7, CopycatHelper.LIGHT_LEVEL, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		int mask = 0;
		for (int i = 0; i < 8; i++) {
			if (state.getValue(CORNERS[i])) {
				mask |= (1 << i);
			}
		}
		return SHAPES[mask];
	}

	private static int getTargetOctant(BlockState state, BlockPos pos, Direction face, Vec3 hit) {
		double sx = hit.x - face.getStepX() * 0.05 - pos.getX();
		double sy = hit.y - face.getStepY() * 0.05 - pos.getY();
		double sz = hit.z - face.getStepZ() * 0.05 - pos.getZ();

		if (sx >= 0.0 && sx < 1.0 && sy >= 0.0 && sy < 1.0 && sz >= 0.0 && sz < 1.0) {
			int ox = (sx >= 0.5) ? 1 : 0;
			int oy = (sy >= 0.5) ? 1 : 0;
			int oz = (sz >= 0.5) ? 1 : 0;
			int clickedOctant = (oy << 2) | (oz << 1) | ox;
			if (state.getValue(CORNERS[clickedOctant])) {
				int tx = ox + face.getStepX();
				int ty = oy + face.getStepY();
				int tz = oz + face.getStepZ();
				if (tx >= 0 && tx <= 1 && ty >= 0 && ty <= 1 && tz >= 0 && tz <= 1) {
					int target = (ty << 2) | (tz << 1) | tx;
					if (!state.getValue(CORNERS[target])) {
						return target;
					}
				}
				return -1;
			}
		}

		double hx = hit.x + face.getStepX() * 0.05 - pos.getX();
		double hy = hit.y + face.getStepY() * 0.05 - pos.getY();
		double hz = hit.z + face.getStepZ() * 0.05 - pos.getZ();
		int tx = Math.max(0, Math.min(1, (hx >= 0.5) ? 1 : 0));
		int ty = Math.max(0, Math.min(1, (hy >= 0.5) ? 1 : 0));
		int tz = Math.max(0, Math.min(1, (hz >= 0.5) ? 1 : 0));
		int target = (ty << 2) | (tz << 1) | tx;
		if (!state.getValue(CORNERS[target])) {
			return target;
		}
		return -1;
	}

	public static int getSavedCornersMask(ItemStack stack) {
		if (stack.has(DataComponents.BLOCK_ENTITY_DATA)) {
			TypedEntityData<?> data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
			if (data != null) {
				CompoundTag tag = data.getUnsafe();
				if (tag != null && tag.contains("corners_mask")) {
					return tag.getIntOr("corners_mask", 0);
				}
			}
		}
		if (stack.has(DataComponents.CUSTOM_DATA)) {
			CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
			if (customData != null) {
				CompoundTag tag = customData.copyTag();
				if (tag.contains("corners_mask")) {
					return tag.getIntOr("corners_mask", 0);
				}
			}
		}
		return 0;
	}

	public static boolean hasSavedStructure(ItemStack stack) {
		return getSavedCornersMask(stack) > 0;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		if (context.getPlayer() != null && (context.getPlayer().isSpectator() || !context.getPlayer().mayBuild())) {
			return false;
		}
		if (context.getItemInHand().is(this.asItem())) {
			if (hasSavedStructure(context.getItemInHand())) {
				return false;
			}
			int target = getTargetOctant(state, context.getClickedPos(), context.getClickedFace(), context.getClickLocation());
			return target != -1;
		}
		return false;
	}

	@Override
	protected boolean canBeReplaced(BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		ItemStack stack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		boolean waterlogged = context.getLevel().getFluidState(pos).getType() == Fluids.WATER;
		int savedMask = getSavedCornersMask(stack);
		if (savedMask > 0) {
			BlockState state = this.defaultBlockState();
			for (int i = 0; i < 8; i++) {
				state = state.setValue(CORNERS[i], (savedMask & (1 << i)) != 0);
			}
			return state.setValue(WATERLOGGED, waterlogged && !isFull(state));
		}
		BlockState existingState = context.getLevel().getBlockState(pos);
		if (existingState.is(this)) {
			int target = getTargetOctant(existingState, pos, context.getClickedFace(), context.getClickLocation());
			if (target != -1) {
				BlockState newState = existingState.setValue(CORNERS[target], true);
				if (isFull(newState)) {
					newState = newState.setValue(WATERLOGGED, false);
				}
				return newState;
			}
		}
		Vec3 hit = context.getClickLocation();
		Direction face = context.getClickedFace();
		double hx = hit.x + face.getStepX() * 0.05 - pos.getX();
		double hy = hit.y + face.getStepY() * 0.05 - pos.getY();
		double hz = hit.z + face.getStepZ() * 0.05 - pos.getZ();
		int tx = Math.max(0, Math.min(1, (hx >= 0.5) ? 1 : 0));
		int ty = Math.max(0, Math.min(1, (hy >= 0.5) ? 1 : 0));
		int tz = Math.max(0, Math.min(1, (hz >= 0.5) ? 1 : 0));
		int octant = (ty << 2) | (tz << 1) | tx;
		BlockState state = this.defaultBlockState();
		for (int i = 0; i < 8; i++) {
			state = state.setValue(CORNERS[i], i == octant);
		}
		return state.setValue(WATERLOGGED, waterlogged);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
		return !isFull(state) && SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
	}

	@Override
	public boolean canPlaceLiquid(LivingEntity entity, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
		return !isFull(state) && SimpleWaterloggedBlock.super.canPlaceLiquid(entity, level, pos, state, fluid);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			tickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (!level.isClientSide()) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof CopycatBlockEntity copycat) {
				applySavedData(copycat, stack);
			}
		}
	}

	private static void applySavedData(CopycatBlockEntity copycat, ItemStack stack) {
		CompoundTag tag = null;
		if (stack.has(DataComponents.BLOCK_ENTITY_DATA)) {
			TypedEntityData<?> ted = stack.get(DataComponents.BLOCK_ENTITY_DATA);
			if (ted != null) {
				tag = ted.getUnsafe();
			}
		}
		if (tag == null && stack.has(DataComponents.CUSTOM_DATA)) {
			CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
			if (cd != null) {
				tag = cd.copyTag();
			}
		}
		if (tag != null && tag.contains("material")) {
			BlockState.CODEC.parse(NbtOps.INSTANCE, tag.get("material")).result().ifPresent(mat -> {
				if (mat != null && !mat.isAir()) {
					copycat.setMaterial(mat);
				}
			});
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CopycatBlockEntity(pos, state);
	}

	public static boolean handleShiftLeftClick(Level level, BlockPos pos, Player player) {
		if (player.isSpectator() || !player.mayBuild()) {
			return false;
		}
		BlockState state = level.getBlockState(pos);
		if (!(state.getBlock() instanceof CopycatCornerSliceBlock block)) {
			return false;
		}
		Vec3 eye = player.getEyePosition(1.0F);
		Vec3 view = player.getViewVector(1.0F);
		double reach = player.blockInteractionRange();
		Vec3 end = eye.add(view.scale(reach));
		BlockHitResult hit = level.clip(new ClipContext(eye, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
		if (hit.getType() != HitResult.Type.BLOCK || !hit.getBlockPos().equals(pos)) {
			return false;
		}
		Direction face = hit.getDirection();
		Vec3 loc = hit.getLocation();
		double px = loc.x - face.getStepX() * 0.01 - pos.getX();
		double py = loc.y - face.getStepY() * 0.01 - pos.getY();
		double pz = loc.z - face.getStepZ() * 0.01 - pos.getZ();
		if (px < 0.0 || px > 1.0 || py < 0.0 || py > 1.0 || pz < 0.0 || pz > 1.0) {
			return false;
		}
		int ox = Math.min(1, Math.max(0, (int) Math.floor(px * 2.0)));
		int oy = Math.min(1, Math.max(0, (int) Math.floor(py * 2.0)));
		int oz = Math.min(1, Math.max(0, (int) Math.floor(pz * 2.0)));
		int clickedOct = (oy << 2) | (oz << 1) | ox;
		if (!state.getValue(CORNERS[clickedOct])) {
			return false;
		}
		if (!level.isClientSide()) {
			int count = 0;
			for (int i = 0; i < 8; i++) {
				if (state.getValue(CORNERS[i])) count++;
			}
			CopycatBlockEntity be = level.getBlockEntity(pos) instanceof CopycatBlockEntity cbe ? cbe : null;
			SoundType sound = (be != null && be.hasMaterial()) ? be.getMaterial().getSoundType(level, pos, player) : state.getSoundType(level, pos, player);
			level.playSound(null, pos, sound.getBreakSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
			if (!player.isCreative()) {
				Block.popResource(level, pos, new ItemStack(block.asItem()));
			}
			if (count <= 1) {
				if (be != null && be.hasMaterial() && !player.isCreative()) {
					Block.popResource(level, pos, new ItemStack(be.getMaterial().getBlock()));
				}
				level.setBlock(pos, state.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
			} else {
				level.setBlock(pos, state.setValue(CORNERS[clickedOct], false), Block.UPDATE_ALL);
			}
		}
		return true;
	}

	@Override
	public void attack(BlockState state, Level level, BlockPos pos, Player player) {
		if (player.isShiftKeyDown()) {
			handleShiftLeftClick(level, pos, player);
		}
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (player.isSpectator() || !player.mayBuild()) {
			return InteractionResult.PASS;
		}
		if (stack.is(this.asItem())) {
			if (hasSavedStructure(stack)) {
				return InteractionResult.PASS;
			}
			int target = getTargetOctant(state, pos, hitResult.getDirection(), hitResult.getLocation());
			if (target != -1) {
				return InteractionResult.PASS;
			}
		}
		return CopycatHelper.handleUseItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
		return createCloneStack(level, pos, state, includeData);
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		return createCloneStack(level, pos, state, includeData);
	}

	private ItemStack createCloneStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		ItemStack stack = new ItemStack(this.asItem());
		if (includeData) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof CopycatBlockEntity cbe) {
				CompoundTag tag = new CompoundTag();
				int mask = 0;
				for (int i = 0; i < 8; i++) {
					if (state.getValue(CORNERS[i])) {
						mask |= (1 << i);
					}
				}
				tag.putInt("corners_mask", mask);
				if (cbe.hasMaterial()) {
					BlockState.CODEC.encodeStart(NbtOps.INSTANCE, cbe.getMaterial()).result().ifPresent(elem -> {
						tag.put("material", elem);
					});
				}
				TypedEntityData<BlockEntityType<?>> ted = TypedEntityData.of(cbe.getType(), tag);
				stack.set(DataComponents.BLOCK_ENTITY_DATA, ted);
			}
		}
		return stack;
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean isMoving) {
		super.affectNeighborsAfterRemoval(state, level, pos, isMoving);
		CopycatHelper.handleOnRemove(state, level, pos, state, isMoving);
	}

	@Override
	public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
		return CopycatHelper.getSoundType(state, level, pos, entity);
	}

	@Override
	public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
		return CopycatHelper.getFriction(state, level, pos, entity);
	}

	@Override
	public boolean hasDynamicLightEmission(BlockState state) {
		return true;
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		return CopycatHelper.getLightEmission(state, level, pos);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		CopycatHelper.fallOn(level, state, pos, entity, fallDistance);
	}

	@Override
	public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
		CopycatHelper.updateEntityMovementAfterFallOn(level, entity);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		CopycatHelper.stepOn(level, pos, state, entity);
	}
}