package net.mcreator.minigames.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class GoldenSkeletonEntity extends Skeleton {
	public GoldenSkeletonEntity(EntityType<? extends Skeleton> type, Level level) {
		super(type, level);
	}

	public String getTexture() {
		return "golden_skeleton";
	}

	public static void init(RegisterSpawnPlacementsEvent event) {}

	public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes();
	}
}
