package net.mcreator.minigames.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class GoldenZombieEntity extends Zombie {
	private String texture = "golden_zombie";

	public GoldenZombieEntity(EntityType<? extends Zombie> type, Level level) {
		super(type, level);
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput tag) {
		super.addAdditionalSaveData(tag);
		tag.putString("Texture", texture);
	}

	public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput tag) {
		super.readAdditionalSaveData(tag);
		texture = tag.getStringOr("Texture", texture);
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return Zombie.createMonsterAttributes();
	}
}
