package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

public class GoldenMangoUsedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		DisplayCircularWooshProcedure.execute(world, x, y, z, entity, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_range"), 1, 20, "gold");
		if (!world.isClientSide()) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.amethyst_block.chime")), SoundSource.PLAYERS, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("block.amethyst_block.chime")), SoundSource.PLAYERS, 1, 1, false);
				}
			}
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(GetItemAttributeProcedure.execute(itemstack, "minigames:ability_range") / 2d), e -> true).stream()
					.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entityiterator instanceof Player) {
					if ((entity.position()).distanceTo((entityiterator.position())) <= GetItemAttributeProcedure.execute(itemstack, "minigames:ability_range")) {
						ApplyEffectProcedure.execute(world, entityiterator, false, GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"), GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length"),
								"minigames:damage_resistance");
						ApplyEffectProcedure.execute(world, entityiterator, false, GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency_2"), GetItemAttributeProcedure.execute(itemstack, "minigames:effect_length_2"),
								"minigames:golden");
						DungeonHealProcedure.execute(entity, entityiterator, GetItemAttributeProcedure.execute(itemstack, "minigames:extra_damage"), "room_complete");
					}
				}
			}
		}
	}
}