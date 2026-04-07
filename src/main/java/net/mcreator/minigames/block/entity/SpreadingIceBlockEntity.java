package net.mcreator.minigames.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.mcreator.minigames.init.MinigamesModBlockEntities;

import java.util.UUID;

public class SpreadingIceBlockEntity extends BlockEntity {
	private static final String OWNER_KEY = "IceDartOwner";

	private UUID ownerUuid;

	public SpreadingIceBlockEntity(BlockPos pos, BlockState state) {
		super(MinigamesModBlockEntities.SPREADING_ICE.get(), pos, state);
	}

	public UUID getOwnerUuid() {
		return ownerUuid;
	}

	public void setOwnerUuid(UUID ownerUuid) {
		this.ownerUuid = ownerUuid;
		setChanged();
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (ownerUuid != null) {
			output.putString(OWNER_KEY, ownerUuid.toString());
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		String stored = input.getStringOr(OWNER_KEY, "");
		if (!stored.isEmpty()) {
			ownerUuid = UUID.fromString(stored);
		} else {
			ownerUuid = null;
		}
	}
}
