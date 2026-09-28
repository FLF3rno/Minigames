package net.mcreator.minigames.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.world.AuxiliaryLightManager;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.mcreator.minigames.block.entity.CopycatBlockEntity;

@EventBusSubscriber
public class CopycatHelper {
	public static boolean isCopycatBlock(Block block) {
		return block instanceof CopycatBlockBlock
			|| block instanceof CopycatSlabBlock
			|| block instanceof CopycatStairBlock
			|| block instanceof CopycatWallBlock
			|| block instanceof CopycatFenceBlock
			|| block instanceof CopycatGateBlock
			|| block instanceof CopycatLayerBlock
			|| block instanceof CopycatCornerSliceBlock;
	}

	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) {
			return;
		}
		Player player = event.getEntity();
		if (!player.isShiftKeyDown()) {
			return;
		}
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof CopycatCornerSliceBlock) {
			event.setCanceled(true);
			CopycatCornerSliceBlock.handleShiftLeftClick(level, pos, player);
		}
	}

	public static InteractionResult handleUseItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!player.isCreative()) {
			return InteractionResult.PASS;
		}

		if (!(level.getBlockEntity(pos) instanceof CopycatBlockEntity be)) {
			return InteractionResult.PASS;
		}

		if (player.isShiftKeyDown()) {
			if (be.hasMaterial()) {
				if (!level.isClientSide()) {
					BlockState prev = be.getMaterial();
					be.setMaterial(Blocks.AIR.defaultBlockState());
					if (!player.isCreative()) {
						Block.popResource(level, pos, new ItemStack(prev.getBlock()));
					}
					SoundType sound = prev.getSoundType(level, pos, player);
					level.playSound(null, pos, sound.getBreakSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
				}
				return InteractionResult.SUCCESS;
			}
		}

		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			if (!isCopycatBlock(block) && !block.defaultBlockState().isAir()) {
				if (!level.isClientSide()) {
					BlockState targetMaterial = null;
					try {
						targetMaterial = block.getStateForPlacement(new BlockPlaceContext(player, hand, stack, hitResult));
					} catch (Exception ignored) {
					}
					if (targetMaterial == null) {
						targetMaterial = block.defaultBlockState();
						if (targetMaterial.hasProperty(BlockStateProperties.AXIS)) {
							targetMaterial = targetMaterial.setValue(BlockStateProperties.AXIS, hitResult.getDirection().getAxis());
						}
						if (targetMaterial.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
							targetMaterial = targetMaterial.setValue(BlockStateProperties.HORIZONTAL_FACING, player.getDirection().getOpposite());
						}
					}
					if (be.hasMaterial() && !player.isCreative()) {
						Block.popResource(level, pos, new ItemStack(be.getMaterial().getBlock()));
					}
					be.setMaterial(targetMaterial);
					SoundType sound = targetMaterial.getSoundType(level, pos, player);
					level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
					if (!player.isCreative()) {
						stack.shrink(1);
					}
				}
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

	public static void handleOnRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			if (level.getBlockEntity(pos) instanceof CopycatBlockEntity be) {
				if (be.hasMaterial() && !level.isClientSide()) {
					Block.popResource(level, pos, new ItemStack(be.getMaterial().getBlock()));
				}
			}
		}
	}

	public static SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
		if (level != null && pos != null && level.getBlockEntity(pos) instanceof CopycatBlockEntity be && be.hasMaterial()) {
			return be.getMaterial().getSoundType(level, pos, entity);
		}
		return SoundType.STONE;
	}

	public static float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
		if (level != null && pos != null && level.getBlockEntity(pos) instanceof CopycatBlockEntity be && be.hasMaterial()) {
			return be.getMaterial().getFriction(level, pos, entity);
		}
		return 0.6F;
	}

	public static int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		if (level != null && pos != null) {
			AuxiliaryLightManager lightMgr = level.getAuxLightManager(pos);
			if (lightMgr != null) {
				int aux = lightMgr.getLightAt(pos);
				if (aux > 0) return aux;
			}
			if (level.getBlockEntity(pos) instanceof CopycatBlockEntity be && be.hasMaterial()) {
				int emission = be.getMaterial().getBlock().getLightEmission(be.getMaterial(), level, pos);
				if (emission > 0) return emission;
				return be.getMaterial().getLightEmission();
			}
		}
		return 0;
	}

	public static void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		if (level != null && pos != null && level.getBlockEntity(pos) instanceof CopycatBlockEntity be && be.hasMaterial()) {
			be.getMaterial().getBlock().fallOn(level, be.getMaterial(), pos, entity, fallDistance);
		} else {
			entity.causeFallDamage(fallDistance, 1.0F, level.damageSources().fall());
		}
	}

	public static void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
		if (level != null && entity != null) {
			BlockPos pos = entity.getBlockPosBelowThatAffectsMyMovement();
			BlockEntity be = level.getBlockEntity(pos);
			if (!(be instanceof CopycatBlockEntity)) {
				be = level.getBlockEntity(entity.blockPosition());
			}
			if (be instanceof CopycatBlockEntity copycat && copycat.hasMaterial()) {
				copycat.getMaterial().getBlock().updateEntityMovementAfterFallOn(level, entity);
				return;
			}
		}
		entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
	}

	public static void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (level != null && pos != null && level.getBlockEntity(pos) instanceof CopycatBlockEntity be && be.hasMaterial()) {
			be.getMaterial().getBlock().stepOn(level, pos, be.getMaterial(), entity);
		}
	}
}
