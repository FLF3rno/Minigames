package net.mcreator.minigames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.world.AuxiliaryLightManager;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import net.mcreator.minigames.block.CopycatHelper;
import net.mcreator.minigames.init.MinigamesModBlockEntities;

public class CopycatBlockEntity extends BlockEntity {
	public static final ModelProperty<BlockState> MATERIAL_PROPERTY = new ModelProperty<>();

	private BlockState material = Blocks.AIR.defaultBlockState();
	private int pendingCornersMask = 0;

	public CopycatBlockEntity(BlockPos pos, BlockState state) {
		super(MinigamesModBlockEntities.COPYCAT.get(), pos, state);
	}

	public BlockState getMaterial() {
		return material;
	}

	public boolean hasMaterial() {
		return material != null && !material.isAir();
	}

	public void updateLighting() {
		if (level != null && worldPosition != null) {
			AuxiliaryLightManager lightMgr = level.getAuxLightManager(worldPosition);
			if (lightMgr != null) {
				int light = 0;
				if (hasMaterial()) {
					light = material.getBlock().getLightEmission(material, level, worldPosition);
					if (light <= 0) {
						light = material.getLightEmission();
					}
				}
				if (light > 0) {
					lightMgr.setLightAt(worldPosition, light);
				} else {
					lightMgr.removeLightAt(worldPosition);
				}
			}
			if (!level.isClientSide()) {
				BlockState current = level.getBlockState(worldPosition);
				BlockState next = current;
				if (current.hasProperty(CopycatHelper.LIGHT_LEVEL)) {
					int target = 0;
					if (hasMaterial()) {
						target = material.getBlock().getLightEmission(material, level, worldPosition);
						if (target <= 0) target = material.getLightEmission();
						target = Math.max(0, Math.min(15, target));
					}
					next = next.setValue(CopycatHelper.LIGHT_LEVEL, target);
				}
				if (current.hasProperty(CopycatHelper.TRANSPARENT)) {
					boolean isTrans = hasMaterial() && (!material.canOcclude() || material.propagatesSkylightDown());
					next = next.setValue(CopycatHelper.TRANSPARENT, isTrans);
				}
				if (next != current) {
					level.setBlock(worldPosition, next, 3);
				}
			}
			level.getChunkSource().getLightEngine().checkBlock(worldPosition);
		}
	}

	public void setMaterial(BlockState state) {
		this.material = state != null ? state : Blocks.AIR.defaultBlockState();
		setChanged();
		requestModelDataUpdate();
		updateLighting();
		if (level != null) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (pendingCornersMask > 0 && level != null && !level.isClientSide()) {
			applyCornersMask(level, worldPosition, pendingCornersMask);
			pendingCornersMask = 0;
		}
		requestModelDataUpdate();
		updateLighting();
		if (level != null && !level.isClientSide()) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}

	private static void applyCornersMask(net.minecraft.world.level.Level lvl, BlockPos pos, int mask) {
		BlockState current = lvl.getBlockState(pos);
		if (current.getBlock() instanceof net.mcreator.minigames.block.CopycatCornerSliceBlock) {
			BlockState updated = current;
			for (int i = 0; i < 8; i++) {
				updated = updated.setValue(net.mcreator.minigames.block.CopycatCornerSliceBlock.CORNERS[i], (mask & (1 << i)) != 0);
			}
			if (updated != current) {
				lvl.setBlock(pos, updated, Block.UPDATE_ALL);
			}
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (level != null && worldPosition != null) {
			AuxiliaryLightManager lightMgr = level.getAuxLightManager(worldPosition);
			if (lightMgr != null) {
				lightMgr.removeLightAt(worldPosition);
			}
		}
	}

	@Override
	public ModelData getModelData() {
		return ModelData.builder().with(MATERIAL_PROPERTY, this.material).build();
	}

	@Override
	public void saveAdditional(ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);
		if (hasMaterial()) {
			valueOutput.store("material", BlockState.CODEC, material);
			try {
				valueOutput.putString("MaterialState", net.minecraft.commands.arguments.blocks.BlockStateParser.serialize(material));
			} catch (Exception ignored) {
			}
			valueOutput.putString("MaterialBlock", net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(material.getBlock()).toString());
		}
		if (this.level != null && this.worldPosition != null) {
			BlockState state = this.level.getBlockState(this.worldPosition);
			if (state.getBlock() instanceof net.mcreator.minigames.block.CopycatCornerSliceBlock) {
				int mask = 0;
				for (int i = 0; i < 8; i++) {
					if (state.getValue(net.mcreator.minigames.block.CopycatCornerSliceBlock.CORNERS[i])) {
						mask |= (1 << i);
					}
				}
				valueOutput.putInt("corners_mask", mask);
			}
		}
	}

	@Override
	public void loadAdditional(ValueInput valueInput) {
		super.loadAdditional(valueInput);
		this.material = valueInput.read("material", BlockState.CODEC).orElse(null);
		if (this.material == null || this.material.isAir()) {
			String stateStr = valueInput.getStringOr("MaterialState", "");
			if (!stateStr.isEmpty()) {
				try {
					var result = net.minecraft.commands.arguments.blocks.BlockStateParser.parseForBlock(net.minecraft.core.registries.BuiltInRegistries.BLOCK, stateStr, false);
					if (result != null && result.blockState() != null && !result.blockState().isAir()) {
						this.material = result.blockState();
					}
				} catch (Exception ignored) {
				}
			}
			if (!hasMaterial()) {
				String blockId = valueInput.getStringOr("MaterialBlock", "");
				if (!blockId.isEmpty()) {
					try {
						Block b = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(net.minecraft.resources.Identifier.parse(blockId));
						if (b != null && b != Blocks.AIR) {
							this.material = b.defaultBlockState();
						}
					} catch (Exception ignored) {
					}
				}
			}
		}
		if (this.material == null) {
			this.material = Blocks.AIR.defaultBlockState();
		}
		int savedMask = valueInput.getIntOr("corners_mask", 0);
		if (savedMask > 0) {
			if (this.level != null && this.worldPosition != null && !this.level.isClientSide()) {
				applyCornersMask(this.level, this.worldPosition, savedMask);
			} else {
				this.pendingCornersMask = savedMask;
			}
		}
		requestModelDataUpdate();
		updateLighting();
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return this.saveWithFullMetadata(lookupProvider);
	}

	@Override
	public void onDataPacket(Connection net, ValueInput valueInput) {
		super.onDataPacket(net, valueInput);
		requestModelDataUpdate();
		updateLighting();
		if (level != null) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}

	@Override
	public void handleUpdateTag(ValueInput input) {
		super.handleUpdateTag(input);
		requestModelDataUpdate();
		updateLighting();
		if (level != null) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}
}
