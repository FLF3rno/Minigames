package net.mcreator.minigames.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import net.mcreator.minigames.init.MinigamesModBlockEntities;
import net.mcreator.minigames.init.MinigamesModBlocks;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.client.renderer.AscenscionBeamManager;
import net.mcreator.minigames.network.PlayPlayerAnimationMessage;

@EventBusSubscriber(modid = "minigames")
public class AscenscionBlockEntity extends BlockEntity {
	private int lightUpdateCooldown = 0;

	public AscenscionBlockEntity(BlockPos pos, BlockState state) {
		super(MinigamesModBlockEntities.ASCENSCION.get(), pos, state);
	}

	public static double getTopY(Level level, BlockPos pos) {
		int startY = pos.getY() + 1;
		int maxY = Math.min(level.getMaxY(), startY + 256);
		BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(pos.getX(), startY, pos.getZ());

		while (mpos.getY() < maxY && level.getBlockState(mpos).getCollisionShape(level, mpos).isEmpty()) {
			mpos.move(0, 1, 0);
		}

		if (mpos.getY() < maxY) {
			int roofLimit = Math.min(level.getMaxY(), mpos.getY() + 32);
			while (mpos.getY() < roofLimit && !level.getBlockState(mpos).getCollisionShape(level, mpos).isEmpty()) {
				mpos.move(0, 1, 0);
			}
			return mpos.getY() + 2.5D;
		}

		return mpos.getY();
	}

	public static void updateLights(Level level, BlockPos pos, double topY) {
		int startY = pos.getY() + 1;
		int endY = (int) Math.floor(topY);
		BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(pos.getX(), startY, pos.getZ());
		for (int y = startY; y <= endY; y++) {
			mpos.setY(y);
			if (!level.isLoaded(mpos)) {
				break;
			}
			BlockState state = level.getBlockState(mpos);
			if (state.isAir()) {
				level.setBlock(mpos, Blocks.LIGHT.defaultBlockState().setValue(BlockStateProperties.LEVEL, 7), 3);
			} else if (state.is(Blocks.WATER) && state.getFluidState().isSource()) {
				level.setBlock(mpos, Blocks.LIGHT.defaultBlockState().setValue(BlockStateProperties.LEVEL, 7).setValue(BlockStateProperties.WATERLOGGED, true), 3);
			}
		}
	}

	public static void cleanUpLights(Level level, BlockPos pos) {
		int startY = pos.getY() + 1;
		int topY = (int) Math.ceil(getTopY(level, pos));
		BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(pos.getX(), startY, pos.getZ());
		for (int y = startY; y <= topY; y++) {
			mpos.setY(y);
			if (!level.isLoaded(mpos)) {
				break;
			}
			BlockState state = level.getBlockState(mpos);
			if (state.is(Blocks.LIGHT)) {
				if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
					level.setBlock(mpos, Blocks.WATER.defaultBlockState(), 3);
				} else {
					level.setBlock(mpos, Blocks.AIR.defaultBlockState(), 3);
				}
			}
		}
	}

	public static void applyBeamEffects(Player player) {
		if (player.isSpectator()) {
			return;
		}
		CompoundTag data = player.getPersistentData();
		data.putInt("InAscenscionBeam", 3);
		if (!"minigames:ascend".equals(data.getStringOr("PlayerCurrentAnimation", ""))) {
			data.putString("PlayerCurrentAnimation", "minigames:ascend");
			data.putBoolean("OverrideCurrentAnimation", true);
			data.putBoolean("FirstPersonAnimation", false);
			if (player.level() instanceof ServerLevel serverLevel) {
				PacketDistributor.sendToPlayersInDimension(serverLevel, new PlayPlayerAnimationMessage(player.getId(), "minigames:ascend", true, false));
			}
		}
		player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 4, 18, false, false, false));
	}

	public static void tick(Level level, BlockPos pos, BlockState state, AscenscionBlockEntity be) {
		if (level.isClientSide()) {
			return;
		}
		double topY = getTopY(level, pos);
		if (be.lightUpdateCooldown-- <= 0) {
			be.lightUpdateCooldown = 20;
			updateLights(level, pos, topY);
		}
		AABB columnBox = new AABB(
			pos.getX(), pos.getY() + 0.5D, pos.getZ(),
			pos.getX() + 1.0D, topY, pos.getZ() + 1.0D
		);
		for (Player player : level.getEntitiesOfClass(Player.class, columnBox)) {
			applyBeamEffects(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide()) {
			return;
		}
		CompoundTag data = player.getPersistentData();
		if (data.contains("InAscenscionBeam")) {
			int timer = data.getIntOr("InAscenscionBeam", 0);
			if (timer > 1) {
				data.putInt("InAscenscionBeam", timer - 1);
			} else {
				data.remove("InAscenscionBeam");
				if (!player.hasEffect(MinigamesModMobEffects.ASCENDING)) {
					data.remove("PlayerCurrentAnimation");
					data.putBoolean("ResetPlayerAnimation", true);
					data.putBoolean("FirstPersonAnimation", false);
					if (player.level() instanceof ServerLevel serverLevel) {
						PacketDistributor.sendToPlayersInDimension(serverLevel, new PlayPlayerAnimationMessage(player.getId(), "", false, false));
					}
				}
				player.removeEffect(MobEffects.LEVITATION);
				double boostY = 0.65D;
				player.setDeltaMovement(player.getDeltaMovement().x, boostY, player.getDeltaMovement().z);
				player.hurtMarked = true;
			}
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (this.level != null) {
			if (this.level.isClientSide()) {
				ClientAccess.add(this.worldPosition);
			} else {
				this.lightUpdateCooldown = 0;
			}
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (this.level != null && this.level.isClientSide()) {
			ClientAccess.remove(this.worldPosition);
		}
	}

	@Override
	public void clearRemoved() {
		super.clearRemoved();
		if (this.level != null && this.level.isClientSide()) {
			ClientAccess.add(this.worldPosition);
		}
	}

	private static class ClientAccess {
		private static void add(BlockPos pos) {
			AscenscionBeamManager.addBlock(pos);
		}

		private static void remove(BlockPos pos) {
			AscenscionBeamManager.removeBlock(pos);
		}
	}
}
