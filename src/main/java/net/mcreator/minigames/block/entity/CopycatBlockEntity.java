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
import net.mcreator.minigames.init.MinigamesModBlockEntities;

public class CopycatBlockEntity extends BlockEntity {
	public static final ModelProperty<BlockState> MATERIAL_PROPERTY = new ModelProperty<>();

	private BlockState material = Blocks.AIR.defaultBlockState();

	public CopycatBlockEntity(BlockPos pos, BlockState state) {
		super(MinigamesModBlockEntities.COPYCAT.get(), pos, state);
	}

	public BlockState getMaterial() {
		return material;
	}

	public boolean hasMaterial() {
		return material != null && !material.isAir();
	}

	private void updateLighting() {
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
		updateLighting();
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
		}
	}

	@Override
	public void loadAdditional(ValueInput valueInput) {
		super.loadAdditional(valueInput);
		this.material = valueInput.read("material", BlockState.CODEC).orElse(Blocks.AIR.defaultBlockState());
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
