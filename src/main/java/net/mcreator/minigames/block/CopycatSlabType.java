package net.mcreator.minigames.block;

import net.minecraft.util.StringRepresentable;

public enum CopycatSlabType implements StringRepresentable {
	TOP("top"),
	BOTTOM("bottom"),
	DOUBLE("double"),
	NORTH("north"),
	SOUTH("south"),
	WEST("west"),
	EAST("east");

	private final String name;

	CopycatSlabType(String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
