package net.mcreator.minigames.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.MinigamesMod;

import java.util.Comparator;
import java.util.ArrayList;

public class FeastRightclickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		ArrayList<Object> arrowInRange = new ArrayList<>();
		double i = 0;
		ApplyCooldownProcedure.execute(entity, itemstack, GetItemAttributeProcedure.execute(itemstack, "minigames:ability_cooldown"));
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.player.burp")), SoundSource.NEUTRAL, 1, 1);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.player.burp")), SoundSource.NEUTRAL, 1, 1, false);
			}
		}
		arrowInRange.clear();
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entityiterator.is(TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:arrow")))) {
					arrowInRange.add(entityiterator);
				}
			}
		}
		i = 0;
		for (int _i1 = 0; _i1 < (int) arrowInRange.size(); _i1++) {
			ParticleFlowHelperProcedure.execute(world, 1, 20, "easin", "minecraft:heart", (arrowInRange.get((int) i) instanceof Entity _entity8 ? _entity8 : null).position(), entity.position());
			if (!(arrowInRange.get((int) i) instanceof Entity _entity11 ? _entity11 : null).level().isClientSide())
				(arrowInRange.get((int) i) instanceof Entity _entity11 ? _entity11 : null).discard();
			i = i + 1;
			MinigamesMod.queueServerWork(20, () -> {
				DungeonHealProcedure.execute(entity, entity, GetItemAttributeProcedure.execute(itemstack, "minigames:effect_potency"), "utility");
			});
		}
	}
}